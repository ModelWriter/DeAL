package test;

public class SanityCheck2
{
	public static void foo()
	{
		Object r = new Object();

		GCAssert.assert("forall Node x : reach[x](x)");
	}
}