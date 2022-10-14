/*
Sunny Ka Patel
SDK438
11267665
CMPT 280.A7
 */

import lib280.base.CursorPosition280;
import lib280.dispenser.ArrayedStack280;
import lib280.graph.Edge280;
import lib280.graph.GraphMatrixRep280;
import lib280.list.LinkedList280;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class QuestProgression {
	
	// File format for quest data:
	// First line: Number of quests N
	// Next N lines consist of the following items, separated by commas:
	//     quest ID, quest name, quest area, quest XP
	//     (Quest ID's must be between 1 and N, but the line for each quest IDs may appear in any order).
	// Remaining lines consist of a comma separated pair of id's i and j where i and j are quest IDs indicating
	// that quest i must be done before quest j (i.e. that (i,j) is an edge in the quest graph).
	
	/**
	 * Read the quest data from a text file and build a graph of quest prerequisites.
	 * @param filename Filename from which to read quest data.
	 * @return A graph representing quest prerequisites.  If quest with id i must be done before a quest with id j, then there is an edge in the graph from vertex i to vertex j.
	 */
	public static GraphMatrixRep280<QuestVertex, Edge280<QuestVertex>> readQuestFile(String filename) {
		Scanner infile;
		
		// Attempt to open the input filename.
		try {
			infile = new Scanner(new File(filename));
		} catch (FileNotFoundException e) {
			System.out.println("Error: Unable to open" + filename);
			e.printStackTrace();
			return null;
		}
		
		// Set the delimiters for parsing to commas, and vertical whitespace.
		infile.useDelimiter("[,\\v]");

		// Read the number of quests for which there is data.
		int numQuests = infile.nextInt();
		
		// read the quest data for each quest.
		LinkedList280<Quest> questList = new LinkedList280<Quest>();
		for(int i=0; i < numQuests; i++) {
			int qId = infile.nextInt();
			String qName = infile.next();
			String qArea = infile.next();
			int qXp = infile.nextInt();		
			questList.insertLast(new Quest(qId, qName, qArea, qXp));
		}
	
		// Make a graph with the vertices we created from the quest data.
		GraphMatrixRep280<QuestVertex, Edge280<QuestVertex>> questGraph = 
				new GraphMatrixRep280<QuestVertex, Edge280<QuestVertex>> (numQuests, true, "QuestVertex", "lib280.graph.Edge280");
		
		// Add enough vertices for all of our quests.
		questGraph.ensureVertices(numQuests);
		
		// Store each quest in a different vertex.  The quest with id i gets stored vertex i.
		questList.goFirst();
		while(questList.itemExists()) {
			questGraph.vertex(questList.item().id()).setQuest(questList.item());
			questList.goForth();
		}
		
		// Continue reading the input file for the quest prerequisite informaion and add an edge to the graph
		// for each prerequisite.
		while(infile.hasNext()) {
			questGraph.addEdge(infile.nextInt(), infile.nextInt());
		}
				
		infile.close();
		
		return questGraph;
	}
	

	/**
	 * Test whether vertex v has incoming edges or not
	 * @param G A graph.
	 * @param v The integer identifier of a node in G (corresponds to quest ID)
	 * @return Returns true if v has no incoming edges.  False otherwise.
	 */
	public static boolean hasNoIncomingEdges(GraphMatrixRep280<QuestVertex,Edge280<QuestVertex>> G, int v) {
		// save the current position in G
		CursorPosition280 savedPosition = G.currentPosition();

		// boolean to store the result
		boolean result = true;

		for (int i = 1; i <= G.numVertices() && result; i++) {
			// position the edge cursor to edge connecting vertices i and v
			G.eSearch(G.vertex(i), G.vertex(v));

			// check if the edge cursor is on an edge or not and store the result
			result = !G.eItemExists();
		}

		// restore the cursor position
		G.goPosition(savedPosition);
		return result;
	}
	
	
	/**
	 * Perform a topological sort of the quests in the quest prerequisite graph G, with priority given
	 * to the highest experience value among the available quests.
	 * @param G The graph on which to perform a topological sort.
	 * @return A list of quests that is the result of the topological sort, that is, the order in which the quests should be done if we always pick the available quest with the largest XP reward first.
	 */
	public static LinkedList280<Quest> questProgression(GraphMatrixRep280<QuestVertex,Edge280<QuestVertex>> G) {
		// a linked-list to store the quests
		LinkedList280<Quest> list = new LinkedList280<>();

		// a stack to store quests with no incoming edges
		ArrayedStack280<Quest> stack = new ArrayedStack280<>(G.numVertices());

		for (int i = 1; i <= G.numVertices(); i++) {
			// check if a quest has no incoming edges and
			// if a quest does not then insert it to the stack
			if (hasNoIncomingEdges(G, i)) {
				stack.insert(G.vertex(i).quest());
			}
		}

		while (!stack.isEmpty()) {
			// get the current quest from the stack
			Quest currentQuest = stack.item();

			// delete the quest node from the stack and
			// add it to the end of the list
			stack.deleteItem();
			list.insertLast(currentQuest);

			// move the edge cursor to the first edge of the currentQuest vertex
			G.eGoFirst(G.vertex(currentQuest.id()));
			while (G.eItemExists()) {
				// get the index of the adjacent vertex and delete the edge
				int index = G.eItemAdjacentIndex();
				G.deleteEItem();

				// if the vertex has no incoming edges left then insert it to the stack
				if (hasNoIncomingEdges(G, index)) {
					stack.insert(G.vertex(index).quest());
				}
			}
		}
		// if the graph has an edge left then throw exception
		if (G.numEdges() > 0) {
			throw new RuntimeException("The graph had at least one cycle!");
		}
		return list;

	}
	
	public static void main(String args[]) {
		// Read the quest data and construct the graph.
		
		// If you get an error reading the file here and you're using Eclipse, 
		// remove the 'QuestPrerequisites-Template/' portion of the filename.
		GraphMatrixRep280<QuestVertex,Edge280<QuestVertex>> questGraph = readQuestFile("QuestPrerequisites-Template/quests16.txt");
		
		// Perform a topological sort on the graph.
		LinkedList280<Quest> questListForMaxXp = questProgression(questGraph);
		
		// Display the quests to be completed in the order determined by the topologial sort.
		questListForMaxXp.goFirst();
		while(questListForMaxXp.itemExists()) {
			System.out.println(questListForMaxXp.item());
			questListForMaxXp.goForth();
		}
				
	}
}
