package org.rajesh.algo.tree.traversal;

import org.rajesh.algo.tree.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Left, Right, Root
 */
public class PostorderRecursive {
    List<Integer> postOrder(Node root) {
        final List<Integer> output = new ArrayList<>();
        traverse(root, output);
        return output;
    }

    void traverse(Node node, List<Integer> output) {
        if (Objects.nonNull(node)) {
            traverse(node.left, output);
            traverse(node.right, output);
            output.add(node.data);
        }
    }
}
