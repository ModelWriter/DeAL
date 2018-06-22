package test;

public class MagicChecker extends TestHarness
{
	final static Object o = new Object();
	final static Node n0 = new Node(0);
	final static Node n1 = new Node(1);
	final static Node n2n1 = new Node(2, n1);
	final static Node n3n2n1 = new Node(3, n2n1);

	public void
	test00()
	{
		assertFalse(MagicTestLibrary.__magic_predicate$0000(o, null));
		assertTrue(MagicTestLibrary.__magic_predicate$0000(n0, null));
		assertFalse(MagicTestLibrary.__magic_predicate$0000(n1, null));
	}

	public void
	test01()
	{
		assertFalse(MagicTestLibrary.__magic_predicate$0001(n0, new Object[]{ new Integer(1) }));
		assertTrue(MagicTestLibrary.__magic_predicate$0001(n0, new Object[]{ new Integer(0) }));
	}

	public void
	test02()
	{
		assertFalse(MagicTestLibrary.__magic_predicate$0002(n1, new Object[]{ n0, new Integer(0) }));
		assertFalse(MagicTestLibrary.__magic_predicate$0002(n1, new Object[]{ n1, new Integer(1) }));
		assertTrue(MagicTestLibrary.__magic_predicate$0002(n1, new Object[]{ n1, new Integer(0) }));
	}

	public void
	test03()
	{
		assertFalse(MagicTestLibrary.__magic_predicate$0003(o, new Object[]{ new Boolean(true) }));
		assertTrue(MagicTestLibrary.__magic_predicate$0003(o, new Object[]{ new Boolean(false) }));
	}

	// float vs. int
	public void
	test04()
	{
		assertFalse(MagicTestLibrary.__magic_predicate$0004(o, new Object[]{ new Float(1.0), new Integer(1) }));
		assertFalse(MagicTestLibrary.__magic_predicate$0004(o, new Object[]{ new Float(1.0), new Integer(2) }));
		assertTrue(MagicTestLibrary.__magic_predicate$0004(o, new Object[]{ new Float(1.1), new Integer(1) }));
	}

	public void
	test05()
	{
		assertFalse(MagicTestLibrary.__magic_predicate$0005(o, new Object[]{ new Integer(1), new Float(1.0) }));
		assertFalse(MagicTestLibrary.__magic_predicate$0005(o, new Object[]{ new Integer(1), new Float(1.1) }));
		assertTrue(MagicTestLibrary.__magic_predicate$0005(o, new Object[]{ new Integer(2), new Float(1.9) }));
	}

	public void
	test06()
	{
		assertFalse(MagicTestLibrary.__magic_predicate$0006(o, new Object[]{ new Float(1.0), new Integer(1) }));
		assertTrue(MagicTestLibrary.__magic_predicate$0006(o, new Object[]{ new Float(1.0), new Integer(2) }));
		assertFalse(MagicTestLibrary.__magic_predicate$0006(o, new Object[]{ new Float(1.1), new Integer(1) }));
	}

	public void
	test07()
	{
		assertFalse(MagicTestLibrary.__magic_predicate$0007(o, new Object[]{ new Integer(1), new Float(1.0) }));
		assertTrue(MagicTestLibrary.__magic_predicate$0007(o, new Object[]{ new Integer(1), new Float(1.1) }));
		assertFalse(MagicTestLibrary.__magic_predicate$0007(o, new Object[]{ new Integer(2), new Float(1.9) }));
	}

	// double vs. int
	public void
	test08()
	{
		assertFalse(MagicTestLibrary.__magic_predicate$0008(o, new Object[]{ new Double(1.0), new Integer(1) }));
		assertFalse(MagicTestLibrary.__magic_predicate$0008(o, new Object[]{ new Double(1.0), new Integer(2) }));
		assertTrue(MagicTestLibrary.__magic_predicate$0008(o, new Object[]{ new Double(1.1), new Integer(1) }));
	}

	public void
	test09()
	{
		assertFalse(MagicTestLibrary.__magic_predicate$0009(o, new Object[]{ new Integer(1), new Double(1.0) }));
		assertFalse(MagicTestLibrary.__magic_predicate$0009(o, new Object[]{ new Integer(1), new Double(1.1) }));
		assertTrue(MagicTestLibrary.__magic_predicate$0009(o, new Object[]{ new Integer(2), new Double(1.9) }));
	}

	public void
	test10()
	{
		assertFalse(MagicTestLibrary.__magic_predicate$0010(o, new Object[]{ new Double(1.0), new Integer(1) }));
		assertTrue(MagicTestLibrary.__magic_predicate$0010(o, new Object[]{ new Double(1.0), new Integer(2) }));
		assertFalse(MagicTestLibrary.__magic_predicate$0010(o, new Object[]{ new Double(1.1), new Integer(1) }));
	}

