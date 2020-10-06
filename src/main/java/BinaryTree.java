public class BinaryTree {
    TreeNode root = null;

    public void insert(TreeNode n) {
        if (root == null) {
            root = n;
            return;
        }
        TreeNode node = root;

        while (node != null) {
            if (n.val < node.val) {
                if (node.left == null) {
                    node.left = n;
                    break;
                } else {
                    node = node.left;
                }
            } else {
                if (node.right == null) {
                    node.right = n;
                    break;
                } else {
                    node = node.right;
                }
            }
        }
    }

    public String toString() {
        TreeNode n = root;
        StringBuilder sb = new StringBuilder();
        inOrderTraversal(n, sb);
        sb.setLength(sb.length()-2);
        return sb.toString();
    }

    public void inOrderTraversal(TreeNode n, StringBuilder sb) {
        if (n == null) return;
        inOrderTraversal(n.left, sb);
        sb.append(n.val + ", ");
        inOrderTraversal(n.right, sb);
        return;
    }

    public static void main(String[] args) {
        BinaryTree tree = new BinaryTree();
        TreeNode root = new TreeNode(10);
        tree.root = root;
        tree.insert(new TreeNode(5));
        tree.insert(new TreeNode(3));
        tree.insert(new TreeNode(20));
        tree.insert(new TreeNode(25));
        tree.insert(new TreeNode(8));
        System.out.println(tree.toString());

        //Time complexity
        //For insert, it is O(Log n) because it doesn't have to traverse the entire tree, because it's a binary search tree, it only has
        //to traverse the left or the right subtress
        //For toString, it is O(N) because it has to traverse the entire tree to print the values


        //For #8
        //The binary search tree is super efficient for searching because of it's property that left sub tree is always less than root
        //and right sub tree values are always higher than root value. For us to search in a binary tree, we don't have to search the entire tree nodes,
        // we just have to search either the left sub tree or right sub tree depending on the search value.

        //For example, let's consider this binary search tree,

        //                            10
        //                          /    \
        //                         8     12
        //                        / \      \
        //                       5   9      15
        //

        //In the above binary search tree, if we have to search for 5, we have to traverse the left sub tree of 10 because 5 is lesser than 10,
        // and when we reach 8, again we have to traverse the left sub tree and when we reach 5, that's the target value. So here we just had to
        // search 3 nodes and identified the node, which is why it's O(Log n) for search and the super efficient one


        //The Linked list on the other hand, is linear search, if we have to search for an element in a lined list of 1000 nodes, there is no way we could
        // do a binary search, we have to traverse in linear fashion, starting from head to tail.

        // Let's consider the below example
        // 1 -> 4-> 5-> 6 -> 9-> Null

        //In the above example, if we have to search for 6, we have to start from head node which is 1 and iterate linearly until we find the element
        // So this is linear search and average and worst case is O(N).

        //So Binary search tree is efficient when the tree is not skewed and only takes O(Log n), which is better than LinkedList which takes O(N)
    }
}

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    public TreeNode(int val) {
        this.val = val;
        this.left = null;
        this.right = null;
    }
}
