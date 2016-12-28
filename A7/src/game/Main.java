package game;

import gui.GUI;

import java.io.File;
import java.util.*;
import java.util.concurrent.Semaphore;

/** Game starting methods. Also serves as a util holder */
public class Main {

	/** Student directory */
	public static final String studentDirectory = "";
	
	
	public static File file= null; // map being used --null if random map
	
	public static Board board= null; 
	public static GUI gui= null;
	
	/** Read in the name of a Map t start with.
	 * @throws IllegalArgumentException if args is null or has length 0.
	 */
	public static void main(String[] args) throws IllegalArgumentException {
			Board b= Board.randomBoard(Math.abs((new Random()).nextLong()));
			gui= new GUI(b);
			b.setGUI(gui);
		}
	
	/** Store in board instance a random board from seed seed 
	 * and create the GUI using it. */
    public void randomBoard(long seed) {
        file = null;
        board = Board.randomBoard(seed);
        gui= new GUI(board);
    }

	/** Return the sum of the natural numbers in 0..i, recursively!
	 * (mathematicially, that's 0 if i < 0) */
	public static int sumTo(int i) {
		if (i < 0) return 0;
		return sumToHelper(i,0);
	}

	/** Helper for sumTo method such that it's tail recursive */
	private static int sumToHelper(int i, int s) {
		if (i == 0) return s;
		return sumToHelper(i-1, s+i);
	}

	/** Return s with quotes added around it.
	 * Used in JSON creation methods throughout project. */
	public static String addQuotes(String s) {
		return "\"" + s + "\"";
	}

	/** Return a random element of elms (null if elms is empty).
	 * Synchronizes on {@code elms} to prevent concurrent modification. */
	public static <T> T randomElement(Collection<T> elms) {
		T val = null;
		synchronized(elms) {
			if (elms.isEmpty())
				return null;
			Iterator<T> it = elms.iterator();
			for (int i = 0; i < (int)(Math.random() * elms.size()) + 1; i++) {
				val = it.next();
			}
		}
		return val;
	}

}
