package test;

// heap traversal integration test
public class HTIT6
{
	public static final void
	main(String[] args)
	{
		int i = 0;
		Node a = new Node(1, null);
		Node b = new Node(0, a);
		Node c = new Node(0, b);
		Node d = new Node(1, a);
		Node e = new Node(1, d);

		GCAssert.assertion("(forall Node n : ((!reach[/c](n)) -> n.v == 0))");
		System.out.println("all-clear");
	}
}