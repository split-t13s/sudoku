package tests;


import dlx.ColumnNode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import dlx.Node;
import dlx.ConstraintMatrix;


public class ConstraintMatrixTest {

    static ConstraintMatrix constraintMatrix;
    static Node root;

    @BeforeAll
    public static void setUp() {
        constraintMatrix = new ConstraintMatrix();
        root = constraintMatrix.createRoot();
    }

    @Test
    public void insert() {
        for (int label = 1; label <= 3; label++) {
            Node newNode = new Node(label);
            newNode.setRight(root);
            newNode.setLeft(root.getLeft());
            newNode.getRight().setLeft(newNode);
            newNode.getLeft().setRight(newNode);
        }
        int labelCheck = 1;
        for (Node currNode = root.getRight(); currNode != root; currNode = currNode.getRight(), labelCheck++) {
            assertEquals(labelCheck, currNode.getLabel());
        }
    }

    @Test
    public void remove() {
        Node[] nodes = new Node[4];
        nodes[0] = root;
        for (int label = 1; label <= 3; label++) {
            nodes[label] = constraintMatrix.insert(root, label);
        }
        int index = randInt(1, 3);
        Node toBeRemoved = nodes[index];
        if (toBeRemoved == root) {
            root = toBeRemoved.getRight();
        }
        toBeRemoved.getRight().setLeft(toBeRemoved.getLeft());
        toBeRemoved.getLeft().setRight(toBeRemoved.getRight());
        toBeRemoved.setRight(null);
        toBeRemoved.setLeft(null);
        for (Node currNode = root.getRight(); currNode != root; currNode = currNode.getRight()) {
            assertThat(index, not(equalTo(currNode.getLabel())));
        }
    }

    @Test
    public void covering() {
        Node[] nodes = new Node[4];
        nodes[0] = root;
        for (int label = 0; label < 3; label++) {
            nodes[label] = constraintMatrix.insert(root, label);
        }
        int index = randInt(1, 3);
        Node toBeCovered = nodes[index];
        toBeCovered.getRight().setLeft(toBeCovered.getLeft());
        toBeCovered.getLeft().setRight(toBeCovered.getRight());
        for (Node currNode = root.getRight(); currNode != root; currNode = currNode.getRight()) {
            assertThat(index, not(equalTo(currNode.getLabel())));
        }
        toBeCovered.getRight().setLeft(toBeCovered);
        toBeCovered.getLeft().setRight(toBeCovered);
        int labelCheck = 0;
        for (Node currNode = root.getRight(); currNode != root; currNode = currNode.getRight(), labelCheck++) {
            assertEquals(labelCheck, currNode.getLabel());
        }
    }

    @Test
    public void createHeader() {
        for (int label = 0; label < 3; label++) {
            ColumnNode column = new ColumnNode(label);
            column.setRight(root);
            column.setLeft(root.getLeft());
            column.getRight().setLeft(column);
            column.getLeft().setRight(column);
            column.setUp(column);
            column.setDown(column);
            column.setSize(0);
        }
        int labelCheck = 0;
        for (Node currNode = root.getRight(); currNode != root; currNode = currNode.getRight(), labelCheck++) {
            assertEquals(labelCheck, currNode.getLabel());
        }
    }

    @Test
    public void createDetail() {
        ColumnNode[] columns = new ColumnNode[3];
        for (int label = 0; label < 3; label++) {
            columns[label] = constraintMatrix.createHeader(label, root);
        }
        Node prev = null;
        for (int i = 0; i < 3; i++) {
            Node newNode = new Node(i + 10);
            ColumnNode header = columns[i];
            if (prev == null) {
                newNode.setLeft(newNode);
                newNode.setRight(newNode);
            } else {
                newNode.setLeft(prev);
                newNode.setRight(prev.getRight());
                newNode.getLeft().setRight(newNode);
                newNode.getRight().setLeft(newNode);
            }
            newNode.setHeader(header);
            header.incrementSize();
            newNode.setDown(header);
            newNode.setUp(header.getUp());
            newNode.getUp().setDown(newNode);
            newNode.getDown().setUp(newNode);
            prev = newNode;
            assertThat(header, is(newNode.getUp()));
            assertThat(header, is(newNode.getHeader()));
        }
        ColumnNode firstConstraint = columns[0];
        Node currNode = firstConstraint.getDown();
        int detailCheck = 10;
        int columnCheck = 0;
        do {
            assertEquals(detailCheck, currNode.getLabel());
            assertEquals(columnCheck, currNode.getHeader().getLabel());
            detailCheck++;
            columnCheck++;
            currNode = currNode.getRight();
        } while (currNode != firstConstraint.getDown());
    }

    @Test
    public void removeRow() {
        // set up matrix
        Node[][] matrix = exampleMatrix();
        ColumnNode[] columns = (ColumnNode[]) matrix[0];
        Node[] rows = matrix[1];
        // end of set up
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
        // set up matrix
        Node[][] matrix = exampleMatrix();
        ColumnNode[] columns = (ColumnNode[]) matrix[0];
        Node[] rows = matrix[1];
        // end of set up
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
        Node[][] matrix = exampleMatrix();
        ColumnNode[] columns = (ColumnNode[]) matrix[0];

    }

    private Node[][] exampleMatrix() {
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
                rows[i+1] = constraintMatrix.createDetail(prevTwo, columns[i], i + 13);
                prevTwo = rows[i+1];
                rows[i+2] = constraintMatrix.createDetail(prevThree, columns[i], i + 16);
                prevThree = rows[i+2];
            } else {
                prevOne = constraintMatrix.createDetail(prevOne, columns[i], i + 10);
                prevTwo = constraintMatrix.createDetail(prevTwo, columns[i], i + 13);
                prevThree = constraintMatrix.createDetail(prevThree, columns[i], i + 16);
            }
        }
        Node[][] matrix = {columns, rows};
        return matrix;
    }

    private int randInt(int min, int max) {
        Random random = new Random();
        int value = random.nextInt((max - min) + 1) + min;
        return value;
    }
}