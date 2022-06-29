package graphs.minspantrees;

import disjointsets.DisjointSets;
// import disjointsets.QuickFindDisjointSets;
import disjointsets.UnionBySizeCompressingDisjointSets;
import graphs.BaseEdge;
import graphs.KruskalGraph;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Computes minimum spanning trees using Kruskal's algorithm.
 * @see MinimumSpanningTreeFinder for more documentation.
 */
public class KruskalMinimumSpanningTreeFinder<G extends KruskalGraph<V, E>, V, E extends BaseEdge<V, E>>
    implements MinimumSpanningTreeFinder<G, V, E> {

    protected DisjointSets<V> createDisjointSets() {
        // return new QuickFindDisjointSets<>();
        /*
        Disable the line above and enable the one below after you've finished implementing
        your `UnionBySizeCompressingDisjointSets`.
         */
        return new UnionBySizeCompressingDisjointSets<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    public MinimumSpanningTree<V, E> findMinimumSpanningTree(G graph) {
        // Here's some code to get you started; feel free to change or rearrange it if you'd like.

        // sort edges in the graph in ascending weight order
        List<E> edges = new ArrayList<>(graph.allEdges());
        edges.sort(Comparator.comparingDouble(E::weight));

        /*
            Creates a set of disjoint sets from all the vertices present in the graph so that they can
            later have union applied to them based on the MST algorithm (using Kruskal's)
         */
        DisjointSets<V> disjointSets = createDisjointSets();
        for (V vertex : graph.allVertices()) {
            disjointSets.makeSet(vertex);
        }

        ArrayList<E> validEdges = new ArrayList<>();

        /*
            Goes through all the possible edges from the smallest weight to biggest,choosing the edges
            with the smallest weight and those that do not make a cycle, and adds those edges to the
            validEdges arrayList
         */
        for (E edge : edges) {
            int setFrom = disjointSets.findSet(edge.from());
            int setTo = disjointSets.findSet(edge.to());
            if (setFrom != setTo) {
                validEdges.add(edge);
                disjointSets.union(edge.from(), edge.to());
            }
        }

        // Set of rep. node index
        Set<Integer> vertexSet = new HashSet<>();
        for (V vertex : graph.allVertices()) {
            vertexSet.add(disjointSets.findSet(vertex));
        }

        // checks if all vertices are in the same set ==> Checks for a set size of 1
        if (vertexSet.size() > 1) {
            // having a size greater than one means the graph is not connected and the MST does not exist
            return new MinimumSpanningTree.Failure<>();
        }

        // if all tests are passed, a successful and valid MST is returned
        return new MinimumSpanningTree.Success<>(validEdges);
    }
}
