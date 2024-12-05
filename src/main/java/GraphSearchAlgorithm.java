package main.java;

import java.util.*;

public abstract class GraphSearchAlgorithm {
    protected Map<Node, List<Node>> graph;
    protected Set<Node> visited;

    public GraphSearchAlgorithm(Map<Node, List<Node>> graph) {
        this.graph = graph;
        this.visited = new HashSet<>();
    }

    // Template method
    public Path search(Node start, Node end) {
        if (!graph.containsKey(start) || !graph.containsKey(end)) {
            return null; // Return null if nodes are not present
        }

        initializeTraversal(start);

        while (!isTraversalComplete()) {
            Node currentNode = getNextNode();
            if (currentNode.equals(end)) {
                return buildPath(start, end);
            }
            processNeighbors(currentNode);
        }
        return null; // No path found
    }

    // Abstract methods to be implemented by subclasses
    protected abstract void initializeTraversal(Node start);

    protected abstract boolean isTraversalComplete();

    protected abstract Node getNextNode();

    protected abstract void processNeighbors(Node currentNode);

    // Build the path after traversal is complete
    protected Path buildPath(Node start, Node end) {
        Path path = new Path();
        path.addNode(end.getLabel());
        while (start != end) {
            end = getPreviousNode(end);
            path.addNode(end.getLabel());
        }
        return path.reverse();
    }

    protected abstract Node getPreviousNode(Node node);
}
