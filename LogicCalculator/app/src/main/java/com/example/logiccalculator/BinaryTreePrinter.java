package com.example.logiccalculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BinaryTreePrinter {

    public static void printNode(TokenTreeNode root) {
        int maxLevel = BinaryTreePrinter.maxLevel(root);
        printNodeInternal(Collections.singletonList(root), 1, maxLevel);
    }

    private static void printNodeInternal(List<TokenTreeNode> nodes, int level, int maxLevel) {
        if (nodes.isEmpty() || BinaryTreePrinter.isAllElementsNull(nodes)) {
            return;
        }
        int floor = maxLevel - level;
        int endgeLines = (int) Math.pow(2, (Math.max(floor - 1, 0)));
        int firstSpaces = (int) Math.pow(2, (floor)) - 1;
        int betweenSpaces = (int) Math.pow(2, (floor + 1)) - 1;

        BinaryTreePrinter.printWhitespaces(firstSpaces);

        List<TokenTreeNode> newNodes = new ArrayList<TokenTreeNode>();
        for (TokenTreeNode node : nodes) {
            if (node != null) {
                if ( Constants.isVariable(node.value) ) {
                    Constants.debug(node.value+"");
                } else {
                    if (node.value == Constants.NOT) {
                        Constants.debug(node.value+"");
                        Constants.debug(node.negatedExpression.value+"");
                    } else {
                        Constants.debug(node.value+"");
                    }
                }
                newNodes.add(node.leftNode);
                newNodes.add(node.rightNode);
            } else {
                newNodes.add(null);
                newNodes.add(null);
                Constants.debug(Constants.VISUAL_BLANK);
            }

            BinaryTreePrinter.printWhitespaces(betweenSpaces);
        }
        Constants.debugln();

        for (int i = 1; i <= endgeLines; i++) {
            for (int j = 0; j < nodes.size(); j++) {
                BinaryTreePrinter.printWhitespaces(firstSpaces - i);
                if (nodes.get(j) == null) {
                    BinaryTreePrinter.printWhitespaces(endgeLines + endgeLines + i + 1);
                    continue;
                }

                if (nodes.get(j).leftNode != null) {
                    Constants.debug("/");
                } else {
                    BinaryTreePrinter.printWhitespaces(1);
                }
                BinaryTreePrinter.printWhitespaces(i + i - 1);

                if (nodes.get(j).rightNode != null) {
                    Constants.debug("\\");
                } else {
                    BinaryTreePrinter.printWhitespaces(1);
                }
                BinaryTreePrinter.printWhitespaces(endgeLines + endgeLines - i);
            }
            Constants.debugln();
        }

        printNodeInternal(newNodes, level + 1, maxLevel);
    }

    private static void printWhitespaces(int count) {
        for (int i = 0; i < count; i++) {
            Constants.debug(Constants.VISUAL_BLANK);
        }
    }

    public static int maxLevel(TokenTreeNode node) {
        if (node == null) {
            return 0;
        }
        return Math.max(BinaryTreePrinter.maxLevel(node.leftNode), BinaryTreePrinter.maxLevel(node.rightNode)) + 1;
    }

    private static boolean isAllElementsNull(List list) {
        for (Object object : list) {
            if (object != null) {
                return false;
            }
        }
        return true;
    }

}
