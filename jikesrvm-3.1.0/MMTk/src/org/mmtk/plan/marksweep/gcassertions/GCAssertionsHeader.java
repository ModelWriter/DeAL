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

import org.mmtk.policy.MarkSweepSpace;
import org.mmtk.utility.Constants;
import org.mmtk.vm.VM;

import org.vmmagic.unboxed.*;
import org.vmmagic.pragma.*;

/**
 * Each instance of this class corresponds to one MarkSweepSpace.  In other 
 * words, it maintains and performs actions with
 * respect to state that is global to a given MarpSweepSpace.
 * Each of the instance methods of this class may be called by any
 * thread (i.e. synchronization must be explicit in any instance or
 * class method).
 */
@Uninterruptible public final class GCAssertionsHeader implements Constants {

  /****************************************************************************
   *
   * Class variables
   */

  public static final int LOCAL_GC_BITS_REQUIRED = 4;
  public static final int GLOBAL_GC_BITS_REQUIRED = 0;
  public static final int GC_HEADER_WORDS_REQUIRED = 0;
  public static final Offset GC_HEADER_WORD1_OFFSET = VM.objectModel.GC_HEADER_OFFSET();
  public static final Offset GC_HEADER_WORD2_OFFSET = GC_HEADER_WORD1_OFFSET.plus(Constants.BYTES_IN_WORD);

  
  /**
   * We use bits in the header for our GC Assertions.  We assume that the 
   * first four bits are used for the mark-sweep collector, so we use begin 
   * use at the fifth bit.  
   * 
   * Since ADDRESS_BASED_HASHING is true, there are 8 bits available.  4 
   * are used by the mark sweep space, and the other 4 are used for 
   * GC Assertions. 
   * 
   * Bit 4 is used for our dead() assertion.
   * Bit 5 is used for our unshared() assertion.
   * Bits 6 and 7 are used for our ownedBy() assertion.
   */
  public static final int MAX_TRAVERSAL_ID = 0x7;
  private static final int OVERLAP_TEST_SHIFT = MarkSweepSpace.LOCAL_GC_BITS_REQUIRED;
  private static final Word OVERLAP_TEST_MASK = Word.fromIntZeroExtend(MAX_TRAVERSAL_ID).lsh(OVERLAP_TEST_SHIFT);  // ...01111000


  /**
   * The excluded-visited flag is set on excluded objects that we've visitedbefore, to properly avoid re-traversal.
   */
  private static final int EXCLUDED_VISITED_SHIFT = MarkSweepSpace.LOCAL_GC_BITS_REQUIRED + 3;
  private static final Word EXCLUDED_VISITED_MASK = Word.fromIntZeroExtend(0x1).lsh(EXCLUDED_VISITED_SHIFT);  // ...01111000


//   private static final Word DEAD = Word.one().lsh(
//       MarkSweepSpace.LOCAL_GC_BITS_REQUIRED);     // ...00010000
//   private static final Word UNSHARED = Word.one().lsh(
//       MarkSweepSpace.LOCAL_GC_BITS_REQUIRED+1);   // ...00100000
//   private static final Word OWNEE = Word.one().lsh(  
//       MarkSweepSpace.LOCAL_GC_BITS_REQUIRED+2);   // ...01000000
//   private static final Word OWNED_ENCOUNTERED = Word.one().lsh(  
//       MarkSweepSpace.LOCAL_GC_BITS_REQUIRED+3);   // ...10000000
  
  /****************************************************************************
  *
  * Object tagging methods
  */

  @Inline
  @Uninterruptible
  public static int getTraversalId(ObjectReference object) { 
    Word value = VM.objectModel.readAvailableBitsWord(object);
    return value.and(OVERLAP_TEST_MASK).rshl(OVERLAP_TEST_SHIFT).toInt();
  }

  @Inline
  @Uninterruptible
  public static void setTraversalId(ObjectReference object, int id) { 
    Word value = VM.objectModel.readAvailableBitsWord(object);
    /*if (VM.VERIFY_ASSERTIONS)
      VM.assertions._assert(value.and(DEAD).NE(DEAD));*/
    VM.objectModel.writeAvailableBitsWord(object, value.or(Word.fromIntZeroExtend(id).lsh(OVERLAP_TEST_SHIFT)));
  }

  @Inline
  @Uninterruptible
  public static boolean isExcludedVisited(ObjectReference object) { 
    Word value = VM.objectModel.readAvailableBitsWord(object);
    return 0 != value.and(EXCLUDED_VISITED_MASK).toInt();
  }

