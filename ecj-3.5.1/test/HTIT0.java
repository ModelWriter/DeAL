package test;

// heap traversal integration test
public class HTIT0
{
	public static final void
	main(String[] args)
	{
		Node a = new Node();
		Node b = new Node(0, a);
		Node c = new Node(0, b);

		GCAssert.assertion("forall Object n : reach[c](n) -> n instanceof Node");
		System.out.println("all-clear");
	}
}