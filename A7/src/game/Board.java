package game;

import gui.*;

import java.awt.Color;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import org.json.*;

/** A Board container  HashSets of Edges and Nodes that make up the graph. 
 * <br><br>
 * Boards are either randomly generated from a seed or loaded from a file.
 */
public final class Board implements JSONString {
    /** The random seed from which this Board was generated: 
     * -1 if loaded from a non-random file. */
    public final long seed;
    
    /** Name of first city. */
    protected static final String FIRST_CITY= "Ithaca";

    private HashSet<Edge> edges= new HashSet<Edge>();    //All edges in this board

    protected int minLength;			//Min length among all edges
    protected int maxLength;			//Max length among all edges

    private HashSet<Node> nodes= new HashSet<Node>();    //All nodes in this board

    public GUI gui= null; 

    /** Constructor: a board from the given serialized version of the board for g */
    protected Board(JSONObject obj) {

        //Read seed if possible; otherwise use -1.
        if (obj.has(SEED_TOKEN)) {
            seed = obj.getLong(SEED_TOKEN);
        } else {
            seed = -1;
        }
        //Read score coefficients
        JSONArray scoreJSON = obj.getJSONArray(Board.SCORE_TOKEN);

        //Read in all nodes of board - read all nodes before reading any edges
        for (String key : obj.keySet()) {
            if (key.startsWith(Board.NODE_TOKEN)) {
                JSONObject nodeJSON = obj.getJSONObject(key);
                Node n = new Node(this, nodeJSON.getString(BoardElement.NAME_TOKEN), null);
                Circle c = n.getCircle();
                c.setX1(nodeJSON.getInt(BoardElement.X_TOKEN));
                c.setY1(nodeJSON.getInt(BoardElement.Y_TOKEN));
                n.x = c.getX1();
                n.y = c.getY1();
                getNodes().add(n);
            }
        }

        //Scale the locations of the nodes based on the gui size
        scaleComponents();

        //Read in all edges of board. Precondition - all nodes already read in
        for (String key : obj.keySet()) {
            if (key.startsWith(Board.EDGE_TOKEN)) {
                JSONObject edgeJSON = obj.getJSONObject(key);
                JSONArray exitArr = edgeJSON.getJSONArray(BoardElement.LOCATION_TOKEN);

                int length = edgeJSON.getInt(BoardElement.LENGTH_TOKEN);
                Node firstExit = getNode((String)exitArr.get(0));
                Node secondExit = getNode((String)exitArr.get(1));

                Edge e = new Edge(this, firstExit, secondExit, length);
                getEdges().add(e);
                firstExit.addExit(e);
                secondExit.addExit(e);
            }
        }
        //board reading finished.

    }

    /** Return a random node in this board */
    public Node getRandomNode() {
        return Main.randomElement(nodes);
    }

    /** Return a random edge in this board */
    public Edge getRandomEdge() {
        return Main.randomElement(edges);
    }

    /** Return a HashSet containing all the Nodes in this board. 
     * Technically allows addition and removal of Nodes to this board -
     * BUT DON'T DO IT. */
    public HashSet<Node> getNodes() {
        return nodes;
    }

    /** Return the number of Nodes in this board */
    public int getNodesSize() {
        return nodes.size();
    }

    /** Return the Node named {@code name} in this board if it exists, null otherwise. */
    public Node getNode(String name) {
        for (Node n : nodes) {
            if (n.name.equals(name))
                return n;
        }

        return null;
    }

    /** Return the set of Edges in this board. 
     * Technically allows addition and removal of Edges to this board -
     * BUT DON'T DO IT. */
    public HashSet<Edge> getEdges() {
        return edges;
    }

    /** Return the number of Edges in this board. */
    public int getEdgesSize() {
        return edges.size();
    }

