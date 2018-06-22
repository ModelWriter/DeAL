package test;

public class SanityCheck7
{
	public static void foo()
	{
		Object r = new Object();
		Object s = new Object();

		GCAssert.assertDisjoint("(forall Node x : reach[r/r](x)) && (forall Node x : reach[s/r](x))");
	}
}