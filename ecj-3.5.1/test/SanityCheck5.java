package test;

public class SanityCheck5
{
	public static void foo()
	{
		Object r = new Object();

		GCAssert.assert("forall Node x : reach[r/{r, x}](x)");
	}
}