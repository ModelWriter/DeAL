package org.vmmagic;

import org.vmmagic.unboxed.Address;

public class StaticOptTable
{

  /* [CR]
   * Magic method table operations
   */

  /** Opt-compile method and return a handle to it for later use. */
  public static int registerStaticMethodHook(Class<?> c, int static_method_index) {
    return -1;
  }

  public static Address lookupStaticMethodHook(int hook) {
    return null;
  }

  public static void flushStaticMethodHooks() {
  }

  public static boolean invokeStaticMethod(Object param1, Object[] param2, Address address) {
    return false;
  }
}