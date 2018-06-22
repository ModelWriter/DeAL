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

import org.vmmagic.pragma.*;
import org.vmmagic.unboxed.*;

import org.mmtk.utility.Log;

/**
 * A single predicate, specialised to different cases
 *
 */
@Uninterruptible
public final class PredicateFamily
{
  private final int[] call_address_indices; // Which address to invoke for the nth variant of the predicate (index into the compiler-internal table)
  private Address current_address;
  private final Object[] environment;
  private final boolean existential; // Existential test?  Otherwise universal.

  private boolean completed = false; // `completed' for a universal test means `failed', for an extistential test it means `satisfied'

  public static final int IGNORE_TEST = -1;

  public static final boolean UNIVERSAL = false;
  public static final boolean EXISTENTIAL = true;

  private final static int
  cache_test(final Class c, final int test_index)
  {
    if (test_index == IGNORE_TEST)
      return IGNORE_TEST;
    else
      return org.vmmagic.StaticOptTable.registerStaticMethodHook(c, test_index);
  }

  /**
   *
   * If this is a universal family, the predicates must return the negation of whether they apply.  That is,
   * each test returns true iff the test concludes testing for this predicate family.
   *
   * @param c A class containing a static method of signature "(Object, Object[]): boolean" that encodes the test
   * @param _method_indices list of indices of static methods on class c, or IGNORE_TEST, implementing each predicate
   * @param _environment Any variables that should be passed into the test as its environment
   * @param _existential Whether the test is existentially quantified
   */
  public PredicateFamily(final Class c,
			 final int[] _method_indices,
			 final Object[] _environment,
			 final boolean _existential)
  {
    this.call_address_indices = new int[_method_indices.length];
    for (int i = 0; i < _method_indices.length; i++)
      this.call_address_indices[i] = cache_test(c, _method_indices[i]);

    this.environment = _environment;
    this.existential = _existential;
  }


  // --------------------------------------------------------------------------------
  // test running

  private final Address
  lookupTestAddress(final int index)
  {
    if (index == IGNORE_TEST)
      return null;
    else
      return org.vmmagic.StaticOptTable.lookupStaticMethodHook(index);
  }

  /**
   * Set the predicate (by index) within the family
   *
   * @param index Predicate index to set
   * @return false iff the predicate is trivial (in that case, the predicate's test method must not be called)
   */
  final boolean
  prepareTest(int index)
  {
    this.current_address = lookupTestAddress(this.call_address_indices[index]);
    return this.shouldBeTested();
  }

  /**
   * Run the relevant test
   *
   * @param node The node to test
   * @return true iff the predicate family is definitely satisfied/dissatisfied and needs not to be tested anymore
   */
  @Inline
  public final boolean
  test(final Object node)
  {
    if (org.vmmagic.StaticOptTable.invokeStaticMethod(node, this.environment, this.current_address)) {
      // set the following to trigger NPEs to make sure that `Assertion' doesn't forget to take care of them
//       Log.write("Finished on: ");
//       Log.writeln(ObjectReference.fromObject(node));
    
      this.current_address = null;
      this.completed = true;
      return true;
    }
    return false;
  }

  /**
   * Compute the boolean valuation of this predicate family
   *
   * Only meaningful after all traversals have been completed.
   *
   * @return true iff the predicate family evaluates to `true.'
   */
  public final boolean
  value()
  {
    return this.existential == this.completed; // We EITHER found a witness for an existential test OR no counter-example for a universal test.
  }

  /**
   * Determine whether the selected predicate in this predicate family can and should be tested
   *
   * @return true iff this predicate family should be tested
   */
  @Inline
  public final boolean
  shouldBeTested()
  {
    return !this.completed && this.current_address != null;
  }

  /**
   * determine whether this is an existential test
   */
  @Inline
  public final boolean
  isExistential()
  {
    return this.existential;
  }
}

