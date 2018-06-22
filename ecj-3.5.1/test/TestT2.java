package test;

public class TestT2
{
	public static Node a, b;

	public static void foo()
	{

		GCAssert.assertDisjoint("forall Node x : reach[a](x)");
	}
}