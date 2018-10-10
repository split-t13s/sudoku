package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import core.GameGrid;
import core.GridNumber;
import core.Square;

public class GameGridTest {

    static int gridSize;
    static GameGrid gameGrid;
    static GridNumber[][] grid;
    static int[][] example = {{5, 0, 0, 6, 7, 0, 9, 0, 0},
            {0, 4, 0, 8, 0, 0, 0, 0, 0},
            {8, 0, 0, 5, 0, 0, 6, 1, 3},
            {0, 6, 2, 4, 0, 0, 0, 7, 0},
            {1, 0, 0, 0, 0, 3, 0, 2, 0},
            {3, 7, 4, 9, 0, 8, 0, 0, 0},
            {0, 9, 6, 1, 0, 7, 8, 0, 2},
            {2, 1, 8, 0, 0, 6, 0, 4, 5},
            {0, 5, 0, 0, 8, 0, 0, 9, 0}};
    static int[][] exampleSolved = {{5, 3, 1, 6, 7, 2, 9, 8, 4},
            {6, 4, 9, 8, 3, 1, 2, 5, 7},
            {8, 2, 7, 5, 4, 9, 6, 1, 3},
            {9, 6, 2, 4, 1, 5, 3, 7, 8},
            {1, 8, 5, 7, 6, 3, 4, 2, 9},
            {3, 7, 4, 9, 2, 8, 5, 6, 1},
            {4, 9, 6, 1, 5, 7, 8, 3, 2},
            {2, 1, 8, 3, 9, 6, 7, 4, 5},
            {7, 5, 3, 2, 8, 4, 1, 9, 6}};

    @BeforeAll
    public static void setUp() {
        gridSize = 9;
        gameGrid = new GameGrid(gridSize);
        grid = gameGrid.getGrid();
        gameGrid.arrayToGrid(example);
    }

    @RepeatedTest(5)
    public void inRow() {
        int row = randInt(0, gridSize - 1);
        int number = randInt(1, gridSize);
        boolean isInRow = false;
        String error = "";
        for (int col = 0; col < grid.length; col++) {
            if (grid[row][col].getValue() == number) {
                error = "number: " + number + " row: " + (row + 1);
                isInRow = true;
            }
        }
        gameGrid.printGrid();
        assertFalse(isInRow, error);
    }

    @RepeatedTest(5)
    public void inCol() {
        int col = randInt(0, gridSize - 1);
        int number = randInt(1, gridSize);
        boolean isInCol = false;
        String error = "";
        for (int row = 0; row < grid.length; row++) {
            if (grid[row][col].getValue() == number) {
                error = "number: " + number + " col: " + (col + 1);
                isInCol = true;
            }
        }
        gameGrid.printGrid();
        assertFalse(isInCol, error);
    }

    @RepeatedTest(5)
    public void inBox() {
        int boxStartRow = 0;
        int boxStartCol = 0;
        int initRow = randInt(0, gridSize - 1);
        int initCol = 8;
        int number = 4;
        boolean isInBox = false;
        String error = "";
        boxStartRow = gameGrid.getBoxStart(initRow);
        boxStartCol = gameGrid.getBoxStart(initCol);
        for (int row = boxStartRow; row < boxStartRow + 3; row++) {
            for (int col = boxStartCol; col < boxStartCol + +3; col++) {
                if (grid[row][col].getValue() == number) {
                    error = "number: " + number + " row: " + row + " col: " + (col + 1);
                    isInBox = true;
                }
            }
        }
        gameGrid.printGrid();
        assertFalse(isInBox, error);
    }

    /**
     * Tests if a newly generated random number would be invalid if it were to be entered into the current grid.
     * This has been applied to the populateGrid() method and has been slightly simplified with the checkDuplicate()
     * method.
     */
    @RepeatedTest(10)
    public void testDuplication() {
        int number = randInt(1, gridSize);
        int row = randInt(0, gridSize - 1);
        int col = randInt(0, gridSize - 1);
        boolean duplicate = false;
        boolean isInRow = gameGrid.inRow(row, number);
        boolean isInCol = gameGrid.inCol(col, number);
        boolean isInBox = gameGrid.inBox(row, col, number);
        String error = "";
        if (isInRow) {
            error = "***IN ROW*** number: " + number + " row: " + (row + 1);
            duplicate = true;
        } else if (isInCol) {
            error = "***IN COL*** number: " + number + " col: " + (col + 1);
            duplicate = true;
        } else if (isInBox) {
            error = "***IN BOX*** number: " + number + " row: " + (row + 1) + " col: " + (col + 1);
            duplicate = true;
        }
        gameGrid.printGrid();
        assertFalse(duplicate, error);
    }

