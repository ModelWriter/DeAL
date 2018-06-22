package org.eclipse.jdt.internal.compiler.gca;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.codegen.*;

import java.util.*;

public class GCAFalse extends GCALiteral
{
	private GCAFalse()
	{
		super("false");
	}

	public static final GCAFalse FALSE = new GCAFalse();

	protected void
	doAccept(GCANodeVisitor visitor)
	{
		visitor.visitFalse(this);
	}

	public void
	emit(final GCAClosure closure, final CodeStream cs)
	{
		cs.goto_(closure.false_label);
	}
}