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

/**
 * Base assertion class.  Acts as a container for `steps' and analyses
 * them
 *
 */
public final class Assertion {

  /**
   * Constructs a new assertion.
   *
   * @param _steps The steps to execute, in order.  The first and the last three slots must be null.
   * @param _checks The check pairs (compiled predicate halves) to include
   */
  public Assertion(final PredicateFamily[] _checks,
		   final Step[] _steps)
  {
  }

  /**
   * Run an assertion
   *
   * This executes the garbage collection part of an assertion, assigning valuations to
   * PredicateFamily objects and the traversal ID.
   *
   * @param _assertion The assertion to test
   * @return whether or not the assertion was indeed tested
   */
  public static final boolean
  doAssert(final Assertion _assertion)
  {
    return true;
  }

  public static final void
  checkAssert(boolean b, boolean c)
  {
  }

}
