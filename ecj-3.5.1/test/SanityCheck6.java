package test;

public class SanityCheck6
{
	public static void foo()
	{
		Object r = new Object();
		Object s = new Object();

		GCAssert.assert("(forall Node x : reach[r/r](x)) && (forall Node x : reach[s/r](x))");
	}
}