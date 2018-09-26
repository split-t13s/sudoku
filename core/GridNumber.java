package core;

import java.util.HashSet;

public class GridNumber {

    public int value;
    public String valueAsString;
    public HashSet<Integer> candidates;     // Hashset used to ignore duplicates

    public GridNumber(){
        this.valueAsString = " ";
        this.value = 0;
        this.candidates = new HashSet<Integer>();
    }

    public GridNumber(int value){
        this.value = value;
        this.valueAsString = Integer.toString(value);
        this.candidates = new HashSet<Integer>();
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

    public void addCandidate(int candidate){
        candidates.add(candidate);
    }

    public void removeCandidate(int candidate){
        candidates.remove(candidate);
    }
}
