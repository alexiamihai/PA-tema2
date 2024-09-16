import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scandal {
	// reprezentarea grafurilor ca harti
	private static Map<Integer, List<Integer>> graph1 = new HashMap<>();
	private static Map<Integer, List<Integer>> reverse_graph = new HashMap<>();
	private static List<Integer> order = new ArrayList<>(); // pentru a retine ordinea topologica
	private static int[] component; // pentru a marca componentele tare conexe
	private static boolean[] visited; // pentru a marca nodurile vizitate

	// dfs pentru a determina ordinea de finalizare a nodurilor
	private static void dfs1(int node) {
		visited[node] = true;
		for (int neigh : graph1.get(node)) {
			if (!visited[neigh]) {
				dfs1(neigh);
			}
		}
		// adaug nodul in lista order daca toti vecinii lui au fost explorati
		order.add(node);
	}

	// dfs pentru a marca componentele tare conexe
	private static void dfs2(int node, int comp) {
		// atribui componenta conexa nodului
		component[node] = comp;
		for (int neigh : reverse_graph.get(node)) {
			// daca vecinul nu a fost atribuit unei componente, apelez recursiv
			// functia pentru el
			if (component[neigh] == -1) {
				dfs2(neigh, comp);
			}
		}
	}

	public static void main(String[] args) {
		try {
			File file = new File("scandal.in");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			PrintWriter pw = new PrintWriter(new FileWriter("scandal.out"));

			String[] firstLine = reader.readLine().split(" ");
			int N = Integer.parseInt(firstLine[0]);
			final int M = Integer.parseInt(firstLine[1]);
			component = new int[2 * N + 1];
			visited = new boolean[2 * N + 1];
			// initializez listele de adiacenta pentru cele 2 grafuri
			for (int i = 1; i <= 2 * N; ++i) {
				graph1.put(i, new ArrayList<>());
				reverse_graph.put(i, new ArrayList<>());
			}

			for (int i = 0; i < M; ++i) {
				String[] line = reader.readLine().split(" ");
				int x = Integer.parseInt(line[0]);
				int y = Integer.parseInt(line[1]);
				int c = Integer.parseInt(line[2]);

				// am ales sa reprezint numerele negative ca nr pozitiv + N
				int nx = x + N; // negativ x
				int ny = y + N; // negativ y

				// adaug muchii in graf in functie de conditii
				if (c == 0) {
					graph1.get(nx).add(y);
					reverse_graph.get(y).add(nx);
					graph1.get(ny).add(x);
					reverse_graph.get(x).add(ny);
				} else if (c == 1) {
					graph1.get(nx).add(ny);
					reverse_graph.get(ny).add(nx);
					graph1.get(y).add(x);
					reverse_graph.get(x).add(y);
				} else if (c == 2) {
					graph1.get(ny).add(nx);
					reverse_graph.get(nx).add(ny);
					graph1.get(x).add(y);
					reverse_graph.get(y).add(x);
				} else if (c == 3) {
					graph1.get(x).add(ny);
					reverse_graph.get(ny).add(x);
					graph1.get(y).add(nx);
					reverse_graph.get(nx).add(y);
				}
			}

			// determin ordinea topologica printr-un dfs pe graf
			Arrays.fill(visited, false);
			for (int i = 1; i <= 2 * N; ++i) {
				if (!visited[i]) {
					dfs1(i);
				}
			}

			// determin componentele tare conexe, traversand graful inversat
			Arrays.fill(component, -1);
			// contor pt nr componentei
			int nr_comp = 0;
			for (int i = order.size() - 1; i >= 0; --i) {
				int node = order.get(i);
				// daca nodul nu a fost atribuit unei componente, aplic dfs2
				// si cresc numarul ei
				if (component[node] == -1) {
					dfs2(node, nr_comp++);
				}
			}

			// verific validitatea solutiei
			boolean isValid = true;
			List<Integer> answer = new ArrayList<>();
			for (int i = 1; i <= N; ++i) {
				// daca nodul si negatia sa se afla in aceeasi componenta conexa
				// solutia nu e corecta
				if (component[i] == component[i + N]) {
					isValid = false;
					break;
				}
				if (component[i] > component[i + N]) {
					answer.add(i);
				}
			}

			if (isValid) {
				pw.println(answer.size());
				for (int x : answer) {
					pw.println(x);
				}
			} else {
				pw.println("0");
			}

			reader.close();
			pw.close();

		} catch (IOException e) {
			System.out.println("File not found.");
			e.printStackTrace();
		}
	}
}
