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
public final class SetTraversalIdStep extends Step
{
  private int traversal_id;

  public SetTraversalIdStep(final int _traversal_id)
  {
    this.traversal_id = _traversal_id;
  }

  public boolean
  run(final GCAssertionsCollector _collector)
  {
    Assertion.assertion.traversal_id = this.traversal_id;
    _collector.setTraversalId(this.traversal_id);
    return false;
  }
}

