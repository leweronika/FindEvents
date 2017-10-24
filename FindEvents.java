import java.io.* ;
import java.util.* ;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Comparator;
import java.util.TreeMap;

/**
 * This class is designed to construct a node graph of locations, each of which
 * may or may not have an event associated with it. When passed an input set of
 * coordinates, the class find the five closest events, and finds the cheapest
 * ticket if there are any available
 *
 * @author Weronika Castiglione
 *
 */

public class FindEvents {

	/**
  * This method involves the Breadth First Search algorithm, this searches
  * through the node graph, and checks to see if an event exists with the
  * same label in a HashMap. If it does, search through list of associated
  * tickets to find the cheapest. Also find distance of event from input node
  * and store these values along with event number in another HashMap.
  *
  * @param s The input vertex from which the search begins
  * @param event HashMap that contains a list of tickets for an event, with the
  * coordinates as the key
  * @param inputX,inputY x and y coordinate of original input node
  * @param Adj linked list of vertices to iterate through for the BFS
  * @param eventG the node graph with all associated vertices and edges
  * @param numberOfEvents number of events in the graph
  *
  * @return treeMap, a sorted map of the closest events to the input coordinate
  */
	public static Map < Double, List < Double >> BFS(Vertex s, Map < String, List < Double >> event, int xInput, int yInput, Vertex[] vertex, LinkedList < Vertex > [] Adj, Graph eventG, int numberOfEvents) {
		// Mark all the vertices as not visited(By default
		// set as false)
		boolean[] visited = new boolean[441];
		for (int v = 0; v < visited.length; v++)
		visited[v] = false;

		//HashMap that will hold distance as the key, and event number & cheapest
		//ticket as values
		Map < Double,
		List < Double >> cheapestTicket = new HashMap < Double,
		List < Double >> ();

		// Create a queue for BFS
		LinkedList < Vertex > queue = new LinkedList < Vertex > ();

		// Mark the current node as visited and enqueue it
		int element = Arrays.asList(vertex).indexOf(eventG.getVertex(s.getLabel()));
		visited[element] = true;
		queue.add(s);

		while ((queue.size() != 0)) {
			// Dequeue a vertex from queue and print it
			s = queue.poll();
			//check if event exists at node, if no move on
			if (event.get(s.getLabel()) != null) {
				//check if there exists any tickets for the event
				double lowestTicket;
				if (event.get(s.getLabel()).size() > 1) {
					//if there are tickets, set first ticket as lowest
					lowestTicket = event.get(s.getLabel()).get(1);
					//loop through ticket values, and replace lowestTicket value if there
					//are lower values
					for (int k = 1; k < event.get(s.getLabel()).size(); k++) {
						if (lowestTicket > event.get(s.getLabel()).get(k))
              lowestTicket = event.get(s.getLabel()).get(k);
					} //end for
				} //end if exists event
				//else mark lowestTicket = 0 to denote that there are no tickets
				else {
					lowestTicket = 0;
				} //end else

				//find distance using Pythagoras
				double distance = Math.sqrt(Math.pow((s.getA() - xInput), 2) + Math.pow((s.getB() - yInput), 2));
				//initial new List to store event number & lowestTicket
				List < Double > info = new ArrayList < Double > ();
				info.add(event.get(s.getLabel()).get(0));
				info.add(lowestTicket);
				//store distance(key) and list(values)
				cheapestTicket.put(distance, info);
			}

			// Get all adjacent vertices of the dequeued vertex s
			// If a adjacent has not been visited, then mark it
			// visited and enqueue it
			Iterator < Vertex > i = Adj[Arrays.asList(vertex).indexOf(eventG.getVertex(s.getLabel()))].listIterator();
			while (i.hasNext()) {
				Vertex n = i.next();
				if (!visited[Arrays.asList(vertex).indexOf(eventG.getVertex(n.getLabel()))]) {
					visited[Arrays.asList(vertex).indexOf(eventG.getVertex(n.getLabel()))] = true;
					queue.add(n);
				} //end check if node has been visited
			} //end while through linked list
		} //end while queue is not empty

		//create new tree map and transfer HashMap values to it. Keys, and therefore
		//distances are now sorted into ascending order
		Map < Double,
		List < Double >> treeMap = new TreeMap < Double,
		List < Double >> (cheapestTicket);

		return treeMap;
	} //end BFS

