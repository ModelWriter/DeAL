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
 * Tracks a specified object:  if we encounter this object at any time, we will set the flag at the index specified within.
 *
 * Not yet implemented, and left out for simplicity for now.
 */
@Uninterruptible
public abstract class TrackObjectStep extends Step {
  private final Object tracee;
  private final int flag_nr;

  /**
   * @param _obj The object to keep on the lookout for
   * @param _flag_nr Number of the flag to toggle if the object is encountered
   */
  public
  TrackObjectStep(final Object _obj, final int _flag_nr)
  {
    this.tracee = _obj;
    this.flag_nr = _flag_nr;
  }

  /**
   * Execute the step for the given collector and indicate whether a closure is requested.
   *
   * @param _collector The collector to execute the assertion step for.
   * @return true iff this execution should be followed by heap closure.
   */
  public abstract boolean
  run(final GCAssertionsCollector _collector);
}

