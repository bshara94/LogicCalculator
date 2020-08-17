package com.example.logiccalculator;

public class TruthTable {
    int columns;
    int rows;
    boolean[][] truthTable;

    /*************************************************************************/
    /*                            CONSTRUCTOR                                */
    /*                                                                       */
    /*  Creates a new truth  table via #createTruthTable()                   */
    /*                                                                       */
    /*  Example: a ^ b v c (3 atoms)                                         */
    /*                                                                       */
    /*  boolean[][] threeAtomsTable = new TruthTable(3).getgetTruthTable();  */
    /*                                                                       */
    /* @param numAtoms Number of atoms in formula                            */
    /*                                                                       */
    /* @see #createTruthTable()                                              */
    /*                                                                       */
    /*************************************************************************/
    public TruthTable(int numAtoms) {
        this.columns = numAtoms;
        truthTable = this.createTruthTable();
    }

    /*************************************************************************/
    /*                                                                       */
    /* Returns the boolean matrix representing the truth table               */
    /*                                                                       */
    /* @see #createTruthTable()                                              */
    /*                                                                       */
    /*************************************************************************/
    public boolean[][] getTruthTable() {
        return truthTable;
    }

    /*************************************************************************/
    /*                                                                       */
    /* Creates the boolean matrix representing the truth table depending on  */
    /* the number of atoms in constructor                                    */
    /*                                                                       */
    /* @see #getTruthTable()                                                 */
    /*                                                                       */
    /*************************************************************************/
    private boolean[][] createTruthTable() {
        boolean[][] interpretMatrix;
        if (columns == 1) {
            this.rows = 2;
            interpretMatrix = new boolean[rows][columns];
            interpretMatrix[0][0] = true;
            interpretMatrix[1][0] = false;
            return interpretMatrix;
        }
        this.rows = 4;
        interpretMatrix = new boolean[rows][columns];
        interpretMatrix[0][0] = true;
        interpretMatrix[1][0] = true;
        interpretMatrix[2][0] = false;
        interpretMatrix[3][0] = false;

        interpretMatrix[0][1] = true;
        interpretMatrix[1][1] = false;
        interpretMatrix[2][1] = true;
        interpretMatrix[3][1] = false;

        if (columns == 2) {
            return interpretMatrix;
        }

        for (int i = 3; i <= columns; i++) {
            interpretMatrix = createNewTable(interpretMatrix, i);
        }
        this.rows = interpretMatrix.length;
        return interpretMatrix;
    }

    private boolean[][] createNewTable(boolean[][] tempMatrix, int cols) {
        int size = (int) Math.pow(2, cols);
        int half = size / 2;

        boolean[][] matrix = new boolean[size][cols];
        // Create truth table of fist atom
        for (int i = 0; i < half; i++) {
            matrix[i][0] = true;
        }
        for (int i = half; i < size; i++) {
            matrix[i][0] = false;
        }

        // Copy temporal table to matrix
        for (int i = 0; i < half; i++) {
            for (int j = 0; j < cols - 1; j++) {
                matrix[i][j + 1] = tempMatrix[i][j];
            }
        }

        // Copy temporal table to matrix (again)
        for (int i = 0; i < half; i++) {
            for (int j = 0; j < cols - 1; j++) {
                matrix[i + half][j + 1] = tempMatrix[i][j];
            }
        }
        return matrix;
    }

}

