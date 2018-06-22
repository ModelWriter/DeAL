package org.eclipse.jdt.internal.compiler.gca;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.codegen.*;

import java.util.*;

public class GCAIterationVar extends GCALiteral
{
	private
	GCAIterationVar()
	{
		super("x");
	}

	public static final GCAIterationVar VAR = new GCAIterationVar();

	protected void
	doAccept(GCANodeVisitor visitor)
	{
		visitor.visitIterationVar(this);
	}

	public void
	emit(final GCAClosure closure, final CodeStream cs)
	{
		closure.loadIterationVar(cs);
	}
}