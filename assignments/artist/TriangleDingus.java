import java.awt.Graphics;
import java.awt.Color;

/**
 * Our triangle dingus
 * 
 * by Jort van Driel 1579584
 * and Thijs Aarnoudse 1551159
 * as group 52
 */
class TriangleDingus extends Dingus {

    // The maximum random amount of hue increase er depth.
    private final float HUE_INCREASE = 5;
    // The coordinates of the first corner.
    private float[] corner1;
    // The coordinates of the second corner.
    private float[] corner2;
    // The coordinates of the third corner.
    private float[] corner3;
    // The hue of this triangle.
    private float hue;
    // If this triangle has depth 0.
    private boolean startingTriangle = false;

    // This constructor initializes the starting triangle by calculating the corner coordinates around the provided 
    // center point.
    public TriangleDingus(float hue, int x, int y, float rotation, float radius) {
        super(1, 1);
        
        // The center of the starting triangle.
        float[] center = new float[] { x, y };

        this.corner1 = calcCorner(center, radius, rotation, 0f);
        this.corner2 = calcCorner(center, radius, rotation, 1f);
        this.corner3 = calcCorner(center, radius, rotation, 2f);
        this.hue = hue + random.nextFloat() * HUE_INCREASE + 3;
        this.startingTriangle = true;
    }

    // This constructor creates the default triangle based on it's parent triangle.
    public TriangleDingus(float hue, float area, float[] corner0, float[] corner1, float[] corner2) {
        super(1, 1);
        
        this.corner1 = corner1;
        this.corner2 = corner2;
        this.hue = hue + random.nextFloat() * HUE_INCREASE;

        // The length of the vector from corner1 to corner2 of the original triangle.
        float length = length(corner1, corner2);
        // The percentage of the vector parallel to the vector of corner1 to corner2,
        // which indicates where corner3 will lie.
        float betweenPercentage = random.nextFloat() * .6f + .3f;
        // The height of our new vector.
        float height = 2f * area / length;
        // The x coordinate on the vector from corner1 to corner2 parallel to which our
        // new corner will lie.
        float betweenX = betweenPercentage * (corner2[0] - corner1[0]) + corner1[0];
        // The y coordinate on the vector from corner1 to corner2 parallel to which our
        // new corner will lie.
        float betweenY = betweenPercentage * (corner2[1] - corner1[1]) + corner1[1];
        // The height x coordinate that will be added to the between x to form our
        // corner x coordinate.
        float heightX = (corner2[1] - corner1[1]) / length * height;
        // The height y coordinate that will be added to the between y to form our
        // corner y coordinate.
        float heightY = -(corner2[0] - corner1[0]) / length * height;
        // If our coordinate should lie at the postive or negative side of the vector
        // from corner1 to corner2, determined by which point is furthest away from the
        // remaining corner of the original triangle.
        boolean positive = length(corner0, new float[] { betweenX + heightX, betweenY + heightY }) > length(corner0,
                new float[] { betweenX - heightX, betweenY - heightY });
        // The coordinates for corner3, created by adding or subtracting the between
        // coordinates with the height coordinates.
        corner3 = new float[] { positive ? betweenX + heightX : betweenX - heightX,
                positive ? betweenY + heightY : betweenY - heightY };
    }

    /**
     * Draws the triangle after setting a color based on the hue and a random saturation and brightness.
     */
    @Override
    void draw(Graphics g) {
        g.setColor(Color.getHSBColor((this.hue % 360f) / 360f, random.nextFloat() * .05f + .875f, 
            random.nextFloat() * .05f + .95f));
        g.fillPolygon(getCord(0), getCord(1), 3);
    }

    /**
     * Returns the length of a vector from point a to point b.
     */
    float length(float[] a, float[] b) {
        return (float) Math.sqrt(Math.pow(a[0] - b[0], 2) + Math.pow(a[1] - b[1], 2));
    }

    /**
     * Returns all x coordinates or y coordinates of this triangle for the values 0 and 1 respectively.
     */
    int[] getCord(int index) {
        return new int[] { Math.round(corner1[index]), Math.round(corner2[index]), Math.round(corner3[index]) };
    }

    /**
     * Returns the corner coordinates that will be used to create the children triangle. As only the starting triangle
     * can have three children only two arrays of coordinates are send for the default triangles.
     */
    float[][][] getNextStartingCords() {
        if (this.startingTriangle) {
            return new float[][][] { {corner3, corner2, corner1}, { corner1, corner2, corner3 }, 
                { corner2, corner1, corner3 } };
        }

        return new float[][][] { { corner1, corner2, corner3 }, { corner2, corner1, corner3 } };
    }

    /**
     * Returns the calculated corner for a given center coordinate, radius from that center point, starting rotation 
     * and the number of this corner.
     */
    float[] calcCorner(float[] center, float radius, float rotation, float number) {
        float angle = (float) number * (2f / 3f) * (float) Math.PI + rotation;

        return new float[] { (float) (center[0] + Math.cos(angle) * radius),
                (float) (center[1] + Math.sin(angle) * radius) };
    }

    /**
     * Returns the specified corner coordinates.
     */
    float[] getCorner(int n) {
        switch (n) {
            case 0:
                return corner1;
            case 1:
                return corner2;
            case 2:
                return corner3;
        }

        return null;
    }

    /**
     * Returns the hue of this triangle.
     */
    float getHue() {
        return hue;
    }

    /**
     * Returns if a given point is inside this triangle.
     */
    boolean isInside(float[] point) {
        // The delta x from the point to corner3.
        double dx = point[0] - corner3[0];
        // The delta y from the point to corner3.
        double dy = point[1] - corner3[1];
        // The delta t for this triangle.
        double det = (corner2[1] - corner3[1]) * (corner1[0] - corner3[0]) - (corner3[0] - corner2[0]) * (corner3[1] - corner1[1]);
        // Point a for this intersection check.
        double a = (corner2[1] - corner3[1]) * dx + (corner3[0] - corner2[0]) * dy;

        if (a < Math.min(det, 0) || a > Math.max(det, 0)) {
            return false;
        }

        // Point b for this intersection check.
        double b = (corner3[1] - corner1[1]) * dx + (corner1[0] - corner3[0]) * dy;

        if (b < Math.min(det, 0) || b > Math.max(det, 0))
            return false;

        // Point c for this intersection check.
        double c = det - a - b;

        if (c < Math.min(det, 0) || c > Math.max(det, 0))
            return false;

        return true;
    }
}