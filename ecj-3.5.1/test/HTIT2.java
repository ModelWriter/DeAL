package test;

// heap traversal integration test
public class HTIT2
{
	public static final void
	main(String[] args)
	{
		Node a = new Node(1, null);
		Node b = new Node(0, a);
		Node c = new Node(0, b);

		GCAssert.assertion("exists Node n : reach[c](n) -> n.v == 1");
		GCAssert.assertion("exists Node n : reach[c](n) -> n.v == 0");
		System.out.println("all-clear");
	}
}