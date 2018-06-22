/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.ast;

import java.util.ArrayList;

import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.codegen.BranchLabel;
import org.eclipse.jdt.internal.compiler.codegen.CodeStream;
import org.eclipse.jdt.internal.compiler.flow.FlowContext;
import org.eclipse.jdt.internal.compiler.flow.FlowInfo;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.problem.*;
import org.eclipse.jdt.internal.compiler.util.Messages;
import org.eclipse.jdt.internal.compiler.gca.Debug;
import org.eclipse.jdt.internal.compiler.gca.PredicateLinkage;
import org.eclipse.jdt.internal.compiler.gca.PredicateFamily;
import org.eclipse.jdt.internal.compiler.gca.GCANodeFactory;
import org.eclipse.jdt.internal.compiler.ASTVisitor;
import java.util.*;

public class Assertion extends Expression
{
	private static int predicate_object_bindings_counter = 0;

	public static final int ASSERT = 1;
	public static final int ASSERT_DISJOINT = 2;
	public static final int ASSUME_DISJOINT = 3;
	public static final int ASSERT_DEBUG = 4; // only emit static helper methods; used for debugging the compiler

	public QuantifiedExpression[] qexprs; // All contained quantified expressions
	public Expression body;
	public final int kind;
	public int method_offset; // predicate methods offset
	public BlockScope scope;
	public PredicateLinkage linkage;
	public LocalVariableBinding predicate_object_bindings;
	public HashSet /* SingleNameReference */ contained_names;
	TypeDeclaration declaring_class;


	public Assertion(final int _kind, final Expression _body)
	{
		super();
		this.kind = _kind;
		this.body = _body;
		this.constant = Constant.NotAConstant;
	}
	
	public boolean
	checkUnsafeCast(Scope scope, TypeBinding castType, TypeBinding expressionType, TypeBinding match, boolean isNarrowing)
	{
		return false;
	}

	/**
	 * Every expression is responsible for generating its implicit conversion when necessary.
	 *
	 * @param currentScope org.eclipse.jdt.internal.compiler.lookup.BlockScope
	 * @param codeStream org.eclipse.jdt.internal.compiler.codegen.CodeStream
	 * @param valueRequired boolean
	 */
	public void
	generateCode(final BlockScope currentScope, CodeStream codeStream, boolean valueRequired)
	{
		if (this.kind == ASSERT_DEBUG)
			return; // don't generate code in debug mode

		if (linkage == null)
			throw new RuntimeException("Internal error: Trying to emit assertion without prior linking!");

		linkage.emit(codeStream, currentScope, this.predicate_object_bindings.resolvedPosition,
			     this.declaring_class, this.method_offset, this.body, this.kind == ASSERT_DISJOINT);

		if (valueRequired)
			codeStream.iconst_0(); // we're void, so this shouldn't really ever happen.
	}

	public int
	generateStaticHelperMethods(final ProblemReporter problem_reporter, final int _method_offset,
				    final TypeDeclaration _declaring_class)
	{
		findQuantifiedExpressions(problem_reporter);
		this.method_offset = _method_offset;
		this.declaring_class = _declaring_class;
		return emitQuantifiedExpressions(problem_reporter, method_offset, _declaring_class);
	}

	private void
	findQuantifiedExpressions(final ProblemReporter problem_reporter)
	{
		final LinkedList qexprs_ll = new LinkedList();

		traverse(new ASTVisitor() {
				public boolean visit(final QuantifiedExpression e, final BlockScope _) {
					qexprs_ll.addLast(e);
					return false;
				}
				public boolean visit(final ReachExpression e, final BlockScope _) {
					problem_reporter.abortDueToInternalError("`reach' outside of quantifier", Assertion.this);
					return false;
				}
			}, (BlockScope) null);

		this.qexprs = new QuantifiedExpression[qexprs_ll.size()];

		for (int i = 0; i < qexprs_ll.size(); i++) {
			this.qexprs[i] = (QuantifiedExpression) qexprs_ll.get(i);
			Debug.println("extraction", "aq#" + i + ": " + this.qexprs[i].toString());
			this.qexprs[i].locateReachExpressions(problem_reporter);
			this.qexprs[i].setQExprIndex(this, i);

		}
		if (this.kind == ASSERT) { // assert disjointness statically if needed
			ReachSpec rs = null;
			for (int qi = 0; qi < this.qexprs.length; qi++) {
				final QuantifiedExpression q = this.qexprs[qi];
				if (q.reach_expressions.length > 0) {
					if (rs == null)
						rs = q.reach_expressions[0].reach_spec;

					for (int ri = 0; ri < q.reach_expressions.length; ri++)
						if (!q.reach_expressions[ri].reach_spec.equals(rs))
							problem_reporter.abortDueToInternalError("reach("
												 + q.reach_expressions[ri].reach_spec
												 + ") does not match reach("
												 + rs
												 + "); use `assertDisjoint' or `assumeDisjoint' instead of `assert'",
												 q);
				}

			}
		}

	}

	private int
	emitQuantifiedExpressions(final ProblemReporter problem_reporter,
				  final int offset, final TypeDeclaration declaring_class)
	{
		this.linkage = new PredicateLinkage();

		for (int i = 0; i < this.qexprs.length; i++) {
			final PredicateFamily predicate_family = GCANodeFactory.SINGLETON.build(this.qexprs[i], this.scope);
			this.linkage.addPredicateFamily(predicate_family);
			Debug.println("extraction", predicate_family.toString());
		}
		this.linkage.link();
		return this.linkage.addMethods(declaring_class, this, offset);
	}

	public StringBuffer printExpression(int indent, StringBuffer output)
	{
		output.append(body.toString());
		return output;
	}


	/**
	 * Traverse an expression in the context of a blockScope
	 * @param visitor
	 * @param scope
	 */
	public void traverse(ASTVisitor visitor, BlockScope scope)
	{
		if (visitor.visit(this, scope))
			body.traverse(visitor, scope);
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
		if (visitor.visit(this, scope)) {
			body.traverse(visitor, scope);
		}
		visitor.endVisit(this, scope);
	}
	

	public TypeBinding resolveType(BlockScope scope) {

		this.predicate_object_bindings = new LocalVariableBinding(("__P" + predicate_object_bindings_counter++).toCharArray(),
									  null,
									  0,
									  false
									  );
		this.predicate_object_bindings.useFlag = LocalVariableBinding.USED;
		scope.addLocalVariable(this.predicate_object_bindings);
		this.predicate_object_bindings.setConstant(Constant.NotAConstant);

		final TypeBinding ty = this.body.resolveTypeExpecting(scope, TypeBinding.BOOLEAN);
		this.scope = scope;
		return TypeBinding.VOID;
	}

	public FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) {
		Iterator it = this.contained_names.iterator();
		while (it.hasNext()) {
			SingleNameReference snr = (SingleNameReference) it.next();
			flowInfo = snr.analyseCode(currentScope, flowContext, flowInfo);
		}

		return flowInfo;
	}
}
