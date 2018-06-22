package gtest;

import org.mmtk.plan.marksweep.gcassertions.spec.*;
import org.vmmagic.pragma.Uninterruptible;
import org.mmtk.utility.Log;

public class Tests extends TestHarness
{
	/* ============================================================ */
	/* Reachability tests */

	/* ---------------------------------------- */
	// Reachability: universal (inside)

	public static int c00 = 0;
	public static int c01 = 0;
	public static int c02 = 0;
	public static int c03 = 0;
	public static int c04 = 0;
	public static int c05 = 0;

	public static final void
	resetCounters()
	{
		c00 = c01 = c02 = c03 = c04 = c05 = 0;
	}

	@Uninterruptible
	public static final class TestClass {
		
		public static final boolean
		_magic00_is_in_env(Object o, Object[] env)
		{
			++c00;
			for (Object o2 : env)
				if (o2 == o)
					return true;
			return false;
		}

		public static final boolean
		_magic01_is_not_in_env(Object o, Object[] env)
		{
			++c01;
			for (Object o2 : env)
				if (o2 == o)
					return false;
			return true;
		}

		public static final boolean
		_magic02_is_node(Object o, Object[] env)
		{
			++c02;
			return o instanceof Node;
		}

		public static final boolean
		_magic03_is_not_node(Object o, Object[] env)
		{
			++c03;
			return !(o instanceof Node);
		}

		public static final boolean
		_magic04_is_one(Object o, Object[] env)
		{
			++c04;
			return (o instanceof Node
				&& ((Node) o).value == 1);
		}

		public static final boolean
		_magic05_is_not_one(Object o, Object[] env)
		{
			++c05;
			return (o instanceof Node
				&& ((Node) o).value != 1);
		}

	}

	final int TEST_IS_IN_ENV	= 0;
	final int TEST_IS_NOT_IN_ENV	= 1;
	final int TEST_IS_NODE		= 2;
	final int TEST_IS_NOT_NODE	= 3;
	final int TEST_IS_1		= 4;
	final int TEST_IS_NOT_1		= 5;

	public void
	testBug0()
	{
		// Specification:
		//   X_a+b && !X_{a,b}+c
		resetCounters();

		Node a = new Node(null, null);
		// a -> * -> *

		final PredicateFamily all_are_covered =
			new PredicateFamily(TestClass.class,
					    // Universal predicate family requires negated predicate
					    new int[] { PredicateFamily.IGNORE_TEST, TEST_IS_NODE },
					    new Object[] { },
					    PredicateFamily.UNIVERSAL);

		Assertion asr = new Assertion(new PredicateFamily[] { all_are_covered },
					    new Step[] {
						    null, // required format, cf constructor
						    // -- regular code
						    new SetTraversalPredicatesStep(new int[] {0}),
						    new ExcludeNodesStep(new Object[] {}),
						    new TraversalStep(new Object[] { a }),
						    new SetTraversalPredicatesStep(new int[] {1}),
						    new TraversalStep(),
						    // -- end of regular code
						    null, // required format, cf constructor
						    null,  // required format, cf constructor
						    null  // required format, cf constructor
					    });
		Assertion.doAssert(asr);
		assertTrue(all_are_covered.value());
	}


	public void
	testUniversalReachability()
	{
		// Specification:
		//   forall x. R_c(x) => x instanceof Node

		resetCounters();
		Node a = new Node(null, null);
		Node b = new Node(null, a);
		Node c = new Node(null, b);
		// c -> b -> a


		final PredicateFamily all_reachable_are_nodes = 
			new PredicateFamily(TestClass.class,
					    // universal tests are negated
					    new int[] { TEST_IS_NOT_NODE, PredicateFamily.IGNORE_TEST },
					    null, // no object
					    PredicateFamily.UNIVERSAL);

		Assertion.doAssert(new Assertion(new PredicateFamily[] {
					all_reachable_are_nodes
				},
				new Step[] {
					null, // required format, cf constructor
					// -- regular code
					new SetTraversalPredicatesStep(new int[] { 0 }), // only one predicate, and we're setting it to TEST_IS_NONE
					new TraversalStep(new Object[] { c }), // collect from c
					new SetTraversalPredicatesStep(new int[] { 1 }), // we're setting it to IGNORE_TEST now
					new TraversalStep(), // collect from the root set
					// -- end of regular code
					null, // required format, cf constructor
					null, // required format, cf constructor
					null  // required format, cf constructor
				}));

		assertTrue(all_reachable_are_nodes.value());
		assertTrue(c03 == 3); // we should have encountered precisely a, b, c
	}

