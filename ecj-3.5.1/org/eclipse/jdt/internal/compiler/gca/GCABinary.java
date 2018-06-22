package org.eclipse.jdt.internal.compiler.gca;
import org.eclipse.jdt.internal.compiler.ast.OperatorIds;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.codegen.*;

import java.util.*;

public class GCABinary extends GCANode implements OperatorIds
{
	int op_id;

	public
	GCABinary(final int _op_id, final String name, final GCANode lhs, final GCANode rhs)
	{
		super(name, 2);
		this.op_id = _op_id;
		this.children[0] = lhs;
		this.children[1] = rhs;

		if (this.children[0].result_ty == null) {
			System.err.println("BINOP " + name + ": child 0 has no ty: " + lhs);
			try { throw new RuntimeException("Foo"); } catch (RuntimeException e) { e.printStackTrace(); };
		}
		if (this.children[1].result_ty == null)
			System.err.println("BINOP " + name + ": child 1 has no ty: " + rhs);
	}

	public GCANode
	fold()
	{
		super.fold();

		switch (this.op_id) {
		case AND_AND:
			for (int i = 0; i <= 1; i++)
				if (this.children[i] == GCATrue.TRUE)
					return this.children[1 - i];
			for (int i = 0; i <= 1; i++)
				if (this.children[i] == GCAFalse.FALSE)
					return GCAFalse.FALSE;
			return this;

		case OR_OR:
			for (int i = 0; i <= 1; i++)
				if (this.children[i] == GCAFalse.FALSE)
					return this.children[1 - i];
			for (int i = 0; i <= 1; i++)
				if (this.children[i] == GCATrue.TRUE)
					return GCATrue.TRUE;

		default:
			return this;
		}
	}

	public GCANode
	copy()
	{
		return new GCABinary(this.op_id, this.op, this.children[0].copy(), this.children[1].copy()).setTypes(this.result_ty, this.implicit_conversion);
	}

	private boolean
	fp_compare(final CodeStream cs)
	{
		if (this.children[0].result_ty == TypeBinding.DOUBLE || this.children[1].result_ty == TypeBinding.DOUBLE) {
			cs.dcmpl();
			return true;
		} else if (this.children[0].result_ty == TypeBinding.FLOAT || this.children[1].result_ty == TypeBinding.FLOAT) {
			cs.fcmpl();
			return true;
		} else
			return false;
	}

	protected void
	doAccept(GCANodeVisitor visitor)
	{
		visitor.visitBinary(this);
	}

	public void
	emit(final GCAClosure closure, final CodeStream cs)
	{
		closure.pushLabels();
		switch (this.op_id) {
		case AND_AND: {
			closure.pushLabels();
			closure.true_label = new BranchLabel(cs);

			children[0].emit(closure, cs);

			closure.true_label.place();
			closure.popLabels();

			children[1].emit(closure, cs);
			break;
		}
		case OR_OR: {
			closure.pushLabels();
			closure.false_label = new BranchLabel(cs);

			children[0].emit(closure, cs);

			closure.false_label.place();
			closure.popLabels();

			children[1].emit(closure, cs);
			break;
		}
		case NOT_EQUAL:
			closure.swapLabels();
		case EQUAL_EQUAL: {
			if (children[0].result_ty == TypeBinding.BOOLEAN || children[1].result_ty == TypeBinding.BOOLEAN) {
				closure.pushLabels();
				closure.true_label = new BranchLabel(cs);
				closure.false_label = new BranchLabel(cs);
				BranchLabel result_label = new BranchLabel(cs);

				children[0].emit(closure, cs);
				closure.false_label.place();
				cs.iconst_0();
				cs.goto_(result_label);
				closure.true_label.place();
				cs.iconst_1();
				result_label.place();

				closure.true_label = new BranchLabel(cs);
				closure.false_label = new BranchLabel(cs);
				result_label = new BranchLabel(cs);
				children[1].emit(closure, cs);
				closure.false_label.place();
				cs.iconst_0();
				cs.goto_(result_label);
				closure.true_label.place();
				cs.iconst_1();
				result_label.place();

				closure.popLabels();
				cs.if_icmpeq(closure.true_label);
			} else if (children[0].result_ty.isNumericType()) {
				children[0].emit(closure, cs);
				children[1].emit(closure, cs);
				if (fp_compare(cs))
					cs.ifeq(closure.true_label);
				else
					cs.if_icmpeq(closure.true_label);
			} else {
				children[0].emit(closure, cs);
				if (children[1] == GCANull.NULL)
					cs.ifnull(closure.true_label);
				else {
					children[1].emit(closure, cs);
					cs.if_acmpeq(closure.true_label);
				}
			}
			cs.goto_(closure.false_label);
			break;
		}
		case GREATER:
			closure.swapLabels();
		case LESS_EQUAL: {
			children[0].emit(closure, cs);
			children[1].emit(closure, cs);
			if (fp_compare(cs))
				cs.ifle(closure.true_label);
			else
				cs.if_icmple(closure.true_label);
			cs.goto_(closure.false_label);
			break;
		}
		case LESS:
			closure.swapLabels();
		case GREATER_EQUAL: {
			children[0].emit(closure, cs);
			children[1].emit(closure, cs);
			if (fp_compare(cs))
				cs.ifge(closure.true_label);
			else
				cs.if_icmpge(closure.true_label);
			cs.goto_(closure.false_label);
			break;
		}

		default:
			throw new RuntimeException("Unknown binary operator `" + this.op + "'");
		}
		closure.popLabels();
	}

}