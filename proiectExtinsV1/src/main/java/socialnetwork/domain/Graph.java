package socialnetwork.domain;

import java.util.LinkedList;

public class Graph {
    private final int size;
    private final LinkedList<Integer>[] adjacency;

    public Graph(int v) {
        size = v + 1;
        adjacency = new LinkedList[size];
        for (int i = 0; i < v; ++i) {
            adjacency[i] = new LinkedList();
        }
    }

    public void addEdge(int node1, int node2) {
        if (adjacency[node1] == null)
            adjacency[node1] = new LinkedList();
        adjacency[node1].add(node2);
    }

    public boolean isNodenULL(int node) {
        return adjacency[node] == null;
    }

    public boolean[] DFS(int node, boolean[] visited) {
        visited[node] = true;
        int aux = 0;
        for (int i = 0; i < adjacency[node].size(); i++) {
            aux = adjacency[node].get(i);
            if (!visited[aux]) {
                DFS(aux, visited);
            }
        }
        return visited;
    }
}
