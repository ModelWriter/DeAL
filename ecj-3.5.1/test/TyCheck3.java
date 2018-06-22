package test;

public class TyCheck3
{
	public static void foo()
	{
		Object r = new Object();

		GCAssert.assert("forall Object x : 2 > r");
	}
}