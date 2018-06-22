package org.eclipse.jdt.internal.compiler.gca;
import org.eclipse.jdt.internal.compiler.*;

import java.util.*;

/**
 * Interface for filtering GCANodes
 */
public abstract class GCANodeTest
{
	public abstract boolean test(GCANode node);
}