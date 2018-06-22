package test;

public class ASTOpPrecedence
{
	public static void foo()
	{
		Object r;

		GCAssert.assert(" 1 == 2 && 3 != 4 && true");
		GCAssert.assert(" 1 <= 2 == 3.2 >= 4.1 == true == 0 > 1 == 2 < 5");
		GCAssert.assert(" true || false && true || false ");
		GCAssert.assert(" 1 == 2 <-> 3 == 4 ");
	}
}