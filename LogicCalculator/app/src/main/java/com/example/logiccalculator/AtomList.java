package com.example.logiccalculator;

import java.util.ArrayList;
import java.util.List;

public class AtomList {

    public AtomList() {

    }

    /*************************************************************************/
    /*                                                                       */
    /*  Returns the list with different atoms of an expression               */
    /*                                                                       */
    /*  Example:                                                             */
    /*                                                                       */
    /*  expr = a ^ b -> ( c v b);                                            */
    /*  List<Character> atoms = parseAtoms(expr);                            */
    /*  [a, b, c]                                                            */
    /*                                                                       */
    /*                                                                       */
    /* @param expr The expression to evaluate                                */
    /* @return An list with all different atoms                              */
    /*                                                                       */
    /*************************************************************************/
    public List<Character> parseAtoms(String expr) {
        List<Character> lst = new ArrayList<Character>();
        for (int i = 0; i < expr.length(); i++) {
            char token = expr.charAt(i);
            boolean found;
            if (Constants.isVariable(token)) {
                found = false;
                for (char c : lst) {
                    if (token == c) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    lst.add(token);
                }
            }
        }
        return lst;
    }

}

