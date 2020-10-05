package assignments.artist;

/*
 * part of HA Random artist
 * TO BE COMPLETED
 *
 * @author huub
 * @author kees
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

    /*---- Screenshots ----
    * do not change
    */
    char current = '0';
    String filename = "randomshot_"; // prefix

    /*---- Dinguses ----*/
    ArrayList<ArrayList<TriangleDingus>> shapes;

    public Painting() {
        setPreferredSize(new Dimension(800, 450)); // make panel 800 by 450 pixels.
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

    int TOTAL_STEPS = 10;
    float STEEPNESS = 2f;
    float START_AREA;

    void regenerate() {
        numberOfRegenerates++; // do not change

        shapes = new ArrayList<>();

        generatev2();
    }

    boolean isInsideSomeTriangle(TriangleDingus dingus) {
        for(ArrayList<TriangleDingus> list : shapes) {
            for(TriangleDingus triangle : list) {
                if(triangle.isInside(dingus.getCorner(2))) {
                    return true;
                }
            }
        }

        return false;
    }

    void generatev2() {
        float radius = random.nextFloat() * 20 + 40;
        float rotation = random.nextFloat() * (2f / 3f) * (float) Math.PI;

        TriangleDingus centerTriangle = new TriangleDingus(getSize().width / 2, getSize().height / 2, rotation, radius);
        shapes.add(new ArrayList<>(Arrays.asList(centerTriangle)));

        START_AREA = 0.75f * (float) Math.sqrt(3d) * (float) Math.pow(radius, 2);

        for(int i = 1; i < TOTAL_STEPS; i++) {
            for(TriangleDingus parent : shapes.get(i - 1)) {
                float[][][] children = parent.getNextStartingCords();

                for(float[][] child : children) {
                    float decrease = (1f / ((STEEPNESS / TOTAL_STEPS) * (float) Math.pow((double)(i + 1f), 2d) + 1f));
                    float randomness = random.nextFloat() * 0.04f + 0.98f;
                    float area = START_AREA * decrease * randomness;
            
                    if (area < 0) {
                        System.out.println("to small");
                        continue;
                    }

                    TriangleDingus dingus = new TriangleDingus(area, child[0], child[1], child[2]);

                    if(isInsideSomeTriangle(dingus)) {
                        continue;
                    }

                    if(shapes.size() == i) {
                        shapes.add(new ArrayList<>());
                    }

                    shapes.get(i).add(dingus);
                }
            }
        }
    }

    void generatev1() {
        float radius = random.nextFloat() * 20 + 40;
        float rotation = random.nextFloat() * (2f / 3f) * (float) Math.PI;

        TriangleDingus centerTriangle = new TriangleDingus(getSize().width / 2, getSize().height / 2, rotation, radius);
        shapes.add(new ArrayList<>(Arrays.asList(centerTriangle)));

        START_AREA = 0.75f * (float) Math.sqrt(3d) * (float) Math.pow(radius, 2);

        float[][][] children = centerTriangle.getNextStartingCords();

        for (float[][] child : children) {
            generateShapesv1(child[0], child[1], child[2], 0);
        }
    }

    void generateShapesv1(float[] corner0, float[] corner1, float[] corner2, int step) {
        float decrease = (1f / ((STEEPNESS / TOTAL_STEPS) * (float) Math.pow((double)(step + 1f), 2d) + 1f));
        float randomness = random.nextFloat() * 0.1f + 0.95f;
        float area = START_AREA * decrease * randomness;

        if (area < 0 || step >= TOTAL_STEPS) {
            if(area < 0) System.out.println("to small");
            return;
        }

        TriangleDingus dingus = new TriangleDingus(area, corner0, corner1, corner2);

        for(ArrayList<TriangleDingus> list : shapes) {
            for(TriangleDingus triangle : list) {
                if(triangle.isInside(dingus.getCorner(2))) {
                    return;
                }
            }
        }

        if(shapes.size() == step) {
            shapes.add(new ArrayList<>());
        }

        shapes.get(step).add(dingus);

        float[][][] children = dingus.getNextStartingCords();

        for (float[][] child : children) {
            generateShapesv1(child[0], child[1], child[2], ++step);
        }
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