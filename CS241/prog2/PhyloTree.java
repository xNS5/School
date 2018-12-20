/*
 * PhyloTree.java
 *
 * Defines a phylogenetic tree, which is a strictly binary tree
 * that represents inferred hierarchical relationships between species
 *
 * There are weights along each edge; the weight from parent to left child
 * is the same as parent to right child.
 *
 * Students may only use functionality provided in the packages
 *     java.lang
 *     java.util
 *     java.io
 *
 * Use of any additional Java Class Library components is not permitted
 *
 * Michael Kennedy
 *
 */

 import java.lang.*;
 import java.util.*;
 import java.io.*;

public class PhyloTree {
    private PhyloTreeNode overallRoot;    // The actual root of the overall tree
    private int printingDepth;            // How many spaces to indent the deepest
                                          // node when printing

    // CONSTRUCTOR

    // PhyloTree
    // Pre-conditions:
    //        - speciesFile contains the path of a valid FASTA input file
    //        - printingDepth is a positive number
    // Post-conditions:
    //        - this.printingDepth has been set to printingDepth
    //        - A linked tree structure representing the inferred hierarchical
    //          species relationship has been created, and overallRoot points to
    //          the root of this tree
    // Notes:
    //        - A lot happens in this step!  See assignment description for details
    //          on the input format file and how to construct the tree
    //        - If you encounter a FileNotFoundException, print to standard error
    //          "Error: Unable to open file " + speciesFile
    //          and exit with status (return code) 1
    //        - Most of this should be accomplished by calls to loadSpeciesFile and buildTree
    public PhyloTree(String speciesFile, int printingDepth) {
      try
        {
          buildTree(loadSpeciesFile(speciesFile));
          return;
        }
      catch(FileNotFoundException e)
        {
          System.out.println();
          System.out.println("Error, unable to open file: " + speciesFile);
          System.exit(1);
        }
    }
    // ACCESSORS

    // getOverallRoot
    // Pre-conditions:
    //    - None
    // Post-conditions:
    //    - Returns the overall root
    public PhyloTreeNode getOverallRoot() {
        return overallRoot;
    }

    // toString
    // Pre-conditions:
    //    - None
    // Post-conditions:
    //    - Returns a string representation of the tree
    // Notes:
    //    - See assignment description for proper format
    //        (it will be a kind of reverse in-order [RNL] traversal)
    //    - Can be a simple wrapper around the following toString
    //    - Hint: StringBuilder is much faster than repeated concatenation
    public String toString() {
      return "";
    }

    // toString
    // Pre-conditions:
    //    - node points to the root of a tree you intend to print
    //    - weightedDepth is the sum of the edge weights from the
    //      overall root to the current root
    //    - maxDepth is the weighted depth of the overall tree
    // Post-conditions:
    //    - Returns a string representation of the tree
    // Notes:
    //    - See assignment description for proper format
    private String toString(PhyloTreeNode node, double weightedDepth, double maxDepth) {
        return "";
    }

    // toTreeString
    // Pre-conditions:
    //    - None
    // Post-conditions:
    //    - Returns a string representation in tree format
    // Notes:
    //    - See assignment description for format details
    //    - Can be a simple wrapper around the following toTreeString
    public String toTreeString() {
        return "";
    }

    // toTreeString
    // Pre-conditions:
    //    - node points to the root of a tree you intend to print
    // Post-conditions:
    //    - Returns a string representation in tree format
    // Notes:
    //    - See assignment description for proper format
    private String toTreeString(PhyloTreeNode node) {
        return "";
    }

    // getHeight
    // Pre-conditions:
    //    - None
    // Post-conditions:
    //    - Returns the tree height as defined in class
    // Notes:
    //    - Can be a simple wrapper on nodeHeight
    public int getHeight() {
        return nodeHeight(overallRoot);
    }

    // getWeightedHeight
    // Pre-conditions:
    //    - None
    // Post-conditions:
    //    - Returns the sum of the edge weights along the
    //      "longest" (highest weight) path from the root
    //      to any leaf node.
    // Notes:
    //   - Can be a simple wrapper for weightedNodeHeight
    public double getWeightedHeight() {
        return weightedNodeHeight(overallRoot);
    }

    // countAllSpecies
    // Pre-conditions:
    //    - None
    // Post-conditions:
    //    - Returns the number of species in the tree
    // Notes:
    //    - Non-terminals do not represent species
    //    - This functionality is provided for you elsewhere
    //      just call the appropriint height_l = nodeHeight(node.getLeftChild());
    public int countAllSpecies() {
        return getOverallRoot().getNumLeafs();
    }

