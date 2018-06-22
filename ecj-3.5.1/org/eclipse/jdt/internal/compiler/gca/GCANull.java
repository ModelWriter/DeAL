package org.eclipse.jdt.internal.compiler.gca;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.codegen.*;

import java.util.*;

public class GCANull extends GCALiteral
{
	private GCANull()
	{
		super("null");
	}

	public static final GCANull NULL = new GCANull();

	protected void
	doAccept(GCANodeVisitor visitor)
	{
		visitor.visitNull(this);
	}

	public void
	emit(final GCAClosure closure, final CodeStream cs)
	{
		cs.aconst_null();
	}

}