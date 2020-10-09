/*
 * part of HA Random artist
 *
 * @author huub
 * @author kees
 * 
 * Completed by Jort van Driel 1579584
 * and Thijs Aarnoudse 1551159
 * as group 52
 */

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;

public class Painting extends JPanel implements ActionListener {

    /*---- Randomness ----*/
    /**
     * you can change the variable SEED if you like the same program with the same
     * SEED will generate exactly the same sequence of pictures
     */
    final static long SEED = 37; // seed for the random number generator; any number works

    /** do not change the following two lines **/
    final static Random random = new Random(SEED); // random generator to be used by all classes
    int numberOfRegenerates = 0;

    // The allowed amount of steps for this painting.
    int TOTAL_STEPS = 50;
    // The steepness with which the area should decrease per step.
    float STEEPNESS = (float)TOTAL_STEPS / 4f;
    // The area of the starting triangle.
    float START_AREA;

    /*---- Screenshots ----
    * do not change
    */
    char current = '0';
    String filename = "randomshot_"; // prefix

    /*---- Dinguses ----*/
    ArrayList<ArrayList<TriangleDingus>> shapes;

    public Painting() {
        setPreferredSize(new Dimension(1600, 800)); // make panel 800 by 450 pixels.
        // ...
    }

    @Override
    protected void paintComponent(Graphics g) { // draw all your shapes
        super.paintComponent(g); // clears the panel
        Collections.reverse(shapes);
        shapes.forEach(a -> a.forEach(s -> s.draw(g)));
    }

    /**
     * reaction to button press
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if ("Regenerate".equals(e.getActionCommand())) {
            regenerate();
            repaint();
        } else { // screenshot
            saveScreenshot(this, filename + current++); // ++ to show of compact code :-)
        }
    }

    void regenerate() {
        numberOfRegenerates++; // do not change

        shapes = new ArrayList<>();

        generateTriangles();
    }

    /**
     * Generates the triangles for this painting.
     */
    void generateTriangles() {
        // The radius of the starting triangle.
        float radius = random.nextFloat() * 40 + 80;
        // The rotation of the starting triangle.
        float rotation = random.nextFloat() * (2f / 3f) * (float) Math.PI;
        // The hue of the starting triangle.
        float hue = random.nextFloat() * 360;
        // The x coordinate of the starting triangle.
        float x = getSize().width / 2f - random.nextFloat() * 750 + 375;
        // The y coordinate of the starting triangle.
        float y = getSize().height / 2f - random.nextFloat() * 300 + 150;

        // The starting/centrer triangle.
        TriangleDingus centerTriangle = new TriangleDingus(hue, (int) x, (int) y, rotation, radius);
        shapes.add(new ArrayList<>(Arrays.asList(centerTriangle)));

        START_AREA = 0.75f * (float) Math.sqrt(3d) * (float) Math.pow(radius, 2);

        for (int depth = 1; depth < TOTAL_STEPS; depth++) {
            for (TriangleDingus parent : shapes.get(depth - 1)) {
                // The children of the parent triangle.
                float[][][] children = parent.getNextStartingCords();

                generateChildren(children, parent, depth);
            }
        }
    }

    /**
     * Generates the children triangles for the given parent and depth.
     */
    void generateChildren(float[][][] children, TriangleDingus parent, int depth) {
        for (float[][] child : children) {
            generateChild(child, parent, depth);
        }
    }

    /**
     * Generates a child triangle for the given parent and depth.
     */
    void generateChild(float[][] child, TriangleDingus parent, int depth) {
        // The amount that this area should decrease.
        float decrease = (1f / ((STEEPNESS / TOTAL_STEPS) * (float) Math.pow((double)(depth + 1f), 2d) + 1f));
        // A random number between 0.98 and 1.02 that will add some randomness to the size decreasing.
        float randomness = random.nextFloat() * 0.04f + 0.98f;
        // The area of this child triangle.
        float area = START_AREA * decrease * randomness;

        if (area < 0) {
            return;
        }

        // The child triangle.
        TriangleDingus dingus = new TriangleDingus(parent.getHue(), area, child[0], child[1], child[2]);

        if (isInsideSomeTriangle(dingus)) {
            return;
        }

        if (shapes.size() == depth) {
            shapes.add(new ArrayList<>());
        }

        shapes.get(depth).add(dingus);
    }

    /**
     * Returns true if the given triangle is inside some triangle that has already been generated.
     * Currently this check isn't fully accurate, but a fully accurate check would be inefficient.
     */
    boolean isInsideSomeTriangle(TriangleDingus dingus) {
        return shapes.parallelStream()
                .flatMap(l -> l.parallelStream())
                .anyMatch(triangle -> triangle.isInside(dingus.getCorner(2)));
    }

    /**
     * saves a screenshot of a Component on disk overides existing files
     *
     * @param component - Component to be saved
     * @param name      - filename of the screenshot, followed by a sequence number
     * 
     *                  do not change
     */
    void saveScreenshot(Component component, String name) {
        String randomInfo = "" + SEED + "+" + (numberOfRegenerates - 1); // minus 1 because the initial picture should
                                                                         // not count
        System.out.println(SwingUtilities.isEventDispatchThread());
        BufferedImage image = new BufferedImage(component.getWidth(), component.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        // call the Component's paint method, using
        // the Graphics object of the image.
        Graphics graphics = image.getGraphics();
        component.paint(graphics); // alternately use .printAll(..)
        graphics.drawString(randomInfo, 0, component.getHeight());

        try {
            ImageIO.write(image, "PNG", new File(name + ".png"));
            System.out.println("Saved screenshot as " + name);
        } catch (IOException e) {
            System.out.println("Saving screenshot failed: " + e);
        }
    }

}