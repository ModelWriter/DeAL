package test;

public class TestT1
{
	public static Node a, b;

	public static boolean foo()
	{
		double d = 0.0;

		GCAssert.assertDisjoint("(exists Node x : (reach[a](x) -> x.v == 0)) && (exists Object o : reach[b](o))");

		return d > 100.0; // just use it somehow
	}
}