    // getAllSpecies
    // Pre-conditions:
    //    - None
    // Post-conditions:
    //    - Returns an ArrayList containing all species in the tree
    // Notes:
    //    - Non-terminals do not represent species
    // Hint:
    //    - Call getAllDescendantSpecies
    public java.util.ArrayList<Species> getAllSpecies() {
        ArrayList<Species> all_species = new ArrayList<Species>();
        getAllDescendantSpecies(overallRoot, all_species);
        return all_species;
    }

    // findTreeNodeByLabel
    // Pre-conditions:
    //    - label is the label of a tree node you intend to find
    //    - Assumes labels are unique in the tree
    // Post-conditions:
    //    - If found: returns the PhyloTreeNode with the specified label
    //    - If not found: returns null
    public PhyloTreeNode findTreeNodeByLabel(String label) { // Recursive, start from overall root.
      return findTreeNodeByLabel(overallRoot, label);
    }

    // findLeastCommonAncestor
    // Pre-conditions:
    //    - label1 and label2 are the labels of two species in the tree
    // Post-conditions:
    //    - If either node cannot be found: returns null
    //    - If both nodes can be found: returns the PhyloTreeNode of their
    //      common ancestor with the largest depth
    //      Put another way, the least common ancestor of nodes A and B
    //      is the only node in the tree where A is in the left tree
    //      and B is in the right tree (or vice-versa)
    // Notes:
    //    - Can be a wrapper around the static findLeastCommonAncestor
     public PhyloTreeNode findLeastCommonAncestor(String label1, String label2) { //use findTreeNodeByLabel
       PhyloTreeNode node1 = findTreeNodeByLabel(label1);
       PhyloTreeNode node2 = findTreeNodeByLabel(label2);
       PhyloTreeNode ancestor = findLeastCommonAncestor(node1,node2);
       return ancestor;
    }

    // findEvolutionaryDistance
    // Pre-conditions:
    //    - label1 and label2 are the labels of two species in the tree
    // Post-conditions:
    //    - If either node cannot be found: returns POSITIVE_INFINITY
    //    - If both nodes can be found: returns the sum of the weights
    //      along the paths from their least common ancestor to each of
    //      the two nodes
     public double findEvolutionaryDistance(String label1, String label2) {
       PhyloTreeNode node1 = findTreeNodeByLabel(overallRoot,label1);
       PhyloTreeNode node2 = findTreeNodeByLabel(overallRoot,label2);
       PhyloTreeNode ancestor = findLeastCommonAncestor(node1, node2);
       return weightedNodeHeight(ancestor);
    }

    // MODIFIER

    // buildTree
    // Pre-conditions:
    //    - species contains the set of species for which you want to infer
    //      a phylogenetic tree
    // Post-conditions:
    //    - A linked tree structure representing the inferred hierarchical
    //      species relationship has been created, and overallRoot points to
    //      the root of said tree
    // Notes:
    //    - A lot happens in this step!  See assignment description for details
    //      on how to construct the tree.
    //    - Be sure to use the tie-breaking conventions described in the pdf
    //    - Important hint: although the distances are defined recursively, you
    //      do NOT want to implement them recursively, as that would be very inefficient
    //
    private void buildTree(Species[] species) {
      MultiKeyMap<Double> species_map = new MultiKeyMap<Double>();
      List<PhyloTreeNode> node_list = new ArrayList<PhyloTreeNode>();
      List<PhyloTreeNode> temp = new ArrayList<PhyloTreeNode>();
      double min = Double.POSITIVE_INFINITY;
      boolean done = false;
      int t = 1;

      for(int i = 0; i < species.length; i++)
        {
          node_list.add(new PhyloTreeNode(null, species[i]));
        }

      int list_size = node_list.size();
      for(int i = 0; i < list_size; i++)
        {
          PhyloTreeNode curr = node_list.get(i);
          for(int j = t; j < list_size; j++)
            {
              PhyloTreeNode next = node_list.get(j);
              species_map.put(curr.getLabel(),next.getLabel(), Species.distance(curr.getSpecies(),next.getSpecies()));
            }
            t++;
        }

        while(node_list.size() != 1)
          {
            PhyloTreeNode first = null;
            PhyloTreeNode second = null;
            int updated_size = node_list.size();
            min = Double.POSITIVE_INFINITY;
            for(int i = 0; i < updated_size; i++)
              {
                PhyloTreeNode curr = node_list.get(i);
                for(int j = i+1; j < updated_size; j++)
                  {
                    PhyloTreeNode next = node_list.get(j);
                    if(!curr.getLabel().equals(next.getLabel()))
                      {
                        double dist = species_map.get(curr.getLabel(), next.getLabel());
                        int val = Double.compare(dist, min);
                        if(val < 0)
                          {
                            min = dist;
                            first = curr;
                            second = next;
                            double score = first.getLabel().compareTo(second.getLabel());
                            if(score > 0)
                              {
                                PhyloTreeNode tmp = first;
                                first = second;
                                second = tmp;
                              }
                          }
                      }
                   }
                }

              String name_1 = first.getLabel();
              String name_2 = second.getLabel();
              PhyloTreeNode x = new PhyloTreeNode(name_1 + "+" +name_2, null, first, second, species_map.get(name_1, name_2)/2.0);
              first.setParent(x);
              second.setParent(x);
              node_list.remove(first);
              node_list.remove(second);
              int temp_size = node_list.size();
              for(int i = 0; i < temp_size; i++)
                {
                  if(!x.getLabel().equals(node_list.get(i).getLabel()))
                    {
                      double n = compute(x, node_list.get(i), species_map);
                      species_map.put(x.getLabel(), node_list.get(i).getLabel(), n);
                    }
                }
              for(int i = 0; i < temp_size; i++)
                {
                  species_map.remove(node_list.get(i).getLabel(), first.getLabel());
                  species_map.remove(node_list.get(i).getLabel(), second.getLabel());
                }
                node_list.add(x);
              }
          overallRoot = node_list.get(0);
          return;
        }

