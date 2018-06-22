/*
 * Auto-generated from org.jikesrvm.runtime.SysCall
 */
package org.jikesrvm.runtime;

import org.vmmagic.pragma.*;
import org.vmmagic.unboxed.*;

  @org.vmmagic.pragma.Uninterruptible
public final class SysCallImpl extends org.jikesrvm.runtime.SysCall {
  public void sysConsoleWriteChar(char v) {
    sysConsoleWriteChar(BootRecord.the_boot_record.sysConsoleWriteCharIP, v);
  }

  @SysCallNative
  private static native void sysConsoleWriteChar(Address nativeIP, char v);


  public void sysConsoleWriteInteger(int value,int hexToo) {
    sysConsoleWriteInteger(BootRecord.the_boot_record.sysConsoleWriteIntegerIP, value, hexToo);
  }

  @SysCallNative
  private static native void sysConsoleWriteInteger(Address nativeIP, int value, int hexToo);


  public void sysConsoleWriteLong(long value,int hexToo) {
    sysConsoleWriteLong(BootRecord.the_boot_record.sysConsoleWriteLongIP, value, hexToo);
  }

  @SysCallNative
  private static native void sysConsoleWriteLong(Address nativeIP, long value, int hexToo);


  public void sysConsoleWriteDouble(double value,int postDecimalDigits) {
    sysConsoleWriteDouble(BootRecord.the_boot_record.sysConsoleWriteDoubleIP, value, postDecimalDigits);
  }

  @SysCallNative
  private static native void sysConsoleWriteDouble(Address nativeIP, double value, int postDecimalDigits);


  public void sysExit(int value) {
    sysExit(BootRecord.the_boot_record.sysExitIP, value);
  }

  @SysCallNative
  private static native void sysExit(Address nativeIP, int value);


  public int sysArg(int argno,byte[] buf,int buflen) {
    return sysArg(BootRecord.the_boot_record.sysArgIP, argno, buf, buflen);
  }

  @SysCallNative
  private static native int sysArg(Address nativeIP, int argno, byte[] buf, int buflen);


  public int sysGetenv(byte[] varName,byte[] buf,int limit) {
    return sysGetenv(BootRecord.the_boot_record.sysGetenvIP, varName, buf, limit);
  }

  @SysCallNative
  private static native int sysGetenv(Address nativeIP, byte[] varName, byte[] buf, int limit);


  public void sysCopy(org.vmmagic.unboxed.Address dst,org.vmmagic.unboxed.Address src,org.vmmagic.unboxed.Extent cnt) {
    sysCopy(BootRecord.the_boot_record.sysCopyIP, dst, src, cnt);
  }

  @SysCallNative
  private static native void sysCopy(Address nativeIP, org.vmmagic.unboxed.Address dst, org.vmmagic.unboxed.Address src, org.vmmagic.unboxed.Extent cnt);


  public org.vmmagic.unboxed.Address sysMalloc(int length) {
    return sysMalloc(BootRecord.the_boot_record.sysMallocIP, length);
  }

  @SysCallNative
  private static native org.vmmagic.unboxed.Address sysMalloc(Address nativeIP, int length);


  public org.vmmagic.unboxed.Address sysCalloc(int length) {
    return sysCalloc(BootRecord.the_boot_record.sysCallocIP, length);
  }

  @SysCallNative
  private static native org.vmmagic.unboxed.Address sysCalloc(Address nativeIP, int length);


  public void sysFree(org.vmmagic.unboxed.Address location) {
    sysFree(BootRecord.the_boot_record.sysFreeIP, location);
  }

  @SysCallNative
  private static native void sysFree(Address nativeIP, org.vmmagic.unboxed.Address location);


  public void sysZero(org.vmmagic.unboxed.Address dst,org.vmmagic.unboxed.Extent cnt) {
    sysZero(BootRecord.the_boot_record.sysZeroIP, dst, cnt);
  }

  @SysCallNative
  private static native void sysZero(Address nativeIP, org.vmmagic.unboxed.Address dst, org.vmmagic.unboxed.Extent cnt);


  public void sysZeroPages(org.vmmagic.unboxed.Address dst,int cnt) {
    sysZeroPages(BootRecord.the_boot_record.sysZeroPagesIP, dst, cnt);
  }

  @SysCallNative
  private static native void sysZeroPages(Address nativeIP, org.vmmagic.unboxed.Address dst, int cnt);


