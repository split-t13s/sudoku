package core;

import java.util.Random;

public class GameGrid {

    public GridNumber[][] grid;
    private Random random;
    private long gameSeed;
    int gridSize;           // Current gridsize will be 9x9; this will be extended to include 4x4, 12x12 and 16x16

    public GameGrid(int gridSize){
        grid = new GridNumber[gridSize][gridSize];
        this.gridSize = gridSize;
        fillGridWithNumbers();
    }

    /**
     * Getter method which returns the game grid
     * @return Number grid
     */
    public GridNumber[][] getGrid(){
        return grid;
    }

    /**
     * Calculates the area of the game grid generated
     * @return integer value of area
     */
    public int getGridArea(){
        int count = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                count++;
            }
        }
        return count;
    }

    /**
     * Fills the generated game grid with GridNumber objects
     */
    public void fillGridWithNumbers(){
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = new GridNumber();
            }
        }
    }

    /**
     * Generates a puzzle of easy difficulty. Easy is currently defined as a puzzle that contains between 36 and 45
     * clues for a 9x9 grid.
     */
    public void easy(){
        gameSeed = System.currentTimeMillis();
        int clues = randInt(gridSize*4, gridSize*5);    //Factor of gridSize used for scalability
        int numFilled = 0;
        while(numFilled != clues) {
            populateGrid();
            numFilled++;
        }
    }

    /**
     * Generates a puzzle of medium difficulty. For a 9x9 grid a puzzle will contain between 27 and 35 clues.
     */
    public void medium(){
        int clues = randInt(gridSize*3, gridSize*4-1);
        int numFilled = 0;
        while (numFilled != clues) {
            populateGrid();
            numFilled++;
        }
    }

    /**
     * Generates a puzzle of hard difficulty. A 9x9 grid will have between 18 and 26 clues.
     */
    public void hard(){
        int clues = randInt(gridSize*2, gridSize*3-1);
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
        int clues = randInt(gridSize*1, gridSize*2-1);
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
    private void populateGrid(){
        int rX = randInt(0,gridSize - 1);
        int rY = randInt(0,gridSize - 1);
        // If coordinates already contain a number then generate new coordinates
        if (grid[rY][rX].getValue() != 0) {
            populateGrid();
        } else {
            // Generate number to be entered into grid
            int value = randInt(1, gridSize);
            boolean invalid = false;
            // Check if the row and/or column already contains the generated number (duplicate)
            for (int i = 0; i < grid.length; i++) {
                if (grid[rY][i].getValue() == value) {
                    invalid = true;
                    break;
                } else if (grid[i][rX].getValue() == value) {
                    invalid = true;
                    break;
                }
            }
            // If the current generated number is a duplicate then a new number must be generated
            if (invalid){
                populateGrid();
            } else {
                // If the current generated number is valid then apply it to the grid
                grid[rY][rX].setValue(value);
                grid[rY][rX].setValueAsString(Integer.toString(value));
            }
        }
    }

    private int randInt(int min, int max){
        Random random = new Random();
        int value = random.nextInt((max - min) + 1) + min;
        return value;
    }

    private int randInt(int min, int max, long gameSeed){
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
    }

    public static void main(String[] args) {
        GameGrid gameGrid = new GameGrid(9);
        gameGrid.extreme();
        gameGrid.printGrid();
    }
}
