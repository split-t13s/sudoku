package dlx;

public class ColumnNode extends Node{

    int size;   //number of nodes linked in to the column header

    public ColumnNode(int label) {
        super(label);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void incrementSize() {
        this.size++;
    }

    public void decrementSize() {
        this.size--;
    }
}
