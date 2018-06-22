package test;

public class Node
{
	public int v;
	public Node next;

	public Node() {}
	public Node(int _v, Node _next) { this.v = _v; this.next = _next; }
	public Node(int _v) { this.v = _v; }
}