import org.junit.Test;

import static org.junit.Assert.*;

public class LinkedListTest {
    @Test
    public void testMethod() {
        LinkedList<Integer> b = new LinkedList<Integer>();
        assertEquals("()", b.toString());
        assertEquals("()", b.toStringReverse());
        assertEquals(0, b.size());
        LinkedList<Integer>.Node n = b.append(6);
        assertEquals("(6)", b.toString());
        assertEquals("(6)", b.toStringReverse());
        assertEquals(1, b.size());
        assertEquals(n, b.getLast());
        LinkedList<Integer>.Node m = b.append(8);
        assertEquals("(6, 8)", b.toString());
        assertEquals("(8, 6)", b.toStringReverse());
        assertEquals(2, b.size());
        assertEquals(m, b.getLast());
        LinkedList<Integer>.Node h = b.prepend(4);
        assertEquals("(4, 6, 8)", b.toString());
        assertEquals("(8, 6, 4)", b.toStringReverse());
        assertEquals(3, b.size());
        assertEquals(h, b.getLast());

        LinkedList<Integer> c = new LinkedList<Integer>();
        LinkedList<Integer>.Node n2 = c.append(2);
        LinkedList<Integer>.Node n3 = c.append(3);
        LinkedList<Integer>.Node n4 = c.append(4);
        LinkedList<Integer>.Node n5 = c.insertAfter(5,n4);
        assertEquals("(2, 3, 4, 5)", c.toString());
        assertEquals("(5, 4, 3, 2)", c.toStringReverse());
        assertEquals(n5, c.getLast());
        LinkedList<Integer>.Node n0 = c.insertBefore(0,n2);
        assertEquals("(0, 2, 3, 4, 5)", c.toString());
        assertEquals("(5, 4, 3, 2, 0)", c.toStringReverse());
        assertEquals(n0, c.getFirst());
        LinkedList<Integer>.Node n1 = c.insertAfter(1,n0);
        assertEquals("(0, 1, 2, 3, 4, 5)", c.toString());
        assertEquals("(5, 4, 3, 2, 1, 0)", c.toStringReverse());
        assertEquals(6, c.size());
        assertEquals(n5, c.getLast());
        c.remove(n0);
        c.remove(n5);
        c.remove(n3);
        assertEquals("(1, 2, 4)", c.toString());
        assertEquals("(4, 2, 1)", c.toStringReverse());
        assertEquals(n4, c.getLast());
        assertEquals(n1, c.getFirst());

    }
}