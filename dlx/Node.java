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

    /**
     * Inserts node at the end of the list.
     * @param root - the first node of the list
     * @param label
     * @return - new node with established links
     */
    public Node insert(Node root, int label) {
        Node newnode = new Node(label);
        newnode.setNext(root);
        newnode.setPrev(root.getPrev());
        newnode.getNext().setPrev(newnode);
        newnode.getPrev().setNext(newnode);
        return newnode;
    }

    /**
     * Remove node from list and update links
     * @param node - the removed node
     */
    public void remove(Node node) {
        node.getNext().setPrev(node.getPrev());
        node.getPrev().setNext(node.getNext());
        node.setNext(null);
        node.setPrev(null);
    }

    /**
     * Remove node from list but keep links.
     * @param node - the removed node
     */
    public void cover(Node node) {
        node.getNext().setPrev(node.getPrev());
        node.getPrev().setNext(node.getNext());
    }

    /**
     * Return a removed node to the list.
     * @param node - the returned node
     */
    public void uncover(Node node) {
        node.getNext().setPrev(node);
        node.getPrev().setNext(node);
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
