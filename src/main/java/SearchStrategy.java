package main.java;

import java.util.*;

public interface SearchStrategy {
    Path search(Map<Node, List<Node>> graph, Node start, Node end);
}
