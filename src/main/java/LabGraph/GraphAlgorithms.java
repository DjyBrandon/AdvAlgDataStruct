package LabGraph;

import LabMapHashtables.Entry;
import LabMapHashtables.Map;
import LabSortedMapBST.Position;

import java.util.Set;
import java.util.HashSet;

/**
 * A collection of graph algorithms.
 */
public class GraphAlgorithms {

    /**
     * Performs depth-first search of the unknown portion of Graph g starting at Vertex u.
     *
     * @param g      Graph instance
     * @param u      Vertex of graph g that will be the source of the search
     * @param known  is a set of previously discovered vertices
     * @param forest is a map from nonroot vertex to its discovery edge in DFS forest
     *               <p>
     *               As an outcome, this method adds newly discovered vertices (including u) to the known set,
     *               and adds discovery graph edges to the forest.
     */
    public static <V, E> void DFS(Graph<V, E> g, Vertex<V> u,
                                  Set<Vertex<V>> known, Map<Vertex<V>, Edge<E>> forest) {
        known.add(u);                              // u has been discovered
        for (Edge<E> e : g.outgoingEdges(u)) {     // for every outgoing edge from u
            Vertex<V> v = g.opposite(u, e);
            if (!known.contains(v)) {
                forest.put(v, e);                      // e is the tree edge that discovered v
                DFS(g, v, known, forest);              // recursively explore from v
            }
        }
    }

    /**
     * Returns an ordered list of edges comprising the directed path from u to v.
     * If v is unreachable from u, or if u equals v, an empty path is returned.
     *
     * @param g      Graph instance
     * @param u      Vertex beginning the path
     * @param v      Vertex ending the path
     * @param forest must be a map that resulting from a previous call to DFS started at u.
     */
    public static <V, E> PositionalList<Edge<E>>
    constructPath(Graph<V, E> g, Vertex<V> u, Vertex<V> v,
                  Map<Vertex<V>, Edge<E>> forest) {
        PositionalList<Edge<E>> path = new LinkedPositionalList<>();
        if (forest.get(v) != null) {           // v was discovered during the search
            Vertex<V> walk = v;                  // we construct the path from back to front
            while (walk != u) {
                Edge<E> edge = forest.get(walk);
                path.addFirst(edge);               // add edge to *front* of path
                walk = g.opposite(walk, edge);     // repeat with opposite endpoint
            }
        }
        return path;
    }

    /**
     * Performs DFS for the entire graph and returns the DFS forest as a map.
     *
     * @return map such that each nonroot vertex v is mapped to its discovery edge
     * (vertices that are roots of a DFS trees in the forest are not included in the map).
     */
    public static <V, E> Map<Vertex<V>, Edge<E>> DFSComplete(Graph<V, E> g) {
        Set<Vertex<V>> known = new HashSet<>();
        Map<Vertex<V>, Edge<E>> forest = new ProbeHashMap<>();
        for (Vertex<V> u : g.vertices())
            if (!known.contains(u))
                DFS(g, u, known, forest);            // (re)start the DFS process at u
        return forest;
    }

    /**
     * Performs breadth-first search of the undiscovered portion of Graph g starting at Vertex s.
     *
     * @param g      Graph instance
     * @param s      Vertex of graph g that will be the source of the search
     * @param known  is a set of previously discovered vertices
     * @param forest is a map from nonroot vertex to its discovery edge in DFS forest
     *               <p>
     *               As an outcome, this method adds newly discovered vertices (including s) to the known set,
     *               and adds discovery graph edges to the forest.
     */
    public static <V, E> void BFS(Graph<V, E> g, Vertex<V> s,
                                  Set<Vertex<V>> known, Map<Vertex<V>, Edge<E>> forest) {
        PositionalList<Vertex<V>> level = new LinkedPositionalList<>();
        known.add(s);
        level.addLast(s);                         // first level includes only s
        while (!level.isEmpty()) {
            PositionalList<Vertex<V>> nextLevel = new LinkedPositionalList<>();
            for (Vertex<V> u : level)
                for (Edge<E> e : g.outgoingEdges(u)) {
                    Vertex<V> v = g.opposite(u, e);
                    if (!known.contains(v)) {
                        known.add(v);
                        forest.put(v, e);                 // e is the tree edge that discovered v
                        nextLevel.addLast(v);             // v will be further considered in next pass
                    }
                }
            level = nextLevel;                      // relabel 'next' level to become the current
        }
    }

