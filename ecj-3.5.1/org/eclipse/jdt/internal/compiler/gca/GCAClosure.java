package org.eclipse.jdt.internal.compiler.gca;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.codegen.*;

import java.util.*;

public class GCAClosure
{
	public final BindingWrapper[] env;
	public final GCANode body;
	public final TypeBinding query_var_ty;

	// during code emitting:
	public int self_index;
	BranchLabel true_label, false_label;
	public LinkedList true_label_stack = new LinkedList();
	public LinkedList false_label_stack = new LinkedList();
	public BlockScope scope;

	public
	GCAClosure(final TypeBinding _query_var_ty,
		   final GCANode _body,
		   final BindingWrapper[] _env)
	{
		this.query_var_ty = _query_var_ty;
		this.env = _env;
		this.body = _body;
	}

	public int
	hashCode()
	{
		int hc = body.hashCode();
		for (int i = 0; i < env.length; i++) {
			hc <<= 2;
			hc ^= env[i].hashCode();
		}
		return hc;
	}

	public boolean
	equals(Object o)
	{
		if (o instanceof GCAClosure) {
			final GCAClosure other = (GCAClosure) o;

			if (other.env.length != this.env.length)
				return false;
			if (!other.body.equals(this.body))
				return false;

			for (int i = 0; i < this.env.length; i++)
				if (!other.env[i].equals(this.env[i])) {
					return false;
				}

			return true;
		} else
			return false;
	}

	public String
	toString()
	{
		if (env.length == 0)
			return body.toString();
		String s = "{";
		for (int i = 0; i < env.length; i++) {
			if (i > 0)
				s += ", ";
			s += "$" + i + " = " + env[i].toString();
		}
		return s + "} " + body;
	}

	public void
	emit(BlockScope scope, CodeStream code_stream)
	{
		this.self_index = 0;
		this.scope = scope;
		if (this.query_var_ty == scope.getJavaLangObject())
			this.self_index = 0;
		else {
//			System.err.println("Emitting for non-Object: " + query_var_ty.debugName());
			// emit prelude that returns `false' if the checked cast would fail
			final BranchLabel success_label = new BranchLabel(code_stream);
			code_stream.aload_0();
			code_stream.instance_of(this.query_var_ty);
			code_stream.ifne(success_label);
			code_stream.generateInlinedValue(0);
			code_stream.ireturn();
			success_label.place();
			code_stream.aload_0();
			code_stream.checkcast(this.query_var_ty);
			code_stream.astore_2();
			this.self_index = 2;
		}

		if (this.body == GCATrue.TRUE) {
			code_stream.generateInlinedValue(1);
			code_stream.ireturn();
		} else {
			final BranchLabel t_label = this.true_label = new BranchLabel(code_stream);
			final BranchLabel f_label = this.false_label = new BranchLabel(code_stream);

			this.body.emit(this, code_stream);

			f_label.place();
			code_stream.generateInlinedValue(0);
			code_stream.ireturn();
			t_label.place();
			code_stream.generateInlinedValue(1);
			code_stream.ireturn();
		}
	}

	public void
	swapLabels()
	{
		final BranchLabel swap = this.true_label;
		this.true_label = this.false_label;
		this.false_label = swap;
	}

	public void
	pushLabels()
	{
		this.true_label_stack.addFirst(this.true_label);
		this.false_label_stack.addFirst(this.false_label);
	}

	public void
	popLabels()
	{
		this.true_label = (BranchLabel) this.true_label_stack.removeFirst();
		this.false_label = (BranchLabel) this.false_label_stack.removeFirst();
	}

	/**
	 * Interpret TOS as boolean and jump as needed
	 */
	public void
	booleanJump(final CodeStream cs)
	{
		cs.ifne(this.true_label);
		cs.goto_(this.false_label);
	}

	public void
	loadIterationVar(CodeStream cs)
	{
		if (this.self_index == 0) // unconverted
			cs.aload_0();
		else if (this.self_index == 2) // converted
			cs.aload_2();
		else
			cs.aload(self_index);
	}

	/**
	 * Always returns false?
	 */
	public boolean
	isTrivial()
	{
		return this.body == GCAFalse.FALSE;
	}
}
