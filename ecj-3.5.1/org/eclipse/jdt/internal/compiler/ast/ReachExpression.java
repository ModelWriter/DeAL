package org.eclipse.jdt.internal.compiler.ast;

import org.eclipse.jdt.internal.compiler.impl.*;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.flow.*;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.gca.Debug;
import org.eclipse.jdt.internal.compiler.ASTVisitor;
import java.util.*;

public class ReachExpression extends Expression
{
	public ReachSpec reach_spec;
	public SingleNameReference parameter;

	public
	ReachExpression(final SingleNameReference _parameter, final ReachSpec _reach_spec)
	{
		super();
		this.reach_spec = _reach_spec;
		this.parameter = _parameter;
		this.constant = Constant.NotAConstant;
		this.resolvedType = TypeBinding.BOOLEAN;
	}

	public String
	toString()
	{
		return "reach[" + this.reach_spec.toString() + "](" + (new String(this.parameter.token)).toString() + ")";
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
	 * @param valueRequired boolean
	 */
	public void
	generateCode(BlockScope currentScope, CodeStream codeStream, boolean valueRequired)
	{
		throw new RuntimeException("ReachExpressions must never be used to generate code!");
	}

	public TypeBinding resolveType(BlockScope scope) {
		this.parameter.resolveTypeExpecting(scope, scope.getJavaLangObject());
		this.reach_spec.resolveTypes(scope);
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
		visitor.visit(this, scope);
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
		visitor.visit(this, scope);
		visitor.endVisit(this, scope);
	}
}
