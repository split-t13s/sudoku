package dlx;

public class LinkedList {

    Node root;

    public LinkedList() {
        root = new Node(0);
        root.setNext(root);
        root.setPrev(root);
    }

    public Node getRoot() {
        return root;
    }
}
