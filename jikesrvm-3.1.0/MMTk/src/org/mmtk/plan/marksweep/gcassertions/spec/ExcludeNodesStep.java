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
 */
@Uninterruptible
public final class ExcludeNodesStep extends Step {
  private final Object[] nodes;

  public ExcludeNodesStep(final Object[] _nodes)
  {
    this.nodes = _nodes;
  }

  public boolean
  run(final GCAssertionsCollector _collector)
  {
    if (this.nodes != null)
      for (Object o : this.nodes)
	_collector.excludeNode(o);
    return false;
  }
}

