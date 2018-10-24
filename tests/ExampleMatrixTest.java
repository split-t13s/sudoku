package tests;

import dlx.ColumnNode;
import dlx.ConstraintMatrix;
import dlx.Node;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExampleMatrixTest {

    static ConstraintMatrix constraintMatrix;
    static Node root;
    static Node[][] matrix;

    @BeforeAll
    public static void setUp() {
        constraintMatrix = new ConstraintMatrix();
        root = constraintMatrix.createRoot();
        matrix = exampleMatrix();
    }


    @Test
    public void removeRow() {
        ColumnNode[] columns = (ColumnNode[]) matrix[0];
        Node[] rows = matrix[1];
        int rowIndex = 1;
        Node currNode = rows[rowIndex];
        do {
            if (currNode.getDown() != currNode) {
                currNode.getDown().setUp(currNode.getUp());
                currNode.getUp().setDown(currNode.getDown());
                currNode.setDown(currNode);
                currNode.setUp(currNode);
                currNode.getHeader().decrementSize();
            }
            currNode = currNode.getRight();
        } while (currNode != rows[rowIndex]);
        Node rowOneScanner = rows[0];
        Node rowThreeScanner = rows[2];
        Node column = columns[0];
        do {
            assertEquals(rowThreeScanner, rowOneScanner.getDown());
            assertEquals(rowOneScanner, rowThreeScanner.getUp());
            assertEquals(2, ((ColumnNode) column).getSize());
            rowOneScanner = rowOneScanner.getRight();
            rowThreeScanner = rowThreeScanner.getRight();
        } while (rowOneScanner != rows[0]);
    }

    @Test
    public void reinsertRow() {
        ColumnNode[] columns = (ColumnNode[]) matrix[0];
        Node[] rows = matrix[1];
        int rowIndex = 1;
        constraintMatrix.removeRow(rowIndex, rows);
        Node currNode = rows[rowIndex];
        do {
            if (currNode.getDown() == currNode) {
                currNode.setDown(currNode.getHeader());
                currNode.setUp(currNode.getHeader().getUp());
                currNode.getDown().setUp(currNode);
                currNode.getUp().setDown(currNode);
                currNode.getHeader().incrementSize();
            }
            currNode = currNode.getRight();
        } while (currNode != rows[rowIndex]);
        // row moves to 'bottom' of matrix
        Node scanner = rows[rowIndex];
        Node column = columns[0];
        do {
            assertEquals(column, scanner.getDown());
            assertEquals(3, ((ColumnNode) column).getSize());
            column = column.getRight();
            scanner = scanner.getRight();
        } while (scanner != rows[rowIndex]);
    }

    @Test
    public void removeColumn() {
        ColumnNode[] columns = (ColumnNode[]) matrix[0];
        int columnIndex = 1;
        Node currNode = columns[columnIndex];
        do {
            constraintMatrix.cover(currNode);
            currNode = currNode.getDown();
        } while (currNode != columns[columnIndex]);

        Node scanner = columns[0].getDown();
        Node check = columns[2].getDown();
        do {
            assertEquals(check, scanner.getRight());
            scanner = scanner.getDown();
            check = check.getDown();
        } while (scanner != columns[0]);
    }

    @Test
    public void reinsertColumn() {
        ColumnNode[] columns = (ColumnNode[]) matrix[0];
        int columnIndex = 1;
        constraintMatrix.removeColumn(columnIndex, columns);
        Node currNode = columns[columnIndex];
        do {
            constraintMatrix.uncover(currNode);
            currNode = currNode.getDown();
        } while (currNode != columns[columnIndex]);
        Node scannerOne = columns[0];
        Node scannerTwo = columns[2];
        Node check = columns[1];
        do {
            assertEquals(check, scannerOne.getRight());
            assertEquals(check, scannerTwo.getLeft());
            scannerOne = scannerOne.getDown();
            scannerTwo = scannerTwo.getDown();
            check = check.getDown();
        } while (scannerOne != columns[0]);
    }

    private static Node[][] exampleMatrix() {
        ColumnNode[] columns = new ColumnNode[3];
        Node[] rows = new Node[3];
        for (int label = 0; label < 3; label++) {
            columns[label] = constraintMatrix.createHeader(label, root);
        }
        Node prevOne = null;
        Node prevTwo = null;
        Node prevThree = null;
        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                rows[i] = constraintMatrix.createDetail(prevOne, columns[i], i + 10);
                prevOne = rows[i];
                rows[i + 1] = constraintMatrix.createDetail(prevTwo, columns[i], i + 13);
                prevTwo = rows[i + 1];
                rows[i + 2] = constraintMatrix.createDetail(prevThree, columns[i], i + 16);
                prevThree = rows[i + 2];
            } else {
                prevOne = constraintMatrix.createDetail(prevOne, columns[i], i + 10);
                prevTwo = constraintMatrix.createDetail(prevTwo, columns[i], i + 13);
                prevThree = constraintMatrix.createDetail(prevThree, columns[i], i + 16);
            }
        }
        Node[][] matrix = {columns, rows};
        return matrix;
    }

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
        Node init = cur;
        do {
            System.out.print(cur.getLabel() + " ");
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
