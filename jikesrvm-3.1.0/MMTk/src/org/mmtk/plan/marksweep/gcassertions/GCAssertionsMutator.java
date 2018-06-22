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

import org.mmtk.plan.MutatorContext;
import org.mmtk.plan.StopTheWorldMutator;
import org.mmtk.plan.marksweep.MS;
import org.mmtk.plan.marksweep.MSCollector;
import org.mmtk.plan.marksweep.MSMutator;
import org.mmtk.utility.Log;
import org.mmtk.utility.deque.ObjectReferenceDeque;
import org.mmtk.utility.deque.SharedDeque;

import org.vmmagic.pragma.*;
import org.vmmagic.unboxed.ObjectReference;


/**
 * This class implements <i>per-mutator thread</i> behavior
 * and state for the <i>MS</i> plan, which implements a full-heap
 * mark-sweep collector.<p>
 *
 * Specifically, this class defines <i>MS</i> mutator-time allocation
 * and per-mutator thread collection semantics (flushing and restoring
 * per-mutator allocator state).<p>
 *
 * @see org.mmtk.plan.markcompact.MC for an overview of the mark-compact algorithm.<p>
 *
 * FIXME The SegregatedFreeList class (and its decendents such as
 * MarkSweepLocal) does not properly separate mutator and collector
 * behaviors, so the ms field below should really not exist in
 * this class as there is no collection-time allocation in this
 * collector.
 *
 * @see MS
 * @see MSCollector
 * @see StopTheWorldMutator
 * @see MutatorContext
 */
@Uninterruptible public class GCAssertionsMutator extends MSMutator {
  
  public boolean inRegion = false;
  private SharedDeque objectsInRegionPool;
  public ObjectReferenceDeque objectsInRegion;
  
  public GCAssertionsMutator() {
    objectsInRegionPool = new SharedDeque("objectsInRegionPool", 
        GCAssertions.metaDataSpace, 1);
    objectsInRegionPool.prepareNonBlocking();
    objectsInRegion = new ObjectReferenceDeque("objectsInRegion", 
        objectsInRegionPool);
  }


//   /**
//      * Perform post-allocation actions.  For many allocators none are
//      * required.
//      *
//      * @param object The newly allocated object
//      * @param typeRef the type reference for the instance being created
//      * @param bytes The size of the space to be allocated (in bytes)
//      * @param allocator The allocator number to be used for this allocation
//      */
//   @Inline
//   public final void postAlloc(ObjectReference object, ObjectReference typeRef,
//       int bytes, int allocator) {
// //     if (inRegion) {
// //       objectsInRegion.push(object);
// //     }
    
//     super.postAlloc(object, typeRef, bytes, allocator);
//   }


//   /**
//    * This method is called when an allDead region ends and we need to mark
//    * all objects allocated since the call to startRegion().  We unset the
//    * inRegion field and then iterate through the objectsInRegion 
//    * SharedDeque, marking objects.
//    */
//   public void allDead() {
// //     inRegion = false;
// //     while (objectsInRegion.isNonEmpty()) {
// //       ObjectReference obj = objectsInRegion.pop();
// //       GCAssertionsHeader.makeDead(obj);
// //     }    
//   }
  
  
//   /****************************************************************************
//   *
//   * Collection
//   */

//   /**
//    * Perform a per-mutator collection phase.
//    *
//    * @param phaseId The collection phase to perform
//    * @param primary Perform any single-threaded activities using this thread.
//    */
//   @Inline
//   public void collectionPhase(short phaseId, boolean primary) {

// //     /**
// //      * In RELEASE phase, the MarkSweepSpace is swept.  This happens when
// //      * MS.collectionPhase(RELEASE) is called.
// //      */
// //     if (phaseId == MS.RELEASE) {
// //       fixRegionObjectReferences();

// //       super.collectionPhase(phaseId, primary);
// //       return;
// //     }

//     super.collectionPhase(phaseId, primary);
//   }
 
//   /**
//    * After each GC, if we are in an assertAllDead region, we need to scan
//    * the list of objects in the region and remove any dead objects.
//    * 
//    * TODO: this is actually unnecessary because the ObjectReferenceDeque
//    * will keep the objects alive, preventing them from being collected.
//    * Fix this somehow.
//    */
//   private void fixRegionObjectReferences() {

// //     ObjectReference firstObj = ObjectReference.nullReference();
// //     ObjectReference obj = ObjectReference.nullReference();

// //     /* Treat first object reference differently.  We need to keep track of it
// //      * so we know when to stop looping.  
// //      * 
// //      * Note that it is impossible for an object to appear twice in the list
// //      * because objects are added to the list only when they are allocated,
// //      * and two objects cannot be allocated to the same address.
// //      */
// //     while (objectsInRegion.isNonEmpty()) {
// //       obj = objectsInRegion.pop();
// //       if (GCAssertions.msSpace.isLive(obj)) {
// //         objectsInRegion.insert(obj);
// //         firstObj = obj;
// //         break;
// //       }
// //     }

// //     if (firstObj != ObjectReference.nullReference()) { 
// //       // Handle rest of objects
// //       obj = objectsInRegion.pop();
// //       while (obj != firstObj) {
// //         if (GCAssertions.msSpace.isLive(obj)) {
// //           objectsInRegion.insert(obj);
// //         }
// //         obj = objectsInRegion.pop();
// //       }
// //     }
//   }
    
    

}