    /** Return true iff there is any intersection of the lines drawn by the
     * edges in edges.
     * 
     * Used for GUI intersection detection, not useful outside of the GUI context.
     * Has nothing to say about the non-GUI version of the board.
     * Students: not Useful for Game.
     */
    public boolean isIntersection() {
        for (Edge r : edges) {
            for (Edge r2 : edges) {
                if (! r.equals(r2)) {
                    if (r.getLine().intersects(r2.getLine()))
                        return true;
                }
            }
        }

        return false;
    }

    /** Update the Minimum and Maximum lengths of all edge instances.
     * Called internally during processing. 
     * No need to call this after game initialized - it won't do anything. */
    public void updateMinMaxLength() {
        minLength = Edge.DEFAULT_MIN_LENGTH;
        maxLength = Edge.DEFAULT_MAX_LENGTH;

        for (Edge e : edges) {
            minLength = Math.min(minLength, e.length);
            maxLength = Math.max(maxLength, e.length);
        }
    }

    /** Return the maximum length of all edges on the board. */
    public int getMaxLength() {
        return maxLength;
    }

    /** Return the minimum length of all edges on the board. */
    public int getMinLength() {
        return minLength;
    }

    /** Return a 2x1 array of edges that have lines that intersect.
     * If no two edges intersect, return null.
     * 
     * Used for GUI intersection detection, not useful outside of the GUI context.
     * Has nothing to say about the non-GUI version of the board.
     * Students: Not useful
     */
    public Edge[] getAIntersection() {
        for (Edge r : edges) {
            for (Edge r2 : edges) {
                if (!r.equals(r2)) {
                    if (r.getLine().intersects(r2.getLine())) {
                        Edge[] intersectingRoads = {r, r2};
                        return intersectingRoads;
                    }

                }
            }
        }

        return null;
    }


    /** Return a String representation of this board, including edges and nodes. */
    @Override
    public String toString() {
        String output = "";
        Iterator<Node> nodesIterator = nodes.iterator();
        while (nodesIterator.hasNext()) {
            Node n = nodesIterator.next();
            output += n + "\t";
            Iterator<Edge> roadsIterator = n.getTrueExits().iterator();
            while (roadsIterator.hasNext()) {
                Edge r = roadsIterator.next();
                output += r.getOther(n).name+"-"+r.length;
                if (roadsIterator.hasNext())
                    output += "\t";
            }
            if (nodesIterator.hasNext())
                output += "\n";
        }
        return output;
    }


    private static final String SCORE_TOKEN = "scoreCoeff";
    private static final String NODE_TOKEN = "node-";
    private static final String EDGE_TOKEN = "edge-";
    private static final String SEED_TOKEN = "seed";

