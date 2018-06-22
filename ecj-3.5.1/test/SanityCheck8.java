package test;

public class SanityCheck8
{
	public static void foo()
	{
		Object r = new Object();
		Object s = new Object();

		GCAssert.assumeDisjoint("(forall Node x : reach[r/r](x)) && (forall Node x : reach[s/r](x))");
	}
}