  public void sysSyncCache(org.vmmagic.unboxed.Address address,int size) {
    sysSyncCache(BootRecord.the_boot_record.sysSyncCacheIP, address, size);
  }

  @SysCallNative
  private static native void sysSyncCache(Address nativeIP, org.vmmagic.unboxed.Address address, int size);


  public void sysPerfCtrInit(int metric) {
    sysPerfCtrInit(BootRecord.the_boot_record.sysPerfCtrInitIP, metric);
  }

  @SysCallNative
  private static native void sysPerfCtrInit(Address nativeIP, int metric);


  public void sysPerfCtrRead(byte[] name) {
    sysPerfCtrRead(BootRecord.the_boot_record.sysPerfCtrReadIP, name);
  }

  @SysCallNative
  private static native void sysPerfCtrRead(Address nativeIP, byte[] name);


  public long sysPerfCtrReadMetric() {
    return sysPerfCtrReadMetric(BootRecord.the_boot_record.sysPerfCtrReadMetricIP);
  }

  @SysCallNative
  private static native long sysPerfCtrReadMetric(Address nativeIP);


  public long sysPerfCtrReadCycles() {
    return sysPerfCtrReadCycles(BootRecord.the_boot_record.sysPerfCtrReadCyclesIP);
  }

  @SysCallNative
  private static native long sysPerfCtrReadCycles(Address nativeIP);


  public int sysStat(byte[] name,int kind) {
    return sysStat(BootRecord.the_boot_record.sysStatIP, name, kind);
  }

  @SysCallNative
  private static native int sysStat(Address nativeIP, byte[] name, int kind);


  public int sysReadByte(int fd) {
    return sysReadByte(BootRecord.the_boot_record.sysReadByteIP, fd);
  }

  @SysCallNative
  private static native int sysReadByte(Address nativeIP, int fd);


  public int sysWriteByte(int fd,int data) {
    return sysWriteByte(BootRecord.the_boot_record.sysWriteByteIP, fd, data);
  }

  @SysCallNative
  private static native int sysWriteByte(Address nativeIP, int fd, int data);


  public int sysReadBytes(int fd,org.vmmagic.unboxed.Address buf,int cnt) {
    return sysReadBytes(BootRecord.the_boot_record.sysReadBytesIP, fd, buf, cnt);
  }

  @SysCallNative
  private static native int sysReadBytes(Address nativeIP, int fd, org.vmmagic.unboxed.Address buf, int cnt);


  public int sysWriteBytes(int fd,org.vmmagic.unboxed.Address buf,int cnt) {
    return sysWriteBytes(BootRecord.the_boot_record.sysWriteBytesIP, fd, buf, cnt);
  }

  @SysCallNative
  private static native int sysWriteBytes(Address nativeIP, int fd, org.vmmagic.unboxed.Address buf, int cnt);


  public int sysBytesAvailable(int fd) {
    return sysBytesAvailable(BootRecord.the_boot_record.sysBytesAvailableIP, fd);
  }

  @SysCallNative
  private static native int sysBytesAvailable(Address nativeIP, int fd);


  public int sysSyncFile(int fd) {
    return sysSyncFile(BootRecord.the_boot_record.sysSyncFileIP, fd);
  }

  @SysCallNative
  private static native int sysSyncFile(Address nativeIP, int fd);


  public int sysSetFdCloseOnExec(int fd) {
    return sysSetFdCloseOnExec(BootRecord.the_boot_record.sysSetFdCloseOnExecIP, fd);
  }

  @SysCallNative
  private static native int sysSetFdCloseOnExec(Address nativeIP, int fd);


  public int sysAccess(byte[] name,int kind) {
    return sysAccess(BootRecord.the_boot_record.sysAccessIP, name, kind);
  }

  @SysCallNative
  private static native int sysAccess(Address nativeIP, byte[] name, int kind);


  public org.vmmagic.unboxed.Address sysMMap(org.vmmagic.unboxed.Address start,org.vmmagic.unboxed.Extent length,int protection,int flags,int fd,org.vmmagic.unboxed.Offset offset) {
    return sysMMap(BootRecord.the_boot_record.sysMMapIP, start, length, protection, flags, fd, offset);
  }

  @SysCallNative
  private static native org.vmmagic.unboxed.Address sysMMap(Address nativeIP, org.vmmagic.unboxed.Address start, org.vmmagic.unboxed.Extent length, int protection, int flags, int fd, org.vmmagic.unboxed.Offset offset);