	public void
	test11()
	{
		assertFalse(MagicTestLibrary.__magic_predicate$0011(o, new Object[]{ new Integer(1), new Double(1.0) }));
		assertTrue(MagicTestLibrary.__magic_predicate$0011(o, new Object[]{ new Integer(1), new Double(1.1) }));
		assertFalse(MagicTestLibrary.__magic_predicate$0011(o, new Object[]{ new Integer(2), new Double(1.9) }));
	}

	// --

	public void
	test12()
	{
		assertFalse(MagicTestLibrary.__magic_predicate$0012(o, new Object[]{ new Short((short)0), new Integer(1) }));
		assertTrue(MagicTestLibrary.__magic_predicate$0012(o, new Object[]{ new Short((short)2), new Integer(2) }));
	}

	public void
	test13()
	{
		assertFalse(MagicTestLibrary.__magic_predicate$0013(n3n2n1, new Object[]{ n2n1 }));
		assertTrue(MagicTestLibrary.__magic_predicate$0013(n3n2n1, new Object[]{ n1 }));
		assertFalse(MagicTestLibrary.__magic_predicate$0013(n1, new Object[]{ n1 }));
	}


	public void
	test14()
	{
		assertTrue(MagicTestLibrary.__magic_predicate$0014(n3n2n1, new Object[]{ }));
		assertFalse(MagicTestLibrary.__magic_predicate$0014(n2n1, new Object[]{ }));
		assertFalse(MagicTestLibrary.__magic_predicate$0014(n1, new Object[]{ }));
	}


	public void
	test15()
	{
		assertTrue(MagicTestLibrary.__magic_predicate$0015(n2n1, new Object[]{ n1 }));
		assertFalse(MagicTestLibrary.__magic_predicate$0015(n1, new Object[]{ n1 }));
	}

	public void
	test16()
	{
		assertTrue(MagicTestLibrary.__magic_predicate$0016(n1, new Object[]{ n1, new Boolean(true) }));
		assertFalse(MagicTestLibrary.__magic_predicate$0016(n1, new Object[]{ n1, new Boolean(false) }));
		assertFalse(MagicTestLibrary.__magic_predicate$0016(n1, new Object[]{ n2n1, new Boolean(true) }));
		assertTrue(MagicTestLibrary.__magic_predicate$0016(n1, new Object[]{ n2n1, new Boolean(false) }));
	}

	public void
	test17()
	{
		assertTrue(MagicTestLibrary.__magic_predicate$0017(n1, new Object[]{ new Integer(1) }));
		assertTrue(MagicTestLibrary.__magic_predicate$0017(n1, new Object[]{ new Integer(2) }));
		assertFalse(MagicTestLibrary.__magic_predicate$0017(n1, new Object[]{ new Integer(0) }));
	}


	public void
	test18()
	{
		assertFalse(MagicTestLibrary.__magic_predicate$0018(n1, new Object[]{ new Integer(1) }));
		assertFalse(MagicTestLibrary.__magic_predicate$0018(n1, new Object[]{ new Integer(2) }));
		assertTrue(MagicTestLibrary.__magic_predicate$0018(n1, new Object[]{ new Integer(0) }));
	}


	// ||
	public void
	test19()
	{
		assertTrue(MagicTestLibrary.__magic_predicate$0019(n1, new Object[]{ new Integer(1), new Boolean(true) }));
		assertTrue(MagicTestLibrary.__magic_predicate$0019(n1, new Object[]{ new Integer(1), new Boolean(false) }));
		assertTrue(MagicTestLibrary.__magic_predicate$0019(n1, new Object[]{ new Integer(2), new Boolean(true) }));
		assertFalse(MagicTestLibrary.__magic_predicate$0019(n1, new Object[]{ new Integer(2), new Boolean(false) }));
	}

	public void
	test20()
	{
		assertTrue(MagicTestLibrary.__magic_predicate$0020(n1, new Object[]{ new Boolean(true), new Integer(1) }));
		assertTrue(MagicTestLibrary.__magic_predicate$0020(n1, new Object[]{ new Boolean(false), new Integer(1) }));
		assertTrue(MagicTestLibrary.__magic_predicate$0020(n1, new Object[]{ new Boolean(true), new Integer(2) }));
		assertFalse(MagicTestLibrary.__magic_predicate$0020(n1, new Object[]{ new Boolean(false), new Integer(2) }));
	}

	// &&
	public void
	test21()
	{
		assertTrue(MagicTestLibrary.__magic_predicate$0021(n1, new Object[]{ new Integer(1), new Boolean(true) }));
		assertFalse(MagicTestLibrary.__magic_predicate$0021(n1, new Object[]{ new Integer(1), new Boolean(false) }));
		assertFalse(MagicTestLibrary.__magic_predicate$0021(n1, new Object[]{ new Integer(2), new Boolean(true) }));
		assertFalse(MagicTestLibrary.__magic_predicate$0021(n1, new Object[]{ new Integer(2), new Boolean(false) }));
	}

