package latinsquare;

import dlx.ColumnNode;
import dlx.ConstraintMatrix;
import dlx.Node;

/**
 * A test class that will apply dlx solve to a latin square.
 */
public class LatinSquare {

    ConstraintMatrix constraintMatrix;
    ColumnNode[] colums;
    Node[] rows;
    Node root;

    public LatinSquare() {
        constraintMatrix = new ConstraintMatrix();
        colums = new ColumnNode[12];
        rows = new Node[8];
        root = constraintMatrix.createRoot();
    }

    public void createConstraints() {
        for (int i = 0; i < colums.length; i++) {
            colums[i] = constraintMatrix.createHeader(i, root);
        }
    }
}
