/* Time spent on a2:  6 hours and 0 minutes.

 * Name: Caleb Zhu and Richard Li
 * Netid: cz225 and rl393
 * What I thought about this assignment:
 * Kind of confusing at first but fair after some thought
 *
 */

/** An instance is a doubly linked list. */
public class LinkedList<E> {
    private int size;  // Number of values in the linked list.
    private Node first; // first node of linked list (null if none)
    private Node last; // last node of linked list (null if none)

    /** Constructor: an empty linked list. */
    public LinkedList() {
    }

    /** Return the number of values in this list. */
    public int size() {
        return size;
    }

    /** Return the first node of the list (null if the list is empty). */
    public Node getFirst() {
        return first;
    }

    /** Return the last node of the list (null if the list is empty). */
    public Node getLast() {
        return last;
    }

    /** Return the value of node n of this list.
     * Precondition: n is a node of this list; it may not be null. */
    public E valueOf(Node n) {
        assert n != null;
        return n.value;
    }

    /** Return a representation of this list: its values, with adjacent
     * ones separated by ", ", "(" at the beginning, and ")" at the end. <br>
     *
     * E.g. for the list containing 6 3 8 in that order, the result it "(6, 3, 8)". */
    public String toString() {

        String result = "";
        Node string = first;
        while (string != null) {
            if (string == last) {
                result += last.value;
            } else {
                result += string.value + ", ";
            }
            string = string.next;
        }

        return '(' + result + ')';
    }

    /** Return a representation of this list: its values in reverse, with adjacent
     * ones separated by ", ", "(" at the beginning, and ")" at the end. <br>
     *
     * E.g. for the list containing 6 3 8 in that order, the result it "(8, 3, 6)".*/
    public String toStringReverse() {

        String result = "";
        Node string = last;

        while(string != null) {
            if (string == first) {
                result += first.value;
            } else {
                result += string.value + ", ";
            }
            string = string.prev;
        }

        return '(' + result + ')';
    }

    /** add value v in a new node at the end of the list and
     * return the new node. */
    public Node append(E v) {
        assert v != null;

        Node z = new Node(last, null, v);
        last = z;
        if (size == 0) {
            first = last;
        } else if (size > 0) {
            last.prev.next = z;
        }
        size++;
        return last;
    }

    /** add value v in a new node at the beginning of the list and
     * return the new node */
    public Node prepend(E v) {
        assert v != null;

        Node z = new Node(null, first, v);
        first = z;
        if (size == 0) {
            last = first;
        } else if (size > 0) {
            first.next.prev = z;
        }
        size++;
        return last;
    }

    /** Insert value v in a new node after node n and
     * return the new node.
     * Precondition: n must be a node of this list; it may not be null. */
    public Node insertAfter(E v, Node n) {
        //TODO: This is the fifth method to write and test

        assert v!= null;

        Node z = new Node(n,null,v);
        if(n.next == null){
            n.next = z;
            last = z;
        }
        else{
            n.next.prev = z;
            z.next = n.next;
            n.next = z;
        }

        size++;
        return z;
    }

    /** Insert value v in a new node before node n and
     * return the new node.
     * Precondition: n must be a node of this list; it may not be null. */
    public Node insertBefore(E v, Node n) {
        assert v!= null;

        Node z = new Node(null,n,v);
        if(n.prev == null){
            n.prev = z;
            first = z;
        }
        else{
            n.prev.next = z;
            z.prev = n.prev;
            n.prev = z;
        }

        size++;
        return z;
    }

    /** Remove node n from this list.
     * Precondition: n must be a node of this list; it may not be null. */
    public void remove(Node n) {
        assert n!= null;

        if(n.next == null && n.prev == null){
            n.value = null;
        }
        else
        if(n.prev == null){
            n.next.prev = null;
            first = n.next;
        }
        else
        if(n.next == null){
            n.prev.next = null;
            last = n.prev;
        }
        else{
            n.next.prev = n.prev;
            n.prev.next = n.next;
        }
        size--;
    }


    /*********************/

    /** An instance is a node of this list. */
    public class Node {
        /** Previous node on list (null if this is the first node). */
        private Node prev;

        /** The value of this element. */
        private E value;

        /** Next node on list. (null if is the last node). */
        private Node next;

        /** Constructor: an instance with previous node p (can be null),
         * next node n (can be null), and value v. */
        private Node(Node p, Node n, E v) {
            prev = p;
            next = n;
            value = v;
        }

        /** Return the value of this node. */
        public E getValue() {
            return value;
        }

        /** Return the node previous to this one (null if this is the
         * first node of the list). */
        public Node prev() {
            return prev;
        }

        /** Return the next node in this list (null if this is the
         * last node of this list). */
        public Node next() {
            return next;
        }
    }

}