	public void
	testExistentialReachability()
	{
		// Specification:
		//   (exists x. !R_b(x) => x == c) && (forall y. R_b(y) => (y == a || y == b))

		resetCounters();
		Node a = new Node(null, null);
		Node b = new Node(null, a);
		Node c = new Node(null, b);
		// c -> b -> a

		final PredicateFamily c_is_outside =
			new PredicateFamily(TestClass.class,
					    new int[] { TEST_IS_IN_ENV, PredicateFamily.IGNORE_TEST },
					    new Object[] { c },
					    PredicateFamily.EXISTENTIAL);

		final PredicateFamily inside_is_a_b = 
			new PredicateFamily(TestClass.class,
					    // universal tests are negated
					    new int[] { TEST_IS_NOT_IN_ENV, PredicateFamily.IGNORE_TEST },
					    new Object[] { a, b },
					    PredicateFamily.UNIVERSAL);

		Assertion.doAssert(new Assertion(new PredicateFamily[] {
					c_is_outside,
					inside_is_a_b
				},
				new Step[] {
					null, // required format, cf constructor
					// -- regular code
					new SetTraversalPredicatesStep(new int[] { 1, 0 }), // c_is_outside: IGNORE, inside_is_a_b: TEST_IS_NODE
					new TraversalStep(new Object[] { b }),
					new SetTraversalPredicatesStep(new int[] { 0, 1 }),
					new TraversalStep(), // collect from the root set
					// -- end of regular code
					null, // required format, cf constructor
					null, // required format, cf constructor
					null  // required format, cf constructor
				}));

		assertTrue(c_is_outside.value());
		assertTrue(inside_is_a_b.value());
		assertTrue(c01 == 2); // we should have encountered precisely a and b
	}


	public void
	testNegatedExistentialReachability()
	{ // we now expect b to be outside the traversal, but since we start with b, that is impossible.

		// Specification:
		//   !(exists x. R_b(x) => x == a)

		resetCounters();
		Node a = new Node(null, null);
		Node b = new Node(null, a);
		Node c = new Node(null, b);
		// c -> b -> a

		final PredicateFamily a_is_outside =
			new PredicateFamily(TestClass.class,
					    new int[] { TEST_IS_IN_ENV, PredicateFamily.IGNORE_TEST },
					    new Object[] { a },
					    PredicateFamily.EXISTENTIAL);

		Assertion.doAssert(new Assertion(new PredicateFamily[] {
					a_is_outside,
				},
				new Step[] {
					null, // required format, cf constructor
					// -- regular code
					new SetTraversalPredicatesStep(new int[] { 1 }), // ignore the inside
					new TraversalStep(new Object[] { b }),
					new SetTraversalPredicatesStep(new int[] { 0 }), // search for `b' outside
					new TraversalStep(), // collect from the root set
					// -- end of regular code
					null, // required format, cf constructor
					null, // required format, cf constructor
					null  // required format, cf constructor
				}));

		assertFalse(a_is_outside.value());
	}

