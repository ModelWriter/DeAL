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

import org.mmtk.plan.marksweep.gcassertions.spec.Assertion;
import org.mmtk.plan.Trace;
import org.mmtk.plan.marksweep.MS;
import org.mmtk.plan.marksweep.MSTraceLocal;
import org.mmtk.plan.marksweep.gcassertions.GCAssertionsHeader;
import org.mmtk.policy.Space;
import org.mmtk.policy.MarkSweepSpace;
import org.mmtk.utility.ArrayHelpers;
import org.mmtk.utility.Log;
import org.mmtk.utility.deque.ObjectReferenceDeque;
import org.mmtk.utility.options.Options;
import org.mmtk.vm.VM;

import org.vmmagic.pragma.*;
import org.vmmagic.unboxed.Address;
import org.vmmagic.unboxed.AddressArray;
import org.vmmagic.unboxed.ObjectReference;
import org.vmmagic.unboxed.Word;

/**
 * This abstract class implements the core functionality for a transitive
 * closure over the heap graph.
 */
@Uninterruptible public final class GCAssertionsTraceLocal extends MSTraceLocal {
  
  // Marks an ObjectReference as seen in the deque
  private static final Word SEEN_MASK = Word.one();   
  private static final Word SEEN = Word.one(); 
  
  /**
   * Constructor
   *
   * @param trace The global trace to use.
   */
  public GCAssertionsTraceLocal(Trace trace, ObjectReferenceDeque modBuffer) {
    super(trace, modBuffer);
  }

  private static Object detected_overlap = null; // Reference to an object that caused an overlap to take place
  private static int current_traversal_id = 0; // ID of the traversal currently taking place, if any

  /**
   * Set the ID of the current traversal, and reset detected_overlap.
   *
   * During object tracing, detected_overlap will be set to the first object encountered
   * that has a lower traversal ID, if any (indicating that it was encountered in an earlier traversal.)
   *
   * @param id The new traversal ID to assume
   */
  public final static void
  setTraversalId(int id)
  {
    if (id < 0 || id > GCAssertionsHeader.MAX_TRAVERSAL_ID) {
      Log.write("Invalid traversal ID: ");
      Log.writeln(id);
      return;
    }
    current_traversal_id = id;
    detected_overlap = null;
  }

  /**
   * Retrieves the most recently set traversal ID
   */
  public static final int
  getTraversalId()
  {
    return current_traversal_id;
  }

  public static final Object
  getOverlapObject()
  {
    return detected_overlap;
  }
  
  /**
   * This method is the core method during the trace of the object graph.
   * The role of this method is to:
   *
   * 1. Ensure the traced object is not collected.
   * 2. If this is the first visit to the object enqueue it to be scanned.
   * 3. Return the forwarded reference to the object.
   * 4. Check the overlap predicate: if we met this object in a previous traversal,
   *    we are overlapping.
   *
   * @param object The object to be traced.
   * @return The new reference to the same object instance.
   */
  @Inline
  public ObjectReference traceObject(ObjectReference object) {
//     if (!object.isNull() && Space.isInSpace(GCAssertions.MARK_SWEEP, object)) {
//       Log.write("  Considering ");
//       Log.write(object);
//       Log.write(" : ");
//       Log.write(VM.objectModel.getTypeDescriptor(object));
//       Log.write(", liveness is ");
//       Log.writeln(GCAssertions.msSpace.isLive(object));
//     }

    if (GCAssertionsCollector.ASSERTIONS_ENABLED) {
      if (GCAssertionsCollector.ALWAYS_TAG_TRAVERSAL || Assertion.getTraversalId() >= 0) { // are we disjointness-testing?
	if (!object.isNull() && Space.isInSpace(GCAssertions.MARK_SWEEP, object)) {
	  if (GCAssertions.msSpace.isLive(object)) { // has been reached during this iteration?
	    final int traversal_id = GCAssertionsHeader.getTraversalId(object);
	    if (traversal_id < current_traversal_id)
	      detected_overlap = object;
	  } else
	    GCAssertionsHeader.setTraversalId(object, current_traversal_id);
	}
      }
    }
    return super.traceObject(object);
  }
  
  /**
   * This method traces an object with knowledge of the fact that object
   * is a root or not. In the GCAssertions framework, we need to know whether
   * the object is a root to report the correct warning to the user.
   *
   * @param object The object to be traced.
   * @return The new reference to the same object instance.
   */
  @Inline
  public ObjectReference traceObject(ObjectReference object, boolean root) {
    return traceObject(object);
  }

//   /**
//    * Finishing processing all GC work.  This method iterates until all work queues
//    * are empty.
//    */
//   @Inline
//   public void completeTrace() {
//     logMessage(4, "Processing GC in parallel");
//     if (!rootLocations.isEmpty()) {
//       processRoots();
//     }
//     logMessage(5, "processing gray objects");
//     //assertMutatorRemsetsFlushed();
//     do {
//       while (!values.isEmpty()) {
//         ObjectReference v = values.pop();

//         /* Check for black objects (objects that have already been scanned but
//          * are still in the values queue).  We had previously set the low-order
//          * bit on these objects so we can reconstruct the path through the
//          * heap to the object we are currently tracing.
//          */
//         Word w = v.toAddress().toWord();
//         if (w.and(SEEN_MASK).EQ(SEEN)) {
//           continue;
//         }
        
//         // set low-order bit to 1 to signify we have seen this, then push back on deque
//         w = w.or(SEEN);
//         values.push(w.toAddress().toObjectReference());
                
//         scanObject(v);
//       }
//       processRememberedSets();
//     } while (!values.isEmpty());
//     //assertMutatorRemsetsFlushed();
        
//   }
  
  public static boolean printTraced = false;

  /**
   * Apply the assertion node processor (if any) to a node that has just been enqueued
   *
   * @param obj The node we are investigating
   */
  @Inline
  protected void
  postProcessNode(ObjectReference obj)
  {
    if (GCAssertionsCollector.ASSERTIONS_ENABLED)
      Assertion.test(obj);

//     if (printTraced) {
//       Log.write("  traced ");
//       Log.write(obj);
//       Log.write(", ");
//       Log.write(VM.objectModel.getTypeDescriptor(obj));
//       Log.writeln("");
//     }
  }
}