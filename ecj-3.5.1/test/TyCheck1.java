package test;

public class TyCheck1
{
	public static void foo()
	{
		Object r = new Object();

		GCAssert.assert("forall Object x : x == 2");
	}
}