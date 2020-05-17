package org.rajesh.algo.tree.traversal;

import org.rajesh.algo.tree.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Left, Root, Right
 */
public class InorderRecursive {
    List<Integer> inOrder(Node root) {
        final List<Integer> output = new ArrayList<>();
        traverse(root, output);
        return output;
    }

    void traverse(Node node, List<Integer> output) {
        if (Objects.nonNull(node)) {
            traverse(node.left, output);
            output.add(node.data);
            traverse(node.right, output);
        }
    }
}
