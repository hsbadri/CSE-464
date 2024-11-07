public class Path {
    private List<String> nodes;

    public Path() {
        nodes = new ArrayList<>();
    }

    public void addNode(String node) {
        nodes.add(node);
    }

    @Override
    public String toString() {
        return String.join(" -> ", nodes);
    }
}
