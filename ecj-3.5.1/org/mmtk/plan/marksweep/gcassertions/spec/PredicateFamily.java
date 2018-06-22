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
 * A single predicate, specialised to different cases
 *
 */
public final class PredicateFamily
{
  public PredicateFamily(final Class c,
			 final int[] _method_indices,
			 final Object[] _environment,
			 final boolean _existential)
  {
  }

  public final boolean
  value()
  {
    return true;
  }
}

