package test;

public class SanityCheck0
{
	public static void foo()
	{
		Object r = new Object();

		GCAssert.assert("reach[r](r)");
	}
}