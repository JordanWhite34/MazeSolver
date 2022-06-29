package mazes.logic.carvers;


import graphs.EdgeWithData;

import graphs.minspantrees.MinimumSpanningTree;
import graphs.minspantrees.MinimumSpanningTreeFinder;
import mazes.entities.Room;
import mazes.entities.Wall;
import mazes.logic.MazeGraph;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Carves out a maze based on Kruskal's algorithm.
 */
public class KruskalMazeCarver extends MazeCarver {
    MinimumSpanningTreeFinder<MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder;
    private final Random rand;

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random();
    }

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder,
                             long seed) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random(seed);
    }

    @Override
    protected Set<Wall> chooseWallsToRemove(Set<Wall> walls) {
        // creates new HashSet to add newly created edges with all data
        Set<EdgeWithData<Room, Wall>> edges = new HashSet<>();

        // puts valid edges into the set above with randomized weights
        for (Wall wall : walls) {
            edges.add(new EdgeWithData<>(wall.getRoom1(), wall.getRoom2(), rand.nextDouble(), wall));
        }

        /*
            based on the given weights, a MazeGraph is created and is passed into the MST Finder to check
            for a valid MST
         */
        MinimumSpanningTree<Room, EdgeWithData<Room, Wall>> mst
            = this.minimumSpanningTreeFinder.findMinimumSpanningTree(new MazeGraph(edges));

        // checks if MST exists, or else will return an empty set
        if (!mst.exists()) {
            return new HashSet<>();
        }

        // gets the edges of the MST assuming that the MST does exist
        Collection<EdgeWithData<Room, Wall>> mstEdges = mst.edges();

        // Creates new set to fill in with the wall data of the MST data that are to be removed
        Set<Wall> remove = new HashSet<>();
        for (EdgeWithData<Room, Wall> edge : mstEdges) {
            // edge.data == Wall
            remove.add(edge.data());
        }

        return remove; // will return walls MST that are to be removed


    }



}
