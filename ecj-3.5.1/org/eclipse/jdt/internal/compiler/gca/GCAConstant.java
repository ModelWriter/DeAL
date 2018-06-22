package org.eclipse.jdt.internal.compiler.gca;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.impl.Constant;

import java.util.*;
import org.eclipse.jdt.internal.compiler.codegen.*;

public class GCAConstant extends GCALiteral
{
	Constant value;
	int implicit_conversion;

	public GCAConstant(final Constant i, int implicit_conversion)
	{
		super(i.toString());
		this.value = i;
	}

	protected void
	doAccept(GCANodeVisitor visitor)
	{
		visitor.visitConstant(this);
	}

	public void
	emit(final GCAClosure closure, final CodeStream cs)
	{
		cs.generateConstant(this.value, this.implicit_conversion);
	}

}