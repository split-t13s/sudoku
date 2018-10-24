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

    private int randInt(int min, int max) {
        Random random = new Random();
        int value = random.nextInt((max - min) + 1) + min;
        return value;
    }
}