  public org.vmmagic.unboxed.Address sysMMapErrno(org.vmmagic.unboxed.Address start,org.vmmagic.unboxed.Extent length,int protection,int flags,int fd,org.vmmagic.unboxed.Offset offset) {
    return sysMMapErrno(BootRecord.the_boot_record.sysMMapErrnoIP, start, length, protection, flags, fd, offset);
  }

  @SysCallNative
  private static native org.vmmagic.unboxed.Address sysMMapErrno(Address nativeIP, org.vmmagic.unboxed.Address start, org.vmmagic.unboxed.Extent length, int protection, int flags, int fd, org.vmmagic.unboxed.Offset offset);


  public int sysMProtect(org.vmmagic.unboxed.Address start,org.vmmagic.unboxed.Extent length,int prot) {
    return sysMProtect(BootRecord.the_boot_record.sysMProtectIP, start, length, prot);
  }

  @SysCallNative
  private static native int sysMProtect(Address nativeIP, org.vmmagic.unboxed.Address start, org.vmmagic.unboxed.Extent length, int prot);


  public int sysGetPageSize() {
    return sysGetPageSize(BootRecord.the_boot_record.sysGetPageSizeIP);
  }

  @SysCallNative
  private static native int sysGetPageSize(Address nativeIP);


  public int sysNumProcessors() {
    return sysNumProcessors(BootRecord.the_boot_record.sysNumProcessorsIP);
  }

  @SysCallNative
  private static native int sysNumProcessors(Address nativeIP);


/**
 *  Create a native thread (aka "unix kernel thread", "pthread").
 *  @param tr
 *  @param ip
 *  @param fp
 *  @return native thread's o/s handle
 */
  public org.vmmagic.unboxed.Word sysThreadCreate(org.vmmagic.unboxed.Address tr,org.vmmagic.unboxed.Address ip,org.vmmagic.unboxed.Address fp) {
    return sysThreadCreate(BootRecord.the_boot_record.sysThreadCreateIP, tr, ip, fp);
  }

  @SysCallNative
  private static native org.vmmagic.unboxed.Word sysThreadCreate(Address nativeIP, org.vmmagic.unboxed.Address tr, org.vmmagic.unboxed.Address ip, org.vmmagic.unboxed.Address fp);


/**
 *  Tells you if the current system supportes sysNativeThreadBind().
 *  @return 1 if it's supported, 0 if it isn't
 */
  public int sysThreadBindSupported() {
    return sysThreadBindSupported(BootRecord.the_boot_record.sysThreadBindSupportedIP);
  }

  @SysCallNative
  private static native int sysThreadBindSupported(Address nativeIP);


  public void sysThreadBind(int cpuId) {
    sysThreadBind(BootRecord.the_boot_record.sysThreadBindIP, cpuId);
  }

  @SysCallNative
  private static native void sysThreadBind(Address nativeIP, int cpuId);


  public void sysThreadYield() {
    sysThreadYield(BootRecord.the_boot_record.sysThreadYieldIP);
  }

  @SysCallNative
  private static native void sysThreadYield(Address nativeIP);


  public org.vmmagic.unboxed.Word sysGetThreadId() {
    return sysGetThreadId(BootRecord.the_boot_record.sysGetThreadIdIP);
  }

  @SysCallNative
  private static native org.vmmagic.unboxed.Word sysGetThreadId(Address nativeIP);


  public void sysSetupHardwareTrapHandler() {
    sysSetupHardwareTrapHandler(BootRecord.the_boot_record.sysSetupHardwareTrapHandlerIP);
  }

  @SysCallNative
  private static native void sysSetupHardwareTrapHandler(Address nativeIP);


  public int sysStashVMThread(org.jikesrvm.scheduler.RVMThread vmThread) {
    return sysStashVMThread(BootRecord.the_boot_record.sysStashVMThreadIP, vmThread);
  }

  @SysCallNative
  private static native int sysStashVMThread(Address nativeIP, org.jikesrvm.scheduler.RVMThread vmThread);


  public void sysThreadTerminate() {
    sysThreadTerminate(BootRecord.the_boot_record.sysThreadTerminateIP);
  }

  @SysCallNative
  private static native void sysThreadTerminate(Address nativeIP);


/**
 *  Allocate the space for a pthread_mutex (using malloc) and initialize
 *  it using pthread_mutex_init with the recursive mutex options.  Note:
 *  it is perfectly OK for the C/C++ code that implements this syscall to
 *  use some other locking mechanism (for example, on systems that don't
 *  have recursive mutexes you could imagine the recursive feature to be
 *  emulated).
 */
  public org.vmmagic.unboxed.Word sysMonitorCreate() {
    return sysMonitorCreate(BootRecord.the_boot_record.sysMonitorCreateIP);
  }

