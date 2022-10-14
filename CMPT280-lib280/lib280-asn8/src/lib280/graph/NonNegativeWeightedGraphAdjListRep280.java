/*
Sunny Ka Patel
SDK438
11267665
CMPT 280.A8
 */

package lib280.graph;

import lib280.base.Pair280;
import lib280.exception.InvalidArgument280Exception;

import java.util.InputMismatchException;
import java.util.Scanner;


public class NonNegativeWeightedGraphAdjListRep280<V extends Vertex280> extends
        WeightedGraphAdjListRep280<V> {

    public NonNegativeWeightedGraphAdjListRep280(int cap, boolean d,
                                                 String vertexTypeName) {
        super(cap, d, vertexTypeName);
    }

    public NonNegativeWeightedGraphAdjListRep280(int cap, boolean d) {
        super(cap, d);
    }


    @Override
    public void setEdgeWeight(V v1, V v2, double weight) {
        // Overriding this method to throw an exception if a weight is negative will cause
        // super.initGraphFromFile to throw an exception when it tries to set a weight to
        // something negative.

        // Verify that the weight is non-negative
        if (weight < 0) throw new InvalidArgument280Exception("Specified weight is negative.");

        // If it is, then just set the edge weight using the superclass method.
        super.setEdgeWeight(v1, v2, weight);
    }

    @Override
    public void setEdgeWeight(int srcIdx, int dstIdx, double weight) {
        // Get the vetex objects associated with each index and pass off to the
        // version of setEdgeWEight that accepts vertex objects.
        this.setEdgeWeight(this.vertex(srcIdx), this.vertex(dstIdx), weight);
    }


    /**
     * Implementation of Dijkstra's algorithm.
     *
     * @param startVertex Start vertex for the single-source shortest paths.
     * @return An array of size G.numVertices()+1 in which offset k contains the shortest
     * path from startVertex to k.  Offset 0 is unused since vertex indices start
     * at 1.
     */
    public Pair280<double[], int[]> shortestPathDijkstra(int startVertex) {
        // boolean array to store if the node is visited or not
        boolean[] visited = new boolean[this.numVertices + 1];

        // double array to store the tentative distance between nodes
        double[] tentativeDistance = new double[this.numVertices + 1];

        // int array to store the predecessor nodes
        int[] predecessorNode = new int[this.numVertices + 1];

        // stores the number of nodes processed so far
        double processed = 0;

        for (int i = 0; i <= this.numVertices; i++) {
            visited[i] = false;
            tentativeDistance[i] = Double.MAX_VALUE; // replacement for infinity
            predecessorNode[i] = -1;
        }

        // set the tentative distance of the starting vertex as 0
        tentativeDistance[startVertex] = 0;

        while (processed <= this.numVertices) {
            int i = 1;
            while (visited[i] && (i < this.numVertices)) {
                i++;
            }
            int cur = i;

            // loop until we reach the unvisited vertex with the smallest tentative distance
            while (i <= this.numVertices) {
                if (!visited[i] && (tentativeDistance[i] < tentativeDistance[cur])) {
                    cur = i;
                }
                i++;
            }
            visited[cur] = true;
            processed++;

            // update tentative distances of adjacent vertices if needed
            for (this.eGoFirst(this.vertexArray[cur - 1]); !this.eAfter(); this.eGoForth()) {
                int z = this.eItem.secondItem().index();

                if (!visited[z] && tentativeDistance[z] > this.eItem.getWeight() + tentativeDistance[cur]) {
                    tentativeDistance[z] = this.eItem.getWeight() + tentativeDistance[cur];
                    predecessorNode[z] = cur;
                }
            }
        }
        return new Pair280<>(tentativeDistance, predecessorNode);
    }

    // Given a predecessors array output from this.shortestPathDijkatra, return a string
    // that represents a path from the start node to the given destination vertex 'destVertex'.
    private static String extractPath(int[] predecessors, int destVertex) {
        if (predecessors[destVertex] == -1) {
            return "ERROR: Vertex is unreachable!";
        }

        String result = "" + destVertex;
        int saved = destVertex;

        while (predecessors[saved] > 0) {
            result = predecessors[saved] + ", " + result;
            saved = predecessors[saved];
        }

        result = "The path to " + destVertex + " is: " + result;
        return result;
    }

    // Regression Test
    public static void main(String args[]) {
        NonNegativeWeightedGraphAdjListRep280<Vertex280> G = new NonNegativeWeightedGraphAdjListRep280<Vertex280>(1, false);

        if (args.length == 0)
            G.initGraphFromFile("/Users/sunny/Desktop/CMPT280/CMPT280-lib280/lib280-asn8/src/lib280/graph/weightedtestgraph.gra");
        else
            G.initGraphFromFile(args[0]);

        System.out.println("Enter the number of the start vertex: ");
        Scanner in = new Scanner(System.in);
        int startVertex;
        try {
            startVertex = in.nextInt();
        } catch (InputMismatchException e) {
            in.close();
            System.out.println("That's not an integer!");
            return;
        }

        if (startVertex < 1 || startVertex > G.numVertices()) {
            in.close();
            System.out.println("That's not a valid vertex number for this graph.");
            return;
        }
        in.close();


        Pair280<double[], int[]> dijkstraResult = G.shortestPathDijkstra(startVertex);
        double[] finalDistances = dijkstraResult.firstItem();
        //double correctDistances[] = {-1, 0.0, 1.0, 3.0, 23.0, 7.0, 16.0, 42.0, 31.0, 36.0};
        int[] predecessors = dijkstraResult.secondItem();

        for (int i = 1; i < G.numVertices() + 1; i++) {
            System.out.println("The length of the shortest path from vertex " + startVertex + " to vertex " + i + " is: " + finalDistances[i]);
            //			if( correctDistances[i] != finalDistances[i] )
            //				System.out.println("Length of path from to vertex " + i + " is incorrect; should be " + correctDistances[i] + ".");
            //			else {
            System.out.println(extractPath(predecessors, i));
            //			}
        }
    }

}
