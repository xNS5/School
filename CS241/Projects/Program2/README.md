# Phylogenic Tree Construction

This program will build a tree based on amino acid sequences. By comparing the amino acid sequences between species, the program 
infers a hierarchical structure. Species live in the leaves of the phylogenic tree. Non-terminals represent the evolutionary
predecessors. Each edge has a weight that indicates that the child deviated from the parent. 

---

# How to Run

Windows: `java.exe Program2 someListFile.txt someOutputDirectory`

MacOS/Linux: `java Program2 someListFile.txt someOutputDirectory`

---

# Algorithms

- PhyloTree - constructor for the phylogenic tree
- getOverallRoot - returns the root node of the tree
- toString - returns a string representation of the tree. Unimplemented due to initial time constraints.
- toString - returns a string representation of the tree, taking in a node, weighted depth and maximum depth. Unimplemented due to time constraints.
- toTreeString - unimplemented due to time constraints
- toTreeString - unimplemented due to time constraints
- getHeight - gets the height of the tree
- getWeightedHeight - gets the weighted height of a node
- countAllSpecies - countes the number of species in a tree
- findTreeNodeByLabel - locates a specific node in the phylo tree by its label
- findLeastCommonAncestor - finds a node of two species common ancestor with the largest depth
- findEvolutionaryDistance - returns the sum of weights from their least common ancestor to each node.
- buildTree - constructs the phylogenic tree
- compute - calculates the distance between from one tree to another
- nodeDepth - returns the depth of a node within the tree
- nodeHeight - returns the height of the subtree rooted at a given node
- weightedNodeHeight - returns the weighted height of a subtree rooted at ths node
- loadSpeciesFile - parses the species file
- getAllDecendantSpecies - gets the decendant species for a given node
- findTreeNodeByLabel - returns the node with a specific label
- findLeastCommonAncestor - returns the ancestor with largest depth of two nodes.

Note: Some method names are repeats, however some take in different parameters.

I wasn't able to complete this project in its entirety. It took more time than I thought to get the buildTree method working, and I wasn't able to divote as much time as I wanted to the other methods. A week before the assignment was due, I found out that the output for my buildTree method was building the tree incorrectly. I spend the last week troubleshooting it, and hastily implementing as many other methods as I could.


