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
package org.mmtk.plan.marksweep.gcassertions;

import org.mmtk.plan.marksweep.gcassertions.spec.Step;
import org.mmtk.plan.marksweep.gcassertions.spec.*;
import org.mmtk.plan.CollectorContext;
import org.mmtk.plan.StopTheWorldCollector;
import org.mmtk.plan.Phase;
import org.mmtk.plan.marksweep.MS;
import org.mmtk.plan.marksweep.MSCollector;
import org.mmtk.plan.marksweep.MSMutator;
import org.mmtk.utility.options.Options;
import org.mmtk.vm.VM;
import org.mmtk.policy.Space;
import org.vmmagic.pragma.*;
import org.vmmagic.unboxed.*;
import org.mmtk.utility.Log;

/**
 * This class implements <i>per-collector thread</i> behavior
 * and state for the <i>MS</i> plan, which implements a full-heap
 * mark-sweep collector.<p>
 *
 * Specifically, this class defines <i>MS</i> collection behavior
 * (through <code>trace</code> and the <code>collectionPhase</code>
 * method).<p>
 *
 * @see MS for an overview of the mark-sweep algorithm.<p>
 *
 * FIXME The SegregatedFreeList class (and its descendants such as
 * MarkSweepLocal) does not properly separate mutator and collector
 * behaviors, so the ms field below should really not exist in
 * this class as there is no collection-time allocation in this
 * collector.
 *
 * @see MS
 * @see MSMutator
 * @see StopTheWorldCollector
 * @see CollectorContext
 */
@Uninterruptible 
public class GCAssertionsCollector extends MSCollector {
  
  /** Trivial magic query method, used if we are running a fake query */
  private static final boolean
  __magic_true(Object _, Object[] __)
  {
    return false;
  }

  /** magic instance-of query method, for running a fake query */
  private static final boolean
  __magic_true3(Object obj, Object[] __)
  {
    return (obj instanceof Faux);
  }


  public static final boolean ASSERTIONS_ENABLED = true; // set to false to disable all assertion processing (and, hopefully, overhead)
  public static final boolean ALWAYS_TAG_TRAVERSAL = false; // If true: disable the `don't traversal tag unless GCAsserting' optimisation

  /****************************************************************************
   * Instance fields
   */
  protected GCAssertionsTraceLocal gcaTrace;
  
  /****************************************************************************
   *
   * Initialization
   */
  
   /**
   * Constructor
   */
  public GCAssertionsCollector() {
    
    super(new GCAssertionsTraceLocal(
        ((GCAssertions)VM.activePlan.global()).msTrace, null));
    gcaTrace = (GCAssertionsTraceLocal)fullTrace;
    init_static();
  }


  /****************************************************************************
  *
  * Hooks for predicate execution
  */

  @Inline
  public final void
  excludeNode(Object object)
  {
    final ObjectReference obj = ObjectReference.fromObject(object);

    if (!obj.isNull()
	&& Space.isInSpace(MS.MARK_SWEEP, obj)) {
//       Log.write("Excluding: ");
//       Log.writeln(obj);

      GCAssertionsHeader.setExcludedVisited(obj, GCAssertions.msSpace.isLive(obj));

      GCAssertions.msSpace.mark(obj);
      GCAssertionsHeader.setTraversalId(obj, GCAssertionsHeader.MAX_TRAVERSAL_ID);
    }
  }

  /**
   * Untag a boundary nodes and add it to the to-trace set
   *
   */
  @Inline
  public final void
  includeNode(Object object)
  {
    final ObjectReference obj = ObjectReference.fromObject(object);

    if (!obj.isNull()
	&& Space.isInSpace(MS.MARK_SWEEP, obj)) {
//       Log.write("Including: ");
//       Log.writeln(obj);
      if (GCAssertionsHeader.isExcludedVisited(obj))
	GCAssertions.msSpace.mark(obj);
      else
	GCAssertions.msSpace.unmark(obj);
      GCAssertionsHeader.setTraversalId(obj, GCAssertionsTraceLocal.getTraversalId());
    }
  }

  /**
   * Sets the Id to apply to heap traversals and activates traversal ID/overlap checking
   *
   */
  public final void
  setTraversalId(final int _trace_id)
  {
    GCAssertionsTraceLocal.setTraversalId(_trace_id);
  }

  /**
   * Enqueues a node for (parallel) collection
   *
   */
  @Inline
  public final void
  enqueueNodeForCollection(final Object obj)
  {
//     Log.write("Enqueueing: ");
//     Log.writeln(ObjectReference.fromObject(obj));
    MS.msSpace.traceObject(gcaTrace, ObjectReference.fromObject(obj));
    //    gcaTrace.processNode(ObjectReference.fromObject(obj));
  }

  /**
   * Enqueues the root set for traversal
   */
  public final void
  enqueueRootSet()
  {
//     Log.writeln("Root set traversal...");
    VM.scanning.computeThreadRoots(getCurrentTrace());
    VM.scanning.computeGlobalRoots(getCurrentTrace());
    VM.scanning.computeStaticRoots(getCurrentTrace());
    VM.scanning.computeBootImageRoots(getCurrentTrace());
    gcaTrace.processRoots();
  }
  
