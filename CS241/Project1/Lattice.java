/*
 * Lattice.java
 *
 * Defines a new "Lattice" type, which is a directed acyclic graph that
 * compactly represents a very large space of speech recognition hypotheses
 *
 * Note that the Lattice type is immutable: after the fields are initialized
 * in the constructor, they cannot be modified.
 *
 * Students may only use functionality provided in the packages
 *     java.lang
 *     java.util
 *     java.io
 *
 * as well as the class java.math.BigInteger
 *
 * Use of any additional Java Class Library components is not permitted
 *
 * Michael Kennedy
 * Ixael Limon-Bermudez
 *
 */
import java.util.*;
import java.io.*;
import java.lang.*;

public class Lattice {
    private String utteranceID;       // A unique ID for the sentence
    private int startIdx, endIdx;     // Indices of the special start and end tokens
    private int numNodes, numEdges;   // The number of nodes and edges, respectively
    private Edge[][] adjMatrix;       // Adjacency matrix representing the lattice
                                      // Two dimensional array of Edge objects
                                      //  adjMatrix[i][j] == null means no edge (i,j)
    private double[] nodeTimes;       // Stores the timestamp for each node


    // Constructor

    // Lattice
    // Preconditions:
    //     - latticeFilename contains the path of a valid lattice file
    // Post-conditions
    //     - Field id is set to the lattice's ID
    //     - Field startIdx contains the node number for the start node
    //     - Field endIdx contains the node number for the end node
    //     - Field numNodes contains the number of nodes in the lattice
    //     - Field numEdges contains the number of edges in the lattice
    //     - Field adjMatrix encodes the edges in the lattice:
    //        If an edge exists from node i to node j, adjMatrix[i][j] contains
    //        the address of an Edge object, which itself contains
    //           1) The edge's label (word)
    //           2) The edge's acoustic model score (amScore)
    //           3) The edge's language model score (lmScore)
    //        If no edge exists from node i to node j, adjMatrix[i][j] == null
    //     - Field nodeTimes is allocated and populated with the timestamps for each node
    // Notes:
    //     - If you encounter a FileNotFoundException, print to standard error
    //         "Error: Unable to open file " + latticeFilename
    //       and exit with status (return code) 1
    //     - If you encounter a NoSuchElementException, print to standard error
    //         "Error: Not able to parse file " + latticeFilename
    //       and exit with status (return code) 2

    public Lattice(String latticeFileName)
    {
      try
        {
	    String[] id_info = new String[5];                                           // Creating an array 5 units long
	    int counter = 0;                                                            //  
          Scanner sc = new Scanner(new File(latticeFileName));
          String line;
          for(int i = 0; i < 5; i++)
            {
              line = sc.nextLine();
              String[] split = line.split(" ");
              id_info[i] = split[1];
            }
          utteranceID = id_info[0];
          startIdx = Integer.parseInt(id_info[1]);
          endIdx = Integer.parseInt(id_info[2]);
          numNodes = Integer.parseInt(id_info[3]);
          numEdges = Integer.parseInt(id_info[4]);
          adjMatrix = new Edge[numNodes][numNodes];
          nodeTimes = new double[numNodes];
          int i = 0;

          while(sc.hasNextLine())
            {
              if(counter > 5 && counter < numNodes+6)
                {
                  line = sc.nextLine();
                  String[] split = line.split(" ");
                  nodeTimes[i] = Double.parseDouble(split[2]);
                  i++;
                }
              else if(counter >= numNodes+6 && sc.hasNextLine())
                {
                  line = sc.nextLine();
                  String[] split = line.split(" ");
                  int n = Integer.parseInt(split[1]);
                  int x = Integer.parseInt(split[2]);
                  String label = split[3];
                  int amScore = Integer.parseInt(split[4]);
                  int lmScore = Integer.parseInt(split[5]);
                  adjMatrix[n][x] = new Edge(label, amScore, lmScore);
                }
              counter++;
            }
          }
        catch(Exception e)
          {
            e.printStackTrace();
            System.out.println(e);
          }
      return;
  }
    // Accessors

