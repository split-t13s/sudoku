package tests;

import core.GridNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class GridNumberTest {

    GridNumber number;

    @BeforeEach
    public void setUp() {
        number = new GridNumber(5, 2, 3);
    }

    @Test
    public void getValue() {
        assertEquals(5, number.getValue());
    }

    @Test
    public void getValueAsString() {
        assertEquals("5", number.getValueAsString());
        GridNumber zero = new GridNumber(0 ,0);
        assertEquals(" ", zero.getValueAsString());
    }

    @Test
    public void setValue() {
        number.setValue(9);
        assertEquals(9, number.getValue());
    }

    @Test
    public void setValueAsString() {
        number.setValueAsString("9");
        assertEquals("9", number.getValueAsString());
        number.setValue(8);
        assertEquals("8", number.getValueAsString());
        number.setValue(0);
        assertEquals(" ", number.getValueAsString());
    }

    @Test
    public void addCandidate() {
        HashSet<Integer> candidateTest = new HashSet<Integer>();
        candidateTest.add(2);
        candidateTest.add(6);
        number.addCandidate(2);
        number.addCandidate(6);
        assertEquals(candidateTest, number.getCandidates());
    }

    @Test
    public void removeCandidate() {
        HashSet<Integer> candidateTest = new HashSet<Integer>();
        candidateTest.add(3);
        candidateTest.add(8);
        number.addCandidate(1);
        number.addCandidate(3);
        number.addCandidate(8);
        assertThat(number.getCandidates(), not(equalTo(candidateTest)));
        number.removeCandidate(1);
        assertEquals(candidateTest, number.getCandidates());
    }
}