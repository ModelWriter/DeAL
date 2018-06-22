package org.eclipse.jdt.internal.compiler.gca;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.lookup.*;

import java.util.*;

/**
 * GCANodes represent boolean expressions found within QuantifiedExpressions in the regular
 * syntax tree.  Unlike regular AST nodes, GCANodes facilitate substitution, simplification,
 * comparison, and hashing.
 */
public abstract class GCANode
{
	public GCANode[] children;
	public String op;
	public TypeBinding result_ty;
	public int implicit_conversion;

	protected
	GCANode(final String _op, final int children_nr)
	{
		this.children = new GCANode[children_nr];
		this.op = _op;
	}

	public int
	hashCode()
	{
		int hc = op.hashCode();
		for (int i = 0; i < this.children.length; i++)
			hc += (this.children[i].hashCode() << 2);
		return hc;
	}

	public boolean
	equals(Object o)
	{
		if (o instanceof GCANode) {
			final GCANode other = (GCANode) o;

			if (!other.op.equals(this.op)) {
				return false;
			}
			if (this.children.length != other.children.length){
				return false;
			}
			for (int i = 0; i < this.children.length; i++)
				if (!this.children[i].equals(other.children[i])) {
					return false;
				}
			return true;
		} else
			return false;
	}

	public GCANode
	subst(GCANodeTest t, GCANode substnode)
	{
		if (t.test(this))
			return substnode;
		else {
			for (int i = 0; i < this.children.length; i++)
				this.children[i] = this.children[i].subst(t, substnode);
			return this;
		}
	}

	// Constant folding
	public GCANode
	fold()
	{
		for (int i = 0; i < this.children.length; i++)
			this.children[i] = this.children[i].fold();
		return this;
	}

	public abstract void
	emit(final GCAClosure closure, final CodeStream cs);

	/**
	 * Under the hypothesis that we have a boxed value of the expected type, unbox
	 */
	public void
	emitUnboxing(final CodeStream cs)
	{
		cs.generateUnboxingConversion(result_ty.id);
	}

	public void
	emitImplicitConversion(final CodeStream cs)
	{
		cs.generateImplicitConversion(this.implicit_conversion);
	}

	public abstract GCANode
	copy();


	public GCANode
	setTypes(final TypeBinding _result_ty, final int _implicit_conversion)
	{
		this.result_ty = _result_ty;
		this.implicit_conversion = _implicit_conversion;
		return this;
	}

	public void
	accept(GCANodeVisitor visitor)
	{
		this.doAccept(visitor);
		for (int i = 0; i < this.children.length; i++)
			this.children[i].accept(visitor);
	}

	protected abstract void
	doAccept(GCANodeVisitor visitor);

	public String
	toString()
	{
		if (this.children.length == 0)
			return op;
		else {
			String s =  op + "(";
			for (int i = 0; i < this.children.length; i++) {
				s += this.children[i];
				if (i + 1 < this.children.length)
					s += ", ";
			}
			return s + ")";
		}
	}
}