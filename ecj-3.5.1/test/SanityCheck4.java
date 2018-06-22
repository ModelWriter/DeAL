package test;

public class SanityCheck3
{
	public static void foo()
	{
		Object r = new Object();

		GCAssert.assert("forall Node x : (forall Object y : reach[r](x))");
	}
}