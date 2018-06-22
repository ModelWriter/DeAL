package org.eclipse.jdt.internal.compiler.gca;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.lookup.*;

import java.util.*;

/**
 * Collects all GCADereference nodes that need guarding against null pointer exceptions
 */
public class GCATraversalDereferenceCollector extends GCANodeVisitor
{
	public static final GCATraversalDereferenceCollector SINGLETON = new GCATraversalDereferenceCollector();

	public ArrayList nodes = new ArrayList(); // will contain all collected

	public void
	clear()
	{
		this.nodes.clear();
	}

	public void
	visitDereference(GCADereference deref)
	{
		if (deref.children[0] instanceof GCAIterationVar
		    && deref.pathLength() > 1)
			this.nodes.add(deref);
	}
}
