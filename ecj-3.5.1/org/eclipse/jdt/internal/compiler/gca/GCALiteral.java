package org.eclipse.jdt.internal.compiler.gca;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.lookup.*;

import java.util.*;

public abstract class GCALiteral extends GCANode
{
	protected GCALiteral(final String _s)
	{
		super(_s, 0);
	}

	public GCANode
	fold()
	{
		return this; // nothing to fold
	}

	public GCANode
	copy()
	{
		return this; // no need to duplicate
	}
}