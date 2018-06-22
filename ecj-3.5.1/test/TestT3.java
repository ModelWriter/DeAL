package test;

public class TestT3
{
	public static Node a, b;

	public static void foo()
	{
		// yes, this is impossible.  It's only used to test GVNing.
		GCAssert.assertDisjoint("forall Node x : (reach[a.next](x) -> x == null) && (reach[{b.next}](x) -> x == null)");
	}
}