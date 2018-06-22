package test;

public class MagicTestLibrary
{
	public static Node n;
	public static Object o;
	public static double d;
	public static float f;
	public static boolean b;
	public static int i;
	public static short s;

	public static void foo()
	{
		GCAssert.assertDebug(" exists Node x : x.v == 0"); // 0000
		GCAssert.assertDebug(" exists Node x : x.v == i"); // 0001
		GCAssert.assertDebug(" exists Node x : (x == n) && (x.v > i)"); // 0002
		GCAssert.assertDebug(" exists Object o : !b"); // 0003

 		GCAssert.assertDebug(" exists Object o : f > i"); // 0004
 		GCAssert.assertDebug(" exists Object o : i > f"); // 0005
		GCAssert.assertDebug(" exists Object o : f < i"); // 0006
		GCAssert.assertDebug(" exists Object o : i < f"); // 0007
		GCAssert.assertDebug(" exists Object o : d > i"); // 0008
		GCAssert.assertDebug(" exists Object o : i > d"); // 0009
		GCAssert.assertDebug(" exists Object o : d < i"); // 0010
		GCAssert.assertDebug(" exists Object o : i < d"); // 0011

		GCAssert.assertDebug(" exists Object o : s == i"); // 0012

		GCAssert.assertDebug(" exists Node n2 : n2.next.next == n"); // 0013
		GCAssert.assertDebug(" exists Node n2 : n2.next.next.next == null"); // 0014

		GCAssert.assertDebug(" exists Node n2 : n2 != n"); // 0015
		GCAssert.assertDebug(" exists Node n2 : (n2 == n) == b"); // 0016
		GCAssert.assertDebug(" exists Node n2 : i >= 1"); // 0017
		GCAssert.assertDebug(" exists Node n2 : i < 1"); // 0018

		GCAssert.assertDebug(" exists Node n : (n.v == i) || b"); // 0019
		GCAssert.assertDebug(" exists Node n : b || (n.v == i)"); // 0020

		GCAssert.assertDebug(" exists Node n : (n.v == i) && b"); // 0021
		GCAssert.assertDebug(" exists Node n : b && (n.v == i)"); // 0022

		GCAssert.assertDebug(" exists Node n : (n.v == i) -> b"); // 0023
		GCAssert.assertDebug(" exists Node n : b -> (n.v == i)"); // 0024

		GCAssert.assertDebug(" exists Node n2 : n2.v == 0 || n2.v == 1 || n2.v == 2"); // 0025
		GCAssert.assertDebug(" exists Node n2 : ((n == n2) && (b != (i == s))) || n2.v == 0"); // 0026
	}
}