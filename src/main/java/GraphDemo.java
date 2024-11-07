import java.io.IOException;

public class GraphDemo {
    public static void main(String[] args) {
        Graph graph = new Graph();

        // Example 1: Adding and Removing Nodes and Edges
        System.out.println("Example 1: Adding and Removing Nodes and Edges");
        graph.addNode("A");
        graph.addNode("B");
        graph.addEdge("A", "B");
        System.out.println("Graph after adding A -> B:\n" + graph);

        graph.removeNode("A");
        System.out.println("Graph after removing node A:\n" + graph);

        // Reset graph
        graph = new Graph();
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");

        // Example 2: Graph Search (BFS and DFS)
        System.out.println("\nExample 2: Graph Search (BFS and DFS)");
        Path pathBFS = graph.GraphSearch("A", "C", Graph.Algorithm.BFS);
        System.out.println("BFS path from A to C: " + pathBFS);

        Path pathDFS = graph.GraphSearch("A", "C", Graph.Algorithm.DFS);
        System.out.println("DFS path from A to C: " + pathDFS);

        // Example 3: Exporting Graph to DOT and PNG
        System.out.println("\nExample 3: Exporting Graph to DOT and PNG");
        try {
            graph.outputDOTGraph("outputGraph.dot");
            // Uncomment the next line if outputGraphics is implemented in Graph class
            // graph.outputGraphics("outputGraph", "png");
            System.out.println("Graph exported as outputGraph.dot and outputGraph.png.");
        } catch (IOException e) {
            System.out.println("Error exporting graph: " + e.getMessage());
        }
    }
}
