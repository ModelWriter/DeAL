package test;

// heap traversal integration test
public class HTIT4
{
	public static final void
	main(String[] args)
	{
		int i = 0;
		Node a = new Node(0, null);
		Node b = new Node(0, a);
		Node c = new Node(0, b);
		Node d = new Node(0, a);
		Node e = new Node(0, d);

		GCAssert.assertDisjoint("(forall Node n : (reach[c](n) || reach[e](n)) -> n.v == 0)");
		System.out.println("all-clear");
	}
}