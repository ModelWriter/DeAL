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

import org.mmtk.plan.marksweep.MSConstraints;
import org.mmtk.policy.MarkSweepSpace;
import org.vmmagic.pragma.*;

/**
 * GCAssertions common constants.
 */
@Uninterruptible public class GCAssertionsConstraints extends MSConstraints {
  
  public int gcHeaderBits() { 
    return MarkSweepSpace.LOCAL_GC_BITS_REQUIRED + GCAssertionsHeader.LOCAL_GC_BITS_REQUIRED; 
  }
  
  public int gcHeaderWords() { 
    return MarkSweepSpace.GC_HEADER_WORDS_REQUIRED + GCAssertionsHeader.GC_HEADER_WORDS_REQUIRED; 
  }

  // Reserve two extra bits to mark objects that we are specifically `on the lookout' for and objects that we have tagged with a desired traversal ID
  public int extraBitsInheader() {
    return 2;
  }
  
}
