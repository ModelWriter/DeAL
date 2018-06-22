package test;

public class TyCheck8
{
	public static void foo()
	{
		GCAssert.assert("(forall Object x : x != null) && x == null");
	}
}
