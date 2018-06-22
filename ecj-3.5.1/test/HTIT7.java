package test;

// heap traversal integration test
public class HTIT7
{
	static int i = 0;
	static Node a = new Node(1, null);
	static Node b = new Node(0, a);
	static Node c = new Node(0, b);
	static Node d = new Node(1, a);
	static Node e = new Node(1, d);

	static void t()
	{


		GCAssert.assertion("(forall Node n : ((!reach[/c](n)) -> n.v == 0))");
	}

	public static final void
	main(String[] args)
	{
		for (int k = 0; k < 100; k++) {
			t();
			Object o;
			for (int j = 0; j < 900; j++)
				o = new char[1000];
			o = null;
		}
		System.out.println("all-clear");
	}
}