    /**
     * Performs BFS for the entire graph and returns the BFS forest as a map.
     *
     * @return map such that each nonroot vertex v is mapped to its discovery edge
     * (vertices that are roots of a BFS trees in the forest are not included in the map).
     */
    public static <V, E> Map<Vertex<V>, Edge<E>> BFSComplete(Graph<V, E> g) {
        Map<Vertex<V>, Edge<E>> forest = new ProbeHashMap<>();
        Set<Vertex<V>> known = new HashSet<>();
        for (Vertex<V> u : g.vertices())
            if (!known.contains(u))
                BFS(g, u, known, forest);
        return forest;
    }

    /**
     * Converts graph g into its transitive closure.
     * This uses the Floyd-Warshall algorithm.
     */
    @SuppressWarnings({"unchecked"})
    public static <V, E> void transitiveClosure(Graph<V, E> g) {
        for (Vertex<V> k : g.vertices())
            for (Vertex<V> i : g.vertices())
                // verify that edge (i,k) exists in the partial closure
                if (i != k && g.getEdge(i, k) != null)
                    for (Vertex<V> j : g.vertices())
                        // verify that edge (k,j) exists in the partial closure
                        if (i != j && j != k && g.getEdge(k, j) != null)
                            // if (i,j) not yet included, add it to the closure
                            if (g.getEdge(i, j) == null)
                                g.insertEdge(i, j, null);
    }


    /**
     * Returns a list of verticies of directed acyclic graph g in topological order.
     * If graph g has a cycle, the result will be incomplete.
     */
    public static <V, E> PositionalList<Vertex<V>> topologicalSort(Graph<V, E> g) {
        // list of vertices placed in topological order
        PositionalList<Vertex<V>> topo = new LinkedPositionalList<>();
        // container of vertices that have no remaining constraints
        Stack<Vertex<V>> ready = new LinkedStack<>();
        // map keeping track of remaining in-degree for each vertex
        Map<Vertex<V>, Integer> inCount = new ProbeHashMap<>();
        for (Vertex<V> u : g.vertices()) {
            inCount.put(u, g.inDegree(u));           // initialize with actual in-degree
            if (inCount.get(u) == 0)                 // if u has no incoming edges,
                ready.push(u);                         // it is free of constraints
        }
        while (!ready.isEmpty()) {
            Vertex<V> u = ready.pop();
            topo.addLast(u);
            for (Edge<E> e : g.outgoingEdges(u)) {   // consider all outgoing neighbors of u
                Vertex<V> v = g.opposite(u, e);
                inCount.put(v, inCount.get(v) - 1);    // v has one less constraint without u
                if (inCount.get(v) == 0)
                    ready.push(v);
            }
        }
        return topo;
    }

    /**
     * Computes shortest-path distances from src vertex to all reachable vertices of g.
     * <p>
     * This implementation uses Dijkstra's algorithm.
     * <p>
     * The edge's element is assumed to be its integral weight.
     */
    public static <V> Map<Vertex<V>, Integer> shortestPathLengths(Graph<V, Integer> g, Vertex<V> src) {
        // d.get(v) is upper bound on distance from src to v
        Map<Vertex<V>, Integer> d = new ProbeHashMap<>();
        // map reachable v to its d value
        Map<Vertex<V>, Integer> cloud = new ProbeHashMap<>();
        // pq will have vertices as elements, with d.get(v) as key
        AdaptablePriorityQueue<Integer, Vertex<V>> pq;
        pq = new HeapAdaptablePriorityQueue<>();
        // maps from vertex to its pq locator
        Map<Vertex<V>, Entry<Integer, Vertex<V>>> pqTokens;
        pqTokens = new ProbeHashMap<>();

        // for each vertex v of the graph, add an entry to the priority queue, with
        // the source having distance 0 and all others having infinite distance
        for (Vertex<V> v : g.vertices()) {
            if (v == src)
                d.put(v, 0);
            else
                d.put(v, Integer.MAX_VALUE);
            pqTokens.put(v, pq.insert(d.get(v), v));       // save entry for future updates
        }
        // now begin adding reachable vertices to the cloud
        while (!pq.isEmpty()) {
            Entry<Integer, Vertex<V>> entry = pq.removeMin();
            int key = entry.getKey();
            Vertex<V> u = entry.getValue();
            cloud.put(u, key);                             // this is actual distance to u
            pqTokens.remove(u);                            // u is no longer in pq
            for (Edge<Integer> e : g.outgoingEdges(u)) {
                Vertex<V> v = g.opposite(u, e);
                if (cloud.get(v) == null) {
                    // perform relaxation step on edge (u,v)
                    int wgt = e.getElement();
                    if (d.get(u) + wgt < d.get(v)) {              // better path to v?
                        d.put(v, d.get(u) + wgt);                   // update the distance
                        pq.replaceKey(pqTokens.get(v), d.get(v));   // update the pq entry
                    }
                }
            }
        }
        return cloud;         // this only includes reachable vertices
    }

