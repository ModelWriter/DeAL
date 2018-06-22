package org.eclipse.jdt.internal.compiler.gca;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;

import java.util.*;

/**
 * Collection of predicate families for a given assertion.  This class handles the allocation
 * and management of static predicate function names.
 */
public final class PredicateLinkage
{
	public ArrayList /* ReachSpec */ reach_specs_list = new ArrayList(); // first entry is always null, cf. `link()'
	public ReachSpec[] reach_specs = null; // initialised after linking
	public HashMap /* GCAClosure, Integer */ predicates = new HashMap();
	public ArrayList /* GCAClosure */ closure_list = new ArrayList();
	public ArrayList /* PredicateClassLinkage */ predicate_families = new ArrayList();
	public HashSet /* String */ instance_classes = new HashSet(); // All classes we run instanceof checks of, stringified

	// during code generation
	public TypeDeclaration declaring_class;
	public int predicate_method_offset;
	public CodeStream cs;
	public BlockScope scope;
	public boolean assert_disjointness;
	private int disjoin_id = 1;
	private static final int NEUTRAL_DISJOIN_ID = 0x0; // this traversal ID is used for `non-intrusive' traversals
	private static final int MAX_DISJOIN_ID = 0x07;
	private HashSet /* NameReference */ ignored_nodes; // nodes that we need to traverse as final pass

	private LinkedList /* EmitStep */ steps = new LinkedList();

	public PredicateLinkage()
	{
		// Reach spec for `anything not reachable within an intended traversal'
		this.reach_specs_list.add(null);
	}

	public void
	addPredicateFamily(final PredicateFamily _predicate_family)
	{
		this.predicate_families.add(new PredicateClassLinkage(_predicate_family));

		// yes, yes.  Quadratic.  Can be made log-linear or better.
		for (int i = 0; i < _predicate_family.reach_specs.length; i++)
			this.addReachSpec(_predicate_family.reach_specs[i]);
	}

	private void
	addReachSpec(final ReachSpec new_reach_spec)
	{
		// first entry is null, so skip it
		for (int i = 1; i < this.reach_specs_list.size(); i++)
			if (new_reach_spec.equals(this.reach_specs_list.get(i)))
				return;
		this.reach_specs_list.add(new_reach_spec);
	}

	private static String
	showReachSpec(ReachSpec s)
	{
		if (s == null)
			return "*";
		else
			return s.toString();
	}

	/**
	 * Link everything, after all predicate families are accounted for
	 */
	public void
	link()
	{
		// pass 1: sort reach specs.  Non-root ones go first, then comes a whole-heap traversal, then come dominance checks.
		// If we have dominator checks, we don't need a `null' pass at the end:
		this.reach_specs = new ReachSpec[this.reach_specs_list.size()];
		int reach_specs_nr = 0;
		for (int i = 1; i < this.reach_specs_list.size(); i++) {
			final ReachSpec rs = (ReachSpec)this.reach_specs_list.get(i);
			if (!rs.isDominator())
				this.reach_specs[reach_specs_nr++] = rs;
		}
		for (int i = 1; i < this.reach_specs_list.size(); i++) {
			final ReachSpec rs = (ReachSpec)this.reach_specs_list.get(i);
			if (rs.isDominator())
				this.reach_specs[reach_specs_nr++] = rs;
		}
		this.reach_specs[reach_specs_nr++] = null;

		this.reach_specs_list = null;

		// pass 2: register all GCAClosures
		for (int i = 0; i < this.predicate_families.size(); i++) {
			final PredicateClassLinkage pcl = (PredicateClassLinkage) this.predicate_families.get(i);
			for (int k = 0; k < this.reach_specs.length; k++) {
				final ReachSpec rs = this.reach_specs[k];
//				Debug.println("T", " -- T" + i + "(#" + k + ": " + showReachSpec(rs) + ")");
				final GCAClosure closure = pcl.getClosure(rs);

				if (!closure.isTrivial() && !this.predicates.containsKey(closure)) {
					int index = this.closure_list.size();
					this.closure_list.add(closure);
					this.predicates.put(closure, new Integer(index));
				}
				Debug.println("T", "T" + i + "(" + showReachSpec(rs) + ") = " + closure);
			}
		}

		// pass 3: link
		for (int i = 0; i < this.predicate_families.size(); i++) {
			final PredicateClassLinkage pcl = (PredicateClassLinkage) this.predicate_families.get(i);
			pcl.link();
		}
		if (this.closure_list.size() == 1)
			Debug.println("T", "1 predicate function needed");
		else
			Debug.println("T", (this.closure_list.size()) + " predicate functions needed");
	}

