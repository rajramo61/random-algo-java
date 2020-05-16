package org.rajesh.algo.tree.traversal;

import org.rajesh.algo.tree.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Root, Left, Right
 */
public class PreorderRecursive {
    List<Integer> preOrder(Node root) {
        final ArrayList<Integer> output = new ArrayList<>();
        traverse(root, output);
        return output;
    }

    void traverse(Node node, List<Integer> output) {
        if (Objects.nonNull(node)) {
            output.add(node.data);
            traverse(node.left, output);
            traverse(node.right, output);
        }
    }
}
