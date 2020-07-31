# Speech Recognition program using a DAG

This program is a "second pass" decoding program which takes a lattice as an input, with each node in the lattice representing
a point in time in a sentence and each edge representing a word that might have been uttered between two end points. 
Each path represents a possible hypothesis for the uttered sentence. Each edge is weighted with two scores: an acoustic model score, and
a language model score (**amScore** and **lmScore**). The two scores are combined by taking a weighted sum by the language model scale
(**lmScale**). The combined score for an edge *e* is: `combinedScore(e) = amScore(e) + lmScale * lmScore(e)`

The program uses a Bellman-Ford algorithm to find the shortest path from the start node to the end node. Each edge is given a negative 
log probability, so the lower the cost of getting from one node to the next the better. The shortest path will be the best path, making
the path with the lowest score has the highest probability of being the phrase or word spoken.

---

# How to Run

Windows: `java.exe Program1 someLatticeListFile.txt 8.0 someOutputDirectory`

MacOS/Linux: `java Program1 someLatticeListFile.txt 8.0 someOutputDirectory`

where 8.0 in the above examples are the lmScale value.

---

# Algorithms

This assignment was a pair programming assignment, and we were tasked with implementing the following methods in the `Lattice.java` file:

- Lattice - parses the input file and constructs an adjacency matrix with the input. 
- getUtteranceID - gets the utterance ID
- getNumNodes - gets the number of nodes
- getNumEdges - gets the number of edges
- toString - prints out the contents of the adjacency matrix
- decode - takes the output from the topologically sorted nodes and constructs a new hypothesis object. The hypothesis object contains the predicted phrase or word.
- topologicalSort - sorts all of the nodes in the lattice based on the number of incoming edges.
- countAllPaths - counts the number of distinct paths in a given lattice file.
- getLatticeDensity - returns the number of non silence words in the lattice divided by the number of seconds from start to end.
- writeAsDot - creates a file representing the contents of the adjacency matrix
- saveAsFile - writes the contents of toString() to a file.
- uniqueWordsAtTime - finds all the words at a given point in time.
- printSortedHits - prints all times in sorted order when a given toek appears.

---

# Potential Improvements

1. Finding a way to improve the Lattice method. One approach that I saw while implementing this method was to simply create multiple
`for` loops to iterate through the data multiple times. The input files start and end the same; the first 5 lines contain the following values:
 _ ID
 _ Start value
 _ End value
 _ Number of Nodes
 _ Number of Edges

 After the first 5 lines, there are "node" values and "edge" values. We opted to instead use one loop and a counter instead of using 3 `for` loops.

 I believe that this is one of the better ways to parse this data, however there are some minor improvements that can be made to reduce the amount of memory used.

 2. I call topologicalSort() a number of times within separate methods, perhaps creating a class variable to store the topologically sorted nodes would speed up the average running time, as well as reduce space allocation.

 3. Finding a way to improve the adjacency matrix. It's currently in the form of a 2D matrix, which takes O(n^2) time to iterate
 through the array. Intead, I could create a vertex class which among other things could contain edge costs. This way, all that one
 would have to do is iterate through each node instead of checking indexes that contain no edges in the original 2D matrix.

 An unfortunate side effect of this is the countAllPaths method most likely will have to be reworked to be a recursive method as opposed to an iterative one.
 A recursive solution has the potential to require more time to execute and not improve the running time in any significant way.
