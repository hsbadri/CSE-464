import java.io.*;
import java.util.*;

public class Graph {
    private Map<String, List<String>> adjacencyList;

    public Graph() {
        adjacencyList = new HashMap<>();
    }

    // Parse a DOT graph file and create a graph object
    public void parseGraph(String filepath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filepath));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains("->")) {
                String[] nodes = line.split("->");
                String src = nodes[0].trim();
                String dest = nodes[1].replace(";", "").trim();
                addEdge(src, dest);
            } else if (line.contains("[label=")) {
                String node = line.split("\\[")[0].trim();
                addNode(node);
            }
        }
        reader.close();
    }

    // Add a node to the graph
    public void addNode(String label) {
        if (!adjacencyList.containsKey(label)) {
            adjacencyList.put(label, new ArrayList<>());
        }
    }

    // Add multiple nodes at once
    public void addNodes(String[] labels) {
        for (String label : labels) {
            addNode(label);
        }
    }

    // Add an edge between two nodes
    public void addEdge(String srcLabel, String dstLabel) {
        addNode(srcLabel); // Ensure the source node exists
        addNode(dstLabel); // Ensure the destination node exists
        if (!adjacencyList.get(srcLabel).contains(dstLabel)) {
            adjacencyList.get(srcLabel).add(dstLabel);
        }
    }

    // Get all nodes
    public Set<String> getNodes() {
        return adjacencyList.keySet();
    }

    // Get edges for a given node
    public List<String> getEdges(String node) {
        return adjacencyList.getOrDefault(node, new ArrayList<>());
    }

    // Output graph details as a string
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nodes: ").append(adjacencyList.keySet()).append("\n");
        sb.append("Edges:\n");
        for (Map.Entry<String, List<String>> entry : adjacencyList.entrySet()) {
            for (String dest : entry.getValue()) {
                sb.append(entry.getKey()).append(" -> ").append(dest).append("\n");
            }
        }
        return sb.toString();
    }

    // Output the graph to a DOT file
    public void outputDOTGraph(String filepath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filepath));
        writer.write("digraph G {\n");
        for (Map.Entry<String, List<String>> entry : adjacencyList.entrySet()) {
            for (String dest : entry.getValue()) {
                writer.write(entry.getKey() + " -> " + dest + ";\n");
            }
        }
        writer.write("}");
        writer.close();
    }

    // Output the graph to a PNG file (requires Graphviz installed)
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
    public void removeNode(String label) {
        if (adjacencyList.containsKey(label)) {
            adjacencyList.remove(label);
            for (List<String> edges : adjacencyList.values()) {
                edges.remove(label);
            }
        } else {
            throw new IllegalArgumentException("Node does not exist");
        }
    }

    // Remove multiple nodes
    public void removeNodes(String[] labels) {
        for (String label : labels) {
            removeNode(label);
        }
    }

    // Remove a specific edge
    public void removeEdge(String srcLabel, String dstLabel) {
        List<String> edges = adjacencyList.get(srcLabel);
        if (edges != null && edges.contains(dstLabel)) {
            edges.remove(dstLabel);
        } else {
            throw new IllegalArgumentException("Edge does not exist");
        }
    }
    public Path GraphSearch(String src, String dst, Algorithm algo) {
        if (algo == Algorithm.BFS) {
            return bfsSearch(src, dst);
        } else {
            return dfsSearch(src, dst);
        }
    }
    public Path GraphSearch(String src, String dst) {
        if (!adjacencyList.containsKey(src) || !adjacencyList.containsKey(dst)) {
            return null;
        }

        Queue<List<String>> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        queue.add(Collections.singletonList(src));
        visited.add(src);

        while (!queue.isEmpty()) {
            List<String> path = queue.poll();
            String lastNode = path.get(path.size() - 1);

            if (lastNode.equals(dst)) {
                Path resultPath = new Path();
                path.forEach(resultPath::addNode);
                return resultPath;
            }

            for (String neighbor : adjacencyList.getOrDefault(lastNode, new ArrayList<>())) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    List<String> newPath = new ArrayList<>(path);
                    newPath.add(neighbor);
                    queue.add(newPath);
                }
            }
        }
        return null; // No path found
    }
    public Path GraphSearch(String src, String dst) {
        if (!adjacencyList.containsKey(src) || !adjacencyList.containsKey(dst)) {
            return null;
        }

        Set<String> visited = new HashSet<>();
        List<String> path = new ArrayList<>();
        boolean found = dfsHelper(src, dst, visited, path);
        
        if (found) {
            Path resultPath = new Path();
            path.forEach(resultPath::addNode);
            return resultPath;
        } else {
            return null;
        }
    }

    private boolean dfsHelper(String current, String dst, Set<String> visited, List<String> path) {
        visited.add(current);
        path.add(current);

        if (current.equals(dst)) {
            return true;
        }

        for (String neighbor : adjacencyList.getOrDefault(current, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                if (dfsHelper(neighbor, dst, visited, path)) {
                    return true;
                }
            }
        }

        path.remove(path.size() - 1); // Backtrack
        return false;
    }
    public enum Algorithm{
        BFS;
        DFS;
    }
}
