package org.eclipse.jdt.internal.compiler.gca;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.codegen.*;

import java.util.*;

public class GCATrue extends GCALiteral
{
	private GCATrue()
	{
		super("true");
	}

	public static final GCATrue TRUE = new GCATrue();

	protected void
	doAccept(GCANodeVisitor visitor)
	{
		visitor.visitTrue(this);
	}

	public void
	emit(final GCAClosure closure, final CodeStream cs)
	{
		cs.goto_(closure.true_label);
	}
}