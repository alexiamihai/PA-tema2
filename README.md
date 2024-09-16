# TEMA 2 - PA
## Politisti, Programatori si Scandal


Pentru toate problemele am folosit aceeasi structura de graf:

```java
private static Map<Integer, List<Integer>> graph1 = new HashMap<>();
```

Cu exceptia problemei 3, pentru care mi s-a parut mai usor sa implementez o clasa Edge
pentru a reprezenta muchiile si costurile lor.

```java

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

```


## Problema 1: Numarare

### Algoritmul

1. **Citirea si Construirea Grafurilor**:
   - Citim datele din fisierul `numarare.in`.
   - Construim doua grafuri orientate aciclice folosind liste de adiacenta.

2. **Sortare Topologica**:
   - Realizam sortarea topologica a nodurilor in primul graf pentru a determina ordinea de procesare a nodurilor.
   - Am folosit algoritmul lui Kahn

3. **Calculul Lanturilor Comune**:
   - Utilizam un vector `dp` pentru a tine evidenta numarului de lanturi elementare care ajung la fiecare nod.
   - Initializam vectorul `dp` cu 0 si setam `dp[N-1]` la 1, deoarece nodul final este considerat un lant de sine statator.
   - Parcurgem nodurile in ordinea sortarii topologice si actualizam valorile in `dp` pe baza lanturilor comune din ambele grafuri.

4. **Scrierea Rezultatelor**:
   - Scriem rezultatul final in fisierul `numarare.out`.

### Complexitatea

- **Complexitatea Spatiala**: \(O(N + M)\), deoarece stocam grafurile si vectorul `dp`.
- **Complexitatea Temporala**: \(O(N + M)\), pentru citirea datelor, sortarea topologica si calculul lanturilor elementare comune.

### Referinte

Pentru sortarea topologica, am urmat pasii prezentati in laborator.


### Exemplu de Implementare

Implementarea noastra in Java citeste datele de intrare, construieste grafurile, efectueaza sortarea topologica
si calculeaza lanturile elementare comune. Rezultatul este apoi scris in fisierul de iesire.

### Diagrama Simplificata a Flow-ului

```plaintext
                 +-----------------+
                 | Citirea Datelor |
                 +--------+--------+
                          |
                          v
         +----------------+-----------------+
         | Construirea Grafurilor Orientate |
         +----------------+-----------------+
                          |
                          v
            +-------------+--------------+
            | Sortarea Topologica a Nodurilor |
            +-------------+--------------+
                          |
                          v
         +----------------+-----------------+
         | Calculul Lanturilor Comune dp[]  |
         +----------------+-----------------+
                          |
                          v
                 +--------+--------+
                 | Scrierea Rezultatului |
                 +-----------------+
```

### Concluzie

Problema de numarare a lanturilor elementare comune in doua grafuri orientate aciclice este rezolvata eficient
folosind tehnici de sortare topologica si programare dinamica. Implementarea propusa ofera o solutie clara si
optimizata, conform restrictiilor si specificatiilor date.


## Problema 2: Trenuri

### Algoritmul

1. **Citirea si Preprocesarea Datelor**:
    - Citim datele de intrare din fisierul `trenuri.in`.
    - Construim un graf orientat folosind un `Map<Integer, List<Integer>>` pentru a reprezenta conexiunile intre orase.
    - Asociem fiecarui oras un numar unic folosind un `Map<String, Integer>` pentru a facilita manipularea grafului.

   ```java

	private static int getCityNumber(String cityName) {
		// verific daca orasul exista deja in map
		if (!cityToNumber.containsKey(cityName)) {
			// daca nu ii atribui un nr nou si il adaug
			cityToNumber.put(cityName, cityNumber);
			cityNumber++;
		}
		return cityToNumber.get(cityName);
	}
   ```


2. **Sortare Topologica**:
    - Realizam sortarea topologica a nodurilor pentru a procesa graful in ordinea corecta si a nu parcurge iarasi
    rutele prin care deja am trecut.
    - Am utilizat tot algoritmul lui Kahn, ca si in problema precedenta.

3. **Calculul Lungimii Drumurilor**:
    - Initializam un `Map<Integer, Integer>` pentru a tine evidenta lungimii celui mai lung drum pana la fiecare nod.
    - Pornim de la nodul de start si actualizam lungimea drumului pentru fiecare vecin, folosind lista de noduri sortate topologic. Astfel, calculam distanta maxima de la nodul din care am plecat, la toate celelalte.

4. **Scrierea Rezultatelor**:
    - Scriem rezultatul final (numarul maxim de orase distincte vizitate) in fisierul `trenuri.out`.

### Complexitatea

- **Complexitatea Spatiala**: \(O(N + M)\), deoarece stocam graful, vectorul de lungimi ale drumurilor si alte structuri de date auxiliare.
- **Complexitatea Temporala**: \(O(N + M)\), pentru citirea datelor, sortarea topologica si calculul lungimilor drumurilor.

