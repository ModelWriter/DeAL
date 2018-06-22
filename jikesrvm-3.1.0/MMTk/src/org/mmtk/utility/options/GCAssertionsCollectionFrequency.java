/*
 *  This file is part of the Jikes RVM project (http://jikesrvm.org).
 *
 *  This file is licensed to You under the Eclipse Public License (EPL);
 *  You may not use this file except in compliance with the License. You
 *  may obtain a copy of the License at
 *
 *      http://www.opensource.org/licenses/eclipse-1.0.php
 *
 *  See the COPYRIGHT.txt file distributed with this work for information
 *  regarding copyright ownership.
 */
package org.mmtk.utility.options;

import org.vmmagic.unboxed.Address;

/**
 * 
 */
public final class GCAssertionsCollectionFrequency extends org.vmutil.options.IntOption {
  /**
   * Create the option
   */
  public GCAssertionsCollectionFrequency() {
    super(Options.set, "assertFrequency",
          "Frequency of GC assertions collections. -1: never, 0: always (default), n: every (n+1)th opportunity",
	  0);
  }
}