    @RepeatedTest(5)
    public void populateGrid() {
        gameGrid.emptyGrid();
        int toBeAdded = randInt(gridSize, gridSize * 4);
        int addedCheck = toBeAdded;
        String error = "";
        while (toBeAdded != 0) {
            int row = randInt(0, gridSize - 1);
            int col = randInt(0, gridSize - 1);
            int number = randInt(1, gridSize);
            if (grid[row][col].getValue() == 0) {
                grid[row][col].setValue(number);
                toBeAdded--;
            }
        }
        int added = 0;
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid.length; col++) {
                if (grid[row][col].getValue() != 0) {
                    added++;
                }
            }
        }
        if (added < addedCheck) {
            error = "Too few numbers added";
        } else if (addedCheck < added) {
            error = "Too many numbers added";
        }
        assertEquals(addedCheck, added, error);
    }

    @RepeatedTest(5)
    public void depopulateGrid() {
        gameGrid.arrayToGrid(exampleSolved);
        int toBeRemoved = randInt(gridSize, gridSize * 4);
        int removedCheck = toBeRemoved;
        String error = "";
        while (toBeRemoved != 0) {
            int row = randInt(0, gridSize - 1);
            int col = randInt(0, gridSize - 1);
            if (grid[row][col].getValue() != 0) {
                grid[row][col].setValue(0);
                toBeRemoved--;
            }
        }
        int removed = 0;
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid.length; col++) {
                if (grid[row][col].getValue() == 0) {
                    removed++;
                }
            }
        }
        if (removed < removedCheck) {
            error = "Too few numbers removed";
        } else if (removedCheck < removed) {
            error = "Too many numbers removed";
        }
        gameGrid.printGrid();
        assertEquals(removedCheck, removed, error);
    }

    @SuppressWarnings("Duplicates")
    /**
     * Brute force
     */
    @Test
    public void whileSolve() {
        Square nextSquare = gameGrid.getNextEmpty();
        List<Square> prevSquares = new ArrayList<Square>();
        int row = 0;
        int col = 0;
        int squareNum = 0;
        boolean backtrace = false;
        while (nextSquare.isEmpty()) {
            if (backtrace) {
                Square prevSquare = prevSquares.get((prevSquares.size() - 1));
                row = prevSquare.getRow();
                col = prevSquare.getCol();
                squareNum = prevSquare.getNumber();
                nextSquare = prevSquare;
                prevSquares.remove(prevSquare);
            } else {
                // Get next empty square
                nextSquare = gameGrid.getNextEmpty();
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
                    if (!gameGrid.checkDuplicate(row, col, number)) {
                        // Set chosen number
                        grid[row][col].setValue(number);
                        nextSquare.setNumber(number);
                        prevSquares.add(nextSquare);
                        nextSquare = gameGrid.getNextEmpty();
                        backtrace = false;
                        break numLoop;
                    } else if (gameGrid.checkDuplicate(row, col, number) && number == 9) {
                        grid[row][col].setValue(0);
                        backtrace = true;
                        break numLoop;
                    }
                }
            } else {
                int[][] solution = gameGrid.gridToArray();
                assertArrayEquals(exampleSolved, solution, "puzzle not solved");
            }
        }
    }

    @Test
    public void writeGridToFile() throws IOException {
        String str = "";
        String check = "5,0,0,6,7,0,9,0,0,0,4,0,8,0,0,0,0,0,8,0,0,5,0,0,6,1,3,0,6,2,4,0,0,0,7,0,1,0,0,0,0,3,0,2,0,3," +
                "7,4,9,0,8,0,0,0,0,9,6,1,0,7,8,0,2,2,1,8,0,0,6,0,4,5,0,5,0,0,8,0,0,9,0";
        // Get numbers from grid and separate with commas
        for (int row = 0; row < example.length; row++) {
            for (int col = 0; col < example.length; col++) {
                str += example[row][col];
                if (grid[row][col] != grid[gridSize - 1][gridSize - 1]) {
                    str += ",";
                }
            }
        }
        Path path = Paths.get("saves/test.sav");
        byte[] strToBytes = str.getBytes();
        Files.write(path, strToBytes);
        String read = Files.readAllLines(path).get(0);
        assertEquals(check, read);
    }

    @Test
    public void readGridFromFile() throws IOException {
        int[][] gridFromFile = new int[gridSize][gridSize];
        Path path = Paths.get("saves/test.sav");
        String read = Files.readAllLines(path).get(0);
        String[] numberArray = read.split(",");
        int number = 0;
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid.length; col++) {
                gridFromFile[row][col] = Integer.parseInt(numberArray[number]);
                number++;
            }
        }
        assertArrayEquals(example, gridFromFile);
    }
    
    private int randInt(int min, int max) {
        Random random = new Random();
        int value = random.nextInt((max - min) + 1) + min;
        return value;
    }
}