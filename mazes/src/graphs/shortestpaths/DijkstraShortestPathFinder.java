package graphs.shortestpaths;

import graphs.BaseEdge;
import graphs.Graph;
import priorityqueues.DoubleMapMinPQ;
import priorityqueues.ExtrinsicMinPQ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * Computes shortest paths using Dijkstra's algorithm.
 * @see SPTShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    extends SPTShortestPathFinder<G, V, E> {

    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        return new DoubleMapMinPQ<>();
        /*
        If you have confidence in your heap implementation, you can disable the line above
        and enable the one below.
         */
        // return new ArrayHeapMinPQ<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    protected Map<V, E> constructShortestPathsTree(G graph, V start, V end) {
        /*
            if there are edges that are not part of the shortest path, return empty map
         */
        if (Objects.equals(start, end)) {
            return new HashMap<>();
        }

        /*
            initialize all needed data structures
         */
        HashSet<V> known = new HashSet<>(); // already processed
        HashMap<V, Double> distTo = new HashMap<>(); // distance from start node
        distTo.put(start, 0.0);


        ExtrinsicMinPQ<V> nodes = createMinPQ(); // gets closest neighbors
        Map<V, E> spt = new HashMap<>(); // initialize the shortest path tree

        nodes.add(start, 0.0);

        while (!known.contains(end)) {
            if (nodes.isEmpty() && !spt.containsKey(end)) { // indicates that the graph is not connected
            return spt;
            }
            V current = nodes.removeMin(); // extracts the closest neighbor
            known.add(current);
            for (E edge : graph.outgoingEdgesFrom(current)) {
                if (!distTo.containsKey(edge.to())) {
                    // initializes distance to infinity according to Dijkstra's Algorithm
                    distTo.put(edge.to(), Double.POSITIVE_INFINITY);
                }
                double oldDistance = distTo.get(edge.to());
                double newDistance = edge.weight() + distTo.get(current);

                /*
                    updates distance if shorter distance is found
                 */
                if (newDistance < oldDistance) {
                    distTo.put(edge.to(), newDistance);
                    spt.put(edge.to(), edge);
                    if (nodes.contains(edge.to())) {
                        nodes.changePriority(edge.to(), newDistance);
                    } else {
                        nodes.add(edge.to(), newDistance);
                    }

                }


            }
        }

        return spt;
    }

    @Override
    protected ShortestPath<V, E> extractShortestPath(Map<V, E> spt, V start, V end) {
        /*
            path consisting of no edges
         */
        if (Objects.equals(start, end)) {
            return new ShortestPath.SingleVertex<>(start);
        }

        /*
        if target node cannot be reached, returns failure
         */
        if (Objects.equals(spt.get(end), null)) {
            return new ShortestPath.Failure<>();
        }

        /*
            Gets the shortest path edges (in reversed order)
         */
        List<E> validEdges = new ArrayList<>();
        E current = spt.get(end);
        while (!Objects.equals(current, null)) {
            validEdges.add(current);
            current = spt.get(current.from());
        }

        /*
            sorts path into correct order
         */
        ArrayList<E> edges = new ArrayList<>();
        for (int i = validEdges.size() -1; i >= 0; i--) {
            edges.add(validEdges.get(i));
        }

        // returns success
        return new ShortestPath.Success<>(edges);
    }

}
