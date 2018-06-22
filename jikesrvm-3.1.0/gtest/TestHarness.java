/*
 * For some reason JUnit isn't co-operating.  It's faster to write my own
 * test harness than to debug this, so here we go.
 */

package gtest;

import java.lang.reflect.Method;
public class TestHarness
{
	public static void main(String[] _) throws Exception
	{
		int tests = 0;
		int successful = 0;

		Class self = Class.forName("gtest.Tests");
		Object o = self.newInstance();
		Method[] methods = self.getMethods();
		for (Method m : methods)
			if (m.getName().startsWith("test")) {
				boolean failed = false;

				++tests;
				System.out.print(m.getName() + " : ");
				try {
					m.invoke(o);
				} catch (Exception e) {
					System.out.println("\u001b[1;31mFAILED\u001b[0m");
					e.printStackTrace();
					failed = true;
				}

				if (!failed) {
					System.out.println("\u001b[1;32mOK\u001b[0m");
					++successful;
				}
			}
		System.out.println("\nSummary: " + successful + " / " + tests + " succeeded");
	}

	public static void
	assertTrue(boolean b)
	{
		if (!b)
			throw new AssertionFailedException();
	}


	public static void
	assertFalse(boolean b)
	{
		if (b)
			throw new AssertionFailedException();
	}

	public static final class AssertionFailedException extends RuntimeException {}
}