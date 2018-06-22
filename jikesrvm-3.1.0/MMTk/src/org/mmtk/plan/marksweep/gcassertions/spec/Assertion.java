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
import org.mmtk.plan.marksweep.MS;

import org.mmtk.utility.options.Options;

/**
 * Base assertion class.  Acts as a container for `steps' and analyses
 * them
 *
 */
@Uninterruptible
public final class Assertion {

  private final Step[] steps;
  private final PredicateFamily[] predicate_families;

 // references to PredicateFamilys from check_pair
  private static ObjectReferenceArray active_predicate_families;
  private static int active_predicate_families_nr;
  static int traversal_id; // ID of our current traversal

  private long overlap_flags;
  private static long last_overlap_flags;

  // current assertion object
  static Assertion assertion = null;

  private void
  init()
  {
    active_predicate_families = ObjectReferenceArray.create(this.predicate_families.length + 1);
    active_predicate_families_nr = 0;
    traversal_id = -1;
  }

  int flags; // Flags.  Reset before each gc.  Updated and read by GC steps.

  /**
   * Constructs a new assertion.
   *
   * @param _steps The steps to execute, in order.  The first and the last three slots must be null.
   * @param _checks The check pairs (compiled predicate halves) to include
   */
  public Assertion(final PredicateFamily[] _checks,
		   final Step[] _steps)
  {
    this.steps = _steps;
    this.predicate_families = _checks;

    // First and last two steps exclude `this' from reachability/dominance analysis and make sure it is traced in the end
    if (VM.VERIFY_ASSERTIONS) VM.assertions._assert(_steps.length >= 3);
    if (VM.VERIFY_ASSERTIONS) VM.assertions._assert(_steps[0] == null);
    if (VM.VERIFY_ASSERTIONS) VM.assertions._assert(_steps[_steps.length - 1] == null);
    if (VM.VERIFY_ASSERTIONS) VM.assertions._assert(_steps[_steps.length - 2] == null);
    if (VM.VERIFY_ASSERTIONS) VM.assertions._assert(_steps[_steps.length - 3] == null);

    final Object[] self_root = new Object[_checks.length + 2];
    self_root[0] = this;
    self_root[1] = _steps;
    for (int i = 0; i < _checks.length; i++)
      self_root[i+2] = _checks[i];

    _steps[0] = new ExcludeNodesStep(self_root);
    _steps[_steps.length - 3] = new SetTraversalPredicatesStep(new int[] {}); // disable all checks
    _steps[_steps.length - 2] = new IncludeNodesStep(self_root);
    _steps[_steps.length - 1] = new TraversalStep(self_root);

    this.overlap_flags = 0l;
  }

  public final void
  setPredicateFamilyIndexVector(final int[] indices)
  {
    active_predicate_families_nr = 0;

    for (int i = 0; i < indices.length; i++) {
      if (this.predicate_families[i].prepareTest(indices[i])) {
	active_predicate_families.set(active_predicate_families_nr++,
				      ObjectReference.fromObject(this.predicate_families[i]));
      }
    }
  }

  @Inline
  public static final void
  test(final Object o)
  {
    for (int i = 0; i < active_predicate_families_nr; i++)
      if (((PredicateFamily)active_predicate_families.get(i).toObject()).test(o)) {
	// If the test yielded `true', we need no more testing for the predicate family.
	active_predicate_families.set(i--, active_predicate_families.get(--active_predicate_families_nr));
      }
  }

  @Inline
  public final void
  setTouchedFlag()
  {
    this.overlap_flags |= (1l << traversal_id);
  }

  @Inline
  public final void
  resetTouchedFlag()
  {
    this.overlap_flags &= ~(1l << traversal_id);
  }

  public boolean
  traversalOverlapped(final int _traversal_id)
  {
    return 0l != (this.overlap_flags & (1 << _traversal_id));
  }

  @Inline
  public static final int
  getTraversalId()
  {
    return traversal_id;
  }

  public final Step[]
  getSteps()
  {
    return this.steps;
  }


  /* ********************* */
  /* Public interface      */

  /**
   * Set the current assertion.  Used for debugging.
   *
   * @param _assertion The assertion to set
   */
  public static void
  setAssertion(final Assertion _assertion)
  {
    assertion = _assertion;

    int traversals = 0;
    for (Step step : _assertion.getSteps()) {
      if (step instanceof SetTraversalIdStep)
	++traversals;
    }

    final int traversals_allowed = GCAssertionsHeader.MAX_TRAVERSAL_ID;

    if (traversals > traversals_allowed) {
      Log.write("  Assertion:  Too many assertion overlaps checks:  ");
      Log.write(traversals);
      Log.write(" > ");
      Log.writeln(traversals_allowed);
      return;
    }

    assertion.init();
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
  @Interruptible
  public static final boolean
  doAssertAlways(final Assertion _assertion)
  {
    setAssertion(_assertion);
    System.gc();
    last_overlap_flags = assertion.overlap_flags;
    assertion = null;
    return true;
  }

  private static boolean initialised = false;
  private static int collection_counter = 0;
  private static int collection_frequency = 0;
  private static boolean collect_on_pressure = false;

  /**
   * Sets the frequency of GCAssert collections, outside of memory-pressure mandated ones.
   *
   * @param _frequency Frequency of extra collections.  0: always, -1: never, n: every (n+1)th opportunity
   */
  public static void
  setCollectionFrequency(final int _frequency)
  {
    collection_counter = collection_frequency = _frequency;
  }

  public static void
  setCollectOnMemoryPressure(boolean _do_collect)
  {
    collect_on_pressure = _do_collect;
  }

  @Interruptible
  private static final void
  initialise_config()
  {
    if (!initialised) {
      setCollectOnMemoryPressure(Options.assertOnHeapFull.getValue());
      setCollectionFrequency(Options.assertFrequency.getValue());
      initialised = true;
    }
  }

  /**
   * same as doAssertAlways, but only executes if there is enough gc pressure that we'd normally collect
   *
   * @param _assertion The assertion to test
   * @return whether or not the assertion was indeed tested
   */
  @Interruptible
  public static final boolean
  doAssert(final Assertion _assertion)
  {
    initialise_config();
//     System.err.print(collection_counter);
//     System.err.print(":");
    final boolean must_collect = (collection_counter--) == 0;
    if (collection_counter < 0)
      collection_counter = collection_frequency;

    setAssertion(_assertion);
    System.gc();
    last_overlap_flags = assertion.overlap_flags;
    assertion = null;

    final boolean suggest_collection = VM.activePlan.global().collectionAlmostRequired();

//     System.err.print("Collection decision: ");
//     System.err.print(must_collect);
//     System.err.print(" || (");
//     System.err.print(collect_on_pressure);
//     System.err.print(" && ");
//     System.err.print(suggest_collection);
//     System.err.println(")");

    if (collect_on_pressure)
	    return suggest_collection;
    else
	    return must_collect;
  }

  @Interruptible
  public static final void
  checkAssert(boolean b, boolean c)
  {
    if (b && !c)
      throw new Error("GC Assertions: failed assertion!");
    if (b && (last_overlap_flags != 0L))
      throw new Error("GC Assertions: not disjoint!");
  }

  @Inline
  public static final Assertion
  getAssertion()
  {
    return assertion;
  }
}
