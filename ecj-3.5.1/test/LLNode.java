package test;

/**
 * Represents a linked list node
 */
public class LLNode {

  public LLNode next;
  public LLNode prev;
  public Object payload;

  public LLNode(LLNode prev, LLNode next, Object payload) {
    this.prev = prev;
    this.next = next;
    this.payload = payload;
  }

}
