public class test{
    static class Node
    {
        int data;
        Node left;
        Node right;
    };

/* Helper function that allocates a new node
with the given data and null left and right
pointers. */
    static Node newNode(int data)
    {
        Node node = new Node();
        node.data = data;
        node.left = null;
        node.right = null;

        return (node);
    }

/* Give a binary search tree and a number,
inserts a new node with the given number in
the correct place in the tree. Returns the new
root pointer which the caller should then use
(the standard trick to avoid using reference
parameters). */
    static Node insert(Node node, int data)
    {

    /* 1. If the tree is empty, return a new,
    single node */
        if (node == null)
            return (newNode(data));
        else
        {

            /* 2. Otherwise, recur down the tree */
            if (data <= node.data)
                node.left = insert(node.left, data);
            else
                node.right = insert(node.right, data);

            /* return the (unchanged) node pointer */
            return node;
        }
    }

// Function to return the minimum node
// in the given binary search tree
    static int minValue(Node node)
    {
        if (node.left == null)
            return node.data;
        return minValue(node.left);
    }

// Driver code
    public static void main(String args[])
    {
        // Create the BST
        Node root = null;
        root = insert(root, 4);
        insert(root, 2);
        insert(root, 1);
        insert(root, 3);
        insert(root, 6);
        insert(root, 5);

        System.out.println(minValue(root));

    }
}
}