### Referinte
- [Find Longest Path in a Directed Acyclic Graph](https://www.geeksforgeeks.org/find-longest-path-directed-acyclic-graph/)

### Diagrama Simplificata a Flow-ului

```plaintext
                 +-----------------+
                 | Citirea Datelor |
                 +--------+--------+
                          |
                          v
         +----------------+-----------------+
         | Construirea Grafurilor Orientate |
         +----------------+-----------------+
                          |
                          v
            +-------------+--------------+
            | Sortarea Topologica a Nodurilor |
            +-------------+--------------+
                          |
                          v
         +----------------+-----------------+
         | Calculul Lungimilor Drumurilor  |
         +----------------+-----------------+
                          |
                          v
                 +--------+--------+
                 | Scrierea Rezultatului |
                 +-----------------+
```

### Concluzie

Problema de determinare a numarului maxim de orase distincte vizitate intr-o retea de trenuri orientata aciclica
este rezolvata eficient folosind tehnici de sortare topologica si programare dinamica. Implementarea propusa
ofera o solutie clara si optimizata, conform restrictiilor si specificatiilor date.


## Problema 3: Drumuri Obligatorii

### Algoritmul

1. **Citirea si Preprocesarea Datelor**:
    - Citim datele de intrare din fisierul `drumuri.in`.
    - Construim un graf orientat folosind o lista de adiacenta pentru a reprezenta conexiunile intre noduri.

2. **Algoritmul Dijkstra**:
    - Implementam algoritmul Dijkstra pentru a calcula distantele minime dintre 2 noduri.

3. **Calculul Lungimilor Drumurilor**:
    - Calculam distantele minime de la x si y la toate celelalte noduri folosind Dijkstra.
    - Inversam graful si calculam distantele minime de la z la toate nodurile.
    - Cum poate exista un drum de cost mai mic decat cel obtinut adunand costurile minime ale celor 2 drumuri
    obtinute direct cu dijkstra de la x la z si de la y la z, parcurgem muchiile pentru a vedea daca exista 2
    drumuri cu muchii comune si cost mai mic.

    -Pentru a lua in considerare o muchie am calculat suma costurilor:
      -> drumului de la x la primul nod al muchiei
      -> drumului de la y la primul nod al muchiei
      -> muchiei
      -> drumului de la al doilea nod al muchiei la z

    ```java
      long tempCost = distFromX[i] + distFromY[i] + edge.cost + distToZ[j];
    ```

4. **Scrierea Rezultatelor**:
    - Scriem rezultatul final (costul minim al submultimii de muchii) in fisierul `drumuri.out`.

### Complexitatea

- **Complexitatea Spatiala**: \(O(N + M)\), deoarece stocam graful, vectorul de distante si alte structuri de date auxiliare.
- **Complexitatea Temporala**: \(O((N + M) \log N)\), pentru fiecare executie a algoritmului Dijkstra, care este rulat de trei ori (o data pe graful original si de doua ori pe graful inversat).

### Referinte

Pentru algoritmul Dijkstra, am urmat pasii prezentati in laborator.


### Diagrama Simplificata a Flow-ului

```plaintext
                 +-----------------+
                 | Citirea Datelor |
                 +--------+--------+
                          |
                          v
         +----------------+-----------------+
         | Construirea Grafurilor Orientate |
         +----------------+-----------------+
                          |
                          v
          +---------------+---------------+
          | Algoritmul Dijkstra pentru    |
          | Calcularea Distantelor Minime |
          +---------------+---------------+
                          |
                          v
          +---------------+---------------+
          | Inversarea Grafului           |
          +---------------+---------------+
                          |
                          v
          +---------------+---------------+
          | Algoritmul Dijkstra pentru    |
          | Calcularea Distantelor Minime |
          | De la Toate Nodurile la z     |
          +---------------+---------------+
                          |
                          v
          +---------------+---------------+
          | Calculul Lungimilor Drumurilor |
          +---------------+---------------+
                          |
                          v
                 +--------+--------+
                 | Scrierea Rezultatului |
                 +-----------------+

```

### Concluzie

Problema de determinare a costului minim al unei submultimi de muchii care asigura existenta drumurilor cerute
intr-un graf orientat aciclic este rezolvata eficient folosind algoritmul Dijkstra si manipularea grafului
inversat. Implementarea propusa ofera o solutie clara si optimizata, conform restrictiilor si specificatiilor date.

## Problema 4: Scandal


### Algoritmul

1. **Citirea si Preprocesarea Datelor**:
    - Citim datele de intrare din fisierul `scandal.in`.
    - Construim doua grafuri orientate folosind liste de adiacenta: graful original si graful inversat, pe baza regulilor date.

2. **Adaugarea Muchiilor in Graf**
    - Am modelat regulile petrecerii pentru a putea folosi algoritmul de tip 2-SAT.
    - Muchiile adaugate in graf pentru fiecare regula:

      #### Regula `c == 0`: "Cel putin unul dintre prietenii x sau y trebuie sa participe la petrecere."
      Aceasta regula poate fi modelata prin doua implicatii:
      - **Daca x nu participa**, atunci y trebuie sa participe. Acest lucru este reprezentat prin implicatia:
      - `nx -> y` (daca nu x, atunci y)
      - **Daca y nu participa**, atunci x trebuie sa participe. Acest lucru este reprezentat prin implicatia:
      - `ny -> x` (daca nu y, atunci x)

      #### Regula `c == 1`: "Daca x nu participa la petrecere, nici y nu va participa."
      Aceasta conditie se traduce direct in implicatia:
      - `nx -> ny` (daca nu x, atunci nu y)
      - `y -> x` (cazul reflexiv al implicatiei)

      #### Regula `c == 2`: "Daca y nu participa la petrecere, nici x nu va participa."
      Similar cu regula pentru `c == 1`, dar cu implicatiile inversate:
      - `ny -> nx` (daca nu y, atunci nu x)
      - `x -> y` (cazul reflexiv al implicatiei)

      #### Regula `c == 3`: "Cel putin unul dintre x si y nu participa la petrecere."
      Aceasta inseamna ca sunt posibile doua cazuri:
      - **Daca x participa**, atunci y nu trebuie sa participe. Acest lucru este reprezentat prin implicatia:
      - `x -> ny` (daca x, atunci nu y)
      - **Daca y participa**, atunci x nu trebuie sa participe. Acest lucru este reprezentat prin implicatia:
      - `y -> nx` (daca y, atunci nu x)

3. **Determinarea Ordinii Topologice (prima trecere DFS)**:
    - Implementam un DFS (Depth-First Search) pentru a determina ordinea de finalizare a nodurilor in graful original.

4. **Determinarea Componentelor Tare Conexe (a doua trecere DFS)**:
    - Utilizam ordinea topologica determinata pentru a gasi componentele tare conexe (SCC) in graful inversat, aplicand din nou DFS.

5. **Verificarea Validitatii Solutiei**:
    - Verificam daca solutia este valida, asigurandu-ne ca un nod si negarea lui nu se afla in aceeasi componenta tare conexa.
    - Daca solutia este valida, determinam lista de prieteni care pot fi invitati la petrecere.

6. **Scrierea Rezultatelor**:
    - Scriem rezultatul final (numarul de invitati si lista acestora) in fisierul `scandal.out`.

### Complexitatea

- **Complexitatea Spatiala**: \(O(N + M)\), deoarece stocam grafurile, vectorul de componente si alte structuri de date auxiliare.
- **Complexitatea Temporala**: \(O(N + M)\), pentru fiecare executie a algoritmului DFS, care este rulat de doua ori (o data pe graful original si o data pe graful inversat).

### Mentiuni

Pentru a determina componentele tare conexe, am utilizat algoritmul lui Kosaraju, care presupune doua treceri
DFS pe graf. Am ales Kosaraju si nu Tarjan intrucat primul este mai usor de implementat. Cum problema nu a
necesitat extragerea si manipularea componentelor conexe, am preferat simplitatea lui Kosaraju.

### Diagrama Simplificata a Flow-ului

```plaintext
                 +----------------------+
                 | Citirea Datelor      |
                 +----------+-----------+
                            |
                            v
         +------------------+------------------+
         | Construirea Grafurilor Orientate    |
         | si Inversate                        |
         +------------------+------------------+
                            |
                            v
          +-----------------+-----------------+
          | Prima Trecere DFS (Ordinea       |
          | Topologica pe Graful Original)   |
          +-----------------+-----------------+
                            |
                            v
          +-----------------+-----------------+
          | A doua Trecere DFS (Componente   |
          | Tare Conexe pe Graful Inversat)  |
          +-----------------+-----------------+
                            |
                            v
          +-----------------+-----------------+
          | Verificarea Validitatii Solutiei |
          +-----------------+-----------------+
                            |
                            v
                 +----------+-----------+
                 | Scrierea Rezultatelor |
                 +----------------------+
```

### Concluzie

Problema de determinare a listei de invitati la petrecere fara a incalca niciuna dintre regulile date este
rezolvata eficient folosind algoritmul lui Kosaraju pentru componente tare conexe. Implementarea propusa ofera o
solutie clara si optimizata, conform restrictiilor si specificatiilor date.

### Referinte

- [Kosaraju's Algorithm for Strongly Connected Components](https://www.topcoder.com/thrive/articles/kosarajus-algorithm-for-strongly-connected-components)
- [2-Satisfiability (2-SAT) Problem](https://www.geeksforgeeks.org/2-satisfiability-2-sat-problem/)
- [2-Satisfiability on Wikipedia](https://en.wikipedia.org/wiki/2-satisfiability)
