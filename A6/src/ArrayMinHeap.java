import java.util.*;

/** An instance is a priority queue of ints implemented as a min-heap in an
  * array of size 1000. The heap values are themselves the priorities */
public class ArrayMinHeap {
    
    private int size; // number of elements in the priority queue (and heap)
    
    /** heap invariant for b[0..size-1]:
      *  b[0..size-1] is viewed as a min-heap, i.e.
      *  1. Each array element in b[0..size-1] contains a value of the heap.
      *  2. The children of each b[i] are b[2i+1] and b[2i+2].
      *  3. The parent of each b[i] is b[(i-1)/2].
      *  4. The priority of the parent of each b[i] is <= the priority of b[i].
      *  5. Priorities for the b[i] used for the comparison in point 4
      *     are the elements themselves: the priority of b[i] is b[i].
      */
    
    private int[] b= new int[1000];
    
    /** Constructor: an empty heap. */
    public ArrayMinHeap() {
    }
    
    /** Return a string that gives this priority queue, in the format:
      * [item0, item1, ..., item(N-1)]
      * Thus, the list is delimited by '['  and ']' and ", " (i.e. a
      * comma and a space char) separate adjacent items. */
    public String toString() {
        String s= "";
        for (int k= 0; k < size; k= k+1) {
            if (s.length() > 0) {
                s = s + ", ";
            }
            s = s + b[k];
        }
        return "[" + s + "]";
    }
    
    /** Return the number of elements in the priority queue.
      * This operation takes constant time. */
    public int size() {
        return size;
    }
    
    /** Return true iff the priority queue is empty. 
      * This operation takes constant time. */
    public boolean isEmpty() {
        return size == 0;
    }
    
    /** Add e with priority e to the priority queue.
      * The time is O(log N).
      * Precondition: the size of the heap is < 100. */ 
    public void add(int e) {
        b[size]= e;
        size= size + 1;
        bubbleUp(size-1);
    }
    
    /** Return the element of the priority queue with lowest priority, without
      * changing the queue. This operation takes constant time.
      * Precondition: the priority queue is not empty. */
    public int peek() {
        assert 0 < size;
        return b[0];
    }
    
    /** Remove and return the element of the priority queue with lowest priority.
      * The worst-case time is O(log n).
      * Precondition: the priority queue is not empty. */
    public int poll() {
        assert 0 < size;
        int val= b[0];
        
        if (size == 1) {
            size= 0;
            return val;
        }
        
        // At least 2 elements in queue
        b[0]= b[size-1];
        size= size - 1;
        
        bubbleDown(0);
        return val;
    }
    
    /** Bubble b[k] up in heap to its right place.
      * Precondition: Every b[i] satisfies the heap property except perhaps
      *       k's priority < parent's priority */
    private void bubbleUp(int k) {
        int bk= b[k];
        
        // Inv: bk belongs in b[k], and b[k] is considered to be empty.
        //    Every b[i] satisfies the heap property except perhaps
        //    k's priority < parent's priority
        while (k > 0) {
            int p= (k-1) / 2;  // k's parent
            int bp= b[p];
            if (bk >= bp) {
                b[k]= bk;
                return;
            }
            
            b[k]= bp;
            k= p;
        }
        // k = 0
        b[k]= bk;
    }
    
    /** Bubble b[k] down in heap until it finds the right place.
      * Precondition: Every b[i] satisfies the heap property except perhaps
      * k's priority > a child's priority. */
    private void bubbleDown(int k) {
        int bk = b[k];
        
        // Invariant: bk belongs in b[k], and b[k] is considered to be empty AND
        //    Every b[i] satisfies the heap property except perhaps
        //    k's priority > a child's priority
        while (2 * k + 1 < size) {
            int c = getSmallerChild(k);
            int bc = b[c];
            
            if (bk <= bc) {
                b[k] = bk;
                return;
            }
            
            b[k]= bc;
            k= c;
        }
        
        b[k]= bk;
    }
    
    /** Return the index of the smaller child of b[q]
      * Precondition: left child exists: 2q+1 < size of heap */
    private int getSmallerChild(int q) {
        int lChild= 2*q + 1;
        if (lChild + 1  ==  size) return lChild;
        
        if (b[lChild] < b[lChild+1])
            return lChild;
        return lChild+1;
    }
}