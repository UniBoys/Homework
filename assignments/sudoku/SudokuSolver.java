package assignments.sudoku;

import java.util.Arrays;
import java.util.List;
import java.util.Date;

/**
 * Class that solves the Asterisk Sudoku.
 * Prints the number of solutions of a Sudoku if there are multiple. If there is only a single solution, prints this solution instead.
 *
 * by <<TODO YOUR NAME AND ID HERE>>
 * and <<TODO YOUR PARTNERS NAME AND ID HERE>>
 * as group <<TODO GROUP NUMBER HERE>>
 */
class SudokuSolver {

    int SUDOKU_SIZE = 9;          // Size of the grid.
    int SUDOKU_MIN_NUMBER = 1;    // Minimum digit to be filled in.
    int SUDOKU_MAX_NUMBER = 9;    // Maximum digit to be filled in.
    int SUDOKU_BOX_DIMENSION = 3; // Dimension of the boxes (sub-grids that should contain all digits).
    List<int[]> ASTERISK_POSITIONS = Arrays.asList(new int []{4, 1}, new int []{2, 2}, new int []{6, 2}, 
                                                    new int []{1, 4}, new int []{4, 4}, new int []{7, 4}, 
                                                    new int []{2, 6}, new int []{6, 6}, new int []{4, 7});

    int[][] grid = new int[][]{  // The puzzle grid; 0 represents empty.
            {0, 9, 0, 7, 3, 0, 4, 0, 0},    // One solution.
            {0, 0, 0, 0, 0, 0, 5, 0, 0},
            {3, 0, 0, 0, 0, 6, 0, 0, 0},

            {0, 0, 0, 0, 0, 2, 6, 4, 0},
            {0, 0, 0, 6, 5, 1, 0, 0, 0},
            {0, 0, 6, 9, 0, 7, 0, 0, 0},

            {5, 8, 0, 0, 0, 0, 0, 0, 0},
            {9, 0, 0, 0, 0, 3, 0, 2, 5},
            {6, 0, 3, 0, 0, 0, 8, 0, 0},
    };
    int[][] lastCorrectGrid;

    int solutionCounter = 0; // Solution counter

    // Is there a conflict when we fill in d at position (r, c)?
    boolean givesConflict(int r, int c, int d) {
        return rowConflict(r, d) || columnConflict(c, d) || boxConflict(r, c, d) 
            || (isInAsterisk(c, r) && asteriskConflict(d));
    }

    // Is there a conflict when we fill in d in row r?
    boolean rowConflict(int r, int d) {
        int[] row = this.grid[r];
        for (int c = 0; c < this.grid.length; c++) {
            int n = row[c];

            if (n == d) {
                return true;
            }
        }
        return false;
    }

    // Is there a conflict in column c when we fill in d?
    boolean columnConflict(int c, int d) {
        for (int r = 0; r < this.grid.length; r++) {
            int n = this.grid[r][c];

            if (n == d) {
                return true;
            }
        }
        return false;
    }

    // Is there a conflict in the box at (r, c) when we fill in d?
    boolean boxConflict(int r, int c, int d) {
        int boxX = c - c % this.SUDOKU_BOX_DIMENSION;
        int boxY = r - r % this.SUDOKU_BOX_DIMENSION;

        for (int x = boxX; x < boxX + 3; x++) {
            for (int y = boxY; y < boxY + 3; y++) {
                if (this.grid[y][x] == d) {
                    return true;
                }
            }
        }

        return false;
    }

    boolean isInAsterisk(int c, int r) {
        return this.ASTERISK_POSITIONS.stream().anyMatch(p -> p[0] == c && p[1] == r);
    }

    // Is there a conflict in the asterisk when we fill in d?
    boolean asteriskConflict(int d) {
        return this.ASTERISK_POSITIONS.stream().anyMatch(p -> this.grid[p[1]][p[0]] == d);
    }

    // Finds the next empty square (in "reading order").
    int[] findEmptySquare() {
        for (int r = 0; r < this.grid.length; r++) {
            for (int c = 0; c < this.grid[r].length; c++) {
                if (this.grid[r][c] == 0) {
                    return new int[]{r, c};
                }
            }
        }

        return null;
    }

    // Find all solutions for the grid, and stores the final solution.
    void solve() {
        int[] location = findEmptySquare();

        if(location == null) {
            this.solutionCounter++;
            this.lastCorrectGrid = cloneGrid(this.grid);

            return;
        }

        for(int i = this.SUDOKU_MIN_NUMBER; i <= this.SUDOKU_MAX_NUMBER; i++) {
            if(givesConflict(location[0], location[1], i)) {
                continue;
            }

            this.grid[location[0]][location[1]] = i;

            solve();

            this.grid[location[0]][location[1]] = 0;
        }
    }

    int[][] cloneGrid(int[][] grid) {
        int[][] clonedGrid = new int[grid.length][grid[0].length];

        for(int y = 0; y < grid.length; y++) {
            int[] row = new int[grid[y].length];

            for(int x = 0; x < row.length; x++) {
                row[x] = grid[y][x];
            }

            clonedGrid[y] = row;
        }

        return clonedGrid;
    }

    // Print the sudoku grid.
    void print() {
        System.out.println("+-----------------+");
        System.out.format("|%s %s %s|%s %s %s|%s %s %s|\n", rowToString(0));
        System.out.format("|%s %s %s|%s>%s<%s|%s %s %s|\n", rowToString(1));
        System.out.format("|%s %s>%s|%s %s %s|%s<%s %s|\n", rowToString(2));
        System.out.println("+-----------------+");
        System.out.format("|%s %s %s|%s %s %s|%s %s %s|\n", rowToString(3));
        System.out.format("|%s>%s<%s|%s>%s<%s|%s>%s<%s|\n", rowToString(4));
        System.out.format("|%s %s %s|%s %s %s|%s %s %s|\n", rowToString(5));
        System.out.println("+-----------------+");
        System.out.format("|%s %s>%s|%s %s %s|%s<%s %s|\n", rowToString(6));
        System.out.format("|%s %s %s|%s>%s<%s|%s %s %s|\n", rowToString(7));
        System.out.format("|%s %s %s|%s %s %s|%s %s %s|\n", rowToString(8));
        System.out.println("+-----------------+");
    }

    Object[] rowToString(int row) {
        Object[] items = new Object[this.SUDOKU_SIZE];

        for (int i = 0; i < items.length; i++) {
            items[i] = numberOrSpace(this.grid[row][i]);
        }

        return items;
    }

    String numberOrSpace(int value) {
        return value == 0 ? " " : "" + value;
    }

    // Run the actual solver.
    void solveIt() {
        Date d = new Date();
        solve();
        System.out.format("solved in %s ms\n", (new Date().getTime() - d.getTime()));

        if (this.solutionCounter == 0) {
            // Is this needed?
            System.out.println("There is no solution.");
        } else if (this.solutionCounter == 1) {
            this.grid = this.lastCorrectGrid;
            print();
        } else {
            System.out.println(this.solutionCounter);
        }
    }

    public static void main(String[] args) {
        (new SudokuSolver()).solveIt();
    }
}