	public void
	testNegativeUniversalReachability0()
	{
		// Specification:
		//   !(forall x. !R_b(x) => x == c)

		resetCounters();
		Node a = new Node(null, null);
		Node b = new Node(null, a);
		Node c = new Node(null, b);
		// c -> b -> a

		final PredicateFamily c_is_everywhere_outside =
			new PredicateFamily(TestClass.class,
					    // Universal predicate family requires negated predicate
					    new int[] { TEST_IS_NOT_IN_ENV, PredicateFamily.IGNORE_TEST },
					    new Object[] { c },
					    PredicateFamily.UNIVERSAL);

		Assertion.doAssert(new Assertion(new PredicateFamily[] {
					c_is_everywhere_outside,
				},
				new Step[] {
					null, // required format, cf constructor
					// -- regular code
					new SetTraversalPredicatesStep(new int[] { 1 }), // ignore the inside
					new TraversalStep(new Object[] { b }),
					new SetTraversalPredicatesStep(new int[] { 0 }), // search for `c' outside
					new TraversalStep(), // collect from the root set
					// -- end of regular code
					null, // required format, cf constructor
					null, // required format, cf constructor
					null  // required format, cf constructor
				}));

		assertFalse(c_is_everywhere_outside.value());
	}


	public void
	testNegativeUniversalReachability1()
	{
		// Specification:
		//   !(forall x. R_b(x) => (x == c || x == a))

		resetCounters();
		Node a = new Node(null, null);
		Node b = new Node(null, a);
		Node c = new Node(null, b);
		// c -> b -> a

		final PredicateFamily c_is_everywhere_outside =
			new PredicateFamily(TestClass.class,
					    // Universal predicate family requires negated predicate
					    new int[] { TEST_IS_NOT_IN_ENV, PredicateFamily.IGNORE_TEST },
					    new Object[] { c, a },
					    PredicateFamily.UNIVERSAL);

		Assertion.doAssert(new Assertion(new PredicateFamily[] {
					c_is_everywhere_outside,
				},
				new Step[] {
					null, // required format, cf constructor
					// -- regular code
					new SetTraversalPredicatesStep(new int[] { 0 }), // check the inside
					new TraversalStep(new Object[] { b }),
					new SetTraversalPredicatesStep(new int[] { 1 }), // ignore the outside
					new TraversalStep(), // collect from the root set
					// -- end of regular code
					null, // required format, cf constructor
					null, // required format, cf constructor
					null  // required format, cf constructor
				}));

		assertFalse(c_is_everywhere_outside.value());
	}


	/* ============================================================ */
	/* Dominance tests */


	public void
	testDominance()
	{
		// Specification:
		//   forall x. !R_/c(x) => x.value == 1
		resetCounters();
		Node a = new Node(null, null, 0);
		Node b = new Node(null, a, 1);
		Node c = new Node(null, b, 1);
		Node d = new Node(null, a, 0);
		// c -> b -> a
		//      d -> a
		a = null;
		b = null;

		final PredicateFamily all_in_area_are_1 =
			new PredicateFamily(TestClass.class,
					    // Universal predicate family requires negated predicate
					    new int[] { TEST_IS_NOT_1, PredicateFamily.IGNORE_TEST },
					    new Object[] { },
					    PredicateFamily.UNIVERSAL);

		Assertion.doAssert(new Assertion(new PredicateFamily[] {
					all_in_area_are_1
				},
				new Step[] {
					null, // required format, cf constructor
					// -- regular code
					new ExcludeNodesStep(new Object[] { c }),
					new SetTraversalPredicatesStep(new int[] { 1 }), // ignore the outside
					new TraversalStep(), // collect from the root set
					new IncludeNodesStep(new Object[] { c }),
					new SetTraversalPredicatesStep(new int[] { 0 }), // check the inside
					new TraversalStep(new Object[] { c }),
					// -- end of regular code
					null, // required format, cf constructor
					null, // required format, cf constructor
					null  // required format, cf constructor
				}));

		assertTrue(all_in_area_are_1.value());
		assertTrue(c05 == 2); // only b and c should have been counted
	}


