package org.eclipse.jdt.internal.compiler.gca;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.codegen.*;

import java.util.*;

public class GCAInstanceOf extends GCANode
{
	TypeBinding ty;
	String ty_name;

	public
	GCAInstanceOf(final GCANode body, final TypeBinding ty)
	{
		super("instanceof", 1);
		this.children[0] = body;
		this.ty = ty;
		this.ty_name = ty.debugName();
	}

	public GCANode
	copy()
	{
		return new GCAInstanceOf(this.children[0], this.ty).setTypes(this.result_ty, this.implicit_conversion);
	}

	public boolean
	equals(Object o)
	{
		if (!super.equals(o))
			return false;
		if (!(o instanceof GCAInstanceOf))
			return false;
		final GCAInstanceOf other = (GCAInstanceOf) o;

		if (!other.ty_name.equals(this.ty_name))
			return false;

		return true;
	}

	public String
	toString()
	{
		return "instanceof(" + this.children[0] + ", " + this.ty_name + ")";
	}

	protected void
	doAccept(GCANodeVisitor visitor)
	{
		visitor.visitInstanceOf(this);
	}

	public void
	emit(final GCAClosure closure, final CodeStream cs)
	{
		this.children[0].emit(closure, cs);
		cs.instance_of(ty);
		closure.booleanJump(cs);
	}
}