package org.rajesh.algo.tree.traversal;

import org.rajesh.algo.tree.Node;

import java.util.*;

/**
 * Breadth first traversal - BFS
 */
public class LevelOrderTraversal {
    // Use a Queue for BFS
    List<Integer> levelOrder(Node root) {
        final List<Integer> output = new ArrayList<>();
        final Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            final Node node = queue.poll();
            output.add(node.data);
            if (Objects.nonNull(node.left)) {
                queue.add(node.left);
            }
            if (Objects.nonNull(node.right)) {
                queue.add(node.right);
            }
        }
        return output;
    }
}
