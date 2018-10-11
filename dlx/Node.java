package dlx;

public class Node {

    Node prev;
    Node next;
    int label;

    public Node (int label) {
        this.label = label;
        next = null;
        prev = null;
    }

    public Node insert(Node root, int label) {
        Node newnode = new Node(label);
        newnode.setNext(root);
        newnode.setPrev(root.getPrev());
        newnode.getNext().setPrev(newnode);
        newnode.getPrev().setNext(newnode);
        return newnode;
    }

    public void remove(Node node) {
        node.getNext().setPrev(node.getPrev());
        node.getPrev().setNext(node.getNext());
        node.setNext(null);
        node.setPrev(null);
    }

    public Node getPrev() {
        return prev;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}