    /** Return a JSON-compliant version of toString().
     * A full serialized version of the board, including:
     * > Seed
     * > Cost constants
     * > Nodes
     * > Edges
     * > Trucks
     * > Parcels */
    @Override
    public String toJSONString() {		
        String s = "{\n" + Main.addQuotes(SEED_TOKEN) + ":" + seed +",\n";

        int i = 0;
        for (Node n : nodes) {
            s += "\n" + Main.addQuotes(NODE_TOKEN + i) + ":" + n.toJSONString() + ",";
            i++;
        }
        i = 0;
        for (Edge e : edges) {
            s += "\n" + Main.addQuotes(EDGE_TOKEN + i) + ":" + e.toJSONString() +",";
            i++;
        }

        return s + "\n}";
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////// Random board Generation ////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////

    /** Return a new random board seeded via random seed. */
    public static Board randomBoard() {
        return randomBoard((long)(Math.random() * Long.MAX_VALUE));
    }

    /** Return a new random board for g seeded with {@code seed}. */
    public static Board randomBoard(long seed) {
        return new Board(new Random(seed), seed);
    }

    /** Constructor: a new random board seeded with {@code seed} and
     * using the {@code Random} parameter {@code r} */
    private Board(Random r, long seed) {
        this.seed = seed;

        //Do board generation
        BoardGeneration.gen(this, r);

        //Finish setting things
        scaleComponents();
        updateMinMaxLength();
    }

    /** Set the GUI to gui. */
    public void setGUI(GUI gui) {
        this.gui= gui;
    }

    /** Library for random board generation.
     * Implemented inside board class to allow construction based on these methods.
     * 
     * Node placement and Edge connections are done using the Delaunay Triangulation Method:
     * http://en.wikipedia.org/wiki/Delaunay_triangulation 
     * @author eperdew, MPatashnik
     */
    private static class BoardGeneration{
        private static final int MIN_NODES = 5;  // minimum number of nodes (cities)
        private static final int MAX_NODES = 50;  // maximum number of nodes (cities)

        private static final double AVERAGE_DEGREE = 2.5;
        private static final int MIN_EDGE_LENGTH = 5;
        private static final int MAX_EDGE_LENGTH = 60;

        private static final int WIDTH = 1600;
        private static final int HEIGHT = 1200;

        private static final int BUFFER = (int)(Circle.DEFAULT_DIAMETER * 2.5);

        private static final int ON_COLOR_MULTIPLIER_MIN = 2;
        private static final int ON_COLOR_MULTIPLIER_MAX = 4;

        /** Generate a full set of random elements for b, using r for all random decisions.
         * @param b - a blank board to put stuff on.
         * @param r - a randomer to use for all random decisions. */
        private static void gen(Board b, Random r) {
            final int numCities = r.nextInt(MAX_NODES - MIN_NODES + 1) + MIN_NODES;
            ArrayList<String> cities = cityNames();
            //Create nodes and add to board
            for (int i = 0; i < numCities; i++) {
                String name;
                if (i == 0) {
                    name = Board.FIRST_CITY;
                } else{
                    name = cities.remove(r.nextInt(cities.size()));
                }
                Node n = new Node(b, name, null);
                Circle c = n.getCircle();
                c.setX1(-Circle.DEFAULT_DIAMETER); 
                c.setY1(-Circle.DEFAULT_DIAMETER);
                while (c.getX1() == -Circle.DEFAULT_DIAMETER || 
                        c.getY1() == -Circle.DEFAULT_DIAMETER) {
                    //Try setting to a new location
                    c.setX1(r.nextInt(WIDTH + 1) + BUFFER);
                    c.setY1(r.nextInt(HEIGHT + 1) + BUFFER);
                    //Check other existing nodes. If too close, re-randomize this node's location
                    for (Node n2 : b.getNodes()) {
                        if (n2.getCircle().getDistance(c) < Circle.BUFFER_RADUIS) {
                            c.setX1(-Circle.DEFAULT_DIAMETER);
                            c.setY1(-Circle.DEFAULT_DIAMETER);
                            break;
                        }
                    }
                }
                n.x = n.getCircle().getX1();
                n.y = n.getCircle().getY1();
                
                b.getNodes().add(n);
            }

            spiderwebEdges(b, r);
        }

        /** Create an edge with a random length that connects n1 and n2
         * and add to the correct collections. Return the created edge.
         */
        private static Edge addEdge(Board b, Random r, Node n1, Node n2) {
            int length = r.nextInt(MAX_EDGE_LENGTH - MIN_EDGE_LENGTH + 1) + MIN_EDGE_LENGTH;
            Edge e = new Edge(b, n1, n2, length);
            b.getEdges().add(e);
            n1.addExit(e);
            n2.addExit(e);
            return e;
        }

        /** The maximum number of attempts to get to average node degree */
        private static int MAX_EDGE_ITERATIONS = 1000;

        /** Create a spiderweb of edges by creating concentric hulls,
         * then connecting between the hulls.
         * Create a connected, planar graph. */
        private static void spiderwebEdges(Board b, Random r) {
            HashSet<Node> nodes = new HashSet<Node>();
            nodes.addAll(b.getNodes());
            ArrayList<HashSet<Node>> hulls = new ArrayList<HashSet<Node>>();

            //Create hulls, add edges
            while (! nodes.isEmpty()) {
                HashSet<Node> nds = addGiftWrapEdges(b, r, nodes);
                hulls.add(nds);
                for (Node n : nds) {
                    nodes.remove(n);
                }
            }
            //At this point, there are either 2*n or 2*n-1 edges, depending
            //if the inner most hull had a polygon in it or not.

            //Connect layers w/ random edges - try to connect each node to its
            //closest on the surrounding hull
            //Guarantee that the map is connected after this step
            for (int i = 0; i < hulls.size() - 1; i++) {
                for (Node n : hulls.get(i+1)) {
                    Node c = Collections.min(hulls.get(i), new DistanceComparator(n));
                    if (! lineCrosses(b, n, c)) {
                        addEdge(b, r, n, c);
                    }
                }
            }

            //Create a hashmap of node -> hull the node is in within hulls.
            HashMap<Node, Integer> hullMap = new HashMap<Node, Integer>();
            for (int i = 0; i < hulls.size(); i++) {
                for (Node n : hulls.get(i)) {
                    hullMap.put(n,i);
                }
            }
            final int maxHull = hulls.size() - 1;

            //If the innermost hull has size 1 or 2, add edges to guarantee that every node
            //has degree at least 2
            HashSet<Node> lastHull = hulls.get(hulls.size() - 1);
            if (lastHull.size() < 3) {
                HashSet<Node> penultimateHull = hulls.get(hulls.size() - 2); //Exists. Just cause.
                int e = 1;
                if (lastHull.size() == 1) e = 2;
                for (Node n : lastHull) {
                    if (n.getExitsSize() < 2) {
                        int i = 0;
                        while (i < e) {
                            Node n2 = randomElement(penultimateHull, r);
                            if (! lineCrosses(b, n, n2) && ! n.isConnectedTo(n2)) {
                                addEdge(b, r, n, n2);
                                i++;
                            }
                        }
                    }
                }
            }

            int iterations = 0;

            while (b.getEdges().size() < b.getNodes().size() * AVERAGE_DEGREE &&
                    iterations < MAX_EDGE_ITERATIONS) {
                //Get random node
                Node n = randomElement(b.getNodes(), r);
                int hull = hullMap.get(n);
                //Try to connect to a node on the hull beyond this one.
                if (hull < maxHull) {
                    for (Node c : hulls.get(hull + 1)) {
                        if (! lineCrosses(b, n,c) && ! n.isConnectedTo(c)) {
                            addEdge(b, r,n,c);
                            break;
                        }
                    }
                }
                //Try to connect to a node on the hull outside this one
                if (hull > 0) {
                    for (Node c : hulls.get(hull - 1)) {
                        if (! lineCrosses(b, n,c) && ! n.isConnectedTo(c)) {
                            addEdge(b, r,n,c);
                            break;
                        }
                    }
                }
                iterations++;
            }

            //Fix triangulation such that it's cleaner.
            delunayTriangulate(b, r);
        }

        /** Gift-wrap the nodes - create a concentric set of edges that surrounds
         * set nodes, with random edge lengths.
         * Return a set of nodes that is the nodes involved in the gift-wrapping. */
        private static HashSet<Node> addGiftWrapEdges(Board b, Random r, HashSet<Node> nodes) {
            HashSet<Node> addedNodes = new HashSet<Node>();
            //Base case - 0 or 1 node. Nothing to do.
            if (nodes.size() <= 1) {
                addedNodes.add(nodes.iterator().next());
                return addedNodes;
            }

            //Base case - 2 nodes. Add the one edge connecting them and return.
            if (nodes.size() == 2) {
                Iterator<Node> n = nodes.iterator();
                Node n1 = n.next();
                Node n2 = n.next();
                addEdge(b, r, n1, n2);
                addedNodes.add(n1);
                addedNodes.add(n2);
                return addedNodes;
            }

            //Non base case - do actual gift wrapping alg
            Node first = Collections.min(nodes, xComp);
            Node lastHull = first;
            Node endpoint = null;
            do {
                for (Node n : nodes) {
                    if (endpoint == null || n != lastHull && isLeftOfLine(lastHull, endpoint, n) 
                            && ! lastHull.isConnectedTo(n)) {
                        endpoint = n;
                    }
                }

                addEdge(b, r, lastHull, endpoint);
                addedNodes.add(lastHull);

                lastHull = endpoint;
            } while (lastHull != first);

            return addedNodes;
        }

        /** Return true iff e2 is left of the line start -> e1.
         * Helper for giftwrapping method */
        private static boolean isLeftOfLine(Node start, Node e1, Node e2) {
            Vector a = start.getCircle().getVectorTo(e1.getCircle());
            Vector b = start.getCircle().getVectorTo(e2.getCircle());
            return Vector.cross(a, b) <= 0;
        }

        /** Return true iff the line that would be formed by connecting the
         * two given nodes crosses an existing edge.
         * Helper for gift-wrapping and spider-webbing methods.
         */
        private static boolean lineCrosses(Board b, Node n1, Node n2) {
            Line l = new Line(n1.getCircle(), n2.getCircle(), null);
            for (Edge e : b.getEdges()) {
                if (l.intersects(e.getLine()))
                    return true;
            }
            return false;
        }

        /** Fix (psuedo) triangulation via the delunay method.
         * Alter the current edge set so that triangles are less skinny. */
        private static void delunayTriangulate(Board b, Random r) {

            //Amount of radians that angle sum necessitates switch
            final double FLIP_CONDITION = Math.PI; 

            //Edge that should be removed, mapped to its new exits
            HashMap<Edge, Node[]> needsFlip = new HashMap<Edge, Node[]>(); 

            for (Node n1 : b.getNodes()) {
                for (Edge e2 : n1.getTrueExits()) {
                    Node n2 = e2.getOther(n1);
                    if (n2 != n1) {
                        for (Edge e3 : n1.getTrueExits()) {
                            Node n3 = e3.getOther(n1);
                            if (n3 != n2 && n3 != n1) {
                                for (Edge e4 : n1.getTrueExits()) {
                                    Node n4 = e4.getOther(n1);
                                    if (n4 != n3 && n4 != n2 && n4 != n1) {
                                        //Check all triangulated quads - n1 connected to n2,
                                        // n3, n4; n2 and n3 each connected to n4.
                                        //We already know that n1 is connected to n2, n3, n4.
                                        //Check other part of condition.
                                        if (n2.isConnectedTo(n4) && n3.isConnectedTo(n4)) {
                                            //This is a pair of adjacent triangles. 
                                            //Check angles to see if flip should be made
                                            Edge e24 = n2.getConnect(n4);
                                            Edge e34 = n3.getConnect(n4);
                                            if (e2.getLine().radAngle(e24.getLine())
                                                    + e3.getLine().radAngle(e34.getLine()) > FLIP_CONDITION) {
                                                //Store the dividing edge as needing a flip
                                                Node[] newExits = {n2, n3};
                                                needsFlip.put(e4, newExits);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            for (Entry<Edge, Node[]> e : needsFlip.entrySet()) {
                //Remove old edge
                b.getEdges().remove(e.getKey());

                Node oldFirst = e.getKey().getFirstExit();
                Node oldSecond = e.getKey().getSecondExit();

                oldFirst.removeExit(e.getKey());
                oldSecond.removeExit(e.getKey());

                Node newFirst = e.getValue()[0];
                Node newSecond = e.getValue()[1];

                //Add new edge if it doesn't cross an existing edge
                if (! lineCrosses(b, newFirst, newSecond)) {
                    addEdge(b, r, newFirst, newSecond);
                }
                else { //Otherwise, put old edge back
                    addEdge(b, r, oldFirst, oldSecond);
                }  
            }
        }

        /** Allows for sorting of Collections of Nodes by their gui distance to
         * each of the nodes in collection n.
         * The node that is closest in the collection to the given node is the one that counts.
         * @author MPatashnik
         *
         */
        private static class DistanceComparator implements Comparator<Node> {
            /** The node to which distance is compared */
            protected final Node node;

            @Override
            public int compare(Node n1, Node n2) {
                double d = node.getCircle().getDistance(n1.getCircle()) - 
                        node.getCircle().getDistance(n2.getCircle());
                if (d < 0) return -1;
                if (d > 0) return 1;
                return 0;
            }

            DistanceComparator(Node node) {
                this.node = node;
            }
        }

        /** An instance of the XComparator for sorting nodes.
         * No real need to instantiate another one. */
        private final static XComparator xComp = new XComparator();

        /** Allows for sorting a Collection of Nodes by the x coordinate.
         * No need to instantiate beyond the xcomparator instantiated above. */
        private static class XComparator implements Comparator<Node>{
            @Override
            public int compare(Node n1, Node n2) {
                return n1.getCircle().getX1() - n2.getCircle().getX1();
            }
        }

        /** Return a random element from elms using r.
         * (Return null if elms is empty.) */
        private static <T> T randomElement(Collection<T> elms, Random r) {
            if (elms.isEmpty())
                return null;

            Iterator<T> it = elms.iterator();
            T val = null;
            int rand = r.nextInt(elms.size()) + 1;
            for (int i = 0; i < rand; i++) {
                val = it.next();
            }
            return val;
        }

    }

    /** Scale the (x,y) coordinates of circles to fit the gui */
    private void scaleComponents() {
        int guiHeight = GUI.DRAWING_BOARD_HEIGHT;
        //if(game != null && game.getGUI() != null)
        if (gui != null)
            guiHeight = gui.getDrawingPanel().getHeight();
        int guiWidth = GUI.DRAWING_BOARD_WIDTH;
        //if(game != null && game.getGUI() != null)
        if (gui != null)
            guiHeight = gui.getDrawingPanel().getWidth();

        double heightRatio = (double)(guiHeight)/ 
                (double)(BoardGeneration.HEIGHT + BoardGeneration.BUFFER * 2);
        double widthRatio = (double)(guiWidth)/ 
                (double)(BoardGeneration.WIDTH + BoardGeneration.BUFFER * 2);

        for (Node n : getNodes()) {
            Circle c = n.getCircle();
            c.setX1((int) (c.getX1() * widthRatio));
            c.setY1((int) (c.getY1() * heightRatio));
        }
    }
    
    /** Return the seed from which this game was generated from (-1 if this game
     * was loaded from a non-randomly generated file.) */
    public long getSeed() {
        return seed;
    }

    /** Location of files for board generation */
    public static final String BOARD_GENERATION_DIRECTORY = "data/BoardGeneration";

    /** Return the city names listed in BoardGeneration/cities.txt */
    private static ArrayList<String> cityNames() {
        File f = new File(BOARD_GENERATION_DIRECTORY + "/cities.txt");
        BufferedReader read;
        try {
            read = new BufferedReader(new FileReader(f));
        }
        catch (FileNotFoundException e) {
            System.out.println("cities.txt not found. Aborting as empty list of city names...");
            return new ArrayList<String>();
        }
        ArrayList<String> result = new ArrayList<String>();
        try {
            String line;
            while((line = read.readLine()) != null) {
                //Strip non-ascii or null characters out of string
                line = line.replaceAll("[\uFEFF-\uFFFF \u0000]", "");
                result.add(line);
            }
            read.close();
        }
        catch (IOException e) {
            System.out.println("Error in file reading. Aborting as empty list of city names...");
            return new ArrayList<String>();
        }
        return result;
    }
}