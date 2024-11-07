import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.io.File;

public class GraphTest {

    @Test
    public void testParseGraph() throws IOException {
        Graph graph = new Graph();
        graph.addNode("A");
        graph.addNode("B");
        graph.addEdge("A", "B");
        assertEquals(2, graph.getNodes().size());
        assertEquals(1, graph.getEdges("A").size());
    }

    @Test
    public void testAddNode() {
        Graph graph = new Graph();
        graph.addNode("A");
        assertTrue(graph.getNodes().contains("A"));
    }

    @Test
    public void testAddEdge() {
        Graph graph = new Graph();
        graph.addEdge("A", "B");
        assertTrue(graph.getEdges("A").contains("B"));
    }

    @Test
    public void testOutputDOTGraph() throws IOException {
        Graph graph = new Graph();
        graph.addEdge("A", "B");
        graph.outputDOTGraph("outputGraph.dot");
        File file = new File("outputGraph.dot");
        assertTrue(file.exists());
    }

    @Test
    public void testRemoveNode() {
        Graph graph = new Graph();
        graph.addNode("A");
        graph.addNode("B");
        graph.addEdge("A", "B");
        graph.removeNode("A");
        assertFalse(graph.getNodes().contains("A"));
        assertFalse(graph.getEdges("A").contains("B"));
    }

    @Test
    public void testRemoveNodes() {
        Graph graph = new Graph();
        graph.addNodes(new String[]{"A", "B", "C"});
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        graph.removeNodes(new String[]{"A", "B"});
        assertFalse(graph.getNodes().contains("A"));
        assertFalse(graph.getNodes().contains("B"));
        assertTrue(graph.getNodes().contains("C"));
    }

    @Test
    public void testRemoveEdge() {
        Graph graph = new Graph();
        graph.addEdge("A", "B");
        graph.removeEdge("A", "B");
        assertFalse(graph.getEdges("A").contains("B"));
    }

    @Test
    public void testRemoveNonExistentNode() {
        Graph graph = new Graph();
        assertThrows(IllegalArgumentException.class, () -> {
            graph.removeNode("NonExistent");
        });
    }

    @Test
    public void testRemoveNonExistentEdge() {
        Graph graph = new Graph();
        graph.addNode("A");
        graph.addNode("B");
        assertThrows(IllegalArgumentException.class, () -> {
            graph.removeEdge("A", "NonExistent");
        });
    }

    // New tests for GraphSearch API with BFS and DFS

    public void testGraphSearchBFS() {
        Graph graph = new Graph();
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");

        Path path = graph.GraphSearch("A", "C", Graph.Algorithm.BFS);
        assertNotNull(path);
        assertEquals("A -> B -> C", path.toString());
    }

    @Test
    public void testGraphSearchDFS() {
        Graph graph = new Graph();
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");

        Path path = graph.GraphSearch("A", "C", Graph.Algorithm.DFS);
        assertNotNull(path);
        assertEquals("A -> B -> C", path.toString());
    }

    @Test
    public void testGraphSearchNoPath() {
        Graph graph = new Graph();
        graph.addNode("A");
        graph.addNode("B");

        Path pathBFS = graph.GraphSearch("A", "B", Graph.Algorithm.BFS);
        Path pathDFS = graph.GraphSearch("A", "B", Graph.Algorithm.DFS);
        assertNull(pathBFS);
        assertNull(pathDFS);
    }

    @Test
    public void testGraphSearchCycleBFS() {
        Graph graph = new Graph();
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        graph.addEdge("C", "A"); // Creates a cycle

        Path path = graph.GraphSearch("A", "C", Graph.Algorithm.BFS);
        assertNotNull(path);
        assertEquals("A -> B -> C", path.toString());
    }

    @Test
    public void testGraphSearchCycleDFS() {
        Graph graph = new Graph();
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        graph.addEdge("C", "A"); // Creates a cycle

        Path path = graph.GraphSearch("A", "C", Graph.Algorithm.DFS);
        assertNotNull(path);
        assertEquals("A -> B -> C", path.toString());
    }

}
