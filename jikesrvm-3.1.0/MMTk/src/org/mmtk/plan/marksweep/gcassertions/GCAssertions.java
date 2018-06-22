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

import org.mmtk.vm.Lock;
import org.mmtk.vm.SynchronizedCounter;
import org.mmtk.plan.Phase;
import org.mmtk.plan.TransitiveClosure;
import org.mmtk.plan.marksweep.MS;
import org.mmtk.plan.marksweep.gcassertions.GCAssertionsHeader;
import org.mmtk.utility.Log;
import org.mmtk.utility.deque.SharedDeque;
import org.mmtk.utility.options.Options;
import org.mmtk.utility.options.GCAssertionsDummy;
import org.mmtk.utility.options.GCAssertionsCollectionOnPressure;
import org.mmtk.utility.options.GCAssertionsCollectionFrequency;
import org.mmtk.utility.options.PrintGCAssertionsWarnings;
import org.mmtk.utility.options.PrintGCAssertionsStats;
import org.mmtk.utility.ArrayHelpers;
import org.mmtk.vm.VM;

import org.vmmagic.pragma.*;
import org.vmmagic.unboxed.*;

/**
 * This class implements the global state of a our GCAssertions collector.
 * It is based on the full-heap mark-sweep collector.
 *
 */
@Uninterruptible
public class GCAssertions extends MS {
  
  /* We require locks to ensure that multiple mutator threads calling assertions 
   * at the same time do not step on one another.
   */
  private static final SynchronizedCounter gcLock = VM.newSynchronizedCounter();
  private static final Lock lock = VM.newLock("Finalizer");
  
  public static final short PROCESS = Phase.createSimple("gca");
  protected static final short processPhase = Phase.createComplex("gca.process", null,
      Phase.scheduleGlobal     (PROCESS),
      Phase.scheduleCollector  (PROCESS));

  /**
   * Constructor
   */
  public GCAssertions() {
    super();

    // Reachability and dominator phases need to happen befor
    this.insertPhaseAfter(Phase.scheduleCollector(PRECOPY), Phase.scheduleComplex(processPhase));
    
    Options.printGCAssertionsWarnings = new PrintGCAssertionsWarnings();
    Options.printGCAssertionsStats = new PrintGCAssertionsStats();
    Options.dummyAssertion = new GCAssertionsDummy();
    Options.assertOnHeapFull = new GCAssertionsCollectionOnPressure();
    Options.assertFrequency = new GCAssertionsCollectionFrequency();
  }
  
  /**
   * This assertion tells the system that this is the start of a region of 
   * allocation we want to examine.  Used in conjunction with 
   * {@link #allDead()}.
   */
  @Interruptible
  public static void startRegion() {
    ((GCAssertionsMutator)VM.activePlan.mutator()).inRegion = true;
  }

//   /**
//    * This assertion tells the system that all objects allocated since the 
//    * last call to {@link #startRegion()} should be unreachable at
//    * the next garbage collection.
//    */
//   @Interruptible
//   public static void allDead() {
//     GCAssertionsMutator mutatorContext = (GCAssertionsMutator)VM.activePlan.mutator();
//     mutatorContext.allDead();
//   }

  
  /**
   * Register specialized methods.
   */
  @Interruptible
  protected void registerSpecializedMethods() {
    super.registerSpecializedMethods();
    TransitiveClosure.registerSpecializedScan(SCAN_MARK, GCAssertionsTraceLocal.class);
  }
  
  /****************************************************************************
  *
  * Collection
  */

  public void collectionPhase(short phaseId) {
    
    //  msTrace.prepare();

    if (phaseId == GCAssertions.PROCESS)
      return;

    else {

      super.collectionPhase(phaseId);

    }
  }
  
}
