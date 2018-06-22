package test;

public class TyCheck2
{
	public static void foo()
	{
		Object r = new Object();

		GCAssert.assert("forall Object x : x.v != null");
	}
}