package test;

public class HookPresentAssert
{
	public static void foo()
	{
		Object r;
		Node kappa = new Node();

		GCAssert.assert(" (forall Node n: reach[r](n) -> !(n.v == 42)) && (exists Object o: o instanceof Object)");
	}
}