    // getUtteranceID
    // Pre-conditions:
    //    - None
    // Post-conditions:
    //    - Returns the utterance ID
    public String getUtteranceID() {
        return utteranceID;
    }

    // getNumNodesnumEdges 274
    // Pre-conditions:
    //    - None
    // Post-conditions:
    //    - Returns the number of nodes in the lattice
    public int getNumNodes() {
        return numNodes;
    }

    // getNumEdges
    // Pre-conditions:
    //    - None
    // Post-conditions:
    //    - Returns the number of edges in the lattice
    public int getNumEdges() {
        return numEdges;
    }

    // toString
    // Pre-conditions:
    //    - None
    // Post-conditions:
    //    - Constructs and returns a string describing the lattice in the same
    //      format as the input files.  Nodes should be sorted ascending by node
    //      index, edges should be sorted primarily by start node index, and
    //      secondarily by end node index
    // Notes:
    //    - Do not store the input string verbatim: reconstruct it on they fly
    //      from the class's fields
    //    - toString simply returns a string, it should not print anything itself
    // Hints:
    //    - You can use the String.format method to print a floating point value
    //      to two decimal places
    //    - A StringBuilder is asymptotically more efficient for accumulating a
    //      String than repeated concatenation
    public String toString()
      {
          StringBuilder sb = new StringBuilder("");
          sb.append("id "+ utteranceID + "\n"+ "start " + startIdx + "\n" +"end "+ endIdx + "\n");
          sb.append("numNodes "+numNodes + "\n"+"numEdges "+ numEdges + "\n");

          for(int i = 0; i < numNodes; i++)
            {
              sb.append("node " + i + " "+ nodeTimes[i]+ "\n");
            }

          for(int a = 0; a < numNodes; a++)
            {
              for(int b = 0; b < numNodes; b++)
                {
                  if(adjMatrix[a][b] != null)
                    {
                      Edge edge = adjMatrix[a][b];
                      sb.append("edge "+ a+ " " + b + " " + edge.getLabel() + " " + edge.getAmScore() + " " + edge.getLmScore() + "\n");
                    }
                }
            }

        return sb.toString();
      }


    // decode
    // Pre-conditions:
    //    - lmScale specifies how much lmScore should be weighted
    //        the overall weight for an edge is amScore + lmScale * lmScore
    // Post-conditions:
    //    - A new Hypothesis object is returned that contains the shortest path
    //      (aka most probable path) from the startIdx to the endIdx
    // Hints:
    //    - You can create a new empty Hypothesis object and then
    //      repeatedly call Hypothesis's addWord method to add the words and
    //      weights, but this needs to be done in order (first to last word)
    //      Backtracking will give you words in reverse order.
    //    - java.lang.Double.POSITIVE_INFINITY represents positive infinity
    // Notes:
    //    - It is okay if this algorithm has time complexity O(V^2)
    public Hypothesis decode(double lmScale) {
        double[] cost = new double[numNodes];
        double infinity = Double.POSITIVE_INFINITY;
        Hypothesis path = new Hypothesis();

        for(int i = 1; i < numNodes; i++)
          {
            cost[i] = infinity;
          }

        cost[startIdx] = 0;
        int[] sorted = topologicalSort();
        int[] parent = new int[numNodes];

        for(int n: sorted)
          {
            for(int i = 0; i < numNodes; i++)
              {
                if((adjMatrix[i][n] != null) && adjMatrix[i][n].getCombinedScore(lmScale) + cost[i] < cost[n])
                  {
                    cost[n] = adjMatrix[i][n].getCombinedScore(lmScale) + cost[i];
                    parent[n] = i;
                  }
              }
           }

          int node = endIdx;
          Stack<Edge> st = new Stack<Edge>();

          while(node != startIdx)
            {
              st.push(adjMatrix[parent[node]][node]);
              node = parent[node];
            }

          while(!st.isEmpty())
            {
              Edge temp = st.pop();
              path.addWord(temp.getLabel(), temp.getCombinedScore(lmScale));
            }

        return path;
    }

