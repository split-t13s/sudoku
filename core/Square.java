package core;

public class Square {
    public boolean empty;
    public int row;
    public int col;
    public int number;

    public Square() {
        this.empty = true;
        this.row = 0;
        this.col = 0;
        this.number = 0;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
