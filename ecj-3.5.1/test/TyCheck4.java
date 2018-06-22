package test;

public class TyCheck4
{
	public static void foo()
	{
		Object r = new Object();

		GCAssert.assert("forall Object x : reach[r](x) > 1");
	}
}