    // topologicalSort
    // Pre-conditions:
    //    - None
    // Post-conditions:
    //    - A new int[] is returned with a topological sort of the nodes
    //      For example, the 0'th element of the returned array has no
    //      incoming edges.  More generally, the node in the i'th element
    //      has no incoming edges from nodes in the i+1'th or later elements
    public int[] topologicalSort() {
        int[] inDegrees = new int[numNodes];
        Stack<Integer> zeroIn = new Stack<Integer>();
        List<Integer> temp = new ArrayList<Integer>();
        int[] result = new int[numNodes];

        for(int i = 0; i < numNodes; i++)
          {
            for(int j = 0; j < numNodes; j++)
              {
                if(adjMatrix[i][j] != null)
                  {
                    inDegrees[j] += 1;
                  }
              }
          }

        for(int i = 0; i < numNodes; i++)
          {
            if(inDegrees[i] == 0)
              {
                zeroIn.push(i);
              }
            }

        while(!zeroIn.isEmpty())
          {
            Integer n = zeroIn.pop();
            temp.add(n);

            for(int i = 0; i < numNodes; i++)
              {
                if(adjMatrix[n][i] != null)
                  {
                    inDegrees[i]--;
                    if(inDegrees[i] == 0)
                      {
                        zeroIn.push(i);
                      }
                    }
              }

          }
        int sum = 0;

        for(int t : inDegrees)
          {
            sum += t;
          }

        if(sum > 0)
          {
            System.out.println("Error. Cycle detected. Terminating program");
            System.exit(0);
          }

         for(int i = 0; i < numNodes; i++)
           {
             result[i] = temp.get(i);
           }

        return result;
    }

    // countAllPaths
    // Pre-conditions:
    //    - None
    // Post-conditions:
    //    - Returns the total number of distinct paths from startIdx to endIdx
    //       (do not count other subpaths)
    // Hints:
    //    - The straightforward recursive traversal is prohibitively slow
    //    - This can be solved efficiently using something similar to the
    //        shortest path algorithm used in decode
    //        Instead of min'ing scores over the incoming edges, you'll want to
    //        do some other operation...
    // Notes: This sucked.
    public java.math.BigInteger countAllPaths()
      {
        java.math.BigInteger[] count = new java.math.BigInteger[numNodes];

        for(int i = 0; i < numNodes; i++)
          {
            count[i] = java.math.BigInteger.ZERO;
          }

        count[startIdx] = java.math.BigInteger.ONE;
        int[] sorted = topologicalSort();

        for(int n : sorted)
          {
            for(int i = 0; i < numNodes; i++)
              {
                if(adjMatrix[n][i] != null)
                  {
                    count[i] = count[n].add(count[i]);
                  }
              }
          }

        return count[endIdx];
      }

    // getLatticeDensity
    // Pre-conditions:
    //    - None
    // Post-conditions:
    //    - Returns the lattice density, which is defined to be:
    //      (# of non -silence- words in lattice) / (# seconds from start to end index)
	//      Note that multiwords (e.g. to_the) count as a single non-silence word
    public double getLatticeDensity() {
      double nonSilence = 0.0;
      double seconds = 0.0;

      for(int i = 0; i < numNodes; i++)
        {
          for(int j = 0; j < numNodes; j++)
            {
              if((adjMatrix[i][j] != null) && (adjMatrix[i][j].getLabel().equals("-silence-")) && (adjMatrix[i][j].getLabel().equals("@reject")))
                {
                  nonSilence +=1;
                }
            }
        }

        double result = (nonSilence/(nodeTimes[endIdx] - nodeTimes[startIdx]));

        return result;
    }

