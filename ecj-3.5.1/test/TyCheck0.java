package test;

public class TyCheck0
{
	public static void foo()
	{
		Object r = new Object();

		GCAssert.assert("r == 2");
	}
}