  @SysCallNative
  private static native org.vmmagic.unboxed.Word sysMonitorCreate(Address nativeIP);


/**
 *  Destroy the monitor pointed to by the argument and free its memory
 *  by calling free.
 */
  public void sysMonitorDestroy(org.vmmagic.unboxed.Word monitor) {
    sysMonitorDestroy(BootRecord.the_boot_record.sysMonitorDestroyIP, monitor);
  }

  @SysCallNative
  private static native void sysMonitorDestroy(Address nativeIP, org.vmmagic.unboxed.Word monitor);


  public void sysMonitorEnter(org.vmmagic.unboxed.Word monitor) {
    sysMonitorEnter(BootRecord.the_boot_record.sysMonitorEnterIP, monitor);
  }

  @SysCallNative
  private static native void sysMonitorEnter(Address nativeIP, org.vmmagic.unboxed.Word monitor);


  public void sysMonitorExit(org.vmmagic.unboxed.Word monitor) {
    sysMonitorExit(BootRecord.the_boot_record.sysMonitorExitIP, monitor);
  }

  @SysCallNative
  private static native void sysMonitorExit(Address nativeIP, org.vmmagic.unboxed.Word monitor);


  public void sysMonitorTimedWaitAbsolute(org.vmmagic.unboxed.Word monitor,long whenWakeupNanos) {
    sysMonitorTimedWaitAbsolute(BootRecord.the_boot_record.sysMonitorTimedWaitAbsoluteIP, monitor, whenWakeupNanos);
  }

  @SysCallNative
  private static native void sysMonitorTimedWaitAbsolute(Address nativeIP, org.vmmagic.unboxed.Word monitor, long whenWakeupNanos);


  public void sysMonitorWait(org.vmmagic.unboxed.Word monitor) {
    sysMonitorWait(BootRecord.the_boot_record.sysMonitorWaitIP, monitor);
  }

  @SysCallNative
  private static native void sysMonitorWait(Address nativeIP, org.vmmagic.unboxed.Word monitor);


  public void sysMonitorBroadcast(org.vmmagic.unboxed.Word monitor) {
    sysMonitorBroadcast(BootRecord.the_boot_record.sysMonitorBroadcastIP, monitor);
  }

  @SysCallNative
  private static native void sysMonitorBroadcast(Address nativeIP, org.vmmagic.unboxed.Word monitor);


  public long sysLongDivide(long x,long y) {
    return sysLongDivide(BootRecord.the_boot_record.sysLongDivideIP, x, y);
  }

  @SysCallNative
  private static native long sysLongDivide(Address nativeIP, long x, long y);


  public long sysLongRemainder(long x,long y) {
    return sysLongRemainder(BootRecord.the_boot_record.sysLongRemainderIP, x, y);
  }

  @SysCallNative
  private static native long sysLongRemainder(Address nativeIP, long x, long y);


  public float sysLongToFloat(long x) {
    return sysLongToFloat(BootRecord.the_boot_record.sysLongToFloatIP, x);
  }

  @SysCallNative
  private static native float sysLongToFloat(Address nativeIP, long x);


  public double sysLongToDouble(long x) {
    return sysLongToDouble(BootRecord.the_boot_record.sysLongToDoubleIP, x);
  }

  @SysCallNative
  private static native double sysLongToDouble(Address nativeIP, long x);


  public int sysFloatToInt(float x) {
    return sysFloatToInt(BootRecord.the_boot_record.sysFloatToIntIP, x);
  }

  @SysCallNative
  private static native int sysFloatToInt(Address nativeIP, float x);


  public int sysDoubleToInt(double x) {
    return sysDoubleToInt(BootRecord.the_boot_record.sysDoubleToIntIP, x);
  }

  @SysCallNative
  private static native int sysDoubleToInt(Address nativeIP, double x);


  public long sysFloatToLong(float x) {
    return sysFloatToLong(BootRecord.the_boot_record.sysFloatToLongIP, x);
  }

  @SysCallNative
  private static native long sysFloatToLong(Address nativeIP, float x);


  public long sysDoubleToLong(double x) {
    return sysDoubleToLong(BootRecord.the_boot_record.sysDoubleToLongIP, x);
  }

  @SysCallNative
  private static native long sysDoubleToLong(Address nativeIP, double x);


