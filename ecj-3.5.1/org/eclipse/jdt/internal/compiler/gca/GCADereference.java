package org.eclipse.jdt.internal.compiler.gca;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.codegen.*;

import java.util.*;

public class GCADereference extends GCANode
{
	FieldBinding[] path;

	public
	GCADereference(GCANode body, FieldBinding[] path)
	{
		super(stringify(path), 1);
		if (path == null)
			throw new RuntimeException("Path is null!");
		this.path = path;
		this.children[0] = body;
		for (int i = 0; i < path.length; i++)
			if (path[i] == null)
				throw new RuntimeException("Path." + i + " is null! (" + toString() + ")");
	}

	public int
	pathLength()
	{
		return this.path.length;
	}

	public GCADereference
	copyWithoutLastPathElement()
	{
		final FieldBinding[] new_path = new FieldBinding[this.path.length - 1];
		System.arraycopy(this.path, 0, new_path, 0, new_path.length);
		return (GCADereference) new GCADereference(this.children[0].copy(), new_path).setTypes(GCANodeFactory.java_lang_Object, 0);
	}

	public GCANode
	copy()
	{
		return new GCADereference(this.children[0].copy(), path).setTypes(this.result_ty, this.implicit_conversion);
	}

	public boolean
	equals(Object o)
	{
		if (!super.equals(o))
			return false;

		if (!(o instanceof GCADereference)) {
			return false;
		}
		final GCADereference other = (GCADereference) o;

		if (other.path.length != this.path.length) {
			return false;
		}
		return (stringify(this.path).equals(stringify(other.path)));
	}

	private static String
	stringify(FieldBinding[] path)
	{
		String retval = "#";
		for (int i = 0; i < path.length; i++) {
			if (i > 0)
				retval += ".";
			if (path[i] == null)
				retval += "?";
			else if (path[i].declaringClass == null) {
				retval += "!";
				retval += path[i].toString().replace(" ", "-");
			} else {
				retval += new String(path[i].declaringClass.signature());
				retval += path[i].toString().replace(" ", "-");
			}
		}
		return retval;
	}

	protected void
	doAccept(GCANodeVisitor visitor)
	{
		visitor.visitDereference(this);
	}

	public void
	emit(final GCAClosure closure, final CodeStream cs)
	{
		this.children[0].emit(closure, cs);
		for (int i = 0; i < path.length; i++) {
			final FieldBinding field = path[i];
			cs.fieldAccess(Opcodes.OPC_getfield, field, null);
		}
	}
}