    /**
     * Reconstructs a shortest-path tree rooted at vertex s, given distance map d.
     * The tree is represented as a map from each reachable vertex v (other than s)
     * to the edge e = (u,v) that is used to reach v from its parent u in the tree.
     */
    public static <V> Map<Vertex<V>, Edge<Integer>>
    spTree(Graph<V, Integer> g, Vertex<V> s, Map<Vertex<V>, Integer> d) {
        Map<Vertex<V>, Edge<Integer>> tree = new ProbeHashMap<>();
        for (Vertex<V> v : d.keySet())
            if (v != s)
                for (Edge<Integer> e : g.incomingEdges(v)) {   // consider INCOMING edges
                    Vertex<V> u = g.opposite(v, e);
                    int wgt = e.getElement();
                    if (d.get(v) == d.get(u) + wgt)
                        tree.put(v, e);                            // edge is used to reach v
                }
        return tree;
    }

    /**
     * Computes a minimum spanning tree of connected, weighted graph g using Kruskal's algorithm.
     * <p>
     * Result is returned as a list of edges that comprise the MST (in arbitrary order).
     */
    public static <V> PositionalList<Edge<Integer>> MST(Graph<V, Integer> g) {
        // tree is where we will store result as it is computed
        PositionalList<Edge<Integer>> tree = new LinkedPositionalList<>();
        // pq entries are edges of graph, with weights as keys
        PriorityQueue<Integer, Edge<Integer>> pq = new HeapPriorityQueue<>();
        // union-find forest of components of the graph
        Partition<Vertex<V>> forest = new Partition<>();
        // map each vertex to the forest position
        Map<Vertex<V>, Position<Vertex<V>>> positions = new ProbeHashMap<>();

        for (Vertex<V> v : g.vertices())
            positions.put(v, forest.makeCluster(v));

        for (Edge<Integer> e : g.edges())
            pq.insert(e.getElement(), e);

        int size = g.numVertices();
        // while tree not spanning and unprocessed edges remain...
        while (tree.size() != size - 1 && !pq.isEmpty()) {
            Entry<Integer, Edge<Integer>> entry = pq.removeMin();
            Edge<Integer> edge = entry.getValue();
            Vertex<V>[] endpoints = g.endVertices(edge);
            Position<Vertex<V>> a = forest.find(positions.get(endpoints[0]));
            Position<Vertex<V>> b = forest.find(positions.get(endpoints[1]));
            if (a != b) {
                tree.addLast(edge);
                forest.union(a, b);
            }
        }

        return tree;
    }

