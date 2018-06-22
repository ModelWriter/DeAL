package test;

public class TyCheck6
{
	public static void foo()
	{
		Object r = new Object();

		GCAssert.assert("forall java.util.LinkedList x : x");
	}
}