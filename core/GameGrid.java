package core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameGrid {

    List<Square> prevSquares;       // temporary - for testing
    public GridNumber[][] grid;
    private Random random;
    private long gameSeed;
    int gridSize;           // Current gridsize will be 9x9; this will be extended to include 4x4, 12x12 and 16x16

    static int[][] example = {{5, 0, 0, 6, 7, 0, 9, 0, 0},
            {0, 4, 0, 8, 0, 0, 0, 0, 0},
            {8, 0, 0, 5, 0, 0, 6, 1, 3},
            {0, 6, 2, 4, 0, 0, 0, 7, 0},
            {1, 0, 0, 0, 0, 3, 0, 2, 0},
            {3, 7, 4, 9, 0, 8, 0, 0, 0},
            {0, 9, 6, 1, 0, 7, 8, 0, 2},
            {2, 1, 8, 0, 0, 6, 0, 4, 5},
            {0, 5, 0, 0, 8, 0, 0, 9, 0}};

    static int[][] exampletwo = {{0, 0, 6, 0, 5, 4, 0, 9, 0},
            {3, 0, 2, 0, 6, 0, 0, 7, 0},
            {0, 5, 0, 7, 0, 0, 6, 0, 8},
            {5, 0, 0, 0, 0, 6, 7, 0, 0},
            {0, 4, 9, 0, 3, 0, 0, 0, 0},
            {0, 0, 7, 5, 0, 0, 0, 8, 9},
            {7, 0, 1, 0, 0, 0, 0, 4, 0},
            {0, 8, 0, 0, 7, 0, 5, 0, 2},
            {0, 9, 0, 2, 0, 3, 0, 0, 0}};

    static int[][] examplethree = {{0, 3, 0, 2, 0, 9, 0, 8, 0},
            {7, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 2, 0, 7, 6, 1, 0, 4, 0},
            {0, 0, 9, 0, 2, 0, 6, 0, 0},
            {0, 7, 0, 0, 0, 0, 0, 3, 0},
            {0, 0, 2, 0, 5, 0, 9, 0, 0},
            {0, 6, 0, 3, 1, 2, 0, 5, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 3},
            {0, 4, 0, 5, 0, 6, 0, 2, 0}};

    public GameGrid(int gridSize) {
        prevSquares = new ArrayList<Square>();
        grid = new GridNumber[gridSize][gridSize];
        this.gridSize = gridSize;
        emptyGrid();
    }

    /**
     * Getter method which returns the game grid
     *
     * @return Number grid
     */
    public GridNumber[][] getGrid() {
        return grid;
    }

    /**
     * Fills the generated game grid with GridNumber objects
     */
    public void emptyGrid() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                grid[i][j] = new GridNumber();
            }
        }
    }

    /**
     * Generates a puzzle of easy difficulty. Easy is currently defined as a puzzle that contains between 36 and 45
     * clues for a 9x9 grid.
     */
    public void easy() {
        int clues = randInt(gridSize * 4, gridSize * 5);    //Factor of gridSize used for scalability
        int numFilled = 0;
        while (numFilled != clues) {
            populateGrid();
            numFilled++;
        }
    }

    /**
     * Generates a puzzle of medium difficulty. For a 9x9 grid a puzzle will contain between 27 and 35 clues.
     */
    public void medium() {
        int clues = randInt(gridSize * 3, gridSize * 4 - 1);
        int numFilled = 0;
        while (numFilled != clues) {
            populateGrid();
            numFilled++;
        }
    }

    /**
     * Generates a puzzle of hard difficulty. A 9x9 grid will have between 18 and 26 clues.
     */
    public void hard() {
        int clues = randInt(gridSize * 2, gridSize * 3 - 1);
        int numFilled = 0;
        while (numFilled != clues) {
            populateGrid();
            numFilled++;
        }
    }

    /**
     * Generates a puzzle of extreme difficulty. A 9x9 grid will have between 9 and 17 clues.
     */
    public void extreme() {
        int clues = randInt(gridSize * 1, gridSize * 2 - 1);
        int numFilled = 0;
        while (numFilled != clues) {
            populateGrid();
            numFilled++;
        }
    }

    /**
     * Recursive method used to fill the game grid with random numbers.
     * This method is invoked by methods that generate puzzles.
     */
    private void populateGrid() {
        int row = randInt(0, gridSize - 1);
        int col = randInt(0, gridSize - 1);
        // If coordinates already contain a number then generate new coordinates
        if (grid[row][col].getValue() != 0) {
            populateGrid();
        } else {
            // Generate number to be entered into grid
            int number = randInt(1, gridSize);
            // Check if the row and/or column already contains the generated number (duplicate)
            boolean isDuplicate = checkDuplicate(row, col, number);
            // If the current generated number is a duplicate then a new number must be generated
            if (isDuplicate) {
                populateGrid();
            } else {
                // If the current generated number is valid then apply it to the grid
                grid[row][col].setValue(number);
                grid[row][col].setValueAsString(Integer.toString(number));
            }
        }
    }

    /**
     * Searches a given row for a given number.
     *
     * @param row    - the row to be searched
     * @param number - the number being searched for
     * @return boolean where true indicates the number is in the row
     */
    public boolean inRow(int row, int number) {
        boolean isInRow = false;
        for (int col = 0; col < grid.length; col++) {
            if (grid[row][col].getValue() == number) {
                isInRow = true;
            }
        }
        return isInRow;
    }

    /**
     * Searches a given column for a given number.
     *
     * @param col    - the column to be searched
     * @param number - the number being searched for
     * @return boolean where true indicates the number is in the column
     */
    public boolean inCol(int col, int number) {
        boolean isInCol = false;
        for (int row = 0; row < grid.length; row++) {
            if (grid[row][col].getValue() == number) {
                isInCol = true;
            }
        }
        return isInCol;
    }

    /**
     * Searches the box for a given number to see if the number is already within the box.
     * The coordinates for the number given are used to interpret the area of the box it is contained in.
     *
     * @param initRow - the row of the given number
     * @param initCol - the column of the given number
     * @param number  - the number being searched for
     * @return boolean where true indicates the number is in the box
     */
    public boolean inBox(int initRow, int initCol, int number) {
        int startRow = getBoxStart(initRow);
        int startCol = getBoxStart(initCol);
        boolean isInBox = false;
        for (int row = startRow; row < startRow + 3; row++) {
            for (int col = startCol; col < startCol + 3; col++) {
                if (grid[row][col].getValue() == number) {
                    isInBox = true;
                }
            }
        }
        return isInBox;
    }

    /**
     * Combination method of duplication checks to simplify checking all possible instances of a duplicate for a given
     * number.
     *
     * @param row    - the row of the given number
     * @param col    - the column of the given number
     * @param number - the number being searched for
     * @return boolean where true indicates the number is a duplicate
     */
    public boolean checkDuplicate(int row, int col, int number) {
        boolean isDuplicate = false;
        boolean isInRow = inRow(row, number);
        boolean isInCol = inCol(col, number);
        boolean isInBox = inBox(row, col, number);
        if (isInRow || isInCol || isInBox) {
            isDuplicate = true;
        }
        return isDuplicate;
    }

    /**
     * Determines the starting row/column of a box given a row/column.
     * This method must be called twice to determine both the starting row and column of box for a given number (once
     * per coordinate).
     *
     * @param init - the row/column of a number within a box
     * @return the start row/column for the box
     */
    public int getBoxStart(int init) {
        int boxStart = 0;
        if (init >= 6) {
            boxStart = 6;
        } else if (3 <= init && init < 6) {
            boxStart = 3;
        } else if (0 <= init && init < 3) {
            boxStart = 0;
        }
        return boxStart;
    }

    @SuppressWarnings("Duplicates")
    /**
     * Searches the grid for the next instance of an empty slot (represented by 0).
     * @return Square object that holds coordinates of next empty square
     */
    public Square getNextEmpty() {
        Square square = new Square();
        rowLoop:
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid.length; col++) {
                if (grid[row][col].getValue() == 0) {
                    square.setRow(row);
                    square.setCol(col);
                    square.setEmpty(true);
                    break rowLoop;
                }
            }
        }
        return square;
    }

    @SuppressWarnings("Duplicates")
    /**
     * Brute force approach
     */
    public void solve(List<Square> prevSquares, boolean redo) {
        Square nextSquare = null;
        int row = 0;
        int col = 0;
        int squareNum = 0;
        if (redo) {
            Square prevSquare = prevSquares.get((prevSquares.size() - 1));
            row = prevSquare.getRow();
            col = prevSquare.getCol();
            squareNum = prevSquare.getNumber();
            nextSquare = prevSquare;
            prevSquares.remove(prevSquare);
        } else {
            nextSquare = getNextEmpty();
            row = nextSquare.getRow();
            col = nextSquare.getCol();
            squareNum = nextSquare.getNumber();
            if (squareNum == 0) {
                squareNum = 1;
            }
        }
        if (nextSquare.isEmpty()) {
            for (int number = squareNum; number <= gridSize; number++) {
                if (!checkDuplicate(row, col, number)) {
                    grid[row][col].setValue(number);
                    nextSquare.setNumber(number);
                    prevSquares.add(nextSquare);
                    System.out.println(number);
                    printGrid();
                    solve(prevSquares, false);
                    break;
                } else if (checkDuplicate(row, col, number) && number == 9) {
                    grid[row][col].setValue(0);
                    solve(prevSquares, true);
                    break;
                }
            }
        } else {
            System.out.println("solved");
        }
    }

    private int randInt(int min, int max) {
        Random random = new Random();
        int value = random.nextInt((max - min) + 1) + min;
        return value;
    }

    private int randInt(int min, int max, long gameSeed) {
        Random random = new Random(gameSeed);
        int clues = random.nextInt((max - min) + 1) + min;
        return clues;
    }

    /**
     * Prints a string representation of the grid
     */
    public void printGrid() {
        for (int i = 0; i < grid.length; i++) {
            System.out.print("|");
            for (int j = 0; j < grid[i].length; j++) {
                System.out.print(grid[i][j].getValueAsString() + "|");
            }
            System.out.println();
        }
        System.out.println();
    }

    private void fillGridWithExample(int[][] example) {
        for (int row = 0; row < example.length; row++) {
            for (int col = 0; col < example.length; col++) {
                grid[row][col].setValue(example[row][col]);
            }
        }
    }

    public List<Square> getPrevSquares() {
        return prevSquares;
    }

    public void setPrevSquares(List<Square> prevSquares) {
        this.prevSquares = prevSquares;
    }

    public static void main(String[] args) {
        GameGrid gameGrid = new GameGrid(9);
        gameGrid.fillGridWithExample(examplethree);
        gameGrid.solve(gameGrid.getPrevSquares(), false);
    }
}
