package org.eclipse.jdt.internal.compiler.gca;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.codegen.*;

import java.util.*;

public class GCAEnvVar extends GCALiteral
{
	int index;

	public
	GCAEnvVar(final int _index)
	{
		super("$" + _index);
		this.index = _index;
	}

	protected void
	doAccept(GCANodeVisitor visitor)
	{
		visitor.visitEnvVar(this);
	}

	public void
	emit(final GCAClosure closure, final CodeStream cs)
	{
		cs.aload_1();
		cs.generateInlinedValue(this.index);
		cs.aaload();

		// convert Object to whatever we need
		if (this.result_ty.isBaseType()) {
			final TypeBinding interty = closure.scope.environment().computeBoxingType(this.result_ty);
			cs.checkcast(interty);
		}

		this.emitUnboxing(cs);
		this.emitImplicitConversion(cs);
		if (this.result_ty == TypeBinding.BOOLEAN)
			closure.booleanJump(cs);
	}
}