package org.mmtk.utility.options;

public class PrintGCAssertionsStats extends org.vmutil.options.BooleanOption {
  
  /**
   * Create the option.
   */
  public PrintGCAssertionsStats() {
    super(Options.set, "Print GCAssertions Stats",
          "Should we print GC Assertions statistics?",
        false);
  }

}
