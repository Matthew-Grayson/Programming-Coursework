package BinarySearchTreeCLI;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * An AVL (self-balancing) binary search tree (BST) implementation.
 *
 * <p>This tree supports creation, insertion, deletion, and traversals
 * (in-order, pre-order, post-order) of a BST. After each insertion
 * or deletion, the tree automatically rebalances using the AVL rules so
 * that the height difference (balance factor) of any node is in {-1, 0, 1}.
 *
 * <p>Notes:
 * <ul>
 *   <li>Duplicates are ignored (insertion of an existing value has no effect).</li>
 *   <li>All operations assume keys are non-null and mutually comparable.</li>
 * </ul>
 *
 * @param <T> the element type stored in this tree; must implement {@link Comparable}
 */
public final class BinarySearchTree<T extends Comparable<? super T>> {

    /**
     * Creates an empty AVL tree.
     */
    public BinarySearchTree() {
    }

    /**
     * A node in the AVL tree.
     *
     * <p>Stores a key, left/right children, and a cached height (leaf = 1).</p>
     */
    private final class Node {
        T key;
        Node left, right;
        int height = 1;

        Node(T k) {
            this.key = k;
        }
    }

    /**
     * The root node of the tree.
     */
    private Node root;

    /**
     * @return {@code true} if the tree is empty.
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Inserts a key (duplicates ignored) and rebalances.
     */
    public void add(T key) {
        Objects.requireNonNull(key, "key");
        root = insert(root, key);
    }

    /**
     * Deletes a key (if present) and rebalances.
     */
    public void delete(T key) {
        root = deleteRec(root, key);
    }

    /**
     * @return {@code true} if the key exists.
     */
    public boolean contains(T key) {
        Node n = root;
        while (n != null) {
            int cmp = key.compareTo(n.key);
            if (cmp == 0) return true;
            n = (cmp < 0) ? n.left : n.right;
        }
        return false;
    }

    /**
     * Height of {@code n}, treating {@code null} as 0.
     */
    private int height(Node n) {
        return (n == null) ? 0 : n.height;
    }

    /**
     * Recompute height from children.
     */
    private void updateHeight(Node n) {
        n.height = 1 + Math.max(height(n.left), height(n.right));
    }

    /**
     * Balance factor = height(left) - height(right).
     */
    private int balanceFactor(Node n) {
        return (n == null) ? 0 : height(n.left) - height(n.right);
    }

    /**
     * Right rotation around {@code y}.
     */
    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;
        x.right = y;
        y.left = T2;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    /**
     * Left rotation around {@code x}.
     */
    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;
        y.left = x;
        x.right = T2;
        updateHeight(x);
        updateHeight(y);
        return y;
    }

    /**
     * Rebalance after insert (uses inserted {@code key} to pick LL/LR/RR/RL).
     */
    private Node rebalanceAfterInsert(Node n, T key) {
        int bf = balanceFactor(n);
        if (bf > 1) {
            if (key.compareTo(n.left.key) < 0) return rotateRight(n);       // LL
            n.left = rotateLeft(n.left);
            return rotateRight(n);             // LR
        }
        if (bf < -1) {
            if (key.compareTo(n.right.key) > 0) return rotateLeft(n);       // RR
            n.right = rotateRight(n.right);
            return rotateLeft(n);           // RL
        }
        return n;
    }

    /**
     * Rebalance after delete (uses child balance factors).
     */
    private Node rebalanceAfterDelete(Node n) {
        int bf = balanceFactor(n);
        if (bf > 1) {
            if (balanceFactor(n.left) >= 0) return rotateRight(n);          // LL
            n.left = rotateLeft(n.left);
            return rotateRight(n);             // LR
        }
        if (bf < -1) {
            if (balanceFactor(n.right) <= 0) return rotateLeft(n);          // RR
            n.right = rotateRight(n.right);
            return rotateLeft(n);           // RL
        }
        return n;
    }

    /**
     * Recursive AVL insertion.
     *
     * @param n   the current subtree root
     * @param key the key to insert
     * @return the updated (and possibly rotated) subtree root
     */
    private Node insert(Node n, T key) {
        if (n == null) return new Node(key);
        int cmp = key.compareTo(n.key);
        if (cmp < 0) n.left = insert(n.left, key);
        else if (cmp > 0) n.right = insert(n.right, key);
        else return n; // duplicate
        updateHeight(n);
        return rebalanceAfterInsert(n, key);
    }

    /**
     * Recursive AVL deletion.
     *
     * <p>Rebalances the subtree on the way back up after the removal.</p>
     *
     * @param n   the current subtree root
     * @param key the key to delete
     * @return the updated (and possibly rotated) subtree root
     */
    private Node deleteRec(Node n, T key) {
        if (n == null) return null;
        int cmp = key.compareTo(n.key);
        if (cmp < 0) n.left = deleteRec(n.left, key);
        else if (cmp > 0) n.right = deleteRec(n.right, key);
        else {
            if (n.left == null) return n.right;
            if (n.right == null) return n.left;
            Node succ = min(n.right);
            n.key = succ.key;
            n.right = deleteRec(n.right, succ.key);
        }
        updateHeight(n);
        return rebalanceAfterDelete(n);
    }

    /**
     * Finds the node with the minimum key in the given subtree.
     */
    private Node min(Node n) {
        while (n.left != null) n = n.left;
        return n;
    }

    /**
     * Available traversal orders.
     */
    public enum Traversal {InOrder, PreOrder, PostOrder}

    /**
     * Traverse the tree and return the keys as a list.
     */
    public List<T> traverse(Traversal t) {
        List<T> out = new ArrayList<>();
        switch (t) {
            case InOrder -> inOrder(root, out);
            case PreOrder -> preOrder(root, out);
            case PostOrder -> postOrder(root, out);
        }
        return out;
    }

    /**
     * In-order traversal (Left → Node → Right).
     */
    private void inOrder(Node n, List<T> out) {
        if (n == null) return;
        inOrder(n.left, out);
        out.add(n.key);
        inOrder(n.right, out);
    }

    /**
     * Pre-order traversal (Node → Left → Right).
     */
    private void preOrder(Node n, List<T> out) {
        if (n == null) return;
        out.add(n.key);
        preOrder(n.left, out);
        preOrder(n.right, out);
    }

    /**
     * Post-order traversal (Left → Right → Node).
     */
    private void postOrder(Node n, List<T> out) {
        if (n == null) return;
        postOrder(n.left, out);
        postOrder(n.right, out);
        out.add(n.key);
    }

    /**
     * Builds a balanced BST containing all integers in the range
     * {@code [minVal, maxVal]} inclusive.
     *
     * @param minVal the minimum value
     * @param maxVal the maximum value
     * @return a new balanced tree with the values
     */
    public static BinarySearchTree<Integer> fromInclusiveRange(int minVal, int maxVal) {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        buildBalanced(bst, minVal, maxVal);
        return bst;
    }

    /**
     * Recursively builds a balanced BST from a range of integers.
     *
     * @param minVal the minimum value in the range
     * @param maxVal the maximum value in the range
     * @return the root node of the constructed balanced AVL tree,
     * or {@code null} if the range is empty
     */
    private static void buildBalanced(BinarySearchTree<Integer> bst, int minVal, int maxVal) {
        if (minVal > maxVal) return;
        int mid = minVal + (maxVal - minVal) / 2;
        bst.add(mid); // AVL insert keeps the tree balanced
        buildBalanced(bst, minVal, mid - 1);
        buildBalanced(bst, mid + 1, maxVal);
    }
}
