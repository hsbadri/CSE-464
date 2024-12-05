import java.util.*;

public class DFSStrategy implements SearchStrategy {
    @Override
    public Path search(Map<Node, List<Node>> graph, Node start, Node end) {
        if (!graph.containsKey(start) || !graph.containsKey(end)) {
            return null;
        }

        Stack<Node> stack = new Stack<>();
        Map<Node, Node> parentMap = new HashMap<>();
        Set<Node> visited = new HashSet<>();

        stack.push(start);
        visited.add(start);
        parentMap.put(start, null);

        while (!stack.isEmpty()) {
            Node currentNode = stack.pop();

            if (currentNode.equals(end)) {
                return buildPath(parentMap, start, end);
            }

            for (Node neighbor : graph.getOrDefault(currentNode, new ArrayList<>())) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    stack.push(neighbor);
                    parentMap.put(neighbor, currentNode);
                }
            }
        }
        return null;
    }

    private Path buildPath(Map<Node, Node> parentMap, Node start, Node end) {
        Path path = new Path();
        for (Node at = end; at != null; at = parentMap.get(at)) {
            path.addNode(at.getLabel());
        }
        return path.reverse();
    }
}
