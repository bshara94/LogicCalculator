package com.example.logiccalculator;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class Processor {
    private final int numAtoms;
    private final int numInterpretations;
    private final String rpnExpr;
    private final String theExpr;
    private final List<Character> atomsList;
    RPNConverter rpnConverter;
    int[] dist;

    public Processor(String theExpr ) {
        this.theExpr = theExpr;
        if (this.theExpr.trim().equals("")) {
            System.out.println("Error!");
        }
        this.rpnConverter = new RPNConverter(theExpr);
        rpnExpr = rpnConverter.process();
        this.atomsList = new AtomList().parseAtoms(rpnExpr);
        this.numAtoms = atomsList.size();
        this.numInterpretations = (int) Math.pow(2, numAtoms);
    }

    public void process(boolean printThruthTable) {
        if (this.theExpr.trim().equals("")) {
            System.out.println("Error!");
        }
        //Log.i("Processor","numAtoms number are:" + numAtoms );
        TruthTable t = new TruthTable(numAtoms);
        boolean[][] truthTable = t.getTruthTable();

        /* Deduction */
        if (rpnConverter.isDeduction()) {
            this.processDeduction(truthTable, printThruthTable);
        } else {
            /* Simple expression */
            this.processLogic(truthTable, printThruthTable);
        }
    }

    public void process(boolean printThruthTable, int initRank, int endRank) {
        TruthTable t = new TruthTable(numAtoms);
        boolean[][] truthTable = t.getTruthTable();
        /* Only the Logic mode can have print rank */
        this.processLogic(truthTable, printThruthTable, initRank, endRank);
    }

    public void processWithDistances() {
        TruthTable t = new TruthTable(numAtoms);
        boolean[][] truthTable = t.getTruthTable();
        RPNEvaluator rpnEvaluator = new RPNEvaluator(rpnExpr, atomsList, truthTable, theExpr);
        /* initRank-1: truthTable starts at 0 */
        boolean[] result = rpnEvaluator.processWithDistances(0, truthTable.length);
        boolean[][] models = rpnEvaluator.getModelsList();
        dist = new int[numInterpretations];
        /* Compute distances */
        for (int i = 0; i < numInterpretations; i++) {
            if ( !result[i] ) { /* False formula result - compute distance */
                int minor = numAtoms;   /* Assume all variables are different */
                /* Check for every changing atom */
                for (boolean[] model : models) {
                    int diff = 0;
                    for (int a = 0; a < numAtoms; a++) {
                        if (truthTable[i][a] != model[a]) {
                            diff++;
                        }
                    }
                    if (diff == 1) {
                        minor = 1;
                        break;
                    }
                    if (diff < minor) {
                        minor = diff;
                    }
                }
                dist[i] = minor;
            } else {
                dist[i] = 0;
            }

        }
        for (int i = 0; i < numInterpretations; i++) {
            for (int j = 0; j < numAtoms; j++) {
                System.out.print(truthTable[i][j] + " ");
            }
            System.out.print(" | " + result[i] + " | ");
            System.out.println("dist()" + dist[i]);
        }
    }

    public int[] getDistList() {
        return dist;
    }

    public void processLogic(boolean[][] truthTable, boolean printThruthTable) {
        RPNEvaluator rpnEvaluator = new RPNEvaluator(rpnExpr, atomsList, truthTable, theExpr);
        rpnEvaluator.process(printThruthTable, 0, truthTable.length);
    }

    private void processLogic(boolean[][] truthTable, boolean printThruthTable, int initRank, int endRank) {
        RPNEvaluator rpnEvaluator = new RPNEvaluator(rpnExpr, atomsList, truthTable, theExpr);
        /* initRank-1: truthTable starts at 0 */
        rpnEvaluator.process(printThruthTable, initRank-1, endRank);
    }

    public void processDeduction(boolean[][] truthTable, boolean printThruthTable) {
        List<String> premisesList = rpnConverter.getPremises();
        List<String> conclusionsList = rpnConverter.getConclusions();

        int numPremises = premisesList.size();
        boolean[][] premisesTruthTable = new boolean[this.numInterpretations][numPremises];
        /* Fill table with premises */
        int j = 0;
        int p = 1;
        for (String s : premisesList) {
            Constants.println("\nPremise #" + p++ + ":");
            Constants.println("~~~~~~~~~~~~");
            RPNConverter converter = new RPNConverter(s);
            String posfixS = converter.process();
            RPNEvaluator rpnEvaluator = new RPNEvaluator(posfixS, this.atomsList, truthTable, s);
            boolean[] result = rpnEvaluator.process(printThruthTable, 0, truthTable.length);
            premisesTruthTable[j] = result;
            j++;
        }

        /* Fill table with conclusions */
        int numConclusions = conclusionsList.size();
        boolean[][] conclusionsTruthTable = new boolean[this.numInterpretations][numConclusions];
        j = 0;
        p = 1;
        for (String s : conclusionsList) {
            Constants.println("\nConclusion #" + p + ":");
            Constants.println("~~~~~~~~~~~~~~~");
            RPNConverter converter = new RPNConverter(s);
            String posfixS = converter.process();
            RPNEvaluator rpnEvaluator = new RPNEvaluator(posfixS, this.atomsList, truthTable, s);
            boolean[] result = rpnEvaluator.process(printThruthTable, 0, truthTable.length);
            conclusionsTruthTable[j] = result;
            j++;
        }
        /* Print final table */
        Constants.println();
        Constants.println();
        Constants.println("Logical Entailment truth table:");
        Constants.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        /* Process line number spaces */
        String blanks = Constants.getBlanksLine(numInterpretations);
        int trimSpace = blanks.length()-1;
        /* Print atoms line */
        Constants.print(blanks + "   ");
        for (j = 0; j < this.numAtoms; j++) {
            Constants.print(this.atomsList.get(j) + " ");
        }
        /* Process blanks on each premise */
        int lenP = 0;
        List<String> blanksList = new ArrayList<String>();
        for (String s : premisesList) {
            lenP += s.length() + 1;
            blanksList.add( Constants.getBlanks( s.length()-1 ) );
        }

        /* Print premises list */
        Constants.print("| ");
        for (String s : premisesList) {
            Constants.print(s + " ");
        }

        Constants.print("| ");
        for (String s : conclusionsList) {
            Constants.print(s + " ");
        }
        Constants.println();
        /* Print line separator */
        Constants.print(blanks + "   ");
        Constants.printLineSeparator(numAtoms, lenP);
        Constants.print("| ");
        /* Hack: This works only for one solely conclusion */
        Constants.printlnSeparator(conclusionsList.get(0).length());

        /* Assume a valid deduction */
        boolean isModel = true;
        int models = 0;
        boolean premisasVerdaderas;
        boolean conclusionesVerdaderas;
        for (int i = 0; i < this.numInterpretations; i++) {
            if (i == 9) {
                blanks = blanks.substring(0, trimSpace--);
            }
            else if (i == 99) {
                blanks = blanks.substring(0, trimSpace--);
            }
            else if (i == 999) {
                blanks = blanks.substring(0, trimSpace--);
            }
            else if (i == 9999) {
                blanks = blanks.substring(0, trimSpace--);
            }
            Constants.printLineNumber(blanks, i+1);
            for (j = 0; j < this.numAtoms; j++) {
                Constants.printBoolean(truthTable[i][j]);
            }
            Constants.print("| ");
            premisasVerdaderas = true;
            for (j = 0; j < numPremises; j++) {
                if (premisesTruthTable[j][i]) {
                    Constants.printBoolean(true);
                } else {
                    Constants.printBoolean(false);
                    premisasVerdaderas = false;
                }
                Constants.print( blanksList.get(j) );
            }
            Constants.print("| ");
            for (j = 0; j < numConclusions; j++) {
                conclusionesVerdaderas = true;
                if (conclusionsTruthTable[j][i]) {
                    Constants.printBoolean(true);
                } else {
                    Constants.printBoolean(false);
                    conclusionesVerdaderas = false;
                }
                if (premisasVerdaderas && conclusionesVerdaderas) {
                    Constants.print(Constants.VISUAL_OK);
                    models++;
                } else if (premisasVerdaderas && !conclusionesVerdaderas) {
                    isModel = false;
                    Constants.print(Constants.VISUAL_ERR);
                }
            }
            Constants.println();
        }
        Constants.println();
        if (isModel) {
            Constants.println("LOGICAL ENTAILMENT HOLDS.");
            if (models == 1) {
                Constants.println("There is only 1 model (See " + Constants.VISUAL_OK + ")");
            } else if (models > 1) {
                Constants.println("There are " + models + " models (See " + Constants.VISUAL_OK + ")");
            } else {
                Constants.println("... But no models! Use Belief Merging approach");
            }
        } else {
            Constants.println("LOGICAL ENTAILMENT DOES NOT HOLD (See " + Constants.VISUAL_ERR + ")");
        }
    }

    public int getNumAtoms() {
        return numAtoms;
    }

    public int getNumInterpretations() {
        return numInterpretations;
    }

    public List<Character> getAtomsList() {
        return atomsList;
    }

}
