package org.eclipse.jdt.internal.compiler.gca;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.codegen.*;

import java.util.*;

/**
 * Such a constant represents a particular traversal.  Depending on the
 * traversal in question, we'll substitute TRUE or FALSE in it-- we never
 * generate code directly.
 */
public class GCATraversalConstant extends GCALiteral
{
	ReachSpec reach_spec;

	public
	GCATraversalConstant(final ReachSpec rs)
	{
		super("reach:" + rs.toString());
		this.reach_spec = rs;
	}

	protected void
	doAccept(GCANodeVisitor visitor)
	{
		visitor.visitTraversalConstant(this);
	}

	public void
	emit(final GCAClosure closure, final CodeStream cs)
	{
		throw new RuntimeException("Trying to emit code with traversal constant!");
	}
}