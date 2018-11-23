package latinsquare;

import dlx.ColumnNode;
import dlx.ConstraintMatrix;
import dlx.Node;

/**
 * A test class that will apply dlx solve to a latin square.
 */
public class LatinSquare {

    ConstraintMatrix constraintMatrix;
    ColumnNode[] columns;
    Node[] rows;
    Node root;

    public LatinSquare() {
        constraintMatrix = new ConstraintMatrix();
        columns = new ColumnNode[12];
        rows = new Node[8];
        root = constraintMatrix.createRoot();
    }

    /**
     * Creates column headers
     */
    public void createConstraints() {
        for (int i = 0; i < columns.length; i++) {
            columns[i] = constraintMatrix.createHeader(i, root);
        }
    }

    /**
     * Creates constraint matrix and populates with nodes.
     * Each row matches exactly 3 constraints for Latin Square (one from each constraint type).
     */
    public void createCandidates() {
        int rowIndex = 0;
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 2; col++) {
                for (int num = 0; num < 2; num++) {
                    // Calculate constraints met by current candidate
                    int[] constraintsMet = new int[3];
                    constraintsMet[0] = row * 2 + col;
                    constraintsMet[1] = 4 + (row * 2 + num);
                    constraintsMet[2] = 8 + (col * 2 + num);
                    Node prev = null;   // Required to call createDetail()
                    for (int constraint = 0; constraint < columns.length; constraint++) {
                        if (constraint == constraintsMet[0] || constraint == constraintsMet[1] || constraint == constraintsMet[2]) {
                            prev = constraintMatrix.createDetail(prev, columns[constraint], 1);
                        } else {
                            prev = constraintMatrix.createDetail(prev, columns[constraint], 0);
                        }
                    }
                    rows[rowIndex] = prev.getRight();
                    rowIndex++;
                }
            }
        }
        //printMatrix(columns);
    }

    public void setupSolve() {
        int[] solution = new int[4];
        boolean[] constraintsMet = new boolean[12];
        solve(solution, constraintsMet);
    }

    public void solve(int[] solution, boolean[] constraintsMet){
        int currConstraint = -1;
        for (int i = 0; i < constraintsMet.length; i++) {
            if (!constraintsMet[i]){
                currConstraint = i;
                break;
            }
        }
        if (currConstraint == -1) {
            // done
        } else {
            System.out.println(currConstraint);
        }
    }

    public static void main (String[] args) {
        LatinSquare ls = new LatinSquare();
        ls.createConstraints();
        ls.createCandidates();
        ls.setupSolve();
    }

    @SuppressWarnings("Duplicates")
    /**
     * Relies on columns[0]
     * @param columns
     */
    private void printMatrix(ColumnNode[] columns) {
        // print columns
        Node col = columns[0];
        do {
            System.out.print(col.getLabel() + "  ");
            col = col.getRight();
        } while (col != root);
        System.out.println();
        // print rows
        Node cur = columns[0].getDown();
        if (cur != columns[0]) {    // Check for rows
            Node init = cur;
            do {
                System.out.print(cur.getLabel() + "  ");
                if (cur.getRight() == init) {
                    cur = init.getDown();
                    init = cur;
                    System.out.println();
                } else {
                    cur = cur.getRight();
                }
            } while (cur != columns[0]);
            System.out.println();
        }
    }
}
