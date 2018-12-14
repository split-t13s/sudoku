package core;

import java.util.HashSet;

public class GridNumber {

    private int value;
    private String valueAsString;
    private HashSet<Integer> candidates;     // Hashset used to ignore duplicates
    private int row;
    private int col;

    public GridNumber(int row, int col){
        this.valueAsString = " ";
        this.value = 0;
        this.candidates = new HashSet<Integer>();
        this.row = row;
        this.col = col;
    }

    public GridNumber(int value, int row, int col){
        this.value = value;
        this.valueAsString = Integer.toString(value);
        this.candidates = new HashSet<Integer>();
        this.row = row;
        this.col = col;
    }

    public int getValue() {
        return value;
    }

    public String getValueAsString() {
        return valueAsString;
    }

    public HashSet<Integer> getCandidates() {
        return candidates;
    }

    public void setValue(int value) {
        this.value = value;
        if (value == 0){
            setValueAsString(" ");
        } else {
            setValueAsString(Integer.toString(value));
        }
    }

    public void setValueAsString(String sValue) {
        this.valueAsString = sValue;
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

    public void addCandidate(int candidate){
        candidates.add(candidate);
    }

    public void removeCandidate(int candidate){
        candidates.remove(candidate);
    }
}
