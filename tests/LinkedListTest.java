package tests;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import java.util.Random;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import dlx.Node;
import dlx.LinkedList;


public class LinkedListTest {

    static LinkedList linkedList;
    static Node root;

    @BeforeAll
    public static void setUp() {
        linkedList = new LinkedList();
        root = linkedList.getRoot();
    }

    @Test
    public void insert() {
       for (int label = 1; label <= 3; label++) {
            Node newNode = new Node(label);
            newNode.setNext(root);
            newNode.setPrev(root.getPrev());
            newNode.getNext().setPrev(newNode);
            newNode.getPrev().setNext(newNode);
        }
        int labelCheck = 1;
        for (Node currNode = root.getNext(); currNode != root; currNode = currNode.getNext(), labelCheck++) {
            assertEquals(labelCheck, currNode.getLabel());
        }
    }

    @Test
    public void remove() {
        Node[] nodes = new Node[4];
        nodes[0] = root;
        for (int label = 1; label <= 3; label++) {
            nodes[label] = linkedList.insert(root, label);
        }
        int index = randInt(1, 3);
        Node toBeRemoved = nodes[index];
        if (toBeRemoved == root) {
            root = toBeRemoved.getNext();
        }
        toBeRemoved.getNext().setPrev(toBeRemoved.getPrev());
        toBeRemoved.getPrev().setNext(toBeRemoved.getNext());
        toBeRemoved.setNext(null);
        toBeRemoved.setPrev(null);
        for (Node currNode = root.getNext(); currNode != root; currNode = currNode.getNext()) {
            assertThat(index, not(equalTo(currNode.getLabel())));
        }
    }

    @Test
    public void covering() {
        Node[] nodes = new Node[4];
        nodes[0] = root;
        for (int label = 1; label <= 3; label++) {
            nodes[label] = linkedList.insert(root, label);
        }
        int index = randInt(1, 3);
        Node toBeCovered = nodes[index];
        toBeCovered.getNext().setPrev(toBeCovered.getPrev());
        toBeCovered.getPrev().setNext(toBeCovered.getNext());
        for (Node currNode = root.getNext(); currNode != root; currNode = currNode.getNext()) {
            assertThat(index, not(equalTo(currNode.getLabel())));
        }
        toBeCovered.getNext().setPrev(toBeCovered);
        toBeCovered.getPrev().setNext(toBeCovered);
        int labelCheck = 1;
        for (Node currNode = root.getNext(); currNode != root; currNode = currNode.getNext(), labelCheck++) {
            assertEquals(labelCheck, currNode.getLabel());
        }
    }

    private int randInt(int min, int max) {
        Random random = new Random();
        int value = random.nextInt((max - min) + 1) + min;
        return value;
    }
}
