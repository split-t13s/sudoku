package dlx;

public class Node {

    Node up;
    Node down;
    Node left;
    Node right;
    ColumnNode header;
    int label;

    public Node (int label) {
        this.label = label;
        right = null;
        left = null;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public Node getUp() {
        return up;
    }

    public void setUp(Node up) {
        this.up = up;
    }

    public Node getDown() {
        return down;
    }

    public void setDown(Node down) {
        this.down = down;
    }

    public ColumnNode getHeader() {
        return header;
    }

    public void setHeader(ColumnNode header) {
        this.header = header;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }
}