    public static void main(String[] args) {
        // 创建一个有向图
        AdjacencyMapGraph<String, Integer> graph = new AdjacencyMapGraph<>(true);

        // 添加顶点
        Vertex<String> v0 = graph.insertVertex("A");
        Vertex<String> v1 = graph.insertVertex("B");
        Vertex<String> v2 = graph.insertVertex("C");
        Vertex<String> v3 = graph.insertVertex("D");
        Vertex<String> v4 = graph.insertVertex("E");
        Vertex<String> v5 = graph.insertVertex("F");

        // 添加边
        graph.insertEdge(v0, v1, 1);
        graph.insertEdge(v0, v3, 4);
        graph.insertEdge(v1, v2, 2);
        graph.insertEdge(v2, v4, 3);
        graph.insertEdge(v3, v1, 5);
        graph.insertEdge(v3, v4, 1);
        graph.insertEdge(v4, v1, 7);
        graph.insertEdge(v4, v5, 1);

        // 输出图的基本信息
        System.out.println("Number of vertices: " + graph.numVertices());
        System.out.println("Number of edges: " + graph.numEdges());
        System.out.println(graph);

        // 执行 BFS 完整遍历
        Map<Vertex<String>, Edge<Integer>> bfsForest = BFSComplete(graph);

        // 输出 BFS 结果
        System.out.println("BFS Forest:");
        for (Vertex<String> v : bfsForest.keySet()) {
            System.out.println(v.getElement() + " -> " + bfsForest.get(v).getElement());
        }

        // 构建路径，假设我们要找从 A 到 F 的 BFS 路径
        PositionalList<Edge<Integer>> bfsPath = constructPath(graph, v0, v5, bfsForest);

        // 输出路径
        System.out.println("BFS Path from A to F:");
        for (Edge<Integer> e : bfsPath) {
            Vertex<String>[] endpoints = graph.endVertices(e);
            System.out.println(endpoints[0].getElement() + " -> " + endpoints[1].getElement() + " with distance " + e.getElement());
        }

        // 执行 DFS 完整遍历
        Map<Vertex<String>, Edge<Integer>> dfsForest = DFSComplete(graph);

        // 输出 DFS 结果
        System.out.println("DFS Forest:");
        for (Vertex<String> v : dfsForest.keySet()) {
            System.out.println(v.getElement() + " -> " + dfsForest.get(v).getElement());
        }

        // 构建路径，假设我们要找从 A 到 F 的 DFS 路径
        PositionalList<Edge<Integer>> dfsPath = constructPath(graph, v0, v5, dfsForest);

        // 输出路径
        System.out.println("DFS Path from A to F:");
        for (Edge<Integer> e : dfsPath) {
            Vertex<String>[] endpoints = graph.endVertices(e);
            System.out.println(endpoints[0].getElement() + " -> " + endpoints[1].getElement() + " with distance " + e.getElement());
        }

        // 调用 Dijkstra 算法从顶点 A 计算到其他所有可达顶点的最短路径长度
        Map<Vertex<String>, Integer> shortestPaths = shortestPathLengths(graph, v0);

        // 输出最短路径长度
        System.out.println("Shortest path lengths from A:");
        for (Vertex<String> v : shortestPaths.keySet()) {
            System.out.println("A to " + v.getElement() + ": " + shortestPaths.get(v));
        }

        // 构建从 A 出发的最短路径树
        Map<Vertex<String>, Edge<Integer>> spTree = spTree(graph, v0, shortestPaths);

        // 输出最短路径树
        System.out.println("Shortest path tree:");
        for (Vertex<String> v : spTree.keySet()) {
            System.out.println(v.getElement() + " via edge with weight " + spTree.get(v).getElement());
        }

        // 使用 Kruskal 算法计算最小生成树，并输出其中的边和权重。
        PositionalList<Edge<Integer>> mst = MST(graph);
        System.out.println("Minimum spanning tree:");
        for (Edge<Integer> e : mst) {
            Vertex<String>[] endpoints = graph.endVertices(e);
            System.out.println(endpoints[0].getElement() + " - " + endpoints[1].getElement() + " with weight " + e.getElement());
        }

        // 调用 transitiveClosure 方法
        transitiveClosure(graph);

        // 输出传递闭包后的图
        System.out.println("Graph after transitive closure:");
        System.out.println("Number of vertices: " + graph.numVertices());
        System.out.println("Number of edges: " + graph.numEdges());
        for (Vertex<String> u : graph.vertices()) {
            for (Edge<Integer> e : graph.outgoingEdges(u)) {
                Vertex<String>[] endpoints = graph.endVertices(e);
                System.out.println(endpoints[0].getElement() + " -> " + endpoints[1].getElement());
            }
        }

        // 创建另一个有向无环图（DAG）用于拓扑排序
        AdjacencyMapGraph<String, Integer> dag = new AdjacencyMapGraph<>(true);

        // 添加顶点
        Vertex<String> va = dag.insertVertex("A");
        Vertex<String> vb = dag.insertVertex("B");
        Vertex<String> vc = dag.insertVertex("C");
        Vertex<String> vd = dag.insertVertex("D");
        Vertex<String> ve = dag.insertVertex("E");

        // 添加边（构成DAG）
        dag.insertEdge(va, vb, 1);
        dag.insertEdge(va, vc, 1);
        dag.insertEdge(vb, vd, 1);
        dag.insertEdge(vc, vd, 1);
        dag.insertEdge(vd, ve, 1);

        // 调用 topologicalSort 方法
        PositionalList<Vertex<String>> topoSort = topologicalSort(dag);

        // 输出拓扑排序结果
        System.out.println("Topological sort of the DAG:");
        for (Vertex<String> v : topoSort) {
            System.out.println(v.getElement());
        }
    }
}