  public double sysDoubleRemainder(double x,double y) {
    return sysDoubleRemainder(BootRecord.the_boot_record.sysDoubleRemainderIP, x, y);
  }

  @SysCallNative
  private static native double sysDoubleRemainder(Address nativeIP, double x, double y);


/**
 *  Used to parse command line arguments that are
 *  doubles and floats early in booting before it
 *  is safe to call Float.valueOf or Double.valueOf.
 * 
 *  This aborts in case of errors, with an appropriate error message.
 * 
 *  NOTE: this does not support the full Java spec of parsing a string
 *        into a float.
 *  @param buf a null terminated byte[] that can be parsed
 *             by strtof()
 *  @return the floating-point value produced by the call to strtof() on buf.
 */
  public float sysPrimitiveParseFloat(byte[] buf) {
    return sysPrimitiveParseFloat(BootRecord.the_boot_record.sysPrimitiveParseFloatIP, buf);
  }

  @SysCallNative
  private static native float sysPrimitiveParseFloat(Address nativeIP, byte[] buf);


/**
 *  Used to parse command line arguments that are
 *  bytes and ints early in booting before it
 *  is safe to call Byte.parseByte or Integer.parseInt.
 * 
 *  This aborts in case of errors, with an appropriate error message.
 * 
 *  @param buf a null terminated byte[] that can be parsed
 *             by strtol()
 *  @return the int value produced by the call to strtol() on buf.
 */
  public int sysPrimitiveParseInt(byte[] buf) {
    return sysPrimitiveParseInt(BootRecord.the_boot_record.sysPrimitiveParseIntIP, buf);
  }

  @SysCallNative
  private static native int sysPrimitiveParseInt(Address nativeIP, byte[] buf);


/**
 * Parse memory sizes passed as command-line arguments.
 */
  public long sysParseMemorySize(byte[] sizeName,byte[] sizeFlag,byte[] defaultFactor,int roundTo,byte[] argToken,byte[] subArg) {
    return sysParseMemorySize(BootRecord.the_boot_record.sysParseMemorySizeIP, sizeName, sizeFlag, defaultFactor, roundTo, argToken, subArg);
  }

  @SysCallNative
  private static native long sysParseMemorySize(Address nativeIP, byte[] sizeName, byte[] sizeFlag, byte[] defaultFactor, int roundTo, byte[] argToken, byte[] subArg);


  public long sysCurrentTimeMillis() {
    return sysCurrentTimeMillis(BootRecord.the_boot_record.sysCurrentTimeMillisIP);
  }

  @SysCallNative
  private static native long sysCurrentTimeMillis(Address nativeIP);


  public long sysNanoTime() {
    return sysNanoTime(BootRecord.the_boot_record.sysNanoTimeIP);
  }

  @SysCallNative
  private static native long sysNanoTime(Address nativeIP);


  public void sysNanoSleep(long howLongNanos) {
    sysNanoSleep(BootRecord.the_boot_record.sysNanoSleepIP, howLongNanos);
  }

  @SysCallNative
  private static native void sysNanoSleep(Address nativeIP, long howLongNanos);


  public org.vmmagic.unboxed.Address sysDlopen(byte[] libname) {
    return sysDlopen(BootRecord.the_boot_record.sysDlopenIP, libname);
  }

  @SysCallNative
  private static native org.vmmagic.unboxed.Address sysDlopen(Address nativeIP, byte[] libname);


  public org.vmmagic.unboxed.Address sysDlsym(org.vmmagic.unboxed.Address libHandler,byte[] symbolName) {
    return sysDlsym(BootRecord.the_boot_record.sysDlsymIP, libHandler, symbolName);
  }

  @SysCallNative
  private static native org.vmmagic.unboxed.Address sysDlsym(Address nativeIP, org.vmmagic.unboxed.Address libHandler, byte[] symbolName);


  public void sysCreateThreadSpecificDataKeys() {
    sysCreateThreadSpecificDataKeys(BootRecord.the_boot_record.sysCreateThreadSpecificDataKeysIP);
  }

  @SysCallNative
  private static native void sysCreateThreadSpecificDataKeys(Address nativeIP);


  public void sysEnableAlignmentChecking() {
    sysEnableAlignmentChecking(BootRecord.the_boot_record.sysEnableAlignmentCheckingIP);
  }

  @SysCallNative
  private static native void sysEnableAlignmentChecking(Address nativeIP);


  public void sysDisableAlignmentChecking() {
    sysDisableAlignmentChecking(BootRecord.the_boot_record.sysDisableAlignmentCheckingIP);
  }