  /****************************************************************************
  *
  * Collection
  */

  /**
   * Perform a per-collector collection phase.
   *
   * @param phaseId The collection phase to perform
   * @param primary Perform any single-threaded activities using this thread.
   */
  @Inline
  public void collectionPhase(short phaseId, boolean primary) {
    if (org.mmtk.plan.TraceLocal.DEBUG_TRACING) {
      Log.write(" collectionPhase: ");
      Log.write(phaseId);
      Log.write(": ");
      Log.writeln(org.mmtk.plan.Phase.getName(phaseId));
    }

    if (phaseId == MS.ROOTS) // handled in the assertion program
      return;
    else if (phaseId == MS.STACK_ROOTS) // handled in the assertion program
      return;
    else if (phaseId == GCAssertions.INITIATE) {
      super.collectionPhase(phaseId, primary);

    } else if (phaseId == GCAssertions.PROCESS) {
      if (Assertion.getAssertion() == null) {
        int dummyAssertion = Options.dummyAssertion.getValue();
        if (dummyAssertion > 0) {
          switch (dummyAssertion) {
            case 1: 
              Assertion.setAssertion(fake_debug_assertion);
              //Log.writeln("Always true");
              break;
            case 2:
              Assertion.setAssertion(fake_debug_assertion2);
              //Log.writeln("Always true with traversal ID");
              break;
            case 3:
              Assertion.setAssertion(fake_debug_assertion3);
              //Log.writeln("instanceof");
              break;
            case 4: 
              Assertion.setAssertion(fake_debug_assertion4);
              //Log.writeln("instanceof with traversal ID");
              break;
            default: 
              VM.assertions._assert(false, "Invalid dummy assertion");
          }
	}
	else
	  Assertion.setAssertion(default_assertion);
      }

      final Assertion assertion = Assertion.getAssertion();

      final Step[] steps = assertion.getSteps();
      int program_counter = 0;
      setTraversalId(0);

      // execute program
      while (program_counter < steps.length) {

	final boolean needs_closure = steps[program_counter++].run(this);

	if (needs_closure)
	  super.collectionPhase(MS.CLOSURE, primary);

	if (GCAssertionsTraceLocal.getOverlapObject() != null) {
	  Assertion.getAssertion().setTouchedFlag();
	}
      }
      // finished executing

      return;
    }
  
    super.collectionPhase(phaseId, primary);
  }


  // --------------------------------------------------------------------------------
  // Debug predicate
  private static Assertion fake_debug_assertion;
  private static Assertion fake_debug_assertion2;
  private static Assertion fake_debug_assertion3;
  private static Assertion fake_debug_assertion4;
  private static Assertion default_assertion;

  @Interruptible
  static private final void init_static ()
  {
    default_assertion =
      new Assertion(new PredicateFamily[0],
		    new Step[] { null,
				 new TraversalStep(), // mark everything from the roots
				 null,
				 null,
				 null });


    final PredicateFamily dummy_predicate =
      new PredicateFamily(GCAssertionsCollector.class,
			  new int[] { 0 },
			  new Object[] {},
			  PredicateFamily.UNIVERSAL);

    final PredicateFamily dummy_predicate3 =
      new PredicateFamily(GCAssertionsCollector.class,
			  new int[] { 1 },
			  new Object[] {},
			  PredicateFamily.UNIVERSAL);

    fake_debug_assertion =
      new Assertion(new PredicateFamily[] { dummy_predicate },
		    new Step[] { null,
				 new SetTraversalPredicatesStep(new int[] { 0 }), // activate test
				 new TraversalStep(), // mark everything from the roots
				 null,
				 null,
				 null });

    fake_debug_assertion2 =
      new Assertion(new PredicateFamily[] { dummy_predicate },
		    new Step[] { null,
				 new SetTraversalIdStep(0), // Nonsensical traversal ID setting will trigger traversal ID accounting
				 new SetTraversalPredicatesStep(new int[] { 0 }), // activate test
				 new TraversalStep(), // mark everything from the roots
				 null,
				 null,
				 null });

    fake_debug_assertion3 =
      new Assertion(new PredicateFamily[] { dummy_predicate3 },
		    new Step[] { null,
				 new SetTraversalPredicatesStep(new int[] { 0 }), // activate test
				 new TraversalStep(), // mark everything from the roots
				 null,
				 null,
				 null });
    
    fake_debug_assertion4 =
      new Assertion(new PredicateFamily[] { dummy_predicate3 },
        new Step[] { null,
         new SetTraversalIdStep(0), // Nonsensical traversal ID setting will trigger traversal ID accounting
         new SetTraversalPredicatesStep(new int[] { 0 }), // activate test
         new TraversalStep(), // mark everything from the roots
         null,
         null,
         null });
  }

  private static final class Faux {};
}