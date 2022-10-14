/*
Sunny Ka Patel
SDK438
11267665
CMPT 280.A8
 */

import lib280.graph.Vertex280;
import lib280.graph.WeightedEdge280;
import lib280.graph.WeightedGraphAdjListRep280;
import lib280.tree.ArrayedMinHeap280;

public class Kruskal {
	
	public static WeightedGraphAdjListRep280<Vertex280> minSpanningTree(WeightedGraphAdjListRep280<Vertex280> G) {

		WeightedGraphAdjListRep280<Vertex280> minST = new WeightedGraphAdjListRep280<>(G.capacity(), false);
		minST.ensureVertices(G.numVertices());

		UnionFind280 UF = new UnionFind280(G.numVertices());
		ArrayedMinHeap280<WeightedEdge280<Vertex280>> heap = new ArrayedMinHeap280<>(G.numVertices() * G.numVertices());

		G.goFirst();
		while(G.itemExists()){
			G.eGoFirst(G.item());
			while(G.eItemExists()){
				heap.insert(G.eItem());
				G.eGoForth();
			}
			G.goForth();
		}

		while(!heap.isEmpty()){
			WeightedEdge280<Vertex280> edge = heap.item();
			heap.deleteItem();

			int A = edge.firstItem().index();
			int B = edge.secondItem().index();

			if(UF.find(A) != UF.find(B)){
				minST.addEdge(A, B);
				minST.setEdgeWeight(A, B,edge.getWeight());
				UF.union(A, B);
			}
		}
		return minST;
	}
	
	
	public static void main(String args[]) {
		WeightedGraphAdjListRep280<Vertex280> G = new WeightedGraphAdjListRep280<Vertex280>(1, false);
		G.initGraphFromFile("Kruskal-template/mst.graph");
		System.out.println(G);
		
		WeightedGraphAdjListRep280<Vertex280> minST = minSpanningTree(G);
		
		System.out.println(minST);
	}
}


