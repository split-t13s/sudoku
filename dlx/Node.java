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

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }
}