	/**
  * Main method, initialises a node graph of vertices using axis of -10 to 10, and
  * add the appropriate edges for each node. Generate random data to populate
  * the graph. Print out five closest results if they exist from treeMap
  *
  */
	public static void main(String[] args) {
		try {
			//check if the correct number of arguments have been given
			if (args.length != 2) throw new IllegalArgumentException("Please enter exactly 2 values");

			//take two command line arguments as the input coordinate
			int inputX = Integer.parseInt(args[0]);
			int inputY = Integer.parseInt(args[1]);

			//check if given coordinate is within the required limits
			if (inputX > 10 || inputX < -10) throw new IllegalArgumentException("Please only enter a value between -10 and 10");
			if (inputY > 10 || inputY < -10) throw new IllegalArgumentException("Please only enter a value between -10 and 10");

			//initialise new graph
			Graph eventGraph = new Graph();

			//create array to hold vertices in graph, and LinkedLists for BFS algorithm
			Vertex[] vertices = new Vertex[441];
			LinkedList < Vertex > [] adj = new LinkedList[441];
			for (int i = 0; i < 441; i++)
			adj[i] = new LinkedList();

			int index = 0;
			//create a vertex for each coordinate and add to graph
			for (int i = -10; i < 11; i++) {
				for (int j = -10; j < 11; j++) {
					vertices[index] = new Vertex(i, j);
					eventGraph.addVertex(vertices[index], true);
					adj[index].add(vertices[index]);
					index++;
				} //inner loop
			} //outer loop
			//add edges for all the vertices
			for (int k = 0; k < vertices.length; k++) {
				if (vertices[k].getA() != -10) {
					eventGraph.addEdge(vertices[k], eventGraph.getVertex((vertices[k].getA() - 1) + "" + (vertices[k].getB()) + ""));
					adj[k].add(eventGraph.getVertex((vertices[k].getA() - 1) + "" + (vertices[k].getB()) + ""));
				}
				if (vertices[k].getA() != 10) {
					eventGraph.addEdge(vertices[k], eventGraph.getVertex((vertices[k].getA() + 1) + "" + (vertices[k].getB()) + ""));
					adj[k].add(eventGraph.getVertex((vertices[k].getA() + 1) + "" + (vertices[k].getB()) + ""));
				}
				if (vertices[k].getB() != -10) {
					eventGraph.addEdge(vertices[k], eventGraph.getVertex((vertices[k].getA()) + "" + (vertices[k].getB() - 1) + ""));
					adj[k].add(eventGraph.getVertex((vertices[k].getA()) + "" + (vertices[k].getB() - 1) + ""));
				}
				if (vertices[k].getB() != 10) {
					eventGraph.addEdge(vertices[k], eventGraph.getVertex((vertices[k].getA()) + "" + (vertices[k].getB() + 1) + ""));
					adj[k].add(eventGraph.getVertex((vertices[k].getA()) + "" + (vertices[k].getB() + 1) + ""));
				}
			} //end addEdge for loop

			//make a hashmap to store the event details in. the key is the label of each vertex
			//where there exists an event, and the values are randomly generated ticket prices
			Map < String,
			List < Double >> eventInfo = new HashMap < String,
			List < Double >> ();

			// generate a random number eventNo between 1 and 441, such that there is at least
			//  one event in the graph, and a max of one event for each vertex
			int eventNo = ThreadLocalRandom.current().nextInt(1, 441 + 1);

			//make array to store labels of coordinates for each event
			String[] coordinatesOfEvents = new String[eventNo];
			boolean repeatedCoordinate;

			//for the number of events, generate random coordinates for each, ensuring that
			//there is at most one event at a coordinate
			for (int m = 0; m < eventNo; m++) {
				//initilaise variables for loops
				repeatedCoordinate = true;
				String xYLabel = "";
				while (repeatedCoordinate) {
					repeatedCoordinate = false;
					int xCoordinate = ThreadLocalRandom.current().nextInt( - 10, 10 + 1);
					int yCoordinate = ThreadLocalRandom.current().nextInt( - 10, 10 + 1);
					xYLabel = xCoordinate + "" + yCoordinate + "";

					//check if coordinate already has an event, repeat random selection if true
					for (int n = 0; n < eventNo; n++) {
						if (xYLabel == coordinatesOfEvents[n])
              repeatedCoordinate = true;
					} //end for
				} //end while
				coordinatesOfEvents[m] = xYLabel;
			} //end for
			//for each event generate random number of tickets, all with random prices
			for (int r = 0; r < eventNo; r++) {
				List < Double > tickets = new ArrayList < Double > ();
				tickets.add((double) r);
				int noOfTickets = ThreadLocalRandom.current().nextInt(0, eventNo + 1);
				for (int p = 0; p < noOfTickets; p++) {
					//generate random prices between $0.99 and $1000
					double ticketValue = ThreadLocalRandom.current().nextDouble(0.99, 1000.0 + 1.0);
					tickets.add(ticketValue);
				} //end for
				//store values in HashMap
				eventInfo.put(coordinatesOfEvents[r], tickets);
			} //end for to generate tickets
			//initialise map to hold returned method values
			Map < Double,
			List < Double >> returnedTicket;
			String inputCoordinateLabel = inputX + "" + inputY + "";

			//call BFS method
			returnedTicket = BFS(eventGraph.getVertex(inputCoordinateLabel), eventInfo, inputX, inputY, vertices, adj, eventGraph, eventNo);

			//create set of keys(distances), and iterate through them to get their values
			Set < Double > keys = returnedTicket.keySet();
			Iterator < Double > itr = keys.iterator();
			int number = 0;
			int check = keys.size();
			int limit;
			//set limit to print out closest five events, or if there are fewer than 5
			//events print all
			if (check <= 5)
        limit = check;
			else
        limit = 5;

			//print results
			System.out.printf("Closest Events to (%d,%d):\n", inputX, inputY);
			while (itr.hasNext() && (number < limit)) {
				if (returnedTicket.get(itr.next()).get(1) == 0)
          System.out.printf("Event %03.0f - %s, Distance %.2f\n", returnedTicket.get(itr.next()).get(0), "No Tickets Available", itr.next());
				else
          System.out.printf("Event %03.0f - $%.2f, Distance %.2f\n", returnedTicket.get(itr.next()).get(0), returnedTicket.get(itr.next()).get(1), itr.next());

				number++;
			} //end while to print out results
		}
		//catch and print exceptions
		catch(Exception exception) {
			System.err.println(exception.getMessage());
		}
	} //main
} //FindEvents
