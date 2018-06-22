package test;

public class ReachSort
{
	public static void foo()
	{
		Object a, b, c, d, e;

		GCAssert.assert(" exists Object o: reach[a, b](o)");
		GCAssert.assert(" exists Object o: reach[{a}, b](o)");
		GCAssert.assert(" exists Object o: reach[a/{c}, b/{d, e}](o)");
		GCAssert.assert(" exists Object o: reach[{c, a, d, b}](o)");
		GCAssert.assert(" exists Object o: reach[/{c, a}](o)");
	}
}