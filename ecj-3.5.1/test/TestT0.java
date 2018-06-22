package test;

public class TestT0
{
	public static Node a, b;

	public static void foo()
	{

		GCAssert.assertDisjoint("exists Node x : (reach[a](x) -> x.v == 0) && (reach[b](x) -> x.v == 1)");
	}
}