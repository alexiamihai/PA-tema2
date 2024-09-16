import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Numarare {

	// reprezentarea grafurilor ca harti
	private static Map<Integer, List<Integer>> graph1 = new HashMap<>();
	private static Map<Integer, List<Integer>> graph2 = new HashMap<>();

	// functie pentru sortarea topologica
	private static List<Integer> topologicalSort() {
		Map<Integer, Integer> inDegree = new HashMap<>();
		// am initializat gradul de intrare pentru fiecare nod cu 0
		for (Integer node : graph1.keySet()) {
			inDegree.put(node, 0);
		}
		// am calculat gradul de intrare pentru fiecare nod
		for (Integer start : graph1.keySet()) {
			// pentru fiecare vecin, verific daca l-am adaugat deja in inDegree
			for (Integer neighbour : graph1.get(start)) {
				if (!inDegree.containsKey(neighbour)) {
					// daca nu, il initializez cu 0, inainte de a-l incrementa
					inDegree.put(neighbour, 0);
				}
				// incrementez gradul de intrare
				inDegree.put(neighbour, inDegree.get(neighbour) + 1);
			}
		}

		// pentru nodurile care pot fi procesate primele in sortarea topologica
		Deque<Integer> zeroInDegree = new ArrayDeque<>();
		// am iterat prin toate perechile (nod, grad) din inDegree
		for (Map.Entry<Integer, Integer> entry : inDegree.entrySet()) {
			Integer node = entry.getKey();
			Integer degree = entry.getValue();

			// daca gradul de intrare este 0, adaug nodul in coada
			if (degree == 0) {
				zeroInDegree.push(node);
			}
		}

		List<Integer> sortedList = new ArrayList<>();
		while (!zeroInDegree.isEmpty()) {
			// am scos nodul din coada si l-am adaugat in lista
			Integer node = zeroInDegree.pop();
			sortedList.add(node);
			// pentru fiecare vecin al nodului curent, i-am decrementat gradul de intrare
			for (Integer neighbor : graph1.getOrDefault(node, new ArrayList<>())) {
				int newDegree = inDegree.get(neighbor) - 1;
				// am actualizat gradul de intrare
				inDegree.put(neighbor, newDegree);
				// si daca gradul lui de intrare a devenit 0, l-am adaugat in coada
				if (newDegree == 0) {
					zeroInDegree.push(neighbor);
				}
			}
		}
		// am inversat sortarea
		Collections.reverse(sortedList);
		return sortedList;
	}

	public static void main(String[] args) {
		try {
			File file = new File("numarare.in");
			BufferedReader reader = new BufferedReader(new FileReader(file));


			// citirea datelor din fisier si crearea grafurilor pe baza acestora
			String[] line = reader.readLine().split(" ");

			int N = Integer.parseInt(line[0]);
			final int M = Integer.parseInt(line[1]);
			// am initializat un vector dp care reprezinta numarul de lanturi elementare
			// de la nodul 1 la nodul i+1 comune in ambele grafuri
			int[] dp = new int[N];
			Arrays.fill(dp, 0);
			// pornim de la nodul final
			dp[N - 1] = 1;

			for (int i = 0; i < N; ++i) {
				graph1.put(i, new ArrayList<>());
				graph2.put(i, new ArrayList<>());
			}

			for (int i = 0; i < M; i++) {
				line = reader.readLine().split(" ");
				int u = Integer.parseInt(line[0]);
				int v = Integer.parseInt(line[1]);
				graph1.get(u).add(v);
			}
			for (int i = 0; i < M; i++) {
				line = reader.readLine().split(" ");
				int u = Integer.parseInt(line[0]);
				int v = Integer.parseInt(line[1]);
				graph2.get(u).add(v);
			}

			// am sortat topologic primul graf
			List<Integer> sortedList = new ArrayList<>();
			sortedList = topologicalSort();
			for (Integer node : sortedList) {
				// lista de vecini pentru nodul curent
				List<Integer> neighbors = graph1.get(node);
				if (neighbors != null) {
					// am parcurs lista de vecini
					for (Integer neighbor : neighbors) {
						// lista de vecini pentru nodul din cel de-al doilea graf
						List<Integer> neighbors2 = graph2.get(node);
						// verific daca vecinul se afla si in al doilea graf
						if (neighbors2 != null && neighbors2.contains(neighbor)) {
							// adaug la nr de lanturi pentru nodul curent, nr de lanturi care
							// ajung la vecin
							dp[node - 1] += dp[neighbor - 1];
							dp[node - 1] %= 1000000007;
						}
					}
				}
			}

			PrintWriter pw = new PrintWriter(new File("numarare.out"));
			pw.println(dp[0]);
			reader.close();
			pw.close();
		} catch (IOException e) {
			System.out.println("File not found.");
			e.printStackTrace();
		}
	}
}
