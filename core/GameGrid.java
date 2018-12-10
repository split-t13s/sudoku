package core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameGrid {

    public GridNumber[][] grid;
    int gridSize;           // Current grid size will be 9x9; this will be extended to include 4x4, 12x12 and 16x16

    public GameGrid(int gridSize) {
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
        int clues = randInt(gridSize * 4, gridSize * 5);
        while (clues != 0) {
            depopulateGrid();
            clues--;
        }
    }

    /**
     * Generates a puzzle of medium difficulty. For a 9x9 grid a puzzle will contain between 27 and 35 clues.
     */
    public void medium() {
        int clues = randInt((gridSize * 5) + 1, gridSize * 6);
        while (clues != 0) {
            depopulateGrid();
            clues--;
        }
    }

    /**
     * Generates a puzzle of hard difficulty. A 9x9 grid will have between 18 and 26 clues.
     */
    public void hard() {
        int clues = randInt((gridSize * 6) + 1, gridSize * 7);
        while (clues != 0) {
            depopulateGrid();
            clues--;
        }
    }

    /**
     * Generates a puzzle of extreme difficulty. A 9x9 grid will have between 9 and 17 clues.
     */
    public void extreme() {
        int clues = randInt((gridSize * 7) + 1, gridSize * 8);
        while (clues != 0) {
            depopulateGrid();
            clues--;
        }
    }

    /**
     * Fills the grid with a small set of random numbers.
     */
    public void initialClues() {
        int clues = randInt(gridSize, gridSize + 3);
        while (clues != 0) {
            populateGrid();
            clues--;
        }
    }

    /**
     * Empties grid and generates complete puzzle.
     */
    public void setupForNewGame() {
        emptyGrid();
        initialClues();
        whileSolve();
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
            }
        }
    }

    /**
     * Removes a random number from the grid.
     */
    private void depopulateGrid() {
        int row = randInt(0, gridSize - 1);
        int col = randInt(0, gridSize - 1);
        // If coordinates already empty then generate new coordinates
        if (grid[row][col].getValue() == 0) {
            depopulateGrid();
        } else {
            grid[row][col].setValue(0);
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
                break;
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
                break;
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
                    break;
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
     * recurSolve() method (previously named solve()) adapted to function with a while-loop to overcome deep recursion
     * StackOverflow error. recurSolve() moved to legacy_methods.old
     */
    public void whileSolve() {
        Square nextSquare = getNextEmpty();
        List<Square> prevSquares = new ArrayList<Square>();
        int row = 0;
        int col = 0;
        int squareNum = 0;
        boolean backtrace = false;
        while (nextSquare.isEmpty()) {
            if (backtrace) {
                // Get last number and remove from previous moves, increment from last number
                Square prevSquare = prevSquares.get((prevSquares.size() - 1));
                row = prevSquare.getRow();
                col = prevSquare.getCol();
                squareNum = prevSquare.getNumber();
                nextSquare = prevSquare;        // prevSquare will be empty so nextSquare.isEmpty() will be true
                prevSquares.remove(prevSquare);
            } else {
                // Get next empty square
                nextSquare = getNextEmpty();
                row = nextSquare.getRow();
                col = nextSquare.getCol();
                squareNum = nextSquare.getNumber();
                if (squareNum == 0) {
                    // 0 is not a valid solution number
                    squareNum = 1;
                }
            }
            if (nextSquare.isEmpty()) {
                numLoop:
                for (int number = squareNum; number <= 9; number++) {
                    if (!checkDuplicate(row, col, number)) {
                        // Set chosen number
                        grid[row][col].setValue(number);
                        nextSquare.setNumber(number);
                        prevSquares.add(nextSquare);
                        nextSquare = getNextEmpty();
                        backtrace = false;
                        break numLoop;
                    } else if (checkDuplicate(row, col, number) && number == 9) {
                        grid[row][col].setValue(0);
                        backtrace = true;
                        break numLoop;
                    }
                }
            } else {
                System.out.println("solved");
            }
        }
    }

    /**
     * Get current state of game grid and store all values in a .sav file.
     *
     * @throws IOException
     */
    public void writeGridToFile() throws IOException {
        String str = "";
        // Get numbers from grid and separate with commas
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid.length; col++) {
                str += grid[row][col].getValue();
                if (grid[row][col] != grid[gridSize - 1][gridSize - 1]) {
                    str += ",";
                }
            }
        }
        // Auto generate filename from current datetime
        String filename = generateFilename();
        String filepath = "saves/" + filename + ".sav";
        Path path = Paths.get(filepath);
        byte[] strToBytes = str.getBytes();
        Files.write(path, strToBytes);
    }

    /**
     * Read .sav file to restore a game grid from a previous state.
     *
     * @param filename - the file to be read.
     * @return - int[][] containing the restored grid.
     * @throws IOException
     */
    public int[][] readGridFromFile(String filename) throws IOException {
        String filepath = "saves/" + filename;
        int[][] gameFromFile = new int[gridSize][gridSize];
        Path path = Paths.get(filepath);
        String read = Files.readAllLines(path).get(0);
        String[] numberArray = read.split(",");
        int number = 0;
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid.length; col++) {
                gameFromFile[row][col] = Integer.parseInt(numberArray[number]);
                number++;
            }
        }
        return gameFromFile;
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

    /**
     * Translates a given integer array to the game grid.
     *
     * @param array - the array to be passed to the grid
     */
    public void arrayToGrid(int[][] array) {
        for (int row = 0; row < array.length; row++) {
            for (int col = 0; col < array.length; col++) {
                grid[row][col].setValue(array[row][col]);
            }
        }
    }

    /**
     * Stores the current game grid in an integer array.
     *
     * @return - the int array containing the grid
     */
    public int[][] gridToArray() {
        int[][] gridArray = new int[gridSize][gridSize];
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid.length; col++) {
                gridArray[row][col] = grid[row][col].getValue();
            }
        }
        return gridArray;
    }

    /**
     * Get current datetime with illegal characters replaced and nanoseconds removed.
     *
     * @return String representation of datetime for filename use
     */
    private String generateFilename() {
        String time = LocalDateTime.now().toString();
        time = time.replace(":", ".");
        time = time.replace("T", "-");
        time = time.substring(0, time.length() - 4);
        return time;
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

    public static void main(String[] args) throws IOException {
        GameGrid gameGrid = new GameGrid(9);
        //gameGrid.arrayToGrid(example);
        gameGrid.setupForNewGame();
        gameGrid.easy();
        gameGrid.writeGridToFile();
//        gameGrid.printGrid();
    }
}