  // compute
  // Pre-conditions:
  //    - node1 is a valid non-terminal
  // Post-conditions:
  //    - returns 0.0 if the node is a terminal

    public static double compute(PhyloTreeNode node1, PhyloTreeNode node2, MultiKeyMap<Double> map)
      {
        PhyloTreeNode n1_left = node1.getLeftChild();
        PhyloTreeNode n1_right = node1.getRightChild();
        double left_leafs = n1_left.getNumLeafs();
        double right_leafs = n1_right.getNumLeafs();
        double val = (((left_leafs/(left_leafs + right_leafs))*(map.get(node2.getLabel(), n1_left.getLabel()))) + ((right_leafs/(left_leafs+right_leafs))*(map.get(node2.getLabel(), n1_right.getLabel()))));
        return val;
      }

    // STATIC

    // nodeDepth
    // Pre-conditions:
    //    - node is null or the root of tree (possibly subtree)
    // Post-conditions:
    //    - If null: returns -1
    //    - Else: returns the depth of the node within the overall tree
    public static int nodeDepth(PhyloTreeNode node) {
        if(node == null)
          {
            return -1;
          }

        if(node != null && !node.isLeaf())
          {
            return 1+ nodeDepth(node.getLeftChild()) + nodeDepth(node.getRightChild());
          }

        return 0;
    }

    // nodeHeight
    // Pre-conditions:
    //    - node is null or the root of tree (possibly subtree)
    // Post-conditions:
    //    - If null: returns -1
    //    - Else: returns the height subtree rooted at node
    public static int nodeHeight(PhyloTreeNode node) {
      if(node == null)
        {
          return -1;
        }

      int height_l = nodeHeight(node.getLeftChild());
      int height_r = nodeHeight(node.getRightChild());

      if(height_l > height_r)
        {
          return height_l+1;
        }

      return height_r+1;

    }

    // weightedNodeHeight
    // Pre-conditions:
    //    - node is null or the root of tree (possibly subtree)
    // Post-conditions:
    //    - If null: returns NEGATIVE_INFINITY
    //    - Else: returns the weighted height subtree rooted at node
    //     (i.e. the sum of the largest weight path from node
    //     to a leaf; this might NOT be the same as the sum of the weights
    //     along the longest path from the node to a leaf)
    public static double weightedNodeHeight(PhyloTreeNode node) {
      if(node == null)
        {
          return Double.NEGATIVE_INFINITY;
        }
      if(!node.isLeaf())
        {
          double weight_l = node.getDistanceToChild() + weightedNodeHeight(node.getLeftChild());
          double weight_r = node.getDistanceToChild() + weightedNodeHeight(node.getRightChild());
          if(weight_l > weight_r)
            {
              return weight_l;
            }
          else
            {
              return weight_r;
            }
        }
      return 0;
    }

