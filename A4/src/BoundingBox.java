import java.util.Iterator;

/**
 * An instance is a 2D bounding box.
 */
public class BoundingBox {
    /**
     * The corner of the bounding box with the smaller x,y coordinates.
     */
    public Vector2D lower; // (minX, minY)

    /**
     * The corner of the bounding box with the larger x,y coordinates.
     */
    public Vector2D upper; // (maxX, maxY)

    /**
     * Constructor: an instance is a copy of bounding box b.
     */
    public BoundingBox(BoundingBox b) {
        lower = new Vector2D(b.lower);
        upper = new Vector2D(b.upper);
    }

    /**
     * Constructor: An instance with lower as smaller coordinates and
     * upper as larger coordinates.
     */
    public BoundingBox(Vector2D lower, Vector2D upper) {
        if (upper.x < lower.x)
            throw new IllegalArgumentException("invalid bbox");
        if (upper.y < lower.y)
            throw new IllegalArgumentException("invalid bbox");

        this.lower = lower;
        this.upper = upper;
    }

    /**
     * Return the width of this bounding box (along x-dimension).
     */
    public double getWidth() {
        return upper.x - lower.x;
    }

    /**
     * Return the height of this bounding box (along y-dimension).
     */
    public double getHeight() {
        return upper.y - lower.y;
    }

    /**
     * Return the larger of the width and height of this bounding box.
     */
    public double getLength() {
        double w = getWidth();
        double h = getHeight();

        if (w > h)
            return w;
        else
            return h;
    }

    /**
     * Return the center of this bounding box.
     */
    public Vector2D getCenter() {
        return new Vector2D(lower.x + (getWidth() / 2), lower.y + (getHeight() / 2));
    }

    /**
     * Return the result of displacing this bounding box by d.
     */
    public BoundingBox displaced(Vector2D d) {
        Vector2D newL = new Vector2D(lower.x + d.x, lower.y + d.y);
        Vector2D newH = new Vector2D(upper.x + d.x, upper.y + d.y);
        return new BoundingBox(newL, newH);
    }

    /**
     * Return true iff this bounding box contains p.
     */
    public boolean contains(Vector2D p) {
        boolean inX = lower.x <= p.x && p.x <= upper.x;
        boolean inY = lower.y <= p.y && p.y <= upper.y;
        return inX && inY;
    }

    /**
     * Return the area of this bounding box.
     */
    public double getArea() {
        // TODO: Implement me.
        return getWidth() * getHeight();
    }

    /**
     * Return true iff this bounding box overlaps with box.
     * Note: added helper functions to clean up the code.
     */
    public boolean overlaps(BoundingBox box) {
        return (xOverlap(box) && yOverlap(box));
    }

    /**
     * Returns true if the x-coordinates of the two boxes intersect.
     */
    public boolean xOverlap(BoundingBox box) {
        if ((this.lower.x < box.lower.x) && (this.upper.x <= box.lower.x))
            return false;
        else if (box.upper.x <= this.lower.x)
            return false;
        return true;
    }

    /**
     * Returns true if the y-coordinates of the two boxes intersect.
     */
    public boolean yOverlap(BoundingBox box) {
        if ((this.lower.y < box.lower.y) && (this.upper.y <= box.lower.y))
            return false;
        else if (box.upper.y <= this.lower.y)
            return false;
        return true;
    }

    /**
     * Return the bounding box of blocks given by iter.
     */
    public static BoundingBox findBBox(Iterator<Block> iter) {
        // Do not modify the following "if" statement.
        if (!iter.hasNext())
            throw new IllegalArgumentException("empty iterator");

        double upperX = Double.MIN_VALUE;
        double lowerX = Double.MAX_VALUE;
        double upperY = Double.MIN_VALUE;
        double lowerY = Double.MAX_VALUE;

        while (iter.hasNext()) {
            BoundingBox tester = iter.next().getBBox();
            if (tester.upper.x > upperX)
                upperX = tester.upper.x;
            if (tester.upper.y > upperY)
                upperY = tester.upper.y;
            if (tester.lower.x < lowerX)
                lowerX = tester.lower.x;
            if (tester.lower.y < lowerY)
                lowerY = tester.lower.y;
            iter.remove();
        }

        Vector2D lower = new Vector2D(lowerX, lowerY);
        Vector2D upper = new Vector2D(upperX, upperY);

        return new BoundingBox(lower, upper);
    }

    /**
     * Return a representation of this bounding box.
     */
    public String toString() {
        return lower + " -- " + upper;
    }
}