	public void
	testNegativeDominance()
	{ // a is actually shared:  reachable, but not dominated.

		// Specification:
		//   !(exists x. !R_/c(x) => x.value == 1)
		resetCounters();
		Node a = new Node(null, null, 1);
		Node b = new Node(null, a, 0); // ONLY a is tagged with `1'
		Node c = new Node(null, b, 0);
		Node d = new Node(null, a, 0);
		// c -> b -> a
		//      d -> a
		a = null;
		b = null;

		final PredicateFamily one_in_area_is_1 =
			new PredicateFamily(TestClass.class,
					    // Universal predicate family requires negated predicate
					    new int[] { TEST_IS_1, PredicateFamily.IGNORE_TEST },
					    new Object[] { },
					    PredicateFamily.EXISTENTIAL);

		Assertion.doAssert(new Assertion(new PredicateFamily[] {
					one_in_area_is_1
				},
				new Step[] {
					null, // required format, cf constructor
					// -- regular code
					new ExcludeNodesStep(new Object[] { c }),
					new SetTraversalPredicatesStep(new int[] { 1 }), // ignore the outside
					new TraversalStep(), // collect from the root set
					new IncludeNodesStep(new Object[] { c }),
					new SetTraversalPredicatesStep(new int[] { 0 }), // check the inside
					new TraversalStep(new Object[] { c }),
					// -- end of regular code
					null, // required format, cf constructor
					null,  // required format, cf constructor
					null  // required format, cf constructor
				}));

		assertFalse(one_in_area_is_1.value());
	}



	/* ============================================================ */
	/* Overlap tests */

	/* ---------------------------------------- */
	// Positive overlap

	public void
	testNegativeExclude()
	{
		// Specification:
		//   !X_c+e
		resetCounters();

		Node a = new Node(null, null);
		Node b = new Node(null, a);
		Node c = new Node(null, b);
		Node d = new Node(null, a);
		Node e = new Node(null, d);
		// c -> b -> a
		// e -> d -> a

		Assertion asr = new Assertion(new PredicateFamily[] {},
					      new Step[] {
						      null, // required format, cf constructor
						      // -- regular code
						      new SetTraversalIdStep(0),
						      new TraversalStep(new Object[] { c }),
						      new SetTraversalIdStep(1),
						      new TraversalStep(new Object[] { e }),
						      new SetTraversalIdStep(2),
						      new TraversalStep(),
						      new SetTraversalIdStep(3),
						      // -- end of regular code
						      null, // required format, cf constructor
						      null,  // required format, cf constructor
						      null  // required format, cf constructor
					      });
		Assertion.doAssert(asr);
		assertTrue(asr.traversalOverlapped(1));
		assertTrue(asr.traversalOverlapped(2));
	}

	public void
	testPositiveExclude()
	{
		// Specification:
		//   X_c+e
		resetCounters();

		Node a = new Node(null, null);
		Node b = new Node(null, a);
		Node c = new Node(null, b);
		Node d = new Node(null, new Node(null, null));
		Node e = new Node(null, d);
		// c -> b -> a
		// e -> d -> fresh

		Assertion asr = new Assertion(new PredicateFamily[] {},
					    new Step[] {
						    null, // required format, cf constructor
						    // -- regular code
						    new SetTraversalIdStep(0),
						    new TraversalStep(new Object[] { c }),
						    new SetTraversalIdStep(1),
						    new TraversalStep(new Object[] { e }),
						    new SetTraversalIdStep(2),
						    new TraversalStep(),
						    new SetTraversalIdStep(3),
						    // -- end of regular code
						    null, // required format, cf constructor
						    null,  // required format, cf constructor
						    null  // required format, cf constructor
					    });
		Assertion.doAssert(asr);
		assertFalse(asr.traversalOverlapped(1));
		assertTrue(asr.traversalOverlapped(2));
	}


	// --------------------------------------------------------------------------------