	/**
	 * @param method_offset Offset of the first method we will be generating in the method table
	 * @param class_file class to generate methods in
	 * @return Number of methods generated
	 */
	public int
	addMethods(final TypeDeclaration declaring_class,
		   final Assertion assertion,
		   final int method_offset)
	{
		if (predicates.size() == 0)
			return 0;

		final AbstractMethodDeclaration[] new_methods = new AbstractMethodDeclaration[declaring_class.methods.length + predicates.size()];
		System.arraycopy(declaring_class.methods, 0, new_methods, 0, method_offset);
		System.arraycopy(declaring_class.methods, method_offset,
				 new_methods, method_offset + predicates.size(),
				 declaring_class.methods.length - method_offset);
		declaring_class.methods = new_methods;

		for (int i = 0; i < predicates.size(); i++) {
			final AssertionPredicateMethod apm = new AssertionPredicateMethod(i + method_offset,
											  assertion,
											  declaring_class,
											  (GCAClosure) this.closure_list.get(i));
			new_methods[i + method_offset] = apm;
		}
		return predicates.size();
	}

	public static final char[] CONSTRUCTOR_NAME = "<init>".toCharArray();

	public static final String GCA_PACKAGE = "org/mmtk/plan/marksweep/gcassertions/spec";

	public static final String GCA_ASSERTION			= (GCA_PACKAGE + "/Assertion");
	public static final String GCA_PREDICATE_FAMILY			= (GCA_PACKAGE + "/PredicateFamily");

	//----------------------------------------
	public static final String GCA_STEP				= (GCA_PACKAGE + "/Step");

	public static final String GCA_EXCLUDE_NODES			= (GCA_PACKAGE + "/ExcludeNodesStep");
	public static final String GCA_INCLUDE_NODES			= (GCA_PACKAGE + "/IncludeNodesStep");
	public static final String GCA_SET_TRAVERSAL_ID			= (GCA_PACKAGE + "/SetTraversalIdStep");
	public static final String GCA_SET_TRAVERSAL_PREDICATES		= (GCA_PACKAGE + "/SetTraversalPredicatesStep");
	public static final String GCA_TRAVERSAL			= (GCA_PACKAGE + "/TraversalStep");

	static final char[]
	getTy(String s)
	{
		return ("L" + s).toCharArray();
	}

	static final char[]
	getArrayTy(String s)
	{
		return ("[L" + s).toCharArray();
	}

	public void
	constructor(final String classname, final String[] args)
	{
		cs.doInvokeConstructor(classname, args);
	}

	public void
	invokestatic(final String classname, final String methodname, final String[] args, final String retty)
	{
		cs.doInvokeStatic(classname, methodname, args, retty);
	}


	public void
	emitSteps()
	{
		cs.generateInlinedValue(this.steps.size() + 4);
		cs.anewarray(GCA_STEP.toCharArray());

		int offset = 0;

		cs.dup();
		cs.generateInlinedValue(offset++);
		cs.aconst_null();
		cs.aastore();

		Iterator it = this.steps.iterator();
		while (it.hasNext()) {
			final EmitStep es = (EmitStep) it.next();
			Debug.println("traversal", " #" + (offset) + ": " + es);
			cs.dup();
			cs.generateInlinedValue(offset++);
			es.doEmit();
			cs.aastore();
		}

		for (int i = 0; i < 3; i++) {
			cs.dup();
			cs.generateInlinedValue(offset++);
			cs.aconst_null();
			cs.aastore();
		}
		
	}