    // writeAsDot - write lattice in dot format
    // Pre-conditions:
    //    - dotFilename is the name of the intended output file
    // Post-conditions:
    //    - The lattice is written in the specified dot format to dotFilename
    // Notes:
    //    - See the assignment description for the exact formatting to use
    //    - For context on the dot format, see
    //        - http://en.wikipedia.org/wiki/DOT_%28graph_description_language%29
    //        - http://www.graphviz.org/pdf/dotguide.pdf
    public void writeAsDot(String dotFilename) {
      try
        {
          File output = new File(dotFilename);
          FileWriter writer = new FileWriter(output);
          writer.write("digraph g{\n");
          writer.write("\trankdir= \"LR\"" + "\n");
          int[] sorted = topologicalSort();

          for(int n : sorted)
            {
              for(int i = 0; i < numNodes; i++)
                {
                  if(adjMatrix[n][i] != null)
                    {
                      writer.write("\t" + n + "->" + i + " [label = " + adjMatrix[n][i].getLabel() + "]\n");
                    }
                }
            }

          writer.write("}");
          writer.close();
        }
        catch(Exception e)
          {
            e.printStackTrace();
            System.out.println("Error");
            System.exit(-1);
          }

        return;
    }

    // saveAsFile - write in the simplified lattice format (same as input format)
    // Pre-conditions:
    //    - latticeOutputFilename is the name of the intended output file
    // Post-conditions:
    //    - The lattice's toString() representation is written to the output file
    // Note:
    //    - This output file should be in the same format as the input .lattice file
    public void saveAsFile(String latticeOutputFilename) {
      try
        {
          File output = new File(latticeOutputFilename);
          FileWriter writer = new FileWriter(output);
          writer.write(toString());
          writer.close();
        }
      catch(Exception e)
        {
          e.printStackTrace();
          System.out.println("Error");
        }
      return;
    }

    // uniqueWordsAtTime - find all words at a certain point in time
    // Pre-conditions:
    //    - time is the time you want to query
    // Post-conditions:
    //    - A HashSet is returned containing all unique words that overlap
    //      with the specified time
    //     (If the time is not within the time range of the lattice, the Hashset should be empty)
    public java.util.HashSet<String> uniqueWordsAtTime(double time)
      {
          HashSet<String> str = new HashSet<String>();
          int index = 0;
          for(int i = 0; i < numNodes; i++)
            {
              if(nodeTimes[i] <= time)
                {
                  index = i;
                }
            }

        for(int i = 0; i < numNodes; i++)
          {
            for(int j = 0; j < index; j++)
              {
                if(adjMatrix[i][j] != null)
                  {
                    if(!(adjMatrix[i][j].getLabel().equals("-silence-")) && !(adjMatrix[i][j].getLabel().equals("@reject@")))
                      {
                        str.add(adjMatrix[i][j].getLabel());
                      }

                   }
                }
            }

          return str;
          }


    // printSortedHits - print in sorted order all times where a given token appears
    // Pre-conditions:
    //    - word is the word (or multiword) that you want to find in the lattice
    // Post-conditions:
    //    - The midpoint (halfway between start and end time) for each instance of word
    //      in the lattice is printed to two decimal places in sorted (ascending) order
    //      All times should be printed on the same line, separated by a single space character
    //      (If no instances appear, nothing is printed)
    // Note:
	//    - java.util.Arrays.sort can be used to sort
    //    - PrintStream's format method can print numbers to two decimal places
    public void printSortedHits(String word)
    {
      double[] times = new double[numNodes];
        for(int i = 0; i < numNodes; i++)
          {
            for(int j = 0; j < numNodes; j++)
              {
                if((adjMatrix[i][j] != null) && (adjMatrix[i][j].getLabel().equals(word)))
                  {
                    times[i] = ((nodeTimes[j] + nodeTimes[i])/2);
                  }
              }
          }
        Arrays.sort(times);
        double count = 0.0;
        for(double n : times)
          {
            count += n;
          }
        if(count > 0)
          {
            for(int i = 2; i < times.length; i++)
              {
                System.out.printf("%.2f ", times[i]);
              }
              System.out.println();
          }
        return;
    }
}