    // loadSpeciesFile
    // Pre-conditions:
    //    - filename contains the path of a valid FASTA input file
    // Post-conditions:
    //    - Creates and returns an array of species objects representing
    //      all valid species in the input file
    // Notes:
    //    - Species without names are skipped
    //    - See assignment description for details on the FASTA format
    // Hints:
    //    - Because the bar character ("|") denotes OR, you need to escape it
    //      if you want to use it to split a string, i.e. you can use "\\|"
    public static Species[] loadSpeciesFile(String filename) throws FileNotFoundException {
      Scanner sc = new Scanner(new File(filename));                                          // Scanner to parse the text files
      List<Species> temp = new ArrayList<Species>();                                         // Arraylist to hold the species
      StringBuilder sb = new StringBuilder();                                                // StringBuilder
      String curr = sc.nextLine();                                                           // Current line is the first line of the text file
      String line;                                                                           // String var
      String name = null;                                                                    // Setting name var to null
      String oldName = null;                                                                 // setting old name to null
      String[] sequence;
      boolean flag = true;

      while(sc.hasNextLine())                                                                // I figured that doing this all in a single while loop would be the most efficient
        {
          String next = sc.nextLine();
          if(flag = true)
          {
            if(curr.charAt(0) == '>')
              {
                String[] str = curr.split("\\|");
                if(str.length != 7)
                  {
                    curr = next;
                    name = null;
                    flag = false;
                    sb = new StringBuilder();
                    continue;
                  }
                else
                  {
                    name = str[6];
                  }
              }
            if(curr.charAt(0) != '>')
              {
                sb.append(curr);
              }
            if(next.charAt(0) == '>' || !sc.hasNextLine())
              {
                if(name != null)
                {
                  if(!sc.hasNextLine())
                    {
                      sb.append(next);
                    }
                  String temp_1 = sb.toString();
                  sequence = temp_1.split("");
                  temp.add(new Species(name, sequence));
                }
              sb = new StringBuilder();
              }
            }
          if(flag == false)
            {
              if (curr.charAt(0) != '>' && sc.hasNextLine())
                {
                  curr = next;
                  continue;
                }
              else
                {
                  flag = true;
                }
              }
              curr = next;
            }

        Species[] sp_list = new Species[temp.size()];
        for(int i = 0; i < temp.size(); i++)
          {
            sp_list[i] = temp.get(i);
          }
        return sp_list;
    }

    // getAllDescendantSpecies
    // Pre-conditions:
    //    - node points to a node in a phylogenetic tree structure
    //    - descendants is a non-null reference variable to an empty arraylist object
    // Post-conditions:
    //    - descendants is populated with all species in the subtree rooted at node
    //      in in-/pre-/post-order (they are equivalent here)
    private static void getAllDescendantSpecies(PhyloTreeNode node, java.util.ArrayList<Species> descendants) {
        if(node.isLeaf())
          {
            descendants.add(node.getSpecies());
            return;
          }
        if(node.getLeftChild() != null && node.getRightChild() != null)
          {
            getAllDescendantSpecies(node.getLeftChild(), descendants);
            getAllDescendantSpecies(node.getRightChild(), descendants);
          }
        return;
    }

    // findTreeNodeByLabel
    // Pre-conditions:
    //    - node points to a node in a phylogenetic tree structure
    //    - label is the label of a tree node that you intend to locate
    // Post-conditions:
    //    - If no node with the label exists in the subtree, return null
    //    - Else: return the PhyloTreeNode with the specified label
    // Notes:
    //    - Assumes labels are unique in the tree
    private static PhyloTreeNode findTreeNodeByLabel(PhyloTreeNode node, String label) {
        if(!node.getLabel().equals(label))
          {
            if(!node.isLeaf())
              {
                findTreeNodeByLabel(node.getLeftChild(), label);
                findTreeNodeByLabel(node.getRightChild(), label);
              }
          }
        if(node.isLeaf() && !node.equals(label))
          {
            node = null;
          }
        return node;
    }

    // findLeastCommonAncestor
    // Pre-conditions:
    //    - node1 and node2 point to nodes in the phylogenetic tree
    // Post-conditions:
    //    - If node1 or node2 are null, return null
    //    - Else: returns the PhyloTreeNode of their common ancestor
    //      with the largest depth
     private static PhyloTreeNode findLeastCommonAncestor(PhyloTreeNode node1, PhyloTreeNode node2) {
       PhyloTreeNode ancestor = null;
       List<PhyloTreeNode> ancestors = new ArrayList<PhyloTreeNode>();

       if(node1.getParent() == null)
        {
          return node1;
        }
       if(node2.getParent() == null)
        {
          return node2;
        }
      if(node1.equals(node2))
        {
          return node1;
        }
      while(true)
        {
          ancestors.add(node1.getParent());
          if(ancestors.contains(node2.getParent()))
            {
              ancestor = node2;
              break;
            }
          node1 = node1.getParent();
          node2 = node2.getParent();
        }
      return ancestor;
    }
  }