	private void
	preloadClasses()
	{
		if (!this.instance_classes.isEmpty()) {
			BranchLabel endlabel = new BranchLabel(this.cs);
			final Iterator it = this.instance_classes.iterator();
			while (it.hasNext()) {
				String class_name = (String)it.next();
//				System.out.println("Pre-loading class `"+class_name+"'");
				this.cs.ldc(class_name);
				this.invokestatic("java/lang/Class", "forName", new String[] { "Ljava/lang/String;" }, "Ljava/lang/Class;");
				this.cs.pop();
			}
			this.cs.goto_(endlabel);
			cs.new_("java/lang/RuntimeException".toCharArray());
			cs.dup_x1();
			cs.swap();
			this.constructor("java/lang/RuntimeException", new String[] { "Ljava/lang/Throwable;" });
			cs.athrow();
			endlabel.place();
		}
	}

	/**
	 * Code emitter for GCA invocation
	 */
	public void
	emit(final CodeStream _cs,
	     final BlockScope _scope,
	     final int predicate_array_varid,
	     final TypeDeclaration _declaring_class,
	     final int _predicate_method_offset,
	     final Expression body,
	     final boolean _assert_disjointness)
	{
		this.predicate_method_offset = _predicate_method_offset;
		this.declaring_class = _declaring_class;
		this.cs = _cs;
		this.scope = _scope;
		this.assert_disjointness = _assert_disjointness && this.reach_specs.length > 2;

		// ---------- Classloading prelude ----------
		this.preloadClasses();

		// ---------- LINE 1 ----------
		// first, create the Predicate array
		cs.generateInlinedValue(this.predicate_families.size());
		cs.anewarray(GCA_PREDICATE_FAMILY.toCharArray());
		// Initialise predicate array with our predicates
   		for (int i = 0; i < this.predicate_families.size(); i++)
   			((PredicateClassLinkage)this.predicate_families.get(i)).emitPredicateInitialiser(cs, scope, i);
 		cs.astore(predicate_array_varid);

		// ---------- LINE 2 ----------
		// Run the assertion and report any errors
 		cs.new_(GCA_ASSERTION.toCharArray());
 		cs.dup();
 		cs.aload(predicate_array_varid);
  		this.computeSteps();
  		this.emitSteps();
 		this.constructor(GCA_ASSERTION, new String[] { "[L" + GCA_PREDICATE_FAMILY, "[L" + GCA_STEP });
 		this.invokestatic(GCA_ASSERTION, "doAssert", new String[] { "L" + GCA_ASSERTION }, "Z");
 		body.generateCode(scope, cs, true);
  		this.invokestatic(GCA_ASSERTION, "checkAssert", new String[] { "Z", "Z" }, "V");

 		// ---------- LINE 3 ----------
 		// clear the Predicate array
 		cs.aconst_null();
 		cs.astore(predicate_array_varid);
	}


	void
	addStep(final EmitStep s)
	{
		this.steps.addLast(s);
	}

	void
	addTraversalIdStep(final int tid)
	{
		if (!assert_disjointness)
			return;
		if (tid >= MAX_DISJOIN_ID)
			throw new RuntimeException("Too many traversal passes!");
		addStep(new SetTraversalIdStep(tid));
	}

	void
	addNeutralTraversalIdStep()
	{
		addTraversalIdStep(NEUTRAL_DISJOIN_ID);
	}

