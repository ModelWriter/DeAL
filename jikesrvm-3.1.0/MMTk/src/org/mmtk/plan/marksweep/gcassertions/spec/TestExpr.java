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
 * Boolean expression, tests for certain properties.
 *
 */
@Uninterruptible
public abstract class TestExpr
{
  /**
   *
   */
  public abstract boolean
  eval();

  public TestExpr
  touchedTraversal(final int index)
  {
    return new TestExpr() {
      public boolean eval() { return Assertion.getAssertion().traversalOverlapped(index); }
    };
  }

  public TestExpr
  literal(final boolean v)
  {
    return new TestExpr() {
      public boolean eval() { return v; }
    };
  }

  public TestExpr
  negate(final TestExpr e)
  {
    return new TestExpr() {
      public boolean eval() { return !e.eval(); }
    };
  }
}