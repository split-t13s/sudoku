package tests;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import java.util.HashSet;
import core.GameGrid;
import core.GridNumber;

import static org.junit.jupiter.api.Assertions.*;

public class GameGridTest {

    int gridSize = 9;
    GameGrid gameGrid = new GameGrid(gridSize);
    GridNumber[][] grid = gameGrid.getGrid();

    @Test
    public void testGridArea() {
        assertEquals(gridSize * gridSize, gameGrid.getGridArea());
    }

    @Test
    public void testEasyClues() {
        int numOfClues = 0;
        int min = gridSize*4;
        int max = gridSize*5;
        gameGrid.easy();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j].getValue() != 0) {
                    numOfClues++;
                }
            }
        }
        assertTrue(min <= numOfClues && numOfClues <= max);
        assertFalse(max < numOfClues);
        assertFalse(numOfClues < min);
    }

    @Test
    @RepeatedTest(3)
    public void testDuplicates() {
        gameGrid.medium();
        boolean duplicate = false;
        for (int i = 0; i < grid.length; i++) {
            // Create HashSets that will store the current row and column
            HashSet<Integer> row = new HashSet<Integer>();
            HashSet<Integer> column = new HashSet<Integer>();
            for (int j = 0; j < grid.length; j++) {
                // Ignore 0 values
                if (grid[i][j].getValue() != 0){
                    // Check if set already contains current value
                    if (row.contains(grid[i][j].getValue())){
                        duplicate = true;
                        break;
                    }
                    row.add(grid[i][j].getValue());
                }
                if (grid[j][i].getValue() != 0){
                    // Check if set already contains current value
                    if (column.contains(grid[j][i].getValue())){
                        duplicate = true;
                        break;
                    }
                    column.add(grid[j][i].getValue());
                }
            }
        }
        assertFalse(duplicate);
    }
}