	// Part 1 of mark program: `reach' traversals
	void
	computeSteps()
	{
		boolean did_root_traversal = false;

		this.ignored_nodes = new HashSet();
		HashSet local_ignores = new HashSet();
		for (int i = 0; i < this.reach_specs.length; i++) {
			local_ignores.clear();
			final ReachSpec rs = this.reach_specs[i];

			addStep(new SetTraversalPredicatesStep(i));
			if (rs == null) {
//				computeIgnoreDominated(i + 1);
				if (!did_root_traversal) {
					addNeutralTraversalIdStep();
					computeRootTraversal(i);
				}
//				computeDominateSteps(i + 1);
				computeFinalCleanup();
				return;
			} else {
				final int traversal_id = disjoin_id++;
				addTraversalIdStep(traversal_id);
			}

			for (int k = 0; k < rs.traversals.length; k++) {
				ReachSpec.Traversal trav = rs.traversals[k];
				for (int l = 0; l < trav.ignores.length; l++)
					local_ignores.add(trav.ignores[l]);
				addStep(new ExcludeNodesStep(trav.ignores));
				if (trav.sources.length == 0 && !did_root_traversal) {
					addStep(new MarkRootsStep());
					did_root_traversal = true;
				} else
					addStep(new MarkStep(trav.sources));
			}

// 			if (local_ignores.size() > 0)
// 				addStep(new IncludeNodesStep(local_ignores));
			this.ignored_nodes.addAll(local_ignores);
		}

	}

	// part 2 of mark program: ignore all dominated nodes
	void
	computeIgnoreDominated(final int offset)
	{
		HashSet ignores = new HashSet();
		for (int i = offset; i < this.reach_specs.length; i++) {
			final ReachSpec rs = this.reach_specs[i];
			if (rs.traversals.length > 0)
				for (int k = 0; k < rs.traversals[0].ignores.length; k++) {
					NameReference nr = rs.traversals[0].ignores[k];
					ignores.add(nr);
				}
		}
		if (!ignores.isEmpty())
			addStep(new ExcludeNodesStep(ignores));
	}

	// part 3 of mark program: mark from root
	void
	computeRootTraversal(final int offset)
	{
//		addStep(new SetTraversalPredicatesStep(offset));
		addStep(new MarkRootsStep());
	}

	// part 4 of mark program: `dominate' tests
	void
	computeDominateSteps(final int offset)
	{
		HashSet local_ignores = new HashSet();
		for (int i = offset; i < this.reach_specs.length; i++) {
			local_ignores.clear();
			final ReachSpec rs = this.reach_specs[i];

			final int traversal_id = disjoin_id++;
			addTraversalIdStep(traversal_id);
			addStep(new SetTraversalPredicatesStep(i));
			// start at offset 1, since we already ignored the outer level
			for (int k = 1; k < rs.traversals.length; k++) {
				ReachSpec.Traversal trav = rs.traversals[k];
				for (int l = 0; l < trav.ignores.length; l++)
					local_ignores.add(trav.ignores[l]);
				addStep(new ExcludeNodesStep(trav.ignores));
				addStep(new MarkStep(trav.sources));
			}
			// Mark `ignored' set for `dominates' check... this is very hackish.
			if (rs.traversals[0].ignores.length > 0) {
				addStep(new IncludeNodesStep(rs.traversals[0].ignores));
				addStep(new MarkStep(rs.traversals[0].ignores));
			}

			if (local_ignores.size() > 0)
				addStep(new IncludeNodesStep(local_ignores));
			this.ignored_nodes.addAll(local_ignores);
		}
	}

	void
	computeFinalCleanup()
	{
		if (!ignored_nodes.isEmpty()) {
			addStep(new IncludeNodesStep(this.ignored_nodes));
			addStep(new MarkStep(this.ignored_nodes));
		}
		// Avoid invalid overlaps with the Assertion cleanup code (this should really be in the runtime):
		addTraversalIdStep(0);
	}


	public class PredicateClassLinkage
	{
		final static int NO_BINDING = -1;
		public int[] bindings; // magic method indices 
		public PredicateFamily predicate_family;

