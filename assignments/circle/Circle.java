import java.util.Scanner;
/** Circle by Jort van Driel 1579584 and Thijs Aarnoudse 1551159 as group 52 */
public class Circle {

    // Creates the scanner from the System input stream
    private Scanner scanner = new Scanner(System.in);

    public void run() {
        try {
            Ring ring1 = readRing();
            Ring ring2 = readRing();
            Vector point = readVector();

            boolean pointIsInsideRing1 = ring1.encompassesPoint(point);
            boolean pointIsInsideRing2 = ring2.encompassesPoint(point);
            
            if(pointIsInsideRing1 && pointIsInsideRing2) {
                System.out.println("The point lies in both circles");
            } else if(pointIsInsideRing1) {
                System.out.println("The point lies in the first circle");
            } else if(pointIsInsideRing2) {
                System.out.println("he point lies in the second circle");
            } else {
                System.out.println("The point does not lie in either circle");
            }
        } catch (Exception e) {
            System.out.println("input error");
        } 
    }

    private Ring readRing() throws Exception {
        Vector midpoint = readVector();
        double radius = scanner.nextDouble();

        if(radius < 0) {
            throw new Exception();
        }

        return new Ring(midpoint, radius);
    }

    private Vector readVector() {
        double x = scanner.nextDouble();
        double y = scanner.nextDouble();

        return new Vector(x, y);
    }

    public static void main(String[] args) {
        (new Circle()).run();
    }

    class Vector {
        final double x, y;

        public Vector(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    class Ring {
        final Vector midpoint;
        final double radius;

        public Ring(Vector midpoint, double radius) {
            this.midpoint = midpoint;
            this.radius = radius;
        }

        public boolean encompassesPoint(Vector vector) {
            return Math.pow(vector.x - midpoint.x, 2) + Math.pow(vector.y - midpoint.y, 2) < Math.pow(radius, 2);
        }
    }
}
