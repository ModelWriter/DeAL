package org.eclipse.jdt.internal.compiler.gca;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.codegen.*;

import java.util.*;


public class BindingWrapper
{
	public NameReference name_reference;
	public Binding source;
	public FieldBinding[] path; // null for static fields

	public void
	emitLoad(final CodeStream cs, final BlockScope scope, final PredicateLinkage plinkage)
	{
		name_reference.generateCode(scope, cs, true);
		//		cs.load(source);
		if (this.path != null) {
			for (int i = 0; i < this.path.length; i++) {
				final FieldBinding field = this.path[i];
				cs.fieldAccess(Opcodes.OPC_getfield, field, null);
			}
		}
		if (this.name_reference.resolvedType.isBaseType()) {
			// Need boxing!
			cs.generateBoxingConversion(this.name_reference.resolvedType.id);
		}
	}

// 	public
// 	BindingWrapper(final Binding single)
// 	{
// 		this.source = single;
// 		this.path = new FieldBinding[0];
// 	}

	public
	BindingWrapper(final SingleNameReference snr)
	{
		this.name_reference = snr;
		this.source = snr.binding;
		this.path = new FieldBinding[0];
	}

	public
	BindingWrapper(final QualifiedNameReference nr)
	{
		this.name_reference = nr;
		this.source = nr.binding;
		this.path = nr.otherBindings;
	}

	public boolean
	equals(Object o)
	{
		if (o instanceof BindingWrapper) {
			final BindingWrapper other = (BindingWrapper) o;
			if (other.path.length != this.path.length) {
				System.err.println("  => different path length");
				return false;
			}

			if (!(new String(other.source.computeUniqueKey())).equals(new String(this.source.computeUniqueKey()))) {
				System.err.println("  => Different source");
				return false;
			}

			for (int i = 0; i < this.path.length; i++)
				if (!((new String(this.path[i].computeUniqueKey()))).equals(new String (other.path[i].computeUniqueKey()))) {
					System.err.println("  => Path index key mismatch");
					return false;
				}
			return true;
		} else
			return false;
	}

	public int
	hashCode()
	{
		int v = 0;
		if (this.source != null)
			v = (new String(source.computeUniqueKey())).hashCode();

		if (this.path == null)
			return v;

		for (int i = 0; i < path.length; i++)
			v += (new String(path[i].computeUniqueKey())).hashCode();
		return v;
	}

	public String
	toString()
	{
		String retval;
		if (this.source == null)
			retval = "*";
		else
			retval = new String(this.source.toString().replace(" ", "-"));
		if (this.path == null)
			return "STATIC:" + retval;
		for (int i = 0; i < path.length; i++) {
			retval += ".";
			retval += (path[i] == null)? "*" : path[i].toString().replace(" ", "-");
		}
		return retval;
	}
}