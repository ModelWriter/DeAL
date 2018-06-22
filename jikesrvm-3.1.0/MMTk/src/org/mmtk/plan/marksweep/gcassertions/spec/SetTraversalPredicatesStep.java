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
 * Selects a check pair to be installed for the next traversal, and indicates
 * whether the check pair should use its external or its internal component.
 */
@Uninterruptible
public final class SetTraversalPredicatesStep extends Step {
  private final int[] indices;

  public
  SetTraversalPredicatesStep(final int[] indices)
  {
    this.indices = indices;
  }

  public boolean
  run(final GCAssertionsCollector _collector)
  {
    Assertion.getAssertion().setPredicateFamilyIndexVector(this.indices);
    return false;
  }
}

