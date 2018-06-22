/*
 *  This file is part of the Jikes RVM project (http://jikesrvm.org).
 *
 *  This file is licensed to You under the Common Public License (CPL);
 *  You may not use this file except in compliance with the License. You
 *  may obtain a copy of the License at
 *
 *      http://www.opensource.org/licenses/cpl1.0.php
 *
 *  See the COPYRIGHT.txt file distributed with this work for information
 *  regarding copyright ownership.
 */
package org.mmtk.plan.marksweep.gcassertions.spec;

import org.mmtk.plan.marksweep.gcassertions.*;
import org.vmmagic.pragma.*;
import org.vmmagic.unboxed.*;

import org.mmtk.utility.Log;
import org.mmtk.vm.VM;


/**
 * Assertion step.  One part of assertion execution.
 *
 * Traversal enqueues the specified node(s) and initiates recursive sweeps.
 */
@Uninterruptible
public final class TraversalStep extends Step {
  private final Object[] nodes;

  /**
   * Traverse the specified nodes.
   */
  public TraversalStep(final Object[] _nodes)
  {
    this.nodes = _nodes;
  }

  /**
   * Traverse the root set.
   */
  public TraversalStep()
  {
    this.nodes = null;
  }

  public boolean
  run(final GCAssertionsCollector _collector)
  {
    if (this.nodes == null)
      _collector.enqueueRootSet();
    else for (Object obj : this.nodes)
	   if (obj != null)
	     _collector.enqueueNodeForCollection(obj);
    return true; // demand closure
  }
}