	public void
	testMultiExclude()
	{
		// Specification:
		//   X_a+b+c
		resetCounters();

		Node a = new Node(null, new Node(null, new Node(null, null)));
		Node b = new Node(null, new Node(null, new Node(null, null)));
		Node c = new Node(null, new Node(null, new Node(null, null)));
		// a -> * -> *
		// b -> * -> *
		// c -> * -> *

		Assertion asr = new Assertion(new PredicateFamily[] {},
					    new Step[] {
						    null, // required format, cf constructor
						    // -- regular code
						    new SetTraversalIdStep(0),
						    new TraversalStep(new Object[] { a }),
						    new SetTraversalIdStep(1),
						    new TraversalStep(new Object[] { b }),
						    new SetTraversalIdStep(2),
						    new TraversalStep(new Object[] { c }),
						    new SetTraversalIdStep(3),
						    new TraversalStep(),
						    new SetTraversalIdStep(4),
						    // -- end of regular code
						    null, // required format, cf constructor
						    null,  // required format, cf constructor
						    null  // required format, cf constructor
					    });
		Assertion.doAssert(asr);
		assertFalse(asr.traversalOverlapped(1));
		assertFalse(asr.traversalOverlapped(2));
		assertTrue(asr.traversalOverlapped(3));
	}


	public void
	testPartExclude()
	{
		// Specification:
		//   X_a+b && !X_{a,b}+c
		resetCounters();

		Node k = new Node(null, null);
		Node a = new Node(null, new Node(null, new Node(null, null)));
		Node b = new Node(null, new Node(null, new Node(null, k)));
		Node c = new Node(null, new Node(null, new Node(k, null)));
		// a -> * -> *
		// b -> * -> * -> k
		// c -> * -> * -> k

		Assertion asr = new Assertion(new PredicateFamily[] {},
					    new Step[] {
						    null, // required format, cf constructor
						    // -- regular code
						    new SetTraversalIdStep(0),
						    new TraversalStep(new Object[] { a }),
						    new SetTraversalIdStep(1),
						    new TraversalStep(new Object[] { b }),
						    new SetTraversalIdStep(2),
						    new TraversalStep(new Object[] { c }),
						    new SetTraversalIdStep(3),
						    new TraversalStep(),
						    new SetTraversalIdStep(4),
						    // -- end of regular code
						    null, // required format, cf constructor
						    null,  // required format, cf constructor
						    null  // required format, cf constructor
					    });
		Assertion.doAssert(asr);
		assertFalse(asr.traversalOverlapped(1));
		assertTrue(asr.traversalOverlapped(2));
		assertTrue(asr.traversalOverlapped(3));
	}

	public void
	testDisjointnessOfExcludedNodes()
	{
		// Specification:
		//   X_a+b && !X_{a,b}+c
		resetCounters();

		Node a = constructA();
		// a -> * -> *

		Assertion asr = new Assertion(new PredicateFamily[] {},
					    new Step[] {
						    null, // required format, cf constructor
						    // -- regular code
						    new SetTraversalIdStep(1),
						    new TraversalStep(new Object[] { a }),
						    new SetTraversalIdStep(2),
						    new ExcludeNodesStep(new Object[] { a }),
						    new TraversalStep(),
						    new IncludeNodesStep(new Object[] { a }),
						    new SetTraversalIdStep(0),
						    // -- end of regular code
						    null, // required format, cf constructor
						    null,  // required format, cf constructor
						    null  // required format, cf constructor
					    });
		Assertion.doAssert(asr);
		assertFalse(asr.traversalOverlapped(1));
		assertFalse(asr.traversalOverlapped(2));
		assertFalse(asr.traversalOverlapped(3));
	}
	public static Node constructA()
	{
		return new Node(null, new Node(null, new Node(null, null)));
	}

	public void
	testStress0()
	{
		for (int i = 0; i < 63; i++)
			testUniversalReachability();
	}


	public void
	testStress1()
	{
		for (int i = 0; i < 63; i++)
			testDominance();
	}


	public void
	testStress2()
	{
		for (int i = 0; i < 63; i++)
			testNegativeExclude();
	}


	public void
	testStress3()
	{
		for (int i = 0; i < 63; i++)
			testPartExclude();
	}

}