  @SysCallNative
  private static native void sysDisableAlignmentChecking(Address nativeIP);


  public void sysReportAlignmentChecking() {
    sysReportAlignmentChecking(BootRecord.the_boot_record.sysReportAlignmentCheckingIP);
  }

  @SysCallNative
  private static native void sysReportAlignmentChecking(Address nativeIP);


  public org.vmmagic.unboxed.Address gcspyDriverAddStream(org.vmmagic.unboxed.Address driver,int id) {
    return gcspyDriverAddStream(BootRecord.the_boot_record.gcspyDriverAddStreamIP, driver, id);
  }

  @SysCallNative
  private static native org.vmmagic.unboxed.Address gcspyDriverAddStream(Address nativeIP, org.vmmagic.unboxed.Address driver, int id);


  public void gcspyDriverEndOutput(org.vmmagic.unboxed.Address driver) {
    gcspyDriverEndOutput(BootRecord.the_boot_record.gcspyDriverEndOutputIP, driver);
  }

  @SysCallNative
  private static native void gcspyDriverEndOutput(Address nativeIP, org.vmmagic.unboxed.Address driver);


  public void gcspyDriverInit(org.vmmagic.unboxed.Address driver,int id,org.vmmagic.unboxed.Address serverName,org.vmmagic.unboxed.Address driverName,org.vmmagic.unboxed.Address title,org.vmmagic.unboxed.Address blockInfo,int tileNum,org.vmmagic.unboxed.Address unused,int mainSpace) {
    gcspyDriverInit(BootRecord.the_boot_record.gcspyDriverInitIP, driver, id, serverName, driverName, title, blockInfo, tileNum, unused, mainSpace);
  }

  @SysCallNative
  private static native void gcspyDriverInit(Address nativeIP, org.vmmagic.unboxed.Address driver, int id, org.vmmagic.unboxed.Address serverName, org.vmmagic.unboxed.Address driverName, org.vmmagic.unboxed.Address title, org.vmmagic.unboxed.Address blockInfo, int tileNum, org.vmmagic.unboxed.Address unused, int mainSpace);


  public void gcspyDriverInitOutput(org.vmmagic.unboxed.Address driver) {
    gcspyDriverInitOutput(BootRecord.the_boot_record.gcspyDriverInitOutputIP, driver);
  }

  @SysCallNative
  private static native void gcspyDriverInitOutput(Address nativeIP, org.vmmagic.unboxed.Address driver);


  public void gcspyDriverResize(org.vmmagic.unboxed.Address driver,int size) {
    gcspyDriverResize(BootRecord.the_boot_record.gcspyDriverResizeIP, driver, size);
  }

  @SysCallNative
  private static native void gcspyDriverResize(Address nativeIP, org.vmmagic.unboxed.Address driver, int size);


  public void gcspyDriverSetTileNameRange(org.vmmagic.unboxed.Address driver,int i,org.vmmagic.unboxed.Address start,org.vmmagic.unboxed.Address end) {
    gcspyDriverSetTileNameRange(BootRecord.the_boot_record.gcspyDriverSetTileNameRangeIP, driver, i, start, end);
  }

  @SysCallNative
  private static native void gcspyDriverSetTileNameRange(Address nativeIP, org.vmmagic.unboxed.Address driver, int i, org.vmmagic.unboxed.Address start, org.vmmagic.unboxed.Address end);


  public void gcspyDriverSetTileName(org.vmmagic.unboxed.Address driver,int i,org.vmmagic.unboxed.Address start,long value) {
    gcspyDriverSetTileName(BootRecord.the_boot_record.gcspyDriverSetTileNameIP, driver, i, start, value);
  }

  @SysCallNative
  private static native void gcspyDriverSetTileName(Address nativeIP, org.vmmagic.unboxed.Address driver, int i, org.vmmagic.unboxed.Address start, long value);


  public void gcspyDriverSpaceInfo(org.vmmagic.unboxed.Address driver,org.vmmagic.unboxed.Address info) {
    gcspyDriverSpaceInfo(BootRecord.the_boot_record.gcspyDriverSpaceInfoIP, driver, info);
  }

  @SysCallNative
  private static native void gcspyDriverSpaceInfo(Address nativeIP, org.vmmagic.unboxed.Address driver, org.vmmagic.unboxed.Address info);


  public void gcspyDriverStartComm(org.vmmagic.unboxed.Address driver) {
    gcspyDriverStartComm(BootRecord.the_boot_record.gcspyDriverStartCommIP, driver);
  }

