package org.eclipse.jdt.internal.compiler.ast;

import org.eclipse.jdt.internal.compiler.impl.*;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.flow.*;
import org.eclipse.jdt.internal.compiler.lookup.*;

public class QuantifiedExpressionResultIndex extends Expression
{

	public final int index;

	public
	QuantifiedExpressionResultIndex(final int _index)
	{
		super();
		this.index = _index;
	}

	public String
	toString()
	{
		return "$_" + index;
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
		throw new RuntimeException("FIXME!");
	}
}
