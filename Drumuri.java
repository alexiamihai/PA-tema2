import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class Drumuri {
	static class Edge implements Comparable<Edge> {
		int target;
		long cost;

		Edge(int target, long cost) {
			this.target = target;
			this.cost = cost;
		}

		public int compareTo(Edge other) {
			return Long.compare(this.cost, other.cost);
		}
	}

	static List<List<Edge>> graph;

	// dijkstra pentru a calcula distentele minime de la sursa la toate nodurile
	static long[] dijkstra(int source, int N, List<List<Edge>> graph) {
		// am initializat vectorul pentru distante minime cu valori maxime
		long[] distance = new long[N + 1];
		Arrays.fill(distance, Long.MAX_VALUE);
		distance[source] = 0;

		// coada de prioritati pentru nodurile de procesat
		PriorityQueue<Edge> pq = new PriorityQueue<>();
		// am adaugat sursa in coada
		pq.offer(new Edge(source, 0));

		// procesez nodurile din coada
		while (!pq.isEmpty()) {
			Edge edge = pq.poll();
			int node = edge.target;
			long cost = edge.cost;

			// cost > distanta minima pe care o aveam deja in vector => putem
			// trece mai departe
			if (cost > distance[node]) {
				continue;
			}

			// am iterat prin vecinii nodului curent
			for (Edge next : graph.get(node)) {
				// am calculat noul cost si daca acesta era mai mic decat distanta
				// am actualizat vectorul de disstante si am adaugat vecinul in coada
				long newCost = cost + next.cost;
				if (newCost < distance[next.target]) {
					distance[next.target] = newCost;
					pq.offer(new Edge(next.target, newCost));
				}
			}
		}

		return distance;
	}

	// functie pentru inversarea grafului, pentru a calcula distantele de la toate
	// nodurile din graf la nodul z
	static List<List<Edge>> reverseGraph(int N) {
		List<List<Edge>> reversedGraph = new ArrayList<>();
		for (int i = 0; i <= N; i++) {
			reversedGraph.add(new ArrayList<>());
		}

		// am iterat prin toate nodurile grafului initial si am adaugat muchii inversate
		for (int i = 1; i <= N; i++) {
			for (Edge edge : graph.get(i)) {
				reversedGraph.get(edge.target).add(new Edge(i, edge.cost));
			}
		}

		return reversedGraph;
	}

	public static void main(String[] args) throws IOException {
		try {
			File file = new File("drumuri.in");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String[] line = reader.readLine().split(" ");
			int N = Integer.parseInt(line[0]);
			int M = Integer.parseInt(line[1]);

			graph = new ArrayList<>();
			for (int i = 0; i <= N; i++) {
				graph.add(new ArrayList<>());
			}

			for (int i = 0; i < M; i++) {
				line = reader.readLine().split(" ");
				int a = Integer.parseInt(line[0]);
				int b = Integer.parseInt(line[1]);
				long c = Long.parseLong(line[2]);
				graph.get(a).add(new Edge(b, c));
			}

			line = reader.readLine().split(" ");
			int x = Integer.parseInt(line[0]);
			int y = Integer.parseInt(line[1]);
			int z = Integer.parseInt(line[2]);

			// am calculay distantele minime de la x și y la toate celelalte noduri
			long[] distFromX = dijkstra(x, N, graph);
			long[] distFromY = dijkstra(y, N, graph);

			// am inversat graful pentru a calcula distantele catre z
			List<List<Edge>> reversedGraph = reverseGraph(N);
			long[] distToZ = dijkstra(z, N, reversedGraph);

			// am calculat costul total al drumurilor de la x la z și de la y la z,
			// urmand ca apoi sa compar costurile noilor drumuri gasite cu acesta
			long cost = distFromX[z] + distFromY[z];

			// daca oricare dintre drumuri nu e valid, setez costul la maxim
			if (cost >= Long.MAX_VALUE || distFromX[z] == Long.MAX_VALUE
				|| distFromY[z] == Long.MAX_VALUE) {
				cost = Long.MAX_VALUE;
			} else {
				// am verificat fiecare muchie pentru a gasi costul minim
				for (int i = 1; i <= N; i++) {
					List<Edge> edges = graph.get(i);
					for (Edge edge : edges) {
						int j = edge.target;
						// am verificat mai intai daca exista drumuri de la x si y la muchie
						// si de la muchie la z
						if (distFromX[i] != Long.MAX_VALUE && distFromY[i] != Long.MAX_VALUE
							&& distToZ[j] != Long.MAX_VALUE) {
							// noul cost = distanta de la x la muchie + distanta de la y la muchie
							// + costul muchiei + distanta pana la nodul z
							long tempCost = distFromX[i] + distFromY[i] + edge.cost + distToZ[j];
							if (tempCost < cost) {
								cost = tempCost;
							}
						}
					}
				}
			}
			PrintWriter pw = new PrintWriter(new File("drumuri.out"));
			// am afisat costul si -1 daca nu exista un drum valid
			pw.println(cost == Long.MAX_VALUE ? -1 : cost);
			reader.close();
			pw.close();
		} catch (IOException e) {
			System.out.println("File not found.");
			e.printStackTrace();
		}
	}
}
