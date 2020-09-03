import java.util.Scanner;
/** Circle by Jort van Driel 1579584 and Thijs Aarnoudse 1551159 as group 52 */
public class Circle {

    // Creates the scanner from the System input stream
    private Scanner scanner = new Scanner(System.in);

    /**
     * Runs the program, by fetching the required data, checking if the point is in the circles and then it reports 
     * which circles encompass the point.
     */
    public void run() {
        try {
            // This is the first ring.
            Ring ring1 = readRing();
            // This is the second ring.
            Ring ring2 = readRing();
            // This is the vector for the point.
            Vector point = readVector();

            // This variable is true if the point is inside the first ring.
            boolean pointIsInsideRing1 = ring1.encompassesPoint(point);
            // This variable is true if the point is inside the second ring.
            boolean pointIsInsideRing2 = ring2.encompassesPoint(point);
            
            if (pointIsInsideRing1 && pointIsInsideRing2) {
                System.out.println("The point lies in both circles");
            } else if (pointIsInsideRing1) {
                System.out.println("The point lies in the first circle");
            } else if (pointIsInsideRing2) {
                System.out.println("he point lies in the second circle");
            } else {
                System.out.println("The point does not lie in either circle");
            }
        } catch (Exception e) {
            System.out.println("input error");
        } 
    }

    private Ring readRing() throws Exception {
        // This is the midpoint of the ring and is read as a vector.
        Vector midpoint = readVector();
        // This is the radius of the ring and read as the next double from the scanner.
        double radius = scanner.nextDouble();

        if (radius < 0) {
            throw new Exception();
        }

        return new Ring(midpoint, radius);
    }

    private Vector readVector() {
        // This is the x-coordinate, read from the scanner.
        double x = scanner.nextDouble();
        // This is the y-coordinate, read from the scanner.
        double y = scanner.nextDouble();

        return new Vector(x, y);
    }

    public static void main(String[] args) {
        (new Circle()).run();
    }

    class Vector {
        // These are the x and y coordinates.
        final double x, y;

        public Vector(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    class Ring {
        // This is the midpoint of the ring.
        final Vector midpoint;
        // This is the radius of the ring.
        final double radius;

        public Ring(Vector midpoint, double radius) {
            this.midpoint = midpoint;
            this.radius = radius;
        }

        /**
         * This function returns true if the given vector lies within this circle.
         */
        public boolean encompassesPoint(Vector vector) {
            return Math.pow(vector.x - midpoint.x, 2) + Math.pow(vector.y - midpoint.y, 2) <= Math.pow(radius, 2);
        }
    }
}
