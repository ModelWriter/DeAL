package org.eclipse.jdt.internal.compiler.ast;

import org.eclipse.jdt.core.compiler.*;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.flow.FlowInfo;
import org.eclipse.jdt.internal.compiler.flow.InitializationFlowContext;
import org.eclipse.jdt.internal.compiler.impl.*;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.problem.*;
import org.eclipse.jdt.internal.compiler.parser.*;
import org.eclipse.jdt.internal.compiler.gca.GCAClosure;
import org.eclipse.jdt.internal.compiler.util.Util;

/**
 * Auto-generated from Assertions
 */
public class AssertionPredicateMethod extends AbstractMethodDeclaration
{
	public Assertion assertion;
	public TypeDeclaration declaring_class;
	public int id;
	public GCAClosure closure;

	public AssertionPredicateMethod(final int _id,
					final Assertion _assertion,
					final TypeDeclaration _declaring_class,
					final GCAClosure _closure)
	{
		super(_declaring_class.compilationResult);
		this.declaring_class = _declaring_class;
		this.id = _id;
		this.assertion = _assertion;
		this.closure = _closure;
		final ClassScope class_scope = _declaring_class.scope;
		this.scope = new MethodScope(class_scope, class_scope.referenceContext, true);

		init();
	}

	public void
	init()
	{
		String suffix = "" + this.id;
		while (suffix.length() < 4)
			suffix = "0" + suffix;
		final String method_name = "__magic_predicate$" + suffix;

		this.modifiers = ClassFileConstants.AccFinal | ClassFileConstants.AccStatic | ClassFileConstants.AccPublic;
		this.selector = method_name.toCharArray();

		final TypeBinding object_ty_binding = scope.getJavaLangObject();
		final TypeBinding object_array_ty_binding = new ArrayBinding(scope.getJavaLangObject(), 1, scope.environment());
		final SingleTypeReference object_ty_ref = new SingleTypeReference("Object".toCharArray(), 0L);
		object_ty_ref.resolvedType = object_ty_binding;
		final SingleTypeReference object_array_ty_ref = new SingleTypeReference("Object[]".toCharArray(), 0L);
		object_ty_ref.resolvedType = object_array_ty_binding;

		this.binding =
			new MethodBinding(this.modifiers,
					  this.selector,
					  TypeBinding.BOOLEAN,
					  new TypeBinding[] { object_ty_binding,
							      object_array_ty_binding },
					  new ReferenceBinding[0],
					  this.declaring_class.binding);
		this.statements = null;

		final Argument arg_x = new Argument(new char[] {'x'}, 0L,
						    object_ty_ref,
						    ClassFileConstants.AccFinal);
		final Argument arg_env = new Argument(new char[] {'e', 'n', 'v'}, 0L,
						      object_array_ty_ref,
						      ClassFileConstants.AccFinal);
		arg_x.bind(this.scope, object_ty_binding, true);
		arg_env.bind(this.scope, object_array_ty_binding, true);
		this.arguments = new Argument[] { arg_x, arg_env };
	}
				

	public void analyseCode(ClassScope classScope, InitializationFlowContext initializationContext, FlowInfo info) {}
	public void parseStatements(Parser parser, CompilationUnitDeclaration unit) {}

	protected void
	generateStatementCode(final CodeStream code_stream)
	{
		closure.emit(this.scope, code_stream);
	}
}