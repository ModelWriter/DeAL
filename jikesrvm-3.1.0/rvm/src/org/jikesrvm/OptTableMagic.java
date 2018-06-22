package org.jikesrvm;

import org.vmmagic.pragma.*;
import org.vmmagic.unboxed.*;
import org.jikesrvm.compilers.common.CompiledMethod;

import org.jikesrvm.classloader.RVMClass;
import org.jikesrvm.classloader.RVMMethod;

/**
 * A magic table 
 */
public class OptTableMagic
{
  private static AddressArray address_table = AddressArray.create(1 /* initial table size */);
  private static int addresses_allocated = 0;


  public static final int
  registerStaticMethodHook(Class c, int index)
  {
    if (addresses_allocated >= address_table.length()) {
      AddressArray new_table = AddressArray.create(address_table.length() * 2);

      // I don't trust arraycopy to do this right... besides, this is very rare.
      for (int i = 0; i < address_table.length(); i++)
	new_table.set(i, address_table.get(i));

      address_table = new_table;
    }

    final RVMClass type = (RVMClass)JikesRVMSupport.getTypeForClass(c);
    final RVMMethod method = type.getStaticMethods()[index];

    method.compile();
    CompiledMethod cm = method.getCurrentCompiledMethod();
    while (cm == null) {
      method.compile();
      cm = method.getCurrentCompiledMethod();
    }

    Address a = ObjectReference.fromObject(cm.getEntryCodeArray()).toAddress();

    address_table.set(addresses_allocated, a);
//     System.err.println("OptTableMagic: " + method + " -> " + addresses_allocated);
    return addresses_allocated++;
  }

  public static final void
  debugOffset(Object _, Object[] __, Address a)
  {
    System.err.println("DO = " + a.toInt());
  }

  public static final Address
  lookupStaticMethodHook(int index)
  {
    if (index < 0 || index >= addresses_allocated)
      System.err.println("OptTableMagic.lookupStaticMethodHook(" + index + "): invalid index\n");
    return address_table.get(index);
  }

  public static final void
  flushStaticMethodHooks()
  {
    addresses_allocated = 0;
  }
}