import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Trenuri {
	private static Map<Integer, List<Integer>> graph = new HashMap<>();
	private static Map<Integer, Integer> longestPath = new HashMap<>();
	private static Set<Integer> allNodes = new HashSet<>();
	private static Map<String, Integer> cityToNumber = new HashMap<>();
	private static int cityNumber = 0;

	// functie pentru sortarea topologica
	private static List<Integer> topologicalSort() {
		Map<Integer, Integer> inDegree = new HashMap<>();
		// am initializat gradul de intrare pentru fiecare nod cu 0
		for (Integer node : graph.keySet()) {
			inDegree.put(node, 0);
		}
		// am calculat gradul de intrare pentru fiecare nod
		for (Integer start : graph.keySet()) {
			// pentru fiecare vecin, verific daca l-am adaugat deja in inDegree
			for (Integer neighbour : graph.get(start)) {
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
			for (Integer neighbor : graph.getOrDefault(node, new ArrayList<>())) {
				int newDegree = inDegree.get(neighbor) - 1;
				// am actualizat gradul de intrare
				inDegree.put(neighbor, newDegree);
				// si daca gradul lui de intrare a devenit 0, l-am adaugat in coada
				if (newDegree == 0) {
					zeroInDegree.push(neighbor);
				}
			}
		}
		return sortedList;
	}

	public static void main(String[] args) {
		try {

			File file = new File("trenuri.in");
			BufferedReader reader = new BufferedReader(new FileReader(file));

			// Citirea primului rând din fișier
			String[] firstLine = reader.readLine().split(" ");
			String startCity = firstLine[0];
			int startCityNumber = getCityNumber(startCity);

			String endCity = firstLine[1];
			final int endCityNumber = getCityNumber(endCity);
			int M = Integer.parseInt(reader.readLine().trim());

			for (int i = 0; i < M; i++) {
				String[] line = reader.readLine().split(" ");
				String x = line[0];
				String y = line[1];
				// am asociat numele fiecarui oras cu un numar
				int xNumber = getCityNumber(x);
				int yNumber = getCityNumber(y);
				// si am daugat fiecare oras nou in graf si in lista cu toate orasele
				if (!graph.containsKey(xNumber)) {
					graph.put(xNumber, new ArrayList<>());
				}
				graph.get(xNumber).add(yNumber);
				allNodes.add(xNumber);
				allNodes.add(yNumber);
			}

			// am sortat nodurile topologic
			List<Integer> sortedNodes = topologicalSort();
			// am initializat longestPath cu -1 pt toate nodurile, in afara de cel de start
			for (Integer node : sortedNodes) {
				longestPath.putIfAbsent(node, -1);
			}
			longestPath.put(startCityNumber, 0);

			for (Integer node : sortedNodes) {
				// verificare - cale valida
				if (longestPath.get(node) != -1) {
					// am obtinut lungimea curenta a celui mai lung drum pana la nod
					int currentLength = longestPath.get(node);
					for (Integer neighbor : graph.getOrDefault(node, new ArrayList<>())) {
						// am verificat aca pot gasi un drum mai lung pt vecinul curent
						if (longestPath.get(neighbor) < currentLength + 1) {
							longestPath.put(neighbor, currentLength + 1);
						}
					}
				}
			}

			reader.close();
			PrintWriter pw = new PrintWriter(new FileWriter("trenuri.out"));

			pw.println(longestPath.getOrDefault(endCityNumber, 0) + 1);
			pw.close();
		} catch (IOException e) {
			System.out.println("File not found.");
			e.printStackTrace();
		}
	}

	private static int getCityNumber(String cityName) {
		// verific daca orasul exista deja in map
		if (!cityToNumber.containsKey(cityName)) {
			// daca nu ii atribui un nr nou si il adaug
			cityToNumber.put(cityName, cityNumber);
			cityNumber++;
		}
		return cityToNumber.get(cityName);
	}

}
