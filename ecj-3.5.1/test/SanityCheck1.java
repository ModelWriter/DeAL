package test;

public class SanityCheck1
{
	public static void foo()
	{
		Object r = new Object();

		GCAssert.assert("forall Node x : reach[r](r)");
	}
}