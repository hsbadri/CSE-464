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
}
