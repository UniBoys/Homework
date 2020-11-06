import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

// Cellulitis TEMPLATE
// Homework Assignment 3 2ip90 

/**
 * @name(s) Jort van Driel, Thijs Aarnoudse
 * @id(s) 1579584, 1551159
 * @group 52
 * @date 18-09-2020
 */
class Cellulitis {
    // The standerd input scanner.

    Scanner scanner = new Scanner(System.in);
    // This will be the config for this program.
    Configuration config;

    /**
     * Runs the cellular automaton program.
     */
    void run() {
        try {
            this.config = new Configuration();
            this.config.readConfig(this.scanner);

            // The cell values for each current generation.
            boolean[] currentGeneration = this.createFirstGeneration();
            this.draw(currentGeneration);

            for (int i = 0; i < this.config.numberOfGenerations - 1; i++) {
                currentGeneration = this.evolution(currentGeneration);
                this.draw(currentGeneration);
            }
        } catch (Exception e) {
            System.out.println("input error");
        }
    }

    /**
     * Return the first generation of cell values by using the the preset cell values in the config. 
     */
    boolean[] createFirstGeneration() {
        // The cell values for the first generation.
        boolean[] firstGeneration = new boolean[this.config.numberOfCells];

        for (int i = 0; i < firstGeneration.length; i++) {
            firstGeneration[i] = this.config.positivePositions.contains(i);
        }

        return firstGeneration;
    }

    /**
     * Draws the values of the inputted generation, where true is drawn as a star (*) and false as a space.
     */
    void draw(boolean[] generation) {
        for (boolean cell : generation) {
            System.out.print(cell ? "*" : " ");
        }

        System.out.print("\n");
    }

    /**
     * Returns the next generation on which the patterns have been applied.
     */
    boolean[] evolution(boolean[] previousGeneration) {
        // The cell values for the next generation.
        boolean[] nextGeneration = new boolean[previousGeneration.length];

        for (int i = 0; i < nextGeneration.length; i++) {
            nextGeneration[i] = satisfiesPatterns(previousGeneration, i);
        }

        return nextGeneration;
    }

    /**
     * Returns if the specified index from the inputted generation satisfies at least one of the patterns that are 
     * specified in the config.
     */
    boolean satisfiesPatterns(boolean[] generation, int index) {
        // Neighbourhood, meaning left neighbour, itself and right neighbour, of a cell.
        boolean[] neighbourhood = getNeighbourhood(generation, index);

        for (Pattern pattern : this.config.patterns) {
            if (pattern.isSatisfied(neighbourhood[0], neighbourhood[1], neighbourhood[2])) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the left, center and right values for an index.
     */
    boolean[] getNeighbourhood(boolean[] generation, int index) {
        return new boolean[]{
                getCell(generation, index - 1),
                getCell(generation, index),
                getCell(generation, index + 1)
        };
    }

    /**
     * Returns the cell from the generation. 
     * If the cell is out of bound then false is returned.
     */
    boolean getCell(boolean[] generation, int index) {
        if (index < 0) {
            return false;
        } else if (index >= generation.length) {
            return false;
        } else {
            return generation[index];
        }
    }

    public static void main(String[] args) {
        new Cellulitis().run();
    }

    class Configuration {
        // The number of generations for the automaton program.
        public int numberOfGenerations;
        // The number of cells wide the automaton will be.
        public int numberOfCells;
        // The preset positives cells in the automaton.
        public List<Integer> positivePositions = new ArrayList<>();
        // The list of patterns that will apply for the automaton.
        public List<Pattern> patterns;

        /**
         * Reads the config from the standard input.
         * Throws an exception when the input is incorrect.
         */
        void readConfig(Scanner scanner) throws Exception {
            // The type of automaton that will run.
            String automaton = scanner.next();
            this.numberOfCells = scanner.nextInt();
            this.numberOfGenerations = scanner.nextInt();

            if (!scanner.next().equals("init_start")) {
                throw new Exception();
            }

            // The index value of the next preset positions for true cells.
            String index;

            while (!(index = scanner.next()).equals("init_end")) {
                // The parsed index value.
                int i = Integer.parseInt(index) - 1;

                if (i < 0 || i >= this.numberOfCells) {
                    throw new Exception();
                }

                this.positivePositions.add(i);
            }

            if (automaton.equals("A")) {
                this.patterns = Arrays.asList(1, 3, 4, 5, 6).stream().map(Pattern::new).collect(Collectors.toList());
            } else if (automaton.equals("B")) {
                this.patterns = Arrays.asList(1, 2, 4, 6).stream().map(Pattern::new).collect(Collectors.toList());
            } else if (automaton.equals("U")) {
                this.patterns = new ArrayList<>();

                for (var i = 0; i < 8; i++) {
                    if (scanner.nextInt() == 1) {
                        this.patterns.add(new Pattern(i));
                    }
                }
            } else {
                throw new Exception();
            }
        }
    }

    class Pattern {
        // The left cell for this pattern.
        public boolean leftCell;
        // The middle cell for this pattern.
        public boolean middleCell;
        // The right cell for this pattern.
        public boolean rightCell;

        public Pattern(int index) {
            this.leftCell = (index >> 2) % 2 == 1;
            this.middleCell = (index >> 1) % 2 == 1;
            this.rightCell = index % 2 == 1;
        }

        /**
         * Returns if the inputted left, middle and right cell satiesfy this pattern.
         */
        public boolean isSatisfied(boolean leftCell, boolean middleCell, boolean rightCell) {
            return this.leftCell == leftCell
                    && this.middleCell == middleCell
                    && this.rightCell == rightCell;
        }
    }
}