  @SysCallNative
  private static native void gcspyDriverStartComm(Address nativeIP, org.vmmagic.unboxed.Address driver);


  public void gcspyDriverStream(org.vmmagic.unboxed.Address driver,int id,int len) {
    gcspyDriverStream(BootRecord.the_boot_record.gcspyDriverStreamIP, driver, id, len);
  }

  @SysCallNative
  private static native void gcspyDriverStream(Address nativeIP, org.vmmagic.unboxed.Address driver, int id, int len);


  public void gcspyDriverStreamByteValue(org.vmmagic.unboxed.Address driver,byte value) {
    gcspyDriverStreamByteValue(BootRecord.the_boot_record.gcspyDriverStreamByteValueIP, driver, value);
  }

  @SysCallNative
  private static native void gcspyDriverStreamByteValue(Address nativeIP, org.vmmagic.unboxed.Address driver, byte value);


  public void gcspyDriverStreamShortValue(org.vmmagic.unboxed.Address driver,short value) {
    gcspyDriverStreamShortValue(BootRecord.the_boot_record.gcspyDriverStreamShortValueIP, driver, value);
  }

  @SysCallNative
  private static native void gcspyDriverStreamShortValue(Address nativeIP, org.vmmagic.unboxed.Address driver, short value);


  public void gcspyDriverStreamIntValue(org.vmmagic.unboxed.Address driver,int value) {
    gcspyDriverStreamIntValue(BootRecord.the_boot_record.gcspyDriverStreamIntValueIP, driver, value);
  }

  @SysCallNative
  private static native void gcspyDriverStreamIntValue(Address nativeIP, org.vmmagic.unboxed.Address driver, int value);


  public void gcspyDriverSummary(org.vmmagic.unboxed.Address driver,int id,int len) {
    gcspyDriverSummary(BootRecord.the_boot_record.gcspyDriverSummaryIP, driver, id, len);
  }

  @SysCallNative
  private static native void gcspyDriverSummary(Address nativeIP, org.vmmagic.unboxed.Address driver, int id, int len);


  public void gcspyDriverSummaryValue(org.vmmagic.unboxed.Address driver,int value) {
    gcspyDriverSummaryValue(BootRecord.the_boot_record.gcspyDriverSummaryValueIP, driver, value);
  }

  @SysCallNative
  private static native void gcspyDriverSummaryValue(Address nativeIP, org.vmmagic.unboxed.Address driver, int value);


  public void gcspyIntWriteControl(org.vmmagic.unboxed.Address driver,int id,int tileNum) {
    gcspyIntWriteControl(BootRecord.the_boot_record.gcspyIntWriteControlIP, driver, id, tileNum);
  }

  @SysCallNative
  private static native void gcspyIntWriteControl(Address nativeIP, org.vmmagic.unboxed.Address driver, int id, int tileNum);


  public org.vmmagic.unboxed.Address gcspyMainServerAddDriver(org.vmmagic.unboxed.Address addr) {
    return gcspyMainServerAddDriver(BootRecord.the_boot_record.gcspyMainServerAddDriverIP, addr);
  }

  @SysCallNative
  private static native org.vmmagic.unboxed.Address gcspyMainServerAddDriver(Address nativeIP, org.vmmagic.unboxed.Address addr);


  public void gcspyMainServerAddEvent(org.vmmagic.unboxed.Address server,int event,org.vmmagic.unboxed.Address name) {
    gcspyMainServerAddEvent(BootRecord.the_boot_record.gcspyMainServerAddEventIP, server, event, name);
  }

  @SysCallNative
  private static native void gcspyMainServerAddEvent(Address nativeIP, org.vmmagic.unboxed.Address server, int event, org.vmmagic.unboxed.Address name);


  public org.vmmagic.unboxed.Address gcspyMainServerInit(int port,int len,org.vmmagic.unboxed.Address name,int verbose) {
    return gcspyMainServerInit(BootRecord.the_boot_record.gcspyMainServerInitIP, port, len, name, verbose);
  }

  @SysCallNative
  private static native org.vmmagic.unboxed.Address gcspyMainServerInit(Address nativeIP, int port, int len, org.vmmagic.unboxed.Address name, int verbose);


  public int gcspyMainServerIsConnected(org.vmmagic.unboxed.Address server,int event) {
    return gcspyMainServerIsConnected(BootRecord.the_boot_record.gcspyMainServerIsConnectedIP, server, event);
  }

