package test;

public class HookPresentAssumeDisjoint
{
	public static void foo()
	{
		GCAssert.assumeDisjoint("true");
	}
}