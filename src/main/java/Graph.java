import java.io.*;
import java.util.*;

import main.java.BFSStrategy;
import main.java.DFSStrategy;

public class Graph {
    private Map<Node, List<Node>> adjacencyList;

    public Graph() {
        adjacencyList = new HashMap<>();
    }

    // Parse a DOT graph file and create a graph object
    public void parseGraph(String filepath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filepath));
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();

            // Skip empty lines
            if (line.isEmpty()) {
                continue;
            }

            // Handle edges
            if (line.contains("->")) {
                String[] nodes = line.split("->");
                if (nodes.length != 2) {
                    throw new IllegalArgumentException("Malformed edge: " + line);
                }
                Node src = new Node(nodes[0].trim());
                Node dest = new Node(nodes[1].replace(";", "").trim());
                addEdge(src, dest);

                // Handle labeled nodes
            } else if (line.contains("[label=")) {
                String[] parts = line.split("\\[");
                if (parts.length < 1 || parts[0].trim().isEmpty()) {
                    throw new IllegalArgumentException("Malformed node: " + line);
                }
                Node node = new Node(parts[0].trim());
                addNode(node);

                // Invalid input
            } else {
                throw new IllegalArgumentException("Unrecognized line format: " + line);
            }
        }
        reader.close();
    }

    // Add a node to the graph
    public void addNode(Node node) {
        adjacencyList.putIfAbsent(node, new ArrayList<>());
    }

    // Add an edge between two nodes
    public void addEdge(Node src, Node dest) {
        addNode(src); // Ensure the source node exists
        addNode(dest); // Ensure the destination node exists
        if (!adjacencyList.get(src).contains(dest)) {
            adjacencyList.get(src).add(dest);
        }
    }

    // Get all nodes
    public Set<Node> getNodes() {
        return adjacencyList.keySet();
    }

    // Get edges for a given node
    public List<Node> getEdges(Node node) {
        return adjacencyList.getOrDefault(node, new ArrayList<>());
    }

    // Output graph details as a string
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nodes: ").append(adjacencyList.keySet()).append("\n");
        sb.append("Edges:\n");
        for (Map.Entry<Node, List<Node>> entry : adjacencyList.entrySet()) {
            for (Node dest : entry.getValue()) {
                sb.append(entry.getKey()).append(" -> ").append(dest).append("\n");
            }
        }
        return sb.toString();
    }

    // Output the graph to a DOT file
    public void outputDOTGraph(String filepath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filepath));
        writer.write("digraph G {\n");
        for (Map.Entry<Node, List<Node>> entry : adjacencyList.entrySet()) {
            for (Node dest : entry.getValue()) {
                writer.write(entry.getKey().getLabel() + " -> " + dest.getLabel() + ";\n");
            }
        }
        writer.write("}");
        writer.close();
    }

    // Remove a single node and all associated edges
    public void removeNode(Node node) {
        if (adjacencyList.containsKey(node)) {
            adjacencyList.remove(node);
            for (List<Node> edges : adjacencyList.values()) {
                edges.remove(node);
            }
        } else {
            throw new IllegalArgumentException("Node does not exist");
        }
    }

    // Remove a specific edge
    public void removeEdge(Node src, Node dest) {
        List<Node> edges = adjacencyList.get(src);
        if (edges != null && edges.contains(dest)) {
            edges.remove(dest);
        } else {
            throw new IllegalArgumentException("Edge does not exist");
        }
    }

    // Enum to specify the search algorithm
    public enum Algorithm {
        BFS, DFS;
    }

    // GraphSearch method with Algorithm selection
    public Path GraphSearch(Node src, Node dest, Algorithm algo) {
        SearchStrategy strategy;
        if (algo == Algorithm.BFS) {
            searchAlgorithm = new BFSStrategy();
        } else if (algo == Algorithm.DFS) {
            searchAlgorithm = new DFSStrategy();
        } else {
            return null;
        }
        return strategy.search(adjacencyList, src, dest);
    }

    // BFS implementation
    private Path bfsSearch(Node src, Node dest) {
        if (!adjacencyList.containsKey(src) || !adjacencyList.containsKey(dest)) {
            return null;
        }

        Queue<List<Node>> queue = new LinkedList<>();
        Set<Node> visited = new HashSet<>();
        queue.add(Collections.singletonList(src));
        visited.add(src);

        while (!queue.isEmpty()) {
            List<Node> path = queue.poll();
            Node lastNode = path.get(path.size() - 1);

            if (lastNode.equals(dest)) {
                Path resultPath = new Path();
                path.forEach(node -> resultPath.addNode(node.getLabel()));
                return resultPath;
            }

            for (Node neighbor : adjacencyList.getOrDefault(lastNode, new ArrayList<>())) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    List<Node> newPath = new ArrayList<>(path);
                    newPath.add(neighbor);
                    queue.add(newPath);
                }
            }
        }
        return null;
    }

    // DFS implementation
    private Path dfsSearch(Node src, Node dest) {
        if (!adjacencyList.containsKey(src) || !adjacencyList.containsKey(dest)) {
            return null;
        }

        Set<Node> visited = new HashSet<>();
        List<Node> path = new ArrayList<>();
        boolean found = dfsHelper(src, dest, visited, path);

        if (found) {
            Path resultPath = new Path();
            path.forEach(node -> resultPath.addNode(node.getLabel()));
            return resultPath;
        } else {
            return null;
        }
    }

    // Helper method for DFS
    private boolean dfsHelper(Node current, Node dest, Set<Node> visited, List<Node> path) {
        visited.add(current);
        path.add(current);

        if (current.equals(dest)) {
            return true;
        }

        for (Node neighbor : adjacencyList.getOrDefault(current, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                if (dfsHelper(neighbor, dest, visited, path)) {
                    return true;
                }
            }
        }

        path.remove(path.size() - 1); // Backtrack
        return false;
    }

    public void outputGraphics(String path, String format) throws IOException {
        String dotFile = path + ".dot";
        outputDOTGraph(dotFile);
        Process process = new ProcessBuilder("dot", "-T" + format, dotFile, "-o", path + "." + format).start();
        try {
            process.waitFor(); // Ensure the process finishes before continuing
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
