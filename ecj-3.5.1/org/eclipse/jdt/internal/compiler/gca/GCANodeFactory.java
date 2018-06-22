package org.eclipse.jdt.internal.compiler.gca;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.lookup.*;

import java.util.*;

public class GCANodeFactory
{
	private HashMap /* <BindingWrapper, GCAEnvVar> */ env = new HashMap();
	private ArrayList /* BindingWrapper */ env_list = new ArrayList();
	private HashSet /* ReachSpec */ reach_specs = new HashSet();
	private Binding iteration_var;
	private BlockScope scope;

	public static TypeBinding java_lang_Object = null; // initialised ASAP


	public static final GCANodeFactory SINGLETON = new GCANodeFactory();

	private
	GCANodeFactory()
	{}

	public PredicateFamily
	build(final QuantifiedExpression qe, BlockScope _scope)
	{
		this.env.clear();
		this.env_list.clear();
		this.iteration_var = qe.variable.binding;
		this.reach_specs.clear();
		this.scope = _scope;

		java_lang_Object = _scope.getJavaLangObject();

		GCANode body = make(qe.body);
		if (!qe.existential)
			body = new GCAUnary(OperatorIds.NOT, "!", body); // universal bodies must be negated, since the purpose of these bodies is to indicate traversal termination

		body = guardBody(body);

		final BindingWrapper[] env_array = new BindingWrapper[this.env_list.size()];
		for (int i = 0; i < this.env_list.size(); i++)
			env_array[i] = (BindingWrapper) env_list.get(i);

		final ReachSpec[] reach_specs_array = new ReachSpec[this.reach_specs.size()];
		final Iterator rs_it = reach_specs.iterator();
		int i = 0;
		while (rs_it.hasNext())
			reach_specs_array[i++] = (ReachSpec)rs_it.next();

		return new PredicateFamily(qe.variable.type.resolveType(_scope, false), body, env_array, qe, reach_specs_array);
	}

	// detect uses of x.f.g and guard against x.f being null
	private GCANode
	guardBody(GCANode body)
	{
		GCATraversalDereferenceCollector.SINGLETON.clear();
		body.accept(GCATraversalDereferenceCollector.SINGLETON);

		for (int i = 0; i < GCATraversalDereferenceCollector.SINGLETON.nodes.size(); i++) {
			GCADereference deref = (GCADereference) GCATraversalDereferenceCollector.SINGLETON.nodes.get(i);

			while (deref.pathLength () > 1) {
				final GCADereference sub_deref = deref.copyWithoutLastPathElement();
				final GCANode guard = new GCABinary(OperatorIds.NOT_EQUAL, "!=", sub_deref, GCANull.NULL.setTypes(this.scope.getJavaLangObject(), 0));
				guard.setTypes(TypeBinding.BOOLEAN, 0);
				body = new GCABinary(OperatorIds.AND_AND, "&&", guard, body).setTypes(TypeBinding.BOOLEAN, 0);
				deref = sub_deref;
			}
		}

		return body;
	}

	private GCANode
	substituteBinding(final BindingWrapper binding)
	{
		if (binding.source == this.iteration_var && binding.path.length == 0)
			return GCAIterationVar.VAR;
		else {
			GCAEnvVar var = (GCAEnvVar) env.get(binding);
			if (var == null) {
				final int index = env_list.size();
				var = new GCAEnvVar(index);
				env_list.add(binding);
				env.put(binding, var);
			}
			return var;
		}
	}

	private final GCANode
	make(final Expression e)
	{
		final GCANode n = makeSub(e);
		n.result_ty = e.resolvedType;
		n.implicit_conversion = e.implicitConversion;
		return n;
	}

	private final GCANode
	makeSub(final Expression e)
	{
		// Distributing all this code over the various classes would integrate the GCA code
		// too much for readability, so I opted for a slow-and-non-OO functional-style
		// translation pattern here.  I don't really like OO anyway.

		if (e instanceof TrueLiteral)
			return GCATrue.TRUE;
		if (e instanceof FalseLiteral)
			return GCAFalse.FALSE;
		if (e instanceof NullLiteral)
			return GCANull.NULL;
		if (e instanceof Literal) {
			final Literal lit = (Literal) e;
			lit.computeConstant();
			return new GCAConstant(lit.constant, lit.implicitConversion);
		}
		if (e instanceof BinaryExpression) {
			final BinaryExpression be = (BinaryExpression) e;
			return new GCABinary(be.getOperatorId(), be.operatorToString(), make(be.left), make(be.right));
		}
		if (e instanceof InstanceOfExpression) {
			final InstanceOfExpression ioe = (InstanceOfExpression) e;
			return new GCAInstanceOf(make(ioe.expression), ioe.type.resolveType(this.scope, false));
		}
		if (e instanceof UnaryExpression) {
			final UnaryExpression ue = (UnaryExpression) e;
			return new GCAUnary(ue.getOperatorId(), ue.operatorToString(), make(ue.expression));
		}
		if (e instanceof ReachExpression) {
			final ReachExpression re = (ReachExpression) e;
			this.reach_specs.add(re.reach_spec);
			return new GCATraversalConstant(re.reach_spec);
		}
		if (e instanceof SingleNameReference) {
			final SingleNameReference sr = (SingleNameReference) e;
			return substituteBinding(new BindingWrapper(sr));
		}
		if (e instanceof QualifiedNameReference) {
			final QualifiedNameReference qr = (QualifiedNameReference) e;
			switch (qr.bits & ASTNode.RestrictiveFlagMASK) {
			case Binding.FIELD:
				return substituteBinding(new BindingWrapper(qr));

			case Binding.LOCAL: {
				if (qr.binding == this.iteration_var) { // forall x : ... x.v ...
					final BindingWrapper br = new BindingWrapper(qr);
					br.path = new FieldBinding[0];
					GCANode n = substituteBinding(br);
					if (qr.otherBindings == null)
						return n;
					else 
						return new GCADereference(n,
									  qr.otherBindings);
				} else
					return substituteBinding(new BindingWrapper(qr));
			}
			default:
				throw new RuntimeException("Unexpected kind of QualifiedNameReference");
			}
		}
		throw new RuntimeException("Trying to build GCA expression from unsupported expression: " + e.getClass());
	}
}