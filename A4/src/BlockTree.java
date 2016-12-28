import java.util.ArrayList;

/**
 * An instance is a non-empty collection of points organized in a hierarchical
 * binary tree structure.
 */
public class BlockTree {
    private BoundingBox box; // bounding box of the blocks contained in this tree.

    private int numBlocks; // Number of blocks in this tree.

    private BlockTree left; // left subtree --null iff this is a leaf

    private BlockTree right; // right subtree --null iff this is a leaf

    private Block block; //The block of a leaf node --null if not a leaf

    // REMARK:
    // Leaf node: left, right == null && block != null
    // Intermediate node: left, right != null && block == null

    /**
     * Constructor: a binary tree containing blocks.
     * Precondition: blocks is nonempty,
     * i.e. it contain at least one block.
     */
    public BlockTree(ArrayList<Block> blocks) {
        // Leave the following two "if" statements as they are.
        if (blocks == null)
            throw new IllegalArgumentException("blocks null");
        if (blocks.size() == 0)
            throw new IllegalArgumentException("no blocks");

        box = BoundingBox.findBBox(blocks.iterator());

        if (blocks.size() == 1) {
            numBlocks = 1;
            block = blocks.get(0);
            left = null;
            right = null;
            box = block.getBBox();
        }

        numBlocks = blocks.size();
        block = null;

        ArrayList<Block> leftSide = new ArrayList<Block>();
        ArrayList<Block> rightSide = new ArrayList<Block>();

        if (box.getWidth() >= box.getHeight()) {
            for (Block b : blocks) {
                if (box.getCenter().x > b.position.x)
                    leftSide.add(b);
                else
                    rightSide.add(b);
            }
        } else {
            for (Block b : blocks) {
                if (box.getCenter().y > b.position.y)
                    leftSide.add(b);
                else
                    rightSide.add(b);
            }
        }
        if (!leftSide.isEmpty())
            left = new BlockTree(leftSide);
        if (!rightSide.isEmpty())
            right = new BlockTree(rightSide);
    }

    /**
     * Return the bounding box of the collection of blocks.
     */
    public BoundingBox getBox() {
        return box;
    }

    /**
     * Return true iff this is a leaf node.
     */
    public boolean isLeaf() {
        return block != null;
    }

    /**
     * Return true iff this is an intermediate node.
     */
    public boolean isIntermediate() {
        return !isLeaf();
    }

    /**
     * Return the number of blocks contained in this tree.
     */
    public int getNumBlocks() {
        return numBlocks;
    }

    /**
     * Return true iff this collection of blocks contains point p.
     */
    public boolean contains(Vector2D p) {
        // Caution. By "contains" we do NOT mean that the bounding box
        // of this block tree contains p. That is not enough. We mean
        // that one of the blocks in this BlockTree contains p.

        if (p.x < box.lower.x || p.x > box.upper.x || p.y < box.lower.y || p.y > box.upper.y)
            return false;

        if (box.getWidth() >= box.getHeight()) {
            if (p.x < right.getBox().lower.x)
                return contains(left, p);
            return contains(right, p);
        } else {
            if (p.y < right.getBox().lower.y)
                return contains(left, p);
            return contains(right, p);
        }
    }

    /* Helper function for contains(Vector2D p) that aids in the recursive call, allowing a tree to be passed
     * in as a parameter, due to the need for a component to be reduced in recursion*/
    public boolean contains(BlockTree subTree, Vector2D p) {

        if (subTree == null)
            return false;
        if (p.x < box.lower.x || p.x > box.upper.x || p.y < box.lower.y || p.y > box.upper.y)
            return false;
        if (subTree.getNumBlocks() == 1)
            return true;

        BoundingBox newBox = subTree.getBox();
        if (newBox.getWidth() >= newBox.getHeight()) {
            if (p.x < subTree.right.getBox().lower.x)
                return contains(subTree.left, p);
            return contains(subTree.right, p);
        } else {
            if (p.y < subTree.right.getBox().lower.y)
                return contains(subTree.left, p);
            return contains(subTree.right, p);
        }
    }

    /**
     * Return true iff (this tree displaced by thisD) and (tree t
     * displaced by d) overlap.
     */
    public boolean overlaps(Vector2D thisD, BlockTree t, Vector2D d) {
        BoundingBox thisTemp = box.displaced(thisD);
        BoundingBox targetTemp = t.box.displaced(d);
        if (!thisTemp.overlaps(targetTemp))
            return false;

        boolean ol = false;

        if (this.isLeaf()) {
            if (t.isLeaf())
                return Block.overlaps(this.block, thisD, t.block, d);
            else {
                ol = t.left.overlaps(d, this, thisD);
                if (!ol)
                    ol = t.right.overlaps(d, this, thisD);
            }
        } else if (!isLeaf() && !t.isLeaf()) {
            thisTemp = left.box.displaced(thisD);
            targetTemp = t.left.box.displaced(d);
            if (thisTemp.overlaps(targetTemp))
                ol = ol || left.overlaps(thisD, t.left, d);
            if (ol)
                return ol;

            targetTemp = t.right.box.displaced(d);
            if (thisTemp.overlaps(targetTemp))
                ol = ol || left.overlaps(thisD, t.right, d);
            if (ol)
                return ol;

            thisTemp = right.box.displaced(thisD);
            targetTemp = t.left.box.displaced(d);
            if (thisTemp.overlaps(targetTemp))
                ol = ol || right.overlaps(thisD, t.left, d);
            if (ol)
                return ol;

            targetTemp = t.right.box.displaced(d);
            if (thisTemp.overlaps(targetTemp))
                ol = ol || right.overlaps(thisD, t.right, d);
        }
        return ol;
    }

    /**
     * Return a representation of this instance.
     */
    public String toString() {
        return toString(new Vector2D(0, 0));
    }

    /**
     * Return a representation of this tree displaced by d.
     */
    public String toString(Vector2D d) {
        return toStringAux(d, "");
    }

    /**
     * Useful for creating appropriate indentation for function toString.
     */
    private static final String indentation = "   ";

    /**
     * Return a representation of this instance displaced by d, with
     * indentation indent.
     *
     * @param d      Displacement vector.
     * @param indent Indentation.
     * @return String representation of this tree (displaced by d).
     */
    private String toStringAux(Vector2D d, String indent) {
        String str = indent + "Box: ";
        str += "(" + (box.lower.x + d.x) + "," + (box.lower.y + d.y) + ")";
        str += " -- ";
        str += "(" + (box.upper.x + d.x) + "," + (box.upper.y + d.y) + ")";
        str += "\n";

        if (isLeaf()) {
            String vStr = "(" + (block.position.x + d.x) + "," + (block.position.y + d.y)
                    + ")" + block.halfwidth;
            str += indent + "Leaf: " + vStr + "\n";
        } else {
            String newIndent = indent + indentation;
            str += left.toStringAux(d, newIndent);
            str += right.toStringAux(d, newIndent);
        }

        return str;
    }
}
