package latinsquare;

import dlx.ColumnNode;
import dlx.ConstraintMatrix;
import dlx.Node;

import java.util.HashMap;
import java.util.HashSet;

/**
 * A test class that will apply dlx solve to a latin square.
 */
public class LatinSquare {

    ConstraintMatrix constraintMatrix;
    ColumnNode[] columns;
    Node[] rows;
    Node root;

    public LatinSquare() {
        constraintMatrix = new ConstraintMatrix();
        columns = new ColumnNode[12];
        rows = new Node[8];
        root = constraintMatrix.createRoot();
    }

    /**
     * Creates column headers
     */
    public void createConstraints() {
        for (int i = 0; i < columns.length; i++) {
            columns[i] = constraintMatrix.createHeader(i, root);
        }
    }

    /**
     * Creates constraint matrix and populates with nodes.
     * Each row matches exactly 3 constraints for Latin Square (one from each constraint type).
     */
    public void createCandidates() {
        int rowIndex = 0;
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 2; col++) {
                for (int num = 0; num < 2; num++) {
                    // Calculate constraints met by current candidate
                    int[] constraintsMet = new int[3];
                    constraintsMet[0] = row * 2 + col;
                    constraintsMet[1] = 4 + (row * 2 + num);
                    constraintsMet[2] = 8 + (col * 2 + num);
                    Node prev = null;   // Required to call createDetail()
                    for (int constraint = 0; constraint < columns.length; constraint++) {
                        if (constraint == constraintsMet[0] || constraint == constraintsMet[1] || constraint == constraintsMet[2]) {
                            prev = constraintMatrix.createDetail(prev, columns[constraint], 1);
                        } else {
                            prev = constraintMatrix.createDetail(prev, columns[constraint], 0);
                        }
                    }
                    rows[rowIndex] = prev.getRight();
                    rowIndex++;
                }
            }
        }
        printMatrix(columns);
    }

    public void setupSolve() {
        HashMap<Integer, Node> rowMap = new HashMap<Integer, Node>();
        int rowIndex = 0;
        for (Node rowHead : rows) {
            rowMap.put(rowIndex, rowHead);
            rowIndex++;
        }
        int[] solution = new int[4];
        boolean[] constraintsMet = new boolean[12];
        int nextIndex = 0;
        solve(rowMap, solution, constraintsMet, nextIndex);
    }

    public void solve(HashMap<Integer, Node> rowMap, int[] solution, boolean[] constraintsMet, int nextIndex){
        int currConstraint = -1;
        for (int i = 0; i < constraintsMet.length; i++) {
            if (!constraintsMet[i]){
                currConstraint = i;
                break;
            }
        }
        if (currConstraint == -1) {
            // done
        } else {
            //currConstraint = 2;       // for testing purposes
            rowLoop: for (Integer rowIndex : rowMap.keySet()) {
            //rowLoop: for (int rowIndex = 0; rowIndex < rowMap.size(); rowIndex++) {
                Node currNode = rowMap.get(rowIndex);
                boolean exit = false;
                do {
                    if (currNode.getHeader().getLabel() == currConstraint && currNode.getLabel() == 1) {
                        System.out.println("row: " + rowIndex + " meets constraint: " + currConstraint);
                        solution[nextIndex] = rowIndex;
                        ++nextIndex;
                        int[] constraintsMetIndices = findConstraintsMet(rowIndex);
                        coverRows(rowMap, constraintsMetIndices);
                        markConstraintsMet(constraintsMet, constraintsMetIndices);
                        solve(rowMap, solution, constraintsMet, nextIndex);
                        exit = true;
                        break rowLoop;
                    } else {
                        currNode = currNode.getRight();
                        // if no row meets constraint
                        if (currNode == rowMap.get(rowMap.size() - 1)) {
                            System.out.println("no valid row must backtrack");
                            // check next row is within range
                            if (solution[--nextIndex] + 1 < rowMap.size()) {
                                //currNode = rows[solution[--nextIndex] + 1];
                                rowIndex = solution[--nextIndex]; //+ 1;
                                solution[--nextIndex] = 0;
                                exit = true;
                            } else {
                                // no solution
                            }
                        }
                    }
                } while (currNode != rowMap.get(rowIndex) && !exit);
            }
        }
    }

    private boolean[] markConstraintsMet(boolean[] constraintsMet, int[] constraintsMetIndices) {
        for (int index : constraintsMetIndices) {
            constraintsMet[index] = true;
        }
        return constraintsMet;
    }

    private int[] findConstraintsMet(int rowIndex) {
        int[] constraintsMet = new int[3];
        int index = 0;
        Node rowNode = rows[rowIndex];
        do {
            if (rowNode.getLabel() == 1) {
                constraintsMet[index] = rowNode.getHeader().getLabel();
                index++;
            }
            rowNode = rowNode.getRight();
        } while (rowNode != rows[rowIndex]);
        return constraintsMet;
    }

    /**
     * For a given row, finds all constraints met by that row and all subsequent rows that also meet those constraints.
     * Indices for these rows are stored so that the rows can be removed from the matrix.
     * @param rowIndex - index for the head node of the row to be removed
     */
    private HashMap<Integer, Node> coverRows(HashMap<Integer, Node> rowMap, int[] constraintsMetIndices) {
        // Find other rows that meet the constraints of the given row
        HashSet<Integer> rowsToRemove = new HashSet<Integer>();
        for (int constraint : constraintsMetIndices) {
            ColumnNode header = columns[constraint];
            Node colNode = header.getDown();
            int currRowIndex = 0;
            do {
                if (colNode.getLabel() == 1) {
                    rowsToRemove.add(currRowIndex);
                    colNode = colNode.getDown();
                    currRowIndex++;
                } else {
                    colNode = colNode.getDown();
                    currRowIndex++;
                }
            } while (colNode != header);
        }

        // Remove rows from matrix
        for (Integer row : rowsToRemove) {
            constraintMatrix.removeRow(row, rows);
            rowMap.remove(row);
        }
        printMatrix(columns);
        return rowMap;
    }

    public static void main (String[] args) {
        LatinSquare ls = new LatinSquare();
        ls.createConstraints();
        ls.createCandidates();
        ls.setupSolve();
    }

    @SuppressWarnings("Duplicates")
    /**
     * Relies on columns[0]
     * @param columns
     */
    private void printMatrix(ColumnNode[] columns) {
        // print columns
        Node col = columns[0];
        do {
            System.out.print(col.getLabel() + "  ");
            col = col.getRight();
        } while (col != root);
        System.out.println();
        // print rows
        Node cur = columns[0].getDown();
        if (cur != columns[0]) {    // Check for rows
            Node init = cur;
            do {
                System.out.print(cur.getLabel() + "  ");
                if (cur.getRight() == init) {
                    cur = init.getDown();
                    init = cur;
                    System.out.println();
                } else {
                    cur = cur.getRight();
                }
            } while (cur != columns[0]);
            System.out.println();
        }
    }
}
