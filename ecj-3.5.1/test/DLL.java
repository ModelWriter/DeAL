package test;
/**
 * A DeAL example that checks well formedness of doubly-linked lists
 */

public class DLL {

  private LLNode head = null;
  private LLNode tail = null;

  /** 
   * Creates a doubly linked list of the specified length
   */
  public DLL(int numNodes) {
    for (int i=0; i<numNodes; i++) {
      add(null);
    }
    //assert(repOK());
  }

  /**
   * Adds an element to the end of the linked list
   */
  public void add(Object element) {
    //assert(repOK());
    if (head == null && tail == null) {
      head = new LLNode(null, null, element);
      tail = head;
    } else {
      LLNode temp = tail;
      tail = new LLNode(temp, null, element);
      temp.next = tail;
    }
    //assert(repOK());
  }

  /**
   * Incorrectly adds an element to the end of the linked lists.  Fails to 
   * set the next pointer for the current tail element.
   */
  public void addMalformed(Object element) {
    if (head == null && tail == null) {
      head = new LLNode(null, null, element);
      tail = head;
    } else {
      LLNode temp = tail;
      tail = new LLNode(temp, null, element);
    }
    //assert(repOK());    // should fail
  }

  /**
   * Checks well-formedness of data structure
   */
  public boolean repOK() {
    // head and tail must either both be null or both non-null
    if (!((head == null && tail == null) || (head != null && tail != null)))
      return false;

    // head.prev == null, tail.next == null
    if (!((head.prev == null) || (tail.next == null))) 
      return false;

    // forall LLNodes, starting from head, node.next.prev == node &&
    // node.prev.next == node
    LLNode curr = head;
    while (curr != null) {
      if (curr.next != null) 
        if (!(curr.next.prev == curr)) 
          return false;

      if (curr.prev != null)
        if (!(curr.prev.next == curr))
          return false;

      curr = curr.next;
    }

    // forall LLNodes, starting from tail node.prev.next == node &&
    // node.prev.next == node
    curr = tail;
    while (curr != null) {
      if (curr.next != null) 
        if (!(curr.next.prev == curr)) 
          return false;

      if (curr.prev != null)
        if (!(curr.prev.next == curr))
          return false;

      curr = curr.prev;
    }

    return true;
  }

  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Usage: java DLL <numNodes>");
      System.exit(1);
    }

    DLL list = new DLL(Integer.parseInt(args[0]));
    list.addMalformed(null);

    //GCAssert.assertion("(forall LLNode x : ((reach[list](x)) -> x.next.prev == x))");
    GCAssert.assertion("forall LLNode x : x.next.prev == x");
    //GCAssert.assertion("forall LLNode x : x.payload == null");
    //GCAssert.assertion("!(forall LLNode x : x.next.prev == null)");
  }

}
