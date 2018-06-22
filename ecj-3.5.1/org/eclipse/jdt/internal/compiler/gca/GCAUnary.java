package org.eclipse.jdt.internal.compiler.gca;
import org.eclipse.jdt.internal.compiler.ast.OperatorIds;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;

import java.util.*;

public class GCAUnary extends GCANode implements OperatorIds
{
	int op_id;

	public GCAUnary(final int _op_id, final String name, final GCANode child)
	{
		super(name, 1);
		this.op_id = _op_id;
		this.children[0] = child;

		if (_op_id == NOT) {
			this.implicit_conversion = 0;
			this.result_ty = TypeBinding.BOOLEAN;
		}
	}

	public GCANode
	fold()
	{
		final GCANode child = this.children[0] = this.children[0].fold();

		switch (this.op_id) {
		case NOT:
			if (child == GCATrue.TRUE)
				return GCAFalse.FALSE;
			else if (child == GCAFalse.FALSE)
				return GCATrue.TRUE;
			else if (child instanceof GCAUnary // double negation elimination
				 && ((GCAUnary) child).op_id == NOT)
				return child.children[0];
			else if (child instanceof GCABinary) {
				GCABinary b = (GCABinary) child;
				if (b.op_id == OperatorIds.NOT_EQUAL) {
					b.op_id = OperatorIds.EQUAL_EQUAL;
					b.op = "==";
					return child;
				} else if (b.op_id == OperatorIds.EQUAL_EQUAL) {
					b.op_id = OperatorIds.NOT_EQUAL;
					b.op = "!=";
					return child;
				}
			}
		default:
			return this;
		}
	}

	public GCANode
	copy()
	{
		return new GCAUnary(this.op_id, this.op, this.children[0].copy()).setTypes(this.result_ty, this.implicit_conversion);
	}

	protected void
	doAccept(GCANodeVisitor visitor)
	{
		visitor.visitUnary(this);
	}

	public void
	emit(final GCAClosure closure, final CodeStream cs)
	{
		closure.pushLabels();
		switch (this.op_id) {
		case NOT: {
			closure.swapLabels();
			this.children[0].emit(closure, cs);
			break;
		}
		default:
			throw new RuntimeException("Unknown unary operator `" + this.op + "'");
		}
		closure.popLabels();
	}
}