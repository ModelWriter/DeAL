package test;

// heap traversal integration test
public class HTIT3
{
	public static final void
	main(String[] args)
	{
		int i = 0;
		Node a = new Node(0, null);
		Node b = new Node(0, a);
		Node c = new Node(0, b);
		Node d = new Node(2, null);

		GCAssert.assertion("(exists Node n : reach[c](n) -> n.v == i) && (forall Node n: n.v == 2 -> n == d ) ");
		System.out.println("all-clear");
	}
}