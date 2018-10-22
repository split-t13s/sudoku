package dlx;

public class ConstraintMatrix {

    Node root;
    Node[] rows;
    ColumnNode[] columns;

    public ConstraintMatrix() {
    }

    /**
     * Inserts node at the end of the list.
     *
     * @param root  - the first node of the list
     * @param label
     * @return - new node with established links
     */
    public Node insert(Node root, int label) {
        Node newnode = new Node(label);
        newnode.setRight(root);
        newnode.setLeft(root.getLeft());
        newnode.getRight().setLeft(newnode);
        newnode.getLeft().setRight(newnode);
        return newnode;
    }

    /**
     * Remove node from list and update links
     *
     * @param node - the removed node
     */
    public void remove(Node node) {
        node.getRight().setLeft(node.getLeft());
        node.getLeft().setRight(node.getRight());
        node.setRight(null);
        node.setLeft(null);
    }

    /**
     * Remove node from list but keep links.
     * Used to remove constraints.
     *
     * @param node - the removed node
     */
    public void cover(Node node) {
        node.getRight().setLeft(node.getLeft());
        node.getLeft().setRight(node.getRight());
    }

    /**
     * Return a removed node to the list.
     *
     * @param node - the returned node
     */
    public void uncover(Node node) {
        node.getRight().setLeft(node);
        node.getLeft().setRight(node);
    }

    public Node getRoot() {
        return root;
    }

    /**
     * Create root node which signifies the start of the data.
     * @return - root node
     */
    public Node createRoot() {
        Node root = new Node(-1);
        root.setRight(root);
        root.setLeft(root);
        return root;
    }

    /**
     * Create a constraint column.
     * @param label - the label for the new constraint
     * @return - constraint node
     */
    public ColumnNode createHeader(int label, Node root) {
        ColumnNode header = new ColumnNode(label);
        header.setRight(root);
        header.setLeft(root.getLeft());
        header.getRight().setLeft(header);
        header.getLeft().setRight(header);
        header.setUp(header);
        header.setDown(header);
        header.setSize(0);
        return header;
    }

    /**
     * Inserts a candidate for a given constraint.
     * @param prev the previous constraint candidate in the row
     * @param header - the constraint
     * @return - the candidate with correct links
     */
    public Node createDetail(Node prev, ColumnNode header, int label) {
        Node newNode = new Node(label);
        if (prev != null) {
            newNode.setLeft(prev);
            newNode.setRight(prev.getRight());
            newNode.getLeft().setRight(newNode);
            newNode.getRight().setLeft(newNode);
        } else {
            newNode.setLeft(newNode);
            newNode.setRight(newNode);
        }
        newNode.setHeader(header);
        header.incrementSize();
        newNode.setDown(header);
        newNode.setUp(header.getUp());
        newNode.getUp().setDown(newNode);
        newNode.getDown().setUp(newNode);
        return newNode;
    }

    /**
     * Remove a row from the matrix.
     * @param rowIndex - the row to be removed
     */
    public void removeRow(int rowIndex, Node[] rows) {
        Node rowHead = rows[rowIndex];
        Node currNode = rowHead;
        do {
            if (currNode.getDown() != currNode) {
                currNode.getDown().setUp(currNode.getUp());
                currNode.getUp().setDown(currNode.getDown());
                currNode.setDown(currNode);
                currNode.setUp(currNode);
                currNode.getHeader().decrementSize();
            }
            currNode = currNode.getRight();
        } while (currNode != rowHead);
    }

    /**
     * Insert a removed row from the matrix.
     * @param rowIndex - the row to be reinserted.
     */
    public void reinsertRow(int rowIndex, Node[] rows) {
        Node rowHead = rows[rowIndex];
        Node currNode = rowHead;
        do {
            if (currNode.getDown() == currNode) {
                currNode.setDown(currNode.getHeader());
                currNode.setUp(currNode.getHeader().getUp());
                currNode.getDown().setUp(currNode);
                currNode.getUp().setDown(currNode);
                currNode.getHeader().incrementSize();
            }
            currNode = currNode.getRight();
        } while (currNode != rowHead);
    }
}
