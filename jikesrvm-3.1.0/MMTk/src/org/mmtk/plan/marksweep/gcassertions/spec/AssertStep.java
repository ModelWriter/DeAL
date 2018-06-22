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
 * Asserts that a specific property is set.
 * Not implemented at this time.
 */
@Uninterruptible
public abstract class AssertStep extends Step {
  private final TestExpr test;
  private final String explanation;

  public AssertStep(final TestExpr _test, final String _explanation)
  {
    this.test = _test;
    this.explanation = _explanation;
  }

  public abstract boolean
  run(final GCAssertionsCollector _collector);
//   {
//     if (!this.test.eval()) {
//       Assertion.assertion.fail();
//       Log.write("  ");
//       Log.writeln(this.explanation);
//     }
//     return false;
//   }
}

