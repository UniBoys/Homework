package assignments.artist;

import java.awt.Graphics;
import java.awt.Color;

class TriangleDingus extends Dingus {

    private float[] corner1;
    private float[] corner2;
    private float[] corner3;

    public TriangleDingus(int area, float[] corner1, float[] corner2, boolean positive) {
        super(0, 0);

        this.corner2 = corner2;
        this.corner3 = corner3;

        float length = length(corner1, corner2);

        float betweenPercentage = random.nextFloat();
        float height = 2f * area / length;
        float betweenX = betweenPercentage * (corner1[0] - corner2[0]) + corner1[0];
        float betweenY = betweenPercentage * (corner1[1] - corner2[1]) + corner1[1];
        float heightX = y / length * height;
        float heightY = -x / length * height;
        corner3 = new float[] {
            positive ? betweenX + heightX : betweenX - heightX, 
            positive ? betweenY + heightY : betweenY - heightY
        };
    }

    float length(float[] a, float[] b) {
        return (float) Math.sqrt(Math.pow(a[0]-b[0], 2) + Math.pow(a[1] - b[1], 2));
    }

    @Override
    void draw(Graphics g) {

    }
}