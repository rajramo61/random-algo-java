package org.rajesh.algo.tree.traversal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class Node {
    int data;
    Node left, right;

    Node(int item) {
        data = item;
        left = right = null;
    }
}

public class InorderRecursive {
    List<Integer> inOrder(Node root) {
        final ArrayList<Integer> output = new ArrayList<>();
        traverse(root, output);
        return output;
    }

    void traverse(Node node, List<Integer> output) {
        if (Objects.isNull(node)) {
            return;
        }
        traverse(node.left, output);
        output.add(node.data);
        traverse(node.right, output);
    }
}
