package com.example.logiccalculator;

import java.util.List;
import java.util.Stack;


public class RPNEvaluator {
    private final String rpnExpr;
    private final String theExpr;
    private final List<Character> atomsList;
    private final boolean[][] truthTable;
    private final int numAtoms;
    private final int numInterpretations;
    private String toString;
    private int numModels;
    private boolean[][] modelsList;

    public RPNEvaluator(String rpnExpr, List<Character> atomsList, boolean[][] truthTable, String theExpr) {
        this.toString = "";
        this.rpnExpr = rpnExpr;
        this.atomsList = atomsList;
        this.truthTable = truthTable;
        this.theExpr = theExpr;
        numAtoms = atomsList.size();
        numInterpretations = truthTable.length;
    }

    public boolean processInterpretation(boolean[] interpretation) {
        Stack<Boolean> stack = new Stack<Boolean>();
        /* Analize amos tokens one by one */
        for (int i = 0; i < rpnExpr.length(); i++) {
            char token = rpnExpr.charAt(i);
            if (Constants.isVoid(token)) {
                continue;
            }
            if (Constants.isOperator(token)) {
                /* If negation, pop an operand from pile */
                if (token == Constants.NOT) {
                    boolean atom = stack.pop();
                    boolean result = !atom;
                    stack.push(result);
                    continue;
                }
                /* If other operator, por two operands */
                boolean atom2 = stack.pop();
                boolean atom1 = stack.pop();
                // Do logical operation
                if (token == Constants.AND) {
                    boolean result = atom1 && atom2;
                    stack.push(result);
                } else if (token == Constants.OR) {
                    boolean result = atom1 || atom2;
                    stack.push(result);
                } else if (token == Constants.IMPLIES) {
                    if ((atom1 == true) && (atom2 == false)) {
                        stack.push(false);
                    } else {
                        stack.push(true);
                    }
                } else if (token == Constants.BIMPLIES) {
                    if (atom1 == atom2) {
                        stack.push(true);
                    } else {
                        stack.push(false);
                    }
                }
            } else { /* It is a variable */
                int pos = findAtom(token);
                boolean atom = interpretation[pos];
                // Push variable to the stack */
                stack.push(atom);
            }
        }
        /* In the stack there is always the expression result */
        boolean result = stack.pop();
        return result;
    }

    private int findAtom(char atom) {
        int pos = 0;
        for (char c : atomsList) {
            if (c == atom) {
                return pos;
            }
            pos++;
        }
        return -1;
    }

    public boolean[] process(boolean printTruthTable, int initRank, int endRank) {
        /*
         * Print truth table:
         *
         * p      q      | ¬p ^ q
         * ----------------------
         * true   true   | false
         * true   false  | false
         * false  true   | true
         * false  false  | false
         *
         *
         * Better case:
         *
         *     p q | ¬p ^ q
         *     ----|-------
         *  1) 1 1 | 0
         *  2) 1 0 | 0
         *  3) 0 1 | 1
         *  4) 0 0 | 0
         *
         */
        numModels = 0;
        boolean[] result = new boolean[numInterpretations];
        boolean[][] tempModelsList = new boolean[numInterpretations][];
        //mainGUI.progressBar.setMinimum(1);
        if ( printTruthTable ) {
            /* Process line number spaces */
            String blanks = Constants.getBlanksLine(numInterpretations);
            int trimSpace = blanks.length()-1;
            /* Print atoms line */
            Constants.print(blanks + "   ");
            for (int j = 0; j < numAtoms; j++) {
                Constants.print(atomsList.get(j) + " ");
            }
            /* Print expression */
            Constants.print("| ");
            Constants.println(theExpr);
            /*  Print line separator */
            Constants.print(blanks + "   ");
            Constants.printlnLineSeparator(numAtoms, theExpr.length());
            /* Print truth table */
            for (int i = initRank; i < endRank; i++) {
                if (i == 9) {
                    blanks = blanks.substring(0, trimSpace--);
                } else if (i == 99) {
                    blanks = blanks.substring(0, trimSpace--);
                } else if (i == 999) {
                    blanks = blanks.substring(0, trimSpace--);
                } else if (i == 9999) {
                    blanks = blanks.substring(0, trimSpace--);
                } else if (i == 99999) {
                    blanks = blanks.substring(0, trimSpace--);
                }
                /* i+1: truthTalble starts at 0 */
                Constants.printLineNumber(blanks, i+1);
                for (int j = 0; j < numAtoms; j++) {
                    Constants.printBoolean(truthTable[i][j]);
                }
                /* Print result of interpretation i */
                Constants.print("| ");
                result[i] = processInterpretation(truthTable[i]);
                Constants.printBoolean(result[i]);
                if (result[i]) {    /* Is model */
                    Constants.print(Constants.VISUAL_OK);
                    tempModelsList[numModels] = truthTable[i];
                    numModels++;
                }
                Constants.println();
            }
        } else {    /* printTruthTable == false */
            for (int i = 0; i < numInterpretations; i++) {
                result[i] = processInterpretation(truthTable[i]);
                if (result[i]) {    /* Is model */
                    tempModelsList[numModels] = truthTable[i];
                    numModels++;
                }
            }
        }

        Constants.println("------------");
        //Constants.println("Models: " + numModels + " ");
        if(Result.OptionNumber ==4)
            if(numModels<numInterpretations)
            Constants.println("There is no equivalence");
        else
            Constants.println("There is equivalence");
        modelsList = new boolean[numModels][];
        System.arraycopy(tempModelsList, 0, modelsList, 0, numModels);
        return result;
    }

    public boolean[] processWithDistances(int initRank, int endRank) {
        numModels = 0;
        toString = "";
        boolean[] result = new boolean[numInterpretations];
        boolean[][] tempModelsList = new boolean[numInterpretations][];
        for (int i = initRank; i < endRank; i++) {
            result[i] = processInterpretation(truthTable[i]);
            if (result[i]) {    /* Is model */
                tempModelsList[numModels] = truthTable[i];
                numModels++;
            }
        }
        modelsList = new boolean[numModels][];
        System.arraycopy(tempModelsList, 0, modelsList, 0, numModels);
        return result;
    }

    public boolean[][] getModelsList() {
        return modelsList;
    }

    public int getNumModels() {
        return numModels;
    }

    @Override
    public String toString() {
        return toString;
    }

}
