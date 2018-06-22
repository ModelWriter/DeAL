package org.mmtk.utility.options;

import org.mmtk.utility.options.Options;

public final class GCAssertionsDummy extends org.vmutil.options.EnumOption {
  /**
   * Create the option.
   */
  public GCAssertionsDummy() {
    super(Options.set, "Dummy GCAssertion",
          "Use this to select a dummy whole-heap query to run",
          new String[] {"NONE", "ALWAYS_TRUE", "ALWAYS_TRUE_WITH_TRAVERSAL_ID", "INSTANCEOF", "INSTANCEOF_WITH_TRAVERSAL_ID"},
          "NONE");
  }
}
