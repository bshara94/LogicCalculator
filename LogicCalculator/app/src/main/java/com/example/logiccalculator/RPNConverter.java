package com.example.logiccalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;


public class RPNConverter {

    private final String expr;
    public List<String> premises;
    public List<String> conclusions;
    private boolean deduction = false;

    public RPNConverter(String expr) {
        this.expr = expr;

        /* If there is no conclusion then is a simple evaluation */
        int pos = expr.indexOf(Constants.VISUAL_DEDUCTION);
        if (pos > 0) {
            deduction = true;
            String first = expr.substring(0, pos);
            String last = expr.substring(pos + 1);
            /* Get premises */
            StringTokenizer st = new StringTokenizer(first, ",");
            premises = new ArrayList<String>();
            while (st.hasMoreTokens()) {
                premises.add(st.nextToken());
            }
            /* Get conclusions */
            st = new StringTokenizer(last, ",");
            conclusions = new ArrayList<String>();
            while (st.hasMoreTokens()) {
                conclusions.add(st.nextToken());
            }
        }
    }

    public boolean isDeduction() {
        return deduction;
    }

    public List<String> getPremises() {
        return premises;
    }

    public List<String> getConclusions() {
        return conclusions;
    }

    public String process() {
        Stack<Character> stack = new Stack<Character>();
        String result = "";
        /* Analize tokens one by one */
        for (int i = 0; i < expr.length(); i++) {
            char token = expr.charAt(i);
            if (Constants.isVoid(token)) {              /* Blanks */
                continue;
            } else  if (token == '(') {                 /* This parenthesis always to the stack */
                stack.push(token);
            } else if (token == ')') {                  /* This parenthesis is a special case */
                result = processParenthesis(result, stack);
            } else if ( Constants.isOperator(token) ) { /* Check operator priority */
                while (!stack.isEmpty()) {
                    char top = stack.peek();            // Check if top of the stack has an operator with higher priority
                    if (Constants.isDEDUCTION(token) || Constants.isSEPARATOR(token)) { /* Delete separtors and deduction symbols */
                        stack.pop();
                        continue;
                    }
                    if (Constants.isNOT(token)) {       /* Logical operatos */
                        if (top == Constants.NOT) {
                            top = stack.pop();
                            result += top;
                        } else {
                            break;
                        }
                    } else if (Constants.isAND(token)) {
                        if (Constants.isAND(top) || Constants.isNOT(top)) {
                            top = stack.pop();
                            result += top;
                        } else;
                        break;
                    } else if (Constants.isOR(token)) {
                        if (Constants.isOR(top) || Constants.isAND(top) || Constants.isNOT(top)) {
                            top = stack.pop();
                            result += top;
                        } else {
                            break;
                        }
                    } else if (Constants.isIMPLIES(token)) {
                        if (Constants.isIMPLIES(top) || Constants.isOR(top) || Constants.isAND(top) || Constants.isNOT(top)) {
                            top = stack.pop();
                            result += top;
                        } else {
                            break;
                        }
                    } else if (Constants.isBIMPLIES(token)) { /* Less priority operator */
                        if (Constants.isBIMPLIES(top) || Constants.isIMPLIES(top) || Constants.isOR(top) || Constants.isAND(top) || Constants.isNOT(top)) {
                            top = stack.pop();
                            result += top;
                        } else {
                            break;
                        }
                    }
                }
                stack.push(token);
            } else {
                result += token;
            }
        }
        // At the end, pop all tokens from the pile
        while (!stack.isEmpty()) {
            result += stack.pop();
        }
        //Constants.debugln("RPN Expression: " + result);
        //Constants.debugln();
        return result;
    }

    private String processParenthesis(String result, Stack<Character> stack) {
        while (!stack.isEmpty()) {
            char top = stack.pop();
            // No left parenthesis in the result
            if (top == '(') {
                return result;
            }
            result += top;
        }
        return result;
    }

}
