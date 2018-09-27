package tests;

import org.junit.Test;
import org.junit.jupiter.api.RepeatedTest;

import java.util.Random;

import core.GameGrid;
import core.GridNumber;
import core.Square;

import static org.junit.jupiter.api.Assertions.*;

public class GameGridTest {

    int gridSize = 9;
    GameGrid gameGrid = new GameGrid(gridSize);
    GridNumber[][] grid = gameGrid.getGrid();

    /**
     * Concept is the same for inCol
     */
    @Test
    @RepeatedTest(5)
    public void inRow() {
        gameGrid.easy();
        gameGrid.printGrid();
        int row = 1;
        int number = 4;
        boolean isInRow = false;
        for (int col = 0; col < grid.length; col++) {
            if (grid[row][col].getValue() == number) {
                System.out.println("yes     row: " + (row + 1) + " col: " + (col + 1));
                isInRow = true;
            }
        }
        assertFalse(isInRow);
    }

    @Test
    @RepeatedTest(5)
    public void inBox() {
        gameGrid.easy();
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
    @Test
    @RepeatedTest(10)
    public void testDuplication() {
        int number = randInt(1, gridSize);
        int row = randInt(0, gridSize - 1);
        int col = randInt(0, gridSize - 1);
        boolean duplicate = false;
        gameGrid.extreme();
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
    @Test
    public void getNextEmpty() {
        gameGrid.easy();
        Square square = new Square();
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

    /**
     * Brute force
     */
    @Test
    public void solve() {

    }

    private int randInt(int min, int max) {
        Random random = new Random();
        int value = random.nextInt((max - min) + 1) + min;
        return value;
    }
}