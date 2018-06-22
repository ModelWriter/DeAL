package test;

public class SanityCheck9
{
	public static void foo()
	{
		Object r = new Object();
		Object s = new Object();

		GCAssert.assert("(forall Node x : reach[r/r](x) && reach[r/r](x))");
	}
}