		public
		PredicateClassLinkage(final PredicateFamily _predicate_family)
		{
			this.predicate_family = _predicate_family;
		}

		public GCAClosure
		getClosure(final ReachSpec _rs)
		{
			return this.predicate_family.specialise(_rs);
		}

		public void
		link()
		{
			this.bindings = new int[PredicateLinkage.this.reach_specs.length];
			this.predicate_family.insertClassesInto(PredicateLinkage.this.instance_classes);

			for (int i = 0; i < PredicateLinkage.this.reach_specs.length; i++) {
				final ReachSpec rs = PredicateLinkage.this.reach_specs[i];
				final GCAClosure closure = this.getClosure(rs);

				if (closure.isTrivial())
					this.bindings[i] = NO_BINDING;
				else {
					Integer key = (Integer) PredicateLinkage.this.predicates.get(closure);
					if (key == null) {
						System.out.println("=== Mismatch!");
						final Iterator it = PredicateLinkage.this.predicates.keySet().iterator();
						while (it.hasNext()) {
							Object k = it.next();
							Object v = PredicateLinkage.this.predicates.get(k);
							if (k != null)
								System.out.println("  # " + k + " -> " + v + "[" + k.equals(closure) + "; " + k.hashCode() + ", " + closure.hashCode() + "]");
							else
								System.out.println("  # " + null + " -> " + v);
						}
						throw new RuntimeException("Internal error: No linkage for linked closure (" + closure + ")?");
					}
					this.bindings[i] = key.intValue();
				}
			}
		}

		public void
		emitPredicateInitialiser(final CodeStream cs, final BlockScope scope, final int index)
		{
			cs.dup();
			cs.generateInlinedValue(index);

			{ // invoke constructor
				cs.new_(GCA_PREDICATE_FAMILY.toCharArray());
				cs.dup();
				// arg 1: class containing our magic methods
				cs.generateClassLiteralAccessForType(PredicateLinkage.this.declaring_class.binding, null);
				// arg 2: array with method IDs
				cs.generateInlinedValue(this.bindings.length);
				cs.newarray(ClassFileConstants.INT_ARRAY);
				for (int i = 0; i < this.bindings.length; i++) {
					cs.dup();
					cs.generateInlinedValue(i);
					if (this.bindings[i] == NO_BINDING)
						cs.generateInlinedValue(NO_BINDING);
					else
						cs.generateInlinedValue(this.bindings[i] + PredicateLinkage.this.predicate_method_offset);
					cs.iastore();
				}
				// arg 3: environment array
				this.predicate_family.emitEnvironmentArray(cs, scope, PredicateLinkage.this);
				// arg 4: existential flag
				cs.generateInlinedValue(predicate_family.qexpr.existential);
				// Constructor
				PredicateLinkage.this.constructor(GCA_PREDICATE_FAMILY,
								  new String[] { "Ljava/lang/Class", "[I", "[Ljava/lang/Object", "Z"});
			}

			cs.aastore();
		}


	}


	public abstract class EmitStep {
		public abstract void doEmit();
	}

	public abstract class EmitNameReferenceStep extends EmitStep {
		HashSet nodes = new HashSet();
		public EmitNameReferenceStep(AbstractSet s)
		{
			this.add(s);
		}
		public EmitNameReferenceStep(NameReference n)
		{
			this.add(n);
		}
		public EmitNameReferenceStep(NameReference[] ns)
		{
			this.add(ns);
		}
		public void
		add(AbstractSet s)
		{
			this.nodes.addAll(s);
		}
		public void
		add(NameReference n)
		{
			this.nodes.add(n);
		}
		public void
		add(NameReference[] ns)
		{
			for (int i = 0; i < ns.length; i++)
				this.nodes.add(ns[i]);
		}
		protected void
		inlineValues()
		{
			cs.generateInlinedValue(nodes.size());
			cs.anewarray("java/lang/Object".toCharArray());
			Iterator it = this.nodes.iterator();
			int index = 0;
			while (it.hasNext()) {
				cs.dup();
				cs.generateInlinedValue(index++);
				((NameReference)it.next()).generateCode(scope, cs, true);
				cs.aastore();
			}
		}
		public String
		toString()
		{
			String s = "";
			Iterator it = this.nodes.iterator();
			while (it.hasNext()) {
				final NameReference nr = (NameReference) it.next();
				if (s != "")
					s += ", ";
				s += nr.toString();
			}
			return s;
		}
	}