  @Inline
  @Uninterruptible
  public static void setExcludedVisited(ObjectReference object, boolean ev) { 
    Word value = VM.objectModel.readAvailableBitsWord(object);
    /*if (VM.VERIFY_ASSERTIONS)
      VM.assertions._assert(value.and(DEAD).NE(DEAD));*/
    VM.objectModel.writeAvailableBitsWord(object, value.or(Word.fromIntZeroExtend(ev ? 1 : 0).lsh(EXCLUDED_VISITED_SHIFT)));
  }
  
//   /**
//    * Return true if <code>object</code> has been marked as unreachable. 
//    *
//    * @param object The object in question
//    * @return <code>true</code> if <code>object</code> has been marked as 
//    * reachable
//    */
//   @Inline
//   @Uninterruptible
//   public static boolean isDead(ObjectReference object) { 
//     Word value = VM.objectModel.readAvailableBitsWord(object);
//     return value.and(DEAD).EQ(DEAD);
//   }
  
//   /**
//    * Mark <code>object</code> as unreachable.  
//    *
//    * <code>object</code> is left in the <code>DEAD</code> state.
//    *
//    * @param object The object whose state is to be changed.
//    */
//   @Inline
//   @Uninterruptible
//   public static void makeDead(ObjectReference object) {
//     Word value = VM.objectModel.readAvailableBitsWord(object);
//     /*if (VM.VERIFY_ASSERTIONS)
//       VM.assertions._assert(value.and(DEAD).NE(DEAD));*/
//     VM.objectModel.writeAvailableBitsWord(object, value.or(DEAD));
//   }

//   /**
//    * Mark given object as unshared.
//    * 
//    * @param object The object that should be marked unshared
//    */
//   @Inline
//   @Uninterruptible
//   public static void makeUnshared(ObjectReference object) {
//     Word value = VM.objectModel.readAvailableBitsWord(object);
//     VM.objectModel.writeAvailableBitsWord(object, value.or(UNSHARED));
//   }
  
//   /**
//    * Return true if given object has been marked as unshared. 
//    *
//    * @param object The object in question
//    * @return <code>true</code> if <code>object</code> has been marked as 
//    * unshared
//    */
//   @Inline
//   @Uninterruptible
//   public static boolean isUnshared(ObjectReference object) { 
//     Word value = VM.objectModel.readAvailableBitsWord(object);
//     return value.and(UNSHARED).EQ(UNSHARED);
//   }
  
//   /**
//    * Set the bit indicating this object is the ownee in an ownership assertion
//    * 
//    * @param object The object that is involved in an ownership assertion
//    */
//   @Inline
//   @Uninterruptible
//   public static void setOwnee(ObjectReference object) {
//     Word value = VM.objectModel.readAvailableBitsWord(object);
//     VM.objectModel.writeAvailableBitsWord(object, value.or(OWNEE));
//   }

//   /**
//    * Check whether this object is the ownee in an ownership assertion
//    * 
//    * @param object The object that we want to check
//    * @return Whether the object is involved in an ownership assertion
//    */
//   @Inline
//   @Uninterruptible
//   public static boolean isOwnee(ObjectReference object) {
//     Word value = VM.objectModel.readAvailableBitsWord(object);
//     return value.and(OWNEE).EQ(OWNEE);
//   }

//   /**
//    * Set the "owned and encountered" bit in the header.  This should be 
//    * set when an ownee object is encountered on a path through the owner.
//    * 
//    * @param object The object that should be marked as "owned and encountered"
//    */
//   @Inline
//   @Uninterruptible
//   public static void setEncountered(ObjectReference object) {
//     if (VM.VERIFY_ASSERTIONS)
//       VM.assertions._assert(isOwnee(object));
//     Word value = VM.objectModel.readAvailableBitsWord(object);
//     VM.objectModel.writeAvailableBitsWord(object, value.or(OWNED_ENCOUNTERED));
//   }
  
//   /**
//    * Unset the "owned and encountered" owned bit in the header.  This 
//    * should be unset after every collection.
//    * 
//    * @param object The object whose "owned and encountered" bit should be unset
//    */
//   @Inline
//   @Uninterruptible
//   public static void unsetEncountered(ObjectReference object) {
//     if (VM.VERIFY_ASSERTIONS)
//       VM.assertions._assert(isOwnee(object));
//     Word value = VM.objectModel.readAvailableBitsWord(object);
//     VM.objectModel.writeAvailableBitsWord(object, value.and(OWNED_ENCOUNTERED.not()));
//   }

//   /**
//    * Return true if given object has been marked as "owned and encountered." 
//    * This should have been set if this ownee object was encountered on 
//    * a path through the owner.
//    *
//    * @param object The object in question
//    * @return <code>true</code> if <code>object</code> has been marked as "owned and encountered"
//    */
//   @Inline
//   @Uninterruptible
//   public static boolean isEncountered(ObjectReference object) { 
//     if (VM.VERIFY_ASSERTIONS)
//       VM.assertions._assert(isOwnee(object));
//     Word value = VM.objectModel.readAvailableBitsWord(object);
//     return value.and(OWNED_ENCOUNTERED).EQ(OWNED_ENCOUNTERED);
//   }
  

}