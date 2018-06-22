package gtest;

public class Node
{
	public Node next;
	public Node prev;
	public int value = 0;

	public
	Node(final Node _next, final Node _prev)
	{
		this.next = _next;
		this.prev = _prev;
	}

	public
	Node(final Node _next, final Node _prev, final int _value)
	{
		this.next = _next;
		this.prev = _prev;
		this.value = _value;
	}
}
