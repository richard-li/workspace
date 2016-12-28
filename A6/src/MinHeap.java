/* Time spent on a6:  8 hours and 0 minutes.

 * Name(s): Richard Li, Caleb Zhu
 * Netid(s): rl393, cz225
 * What I thought about this assignment:
 * Gave good understanding of heaps and their
 * implementation.
 *
 */

import java.util.*;

/** An instance is a priority queue of elements of type E
 * implemented asa min-heap. */
public class MinHeap<E> implements PQueue<E> {

    private int size; // number of elements in the priority queue (and heap)

    /** heap invariant for b[0..size-1]:
     *  b[0..size-1] is viewed as a min-heap, i.e.
     *  1. Each array element in b[0..size-1] contains a value of the heap.
     *  2. The children of each b[i] are b[2i+1] and b[2i+2].
     *  3. The parent of each b[i] is b[(i-1)/2].
     *  4. The priority of the parent of each b[i] is <= the priority of b[i].
     *  5. Priorities for the b[i] used for the comparison in point 4
     *     are given in map. map contains one entry for each element of
     *     the heap, and map and b have the same size.
     *     For each element e in the heap, the map entry contains in the
     *     EInfo object the priority of e and its index in b.
     */
    private ArrayList<E> b= new ArrayList<E>();
    private HashMap<E, EInfo> map= new HashMap<E, EInfo>();

    /** Constructor: an empty heap. */
    public MinHeap() {
    }

    /** Return a string that gives this priority queue, in the format:
     * [item0:priority0, item1:priority1, ..., item(N-1):priority(N-1)]
     * Thus, the list is delimited by '['  and ']' and ", " (i.e. a
     * comma and a space char) separate adjacent items. */
    public @Override String toString() {
        String s= "";
        for (E t : b) {
            if (s.length() > 0) {
                s = s + ", ";
            }
            s = s + t + ":" + map.get(t).priority;
        }
        return "[" + s + "]";
    }

    /** Return a string that gives the priorities in this priority queue,
     * in the format: [priority0, priority1, ..., priority(N-1)]
     * Thus, the list is delimited by '['  and ']' and ", " (i.e. a
     * comma and a space char) separate adjacent items. */
    public String toStringPriorities() {
        // Complete this only if you want to. We do not test it.
        throw new UnsupportedOperationException();
    }

    /** Return the number of elements in the priority queue.
     * This operation takes constant time. */
    public @Override int size() {
        return size;
    }

    /** Return true iff the priority queue is empty.
     * This operation takes constant time. */
    public @Override boolean isEmpty() {
        return size == 0;
    }

    /** Add e with priority p to the priority queue.
     *  Throw an illegalArgumentException if e is already in the queue.
     *  The expected time is O(log N) and the worst-case time is O(N). */
    public @Override void add(E e, double p) throws IllegalArgumentException {
        if (map.containsKey(e)) {
            throw new IllegalArgumentException("e is already in priority queue");
        }
        // TODO  First: Do add and bubbleUp.
        b.add(e);
        EInfo info = new EInfo(b.indexOf(e), p);
        map.put(e, info);
        size++;
        bubbleUp(b.indexOf(e));
    }

    /** Return the element of the priority queue with lowest priority, without
     *  changing the queue. This operation takes constant time.
     *  Precondition: the priority queue is not empty. */
    public E peek() {
        assert 0 < size;
        /// TODO  Second: Do peek.
        return b.get(0);
    }

    /** Remove and return the element of the priority queue with lowest priority.
     * The expected time is O(log n) and the worst-case time is O(N).
     *  Precondition: the priority queue is not empty. */
    public @Override E poll() {
        assert 0 < size;
        // TODO  THIRD: Do poll and bubbleDown.
        E e = peek();
        if (size == 1) {
            size = 0;
            b.remove(0);
            map.remove(e);
            return e;
        }
        map.remove(e);
        b.set(0, b.get(size-1));
        b.remove(size-1);
        map.get(b.get(0)).index = 0;
        size--;
        if(size==1)
            return e;
        bubbleDown(0);
        return e;
    }


    /** Change the priority of element e to p.
     *  The expected time is O(log N) and the worst-case is time O(N).
     *  Precondition: e is in the priority queue */
    public @Override void updatePriority(E e, double p) {
        // TODO  FOURTH: Do updatePriority.
        int index = map.get(e).index;
        map.get(e).priority = p;
        if(index >= 0 && map.get(b.get((index-1)/2)).priority > p)
            bubbleUp(index);
        if((2*index+1) < size && map.get(b.get((getSmallerChild(index)))).priority < p)
            bubbleDown(index);
    }

    /** Bubble b[k] up in heap to its right place.
     *  Precondition: Every b[i] satisfies the heap property except perhaps
     *       k's priority < parent's priority */
    private void bubbleUp(int k) {
        // TODO  First: Do add and bubbleUp.
        E child = b.get(k);
        double childPriority = map.get(child).priority;

        while (k > 0) {
            int parentIndex = (k - 1) / 2;  // k's parent
            E parent = b.get(parentIndex);
            double parentPriority = map.get(parent).priority;
            if (childPriority >= parentPriority)
                return;
            b.set(k, parent);
            map.get(parent).index = k;
            k = parentIndex;
            map.get(child).index = k;
            b.set(k, child);
        }
    }

    /** Bubble b[k] down in heap until it finds the right place.
     *  Precondition: Every b[i] satisfies the heap property except perhaps
     *  k's priority > a child's priority. */
    private void bubbleDown(int k) {
        // TODO  THIRD: Do poll and bubbleDown.
        E parent = b.get(k);
        double parentPriority = map.get(parent).priority;
        int smaller = getSmallerChild(k);
        while (smaller < size) {
            E smallerChild = b.get(smaller);
            double smallPriority = map.get(smallerChild).priority;
            if (parentPriority <= smallPriority)
                return;
            b.set(k, smallerChild);
            map.get(smallerChild).index = k;
            k = smaller;
            b.set(k, parent);
            smaller = 2*k+2;
            if(smaller >=size || smaller!= getSmallerChild(k))
                smaller--;
            }
    }

    /** Return the index of the smaller child of b[q]
     *  Precondition: left child exists: 2q+1 < size of heap */
    private int getSmallerChild(int q) {
        // Complete this only if you want to. We do not test it.
        int lchildIndex = 2*q + 1;
        double lchild  = map.get(b.get(lchildIndex)).priority;
        if(lchildIndex + 1 == size)
            return lchildIndex;
        double rchild = map.get(b.get(lchildIndex + 1)).priority;
        if (lchild < rchild)
            return lchildIndex;
        return lchildIndex + 1;
    }

    /** An instance contains the index and priority of an element of the heap. */
    private static class EInfo {
        private int index;  // index of this element in map
        private double priority; // priority of this element

        /** Constructor: an instance in b[i] with priority p. */
        private EInfo(int i, double p) {
            index= i;
            priority= p;
        }
    }
}