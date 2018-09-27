package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import java.util.Random;

import core.GameGrid;
import core.GridNumber;
import core.Square;

import static org.junit.jupiter.api.Assertions.*;

public class GameGridTest {

    int gridSize;
    GameGrid gameGrid;
    GridNumber[][] grid;

    @BeforeEach
    public void setUp(){
        gridSize = 9;
        gameGrid = new GameGrid(gridSize);
        grid = gameGrid.getGrid();
        gameGrid.easy();
    }

    /**
     * Concept is the same for inCol
     */
    @RepeatedTest(5)
    public void inRow() {
        gameGrid.printGrid();
        int row = randInt(0, gridSize - 1);
        int number = randInt(1, gridSize);
        boolean isInRow = false;
        for (int col = 0; col < grid.length; col++) {
            if (grid[row][col].getValue() == number) {
                System.out.println("number: " + number + "row: " + (row + 1));
                isInRow = true;
            }
        }
        assertFalse(isInRow);
    }

    @RepeatedTest(5)
    public void inCol() {
        gameGrid.printGrid();
        int col = randInt(0, gridSize - 1);
        int number = randInt(1, gridSize);
        boolean isInCol = false;
        for (int row = 0; row < grid.length; row++) {
            if (grid[row][col].getValue() == number) {
                System.out.println("number: " + "col: " + (col + 1));
                isInCol = true;
            }
        }
        assertFalse(isInCol);
    }

    @RepeatedTest(5)
    public void inBox() {
        gameGrid.printGrid();
        int boxStartRow = 0;
        int boxStartCol = 0;
        int initRow = randInt(0, gridSize - 1);
        int initCol = 8;
        int number = 4;
        boolean isInBox = false;
        boxStartRow = gameGrid.getBoxStart(initRow);
        boxStartCol = gameGrid.getBoxStart(initCol);
        for (int row = boxStartRow; row < boxStartRow + 3; row++) {
            for (int col = boxStartCol; col < boxStartCol + +3; col++) {
                if (grid[row][col].getValue() == number) {
                    isInBox = true;
                }
            }
        }
        assertFalse(isInBox);
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
        // logging will be used in lieu of println at a later date
        if (isInRow) {
            System.out.println("***IN ROW*** number: " + number + " row: " + (row + 1));
            duplicate = true;
        } else if (isInCol) {
            System.out.println("***IN COL*** number: " + number + " col: " + (col + 1));
            duplicate = true;
        } else if (isInBox) {
            System.out.println("***IN BOX*** number: " + number + " row: " + (row + 1) + " col: " + (col + 1));
            duplicate = true;
        }
        gameGrid.printGrid();
        assertFalse(duplicate);
    }

    @SuppressWarnings("Duplicates")
    @RepeatedTest(5)
    public void getNextEmpty() {
        Square square = new Square();
        System.out.println("length: " + grid.length);
        rowLoop: for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid.length; col++) {
                if (grid[row][col].getValue() == 0) {
                    square.setRow(row);
                    square.setCol(col);
                    square.setEmpty(true);
                    break rowLoop;
                }
            }
        }
        assertTrue(square.isEmpty());
    }

    private int randInt(int min, int max) {
        Random random = new Random();
        int value = random.nextInt((max - min) + 1) + min;
        return value;
    }
}