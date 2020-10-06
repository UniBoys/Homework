package assignments.artist;

import java.awt.Graphics;
import java.awt.Color;

class TriangleDingus extends Dingus {

    private float[] corner1;
    private float[] corner2;
    private float[] corner3;

    public TriangleDingus(int x, int y, float rotation, float radius) {
        super(1, 1);
        color = new Color((int) (255f * random.nextFloat()),(int) (255f * random.nextFloat()),(int) (255f * random.nextFloat()));
        float[] center = new float[] { x, y };

        this.corner1 = calcCorner(center, radius, rotation, 0f);
        this.corner2 = calcCorner(center, radius, rotation, 1f);
        this.corner3 = calcCorner(center, radius, rotation, 2f);
    }

    public TriangleDingus(float area, float[] corner0, float[] corner1, float[] corner2) {
        super(1, 1);
        color = new Color((int) (255f * random.nextFloat()),(int) (255f * random.nextFloat()),(int) (255f * random.nextFloat()));
        this.corner1 = corner1;
        this.corner2 = corner2;

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

    float length(float[] a, float[] b) {
        return (float) Math.sqrt(Math.pow(a[0] - b[0], 2) + Math.pow(a[1] - b[1], 2));
    }

    @Override
    void draw(Graphics g) {
        g.setColor(color);
        g.fillPolygon(getCord(0), getCord(1), 3);
    }

    int[] getCord(int index) {
        return new int[] { round(corner1[index]), round(corner2[index]), round(corner3[index]) };
    }

    int round(float number) {
        return Math.round(number);
    }

    float[][][] getNextStartingCords() {
        return new float[][][] { { corner1, corner2, corner3 }, { corner2, corner1, corner3 } };
    }

    float[] calcCorner(float[] center, float radius, float rotation, float number) {
        float angle = (float) number * (2f / 3f) * (float) Math.PI + rotation;

        return new float[] { (float) (center[0] + Math.cos(angle) * radius),
                (float) (center[1] + Math.sin(angle) * radius) };
    }

    float[] getCorner(int n) {
        switch(n) {
            case 0:
                return corner1;
            case 1:
                return corner2;
            case 2:
                return corner3;
        }

        return null;
    }

    boolean isInside(float[] point) {
        double dx = point[0] - corner3[0];
        double dy = point[1] - corner3[1];
        double det = (corner2[1] - corner3[1]) * (corner1[0] - corner3[0]) - (corner3[0] - corner2[0]) * (corner3[1] - corner1[1]);
        double a = (corner2[1] - corner3[1]) * dx + (corner3[0] - corner2[0]) * dy;

        if (a < Math.min(det, 0) || a > Math.max(det, 0)) {
            return false;
        }

        double b = (corner3[1] - corner1[1]) * dx + (corner1[0] - corner3[0]) * dy;

        if (b < Math.min(det, 0) || b > Math.max(det, 0))
            return false;

        double c = det - a - b;

        if (c < Math.min(det, 0) || c > Math.max(det, 0))
            return false;

        return true;
    }
}