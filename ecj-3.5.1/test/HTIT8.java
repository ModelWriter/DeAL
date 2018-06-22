package test;

import java.util.ArrayList;

// heap traversal integration test
public class HTIT8
{
	public static final void
	main(String[] args)
	{
		ArrayList<Node> a = new ArrayList<Node>();
		ArrayList<Node> b = new ArrayList<Node>();


		a.add(new Node(1, new Node(1, null)));
		a.add(new Node(1, new Node(1, null)));
		b.add(new Node(0, new Node(0, null)));
		b.add(new Node(0, new Node(0, null)));

		GCAssert.assertDisjoint("forall Node x : (reach[a](x) || reach[b](x)) && !(reach[/{a,b}](x))");

		System.out.println("all-clear");
	}
}