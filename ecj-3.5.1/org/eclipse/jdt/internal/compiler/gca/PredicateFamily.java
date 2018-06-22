package org.eclipse.jdt.internal.compiler.gca;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.codegen.*;

import java.util.*;

/**
 * A predicate family represents the body of a quantified expression and provides
 * operations for specialising this operation for various reach specs.
 */
public class PredicateFamily
{
	BindingWrapper[] env;
	ReachSpec[] reach_specs;
	GCANode body;
	TypeBinding query_var_ty;
	QuantifiedExpression qexpr;

	public static TypeBinding object_type = null; // initialised the first time we process a spec

	public
	PredicateFamily(final TypeBinding _query_var_ty,
			final GCANode _body,
			final BindingWrapper[] _env,
			final QuantifiedExpression _owner,
			final ReachSpec[] _reach_specs)
	{
		this.query_var_ty = _query_var_ty;
		this.body = _body;
		this.env = _env;
		this.qexpr = _owner;
		this.reach_specs = _reach_specs;
	}

	public void
	insertClassesInto(final Set /* String */ set) {
		set.add(this.query_var_ty.debugName());

		this.body.accept(new GCANodeVisitor() {
				public void visitInstanceOf(final GCAInstanceOf _instance_of) {
					set.add(_instance_of.ty.debugName());
				}});
	}

	public String
	toString()
	{
		String retval = this.body + "\n";
		for (int i = 0; i < this.env.length; i++)
			retval += "  $" + i + " = " + env[i] + "\n";
		for (int i = 0; i < this.reach_specs.length; i++)
			retval += "  with t#" + i + " = " + reach_specs[i] + "\n";
		return retval;
	}


	public void
	emitEnvironmentArray(final CodeStream cs, final BlockScope scope, final PredicateLinkage plinkage)
	{
		if (env == null || this.env.length == 0) {
			cs.aconst_null();
			return;
		}
		cs.generateInlinedValue(this.env.length);
		cs.anewarray("java/lang/Object".toCharArray());
		for (int i = 0; i < env.length; i++) {
			cs.dup();
			cs.generateInlinedValue(i);
			this.env[i].emitLoad(cs, scope, plinkage);
			cs.aastore();
		}
	}


	/**
	 * Specialise to a given reach spec
	 *
	 * @param spec The reach spec to specialise for, or null to filter out all
	 */
	public GCAClosure
	specialise(final ReachSpec spec)
	{
		GCANode clone = this.body.copy();
		clone = clone.subst(new GCANodeTest() {
				public final boolean test(GCANode n)
				{
					if (n instanceof GCATraversalConstant) {
						GCATraversalConstant trav = (GCATraversalConstant) n;
						if (spec != null && spec.equals(trav.reach_spec))
							return true;
					}
					return false;
				}
			},
			GCATrue.TRUE);

		clone = clone.subst(new GCANodeTest() {
				public final boolean test(GCANode n)
				{
					return (n instanceof GCATraversalConstant);
				}
			},
			GCAFalse.FALSE);

		return new GCAClosure(this.query_var_ty, clone.fold(), env);
	}
}