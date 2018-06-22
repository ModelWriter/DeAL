package org.mmtk.utility.options;

public class PrintGCAssertionsWarnings extends org.vmutil.options.BooleanOption {
  
  /**
   * Create the option.
   */
  public PrintGCAssertionsWarnings() {
    super(Options.set, "Print GCAssertions Warnings",
          "Should we print GC Assertions warnings?",
        true);
  }

}