	public void
	test22()
	{
		assertTrue(MagicTestLibrary.__magic_predicate$0022(n1, new Object[]{ new Boolean(true), new Integer(1) }));
		assertFalse(MagicTestLibrary.__magic_predicate$0022(n1, new Object[]{ new Boolean(false), new Integer(1) }));
		assertFalse(MagicTestLibrary.__magic_predicate$0022(n1, new Object[]{ new Boolean(true), new Integer(2) }));
		assertFalse(MagicTestLibrary.__magic_predicate$0022(n1, new Object[]{ new Boolean(false), new Integer(2) }));
	}


	// ->
	public void
	test23()
	{
		assertTrue(MagicTestLibrary.__magic_predicate$0023(n1, new Object[]{ new Integer(1), new Boolean(true) }));
		assertFalse(MagicTestLibrary.__magic_predicate$0023(n1, new Object[]{ new Integer(1), new Boolean(false) }));
		assertTrue(MagicTestLibrary.__magic_predicate$0023(n1, new Object[]{ new Integer(2), new Boolean(true) }));
		assertTrue(MagicTestLibrary.__magic_predicate$0023(n1, new Object[]{ new Integer(2), new Boolean(false) }));
	}

	public void
	test24()
	{
		assertTrue(MagicTestLibrary.__magic_predicate$0024(n1, new Object[]{ new Boolean(true), new Integer(1) }));
		assertTrue(MagicTestLibrary.__magic_predicate$0024(n1, new Object[]{ new Boolean(false), new Integer(1) }));
		assertFalse(MagicTestLibrary.__magic_predicate$0024(n1, new Object[]{ new Boolean(true), new Integer(2) }));
		assertTrue(MagicTestLibrary.__magic_predicate$0024(n1, new Object[]{ new Boolean(false), new Integer(2) }));
	}

	public void
	test25()
	{
		assertTrue(MagicTestLibrary.__magic_predicate$0025(n0, new Object[]{ }));
		assertTrue(MagicTestLibrary.__magic_predicate$0025(n1, new Object[]{ }));
		assertTrue(MagicTestLibrary.__magic_predicate$0025(n2n1, new Object[]{ }));
		assertFalse(MagicTestLibrary.__magic_predicate$0025(n3n2n1, new Object[]{ }));
	}

	public void
	test26()
	{
		Node n2 = n2n1;

		assertTrue(MagicTestLibrary.__magic_predicate$0026(n0, new Object[]{ n1, new Boolean(true), new Integer(1), new Short((short)1) }));
		assertTrue(MagicTestLibrary.__magic_predicate$0026(n0, new Object[]{ n1, new Boolean(true), new Integer(1), new Short((short)2) }));
		assertTrue(MagicTestLibrary.__magic_predicate$0026(n0, new Object[]{ n1, new Boolean(false), new Integer(1), new Short((short)1) }));
		assertTrue(MagicTestLibrary.__magic_predicate$0026(n0, new Object[]{ n1, new Boolean(false), new Integer(1), new Short((short)2) }));
		assertTrue(MagicTestLibrary.__magic_predicate$0026(n0, new Object[]{ n0, new Boolean(true), new Integer(1), new Short((short)1) }));
		assertTrue(MagicTestLibrary.__magic_predicate$0026(n0, new Object[]{ n0, new Boolean(true), new Integer(1), new Short((short)2) }));
		assertTrue(MagicTestLibrary.__magic_predicate$0026(n0, new Object[]{ n0, new Boolean(false), new Integer(1), new Short((short)1) }));
		assertTrue(MagicTestLibrary.__magic_predicate$0026(n0, new Object[]{ n0, new Boolean(false), new Integer(1), new Short((short)2) }));

		assertFalse(MagicTestLibrary.__magic_predicate$0026(n2, new Object[]{ n1, new Boolean(true), new Integer(1), new Short((short)1) }));
		assertFalse(MagicTestLibrary.__magic_predicate$0026(n2, new Object[]{ n1, new Boolean(true), new Integer(1), new Short((short)2) }));
		assertFalse(MagicTestLibrary.__magic_predicate$0026(n2, new Object[]{ n1, new Boolean(false), new Integer(1), new Short((short)1) }));
		assertFalse(MagicTestLibrary.__magic_predicate$0026(n2, new Object[]{ n1, new Boolean(false), new Integer(1), new Short((short)2) }));
		assertFalse(MagicTestLibrary.__magic_predicate$0026(n2, new Object[]{ n2, new Boolean(true), new Integer(1), new Short((short)1) }));
		assertTrue(MagicTestLibrary.__magic_predicate$0026(n2, new Object[]{ n2, new Boolean(true), new Integer(1), new Short((short)2) }));
		assertTrue(MagicTestLibrary.__magic_predicate$0026(n2, new Object[]{ n2, new Boolean(false), new Integer(1), new Short((short)1) }));
		assertFalse(MagicTestLibrary.__magic_predicate$0026(n2, new Object[]{ n2, new Boolean(false), new Integer(1), new Short((short)2) }));
	}


}