	public class ExcludeNodesStep extends EmitNameReferenceStep {
		public ExcludeNodesStep(AbstractSet s) { super (s); }
		public ExcludeNodesStep(NameReference s) { super (s); }
		public ExcludeNodesStep(NameReference[] s) { super (s); }
		public void doEmit()
		{
			cs.new_(GCA_EXCLUDE_NODES.toCharArray());
			cs.dup();
			inlineValues();
			constructor(GCA_EXCLUDE_NODES, new String[] { "[Ljava/lang/Object" });
		}
		public String toString() { return "exclude-nodes(" + super.toString() + ")"; }
	}

	public class IncludeNodesStep extends EmitNameReferenceStep {
		public IncludeNodesStep(AbstractSet s) { super (s); }
		public IncludeNodesStep(NameReference s) { super (s); }
		public IncludeNodesStep(NameReference[] s) { super (s); }
		public void doEmit()
		{
			cs.new_(GCA_INCLUDE_NODES.toCharArray());
			cs.dup();
			inlineValues();
			constructor(GCA_INCLUDE_NODES, new String[] { "[Ljava/lang/Object" });
		}
		public String toString() { return "include-nodes(" + super.toString() + ")"; }
	}

	public class MarkRootsStep extends EmitStep {
		public void doEmit()
		{
			cs.new_(GCA_TRAVERSAL.toCharArray());
			cs.dup();
			constructor(GCA_TRAVERSAL, new String[] { });
		}
		public String toString() { return "mark-roots"; }
	}

	public class MarkStep extends EmitNameReferenceStep {
		public MarkStep(AbstractSet s) { super (s); }
		public MarkStep(NameReference s) { super (s); }
		public MarkStep(NameReference[] s) { super (s); }
		public void doEmit()
		{
			cs.new_(GCA_TRAVERSAL.toCharArray());
			cs.dup();
			inlineValues();
			constructor(GCA_TRAVERSAL, new String[] { "[Ljava/lang/Object" });
		}
		public String toString() { return "mark(" + super.toString() + ")"; }
	}

	public class SetTraversalIdStep extends EmitStep {
		int id;
		public SetTraversalIdStep(final int _id)
		{
			this.id = _id;
		}

		public void doEmit()
		{
			cs.new_(GCA_SET_TRAVERSAL_ID.toCharArray());
			cs.dup();
			cs.generateInlinedValue(id);
			constructor(GCA_SET_TRAVERSAL_ID, new String[] { "I" });
		}
		public String toString() { return "set-traversal-id(" + this.id + ")"; }
	}

	public class SetTraversalPredicatesStep extends EmitStep {
		int id;
		public SetTraversalPredicatesStep(final int _id)
		{
			this.id = _id;
		}
		public void doEmit()
		{
			cs.new_(GCA_SET_TRAVERSAL_PREDICATES.toCharArray());
			cs.dup();
			cs.generateInlinedValue(predicate_families.size());
			cs.newarray(TypeIds.T_int);
			for (int i = 0; i < predicate_families.size(); i++) {
				cs.dup();
				cs.generateInlinedValue(i);
				cs.generateInlinedValue(this.id);
				cs.iastore();
			}
			constructor(GCA_SET_TRAVERSAL_PREDICATES, new String[] { "[I" });
		}
		public String toString() { return "set-traversal-predicates(" + this.id + ")"; }
	}
}
