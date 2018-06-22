package org.eclipse.jdt.internal.compiler.gca;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.lookup.*;

import java.util.*;

public abstract class GCANodeVisitor
{
	public void
	visitBinary(GCABinary _) {}

	public void
	visitUnary(GCAUnary _) {}

	public void
	visitConstant(GCAConstant _) {}

	public void
	visitInstanceOf(GCAInstanceOf _) {}

	public void
	visitNull(GCANull _) {}

	public void
	visitTrue(GCATrue _) {}

	public void
	visitFalse(GCAFalse _) {}

	public void
	visitIterationVar(GCAIterationVar _) {}

	public void
	visitEnvVar(GCAEnvVar _) {}

	public void
	visitTraversalConstant(GCATraversalConstant _) {}

	public void
	visitDereference(GCADereference _) {}
}