  @SysCallNative
  private static native int gcspyMainServerIsConnected(Address nativeIP, org.vmmagic.unboxed.Address server, int event);


  public org.vmmagic.unboxed.Address gcspyMainServerOuterLoop() {
    return gcspyMainServerOuterLoop(BootRecord.the_boot_record.gcspyMainServerOuterLoopIP);
  }

  @SysCallNative
  private static native org.vmmagic.unboxed.Address gcspyMainServerOuterLoop(Address nativeIP);


  public void gcspyMainServerSafepoint(org.vmmagic.unboxed.Address server,int event) {
    gcspyMainServerSafepoint(BootRecord.the_boot_record.gcspyMainServerSafepointIP, server, event);
  }

  @SysCallNative
  private static native void gcspyMainServerSafepoint(Address nativeIP, org.vmmagic.unboxed.Address server, int event);


  public void gcspyMainServerSetGeneralInfo(org.vmmagic.unboxed.Address server,org.vmmagic.unboxed.Address info) {
    gcspyMainServerSetGeneralInfo(BootRecord.the_boot_record.gcspyMainServerSetGeneralInfoIP, server, info);
  }

  @SysCallNative
  private static native void gcspyMainServerSetGeneralInfo(Address nativeIP, org.vmmagic.unboxed.Address server, org.vmmagic.unboxed.Address info);


  public void gcspyMainServerStartCompensationTimer(org.vmmagic.unboxed.Address server) {
    gcspyMainServerStartCompensationTimer(BootRecord.the_boot_record.gcspyMainServerStartCompensationTimerIP, server);
  }

  @SysCallNative
  private static native void gcspyMainServerStartCompensationTimer(Address nativeIP, org.vmmagic.unboxed.Address server);


  public void gcspyMainServerStopCompensationTimer(org.vmmagic.unboxed.Address server) {
    gcspyMainServerStopCompensationTimer(BootRecord.the_boot_record.gcspyMainServerStopCompensationTimerIP, server);
  }

  @SysCallNative
  private static native void gcspyMainServerStopCompensationTimer(Address nativeIP, org.vmmagic.unboxed.Address server);


  public void gcspyStartserver(org.vmmagic.unboxed.Address server,int wait,org.vmmagic.unboxed.Address serverOuterLoop) {
    gcspyStartserver(BootRecord.the_boot_record.gcspyStartserverIP, server, wait, serverOuterLoop);
  }

  @SysCallNative
  private static native void gcspyStartserver(Address nativeIP, org.vmmagic.unboxed.Address server, int wait, org.vmmagic.unboxed.Address serverOuterLoop);


  public void gcspyStreamInit(org.vmmagic.unboxed.Address stream,int id,int dataType,org.vmmagic.unboxed.Address name,int minValue,int maxValue,int zeroValue,int defaultValue,org.vmmagic.unboxed.Address pre,org.vmmagic.unboxed.Address post,int presentation,int paintStyle,int maxStreamIndex,int red,int green,int blue) {
    gcspyStreamInit(BootRecord.the_boot_record.gcspyStreamInitIP, stream, id, dataType, name, minValue, maxValue, zeroValue, defaultValue, pre, post, presentation, paintStyle, maxStreamIndex, red, green, blue);
  }

  @SysCallNative
  private static native void gcspyStreamInit(Address nativeIP, org.vmmagic.unboxed.Address stream, int id, int dataType, org.vmmagic.unboxed.Address name, int minValue, int maxValue, int zeroValue, int defaultValue, org.vmmagic.unboxed.Address pre, org.vmmagic.unboxed.Address post, int presentation, int paintStyle, int maxStreamIndex, int red, int green, int blue);


  public void gcspyFormatSize(org.vmmagic.unboxed.Address buffer,int size) {
    gcspyFormatSize(BootRecord.the_boot_record.gcspyFormatSizeIP, buffer, size);
  }

  @SysCallNative
  private static native void gcspyFormatSize(Address nativeIP, org.vmmagic.unboxed.Address buffer, int size);


  public int gcspySprintf(org.vmmagic.unboxed.Address str,org.vmmagic.unboxed.Address format,org.vmmagic.unboxed.Address value) {
    return gcspySprintf(BootRecord.the_boot_record.gcspySprintfIP, str, format, value);
  }

  @SysCallNative
  private static native int gcspySprintf(Address nativeIP, org.vmmagic.unboxed.Address str, org.vmmagic.unboxed.Address format, org.vmmagic.unboxed.Address value);


}
