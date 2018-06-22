package org.eclipse.jdt.internal.compiler.ast;

import org.eclipse.jdt.internal.compiler.impl.*;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.flow.*;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.gca.*;
import org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;
import java.util.*;

public class QuantifiedExpression extends Expression
{
	public static final boolean UNIVERSAL = false;
	public static final boolean EXISTENTIAL = true;

	public final boolean existential;
	public Expression body;
	public Argument variable;
	public int qexpr_index = -1; // Magic index.  During code generation, this is our relative offset to the qexpr local variable index.
	public Assertion owner_assertion;

	ReachExpression[] reach_expressions; // contained reach expressions

	public
	QuantifiedExpression(final boolean _existential,
			     final Argument _variable,
			     final Expression _body)
	{
		super();
		this.existential = _existential;
		this.variable = _variable;
		this.body = _body;
		this.constant = Constant.NotAConstant;
		this.variable.quantifier_bound = true;
	}

	void
	setQExprIndex(final Assertion _owner, final int i)
	{
		this.owner_assertion = _owner;
		this.qexpr_index = i;
	}

	void
	locateReachExpressions(final ProblemReporter problem_reporter)
	{
		final LinkedList rexpr_ll = new LinkedList();

		this.body.traverse(new ASTVisitor() {
				public boolean visit(final QuantifiedExpression e, final BlockScope _) {
					problem_reporter.abortDueToInternalError("Quantified expression `" + e + "' inside `" + QuantifiedExpression.this.toString() + "'",
										 QuantifiedExpression.this);
					return false;
				}
				public boolean visit(final ReachExpression e, final BlockScope _) {
					rexpr_ll.addLast(e);
					return false;
				}
			}, (BlockScope) null);

		this.reach_expressions = new ReachExpression[rexpr_ll.size()];
		for (int i = 0; i < rexpr_ll.size(); i++) {
			this.reach_expressions[i] = (ReachExpression) rexpr_ll.get(i);
			Debug.println("extraction", "  qr#" + i + ": " + this.reach_expressions[i].toString());
			if (this.reach_expressions[i].parameter.binding != this.variable.binding)
				problem_reporter.abortDueToInternalError("Parameter to `reach' must be quantified variable", this.reach_expressions[i]);
			this.reach_expressions[i].reach_spec.assertNotVariable(this.variable.binding, problem_reporter, this);
		}
	}

	public String
	toString()
	{
		String quant = this.existential ? "exists" : "forall";
		return quant + " " + ((new String(this.variable.name))) + ": " + this.body.toString();
	}

	public StringBuffer printExpression(int indent, StringBuffer output)
	{
		return output.append(toString());
	}

	/**
	 * Every expression is responsible for generating its implicit conversion when necessary.
	 *
	 * @param currentScope org.eclipse.jdt.internal.compiler.lookup.BlockScope
	 * @param codeStream org.eclipse.jdt.internal.compiler.codegen.CodeStream
	 */
	public void
	generateCode(BlockScope currentScope, CodeStream cs, boolean _)
	{
		cs.aload(this.owner_assertion.predicate_object_bindings.resolvedPosition);
		cs.generateInlinedValue(this.qexpr_index);
		cs.aaload();
		cs.doInvokeVirtual(PredicateLinkage.GCA_PREDICATE_FAMILY, "value", new String[] {}, "Z");
	}

	public TypeBinding resolveType(BlockScope scope) {
		MethodScope sub_scope = new MethodScope(scope, scope.outerMostMethodScope().referenceContext, false);
//		BlockScope sub_scope = new BlockScope(scope);
//		System.err.println("FROM-SCOPE : " + scope);
//		System.err.println("RESOLVE-TYPE : " + sub_scope);
//		this.variable.bind(sub_scope, null, true);
		this.variable.resolve(sub_scope);
//		System.err.println("  added var");

 		if (!this.variable.type.resolveType(sub_scope, true).isCompatibleWith(scope.getJavaLangObject())) {
 			scope.problemReporter().typeMismatchError(this.variable.type.resolveType(sub_scope, true), sub_scope.getJavaLangObject(), this.variable, null);
 			return null;
 		}
//		this.body.resolve(sub_scope);
		final TypeBinding ty = this.body.resolveTypeExpecting(sub_scope, TypeBinding.BOOLEAN);
		return TypeBinding.BOOLEAN;
	}

	/**
	 * Traverse an expression in the context of a blockScope
	 * @param visitor
	 * @param scope
	 */
	public void
	traverse(ASTVisitor visitor, BlockScope scope)
	{
		if (visitor.visit(this, scope)) {
			body.traverse(visitor, scope);
		}
		visitor.endVisit(this, scope);
	}

	/**
	 * Traverse an expression in the context of a classScope
	 * @param visitor
	 * @param scope
	 */
	public void
	traverse(ASTVisitor visitor, ClassScope scope)
	{
		if (visitor.visit(this, scope))
			body.traverse(visitor, scope);
		visitor.endVisit(this, scope);
	}
}
