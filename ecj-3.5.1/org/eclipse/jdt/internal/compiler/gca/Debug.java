package org.eclipse.jdt.internal.compiler.gca;

import java.util.*;

public class Debug
{
	private static boolean enable_all = false;
	private static final HashSet enabled = new HashSet();

	private static final HashSet valid;

	static {
		String[] valid_s = new String[] { "ast", "hook", "all", "extraction", "T", "traversal" };
		valid = new HashSet();
		for (int i = 0; i < valid_s.length; i++)
			valid.add(valid_s[i]);
	}

	public static void
	setDebug(String d)
	{
		if (d.equals("all"))
			enable_all = true;
		else
			enabled.add(d);
	}


	public static void
	print(String subsys, String s)
	{
		if (enable_all || enabled.contains(subsys))
			System.out.print(s);
	}
	
	public static void
	println(String subsys, String s)
	{
		print(subsys, s + "\n");
	}


	public static void
	print(String msg)
	{
		print("all", msg);
	}

	public static void
	println(String msg)
	{
		println("all", msg);
	}
	
}