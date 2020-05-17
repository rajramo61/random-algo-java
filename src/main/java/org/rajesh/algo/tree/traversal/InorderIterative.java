package org.rajesh.algo.tree.traversal;

import org.rajesh.algo.tree.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Objects;

public class InorderIterative {

    // This approach uses O(n) time complexity
    // This approach uses O(h) space - h is the height of tree (log n) in average and O(n) in worst case.
    List<Integer> inOrder(Node root) {

        if (Objects.isNull(root)) return null;

        Stack<Node> stack = new Stack<>();
        final List<Integer> output = new ArrayList<>();

        while (true) {
            if (Objects.nonNull(root.left)) {
                stack.push(root);
                root = root.left;
            } else {
                if (stack.isEmpty()) break;
                output.add(stack.pop().data);
                root = root.right;
            }
        }
        return output;
    }
}
