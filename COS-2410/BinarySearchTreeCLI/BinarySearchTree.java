package BinarySearchTreeCLI;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple binary search tree (BST) implementation.
 *
 * <p>Supports creation of a balanced BST, as well as insertion, deletion,
 * and traversals (in-order, pre-order, post-order).</p>
 *
 * <p>Limitations:
 * <ul>
 *   <li>Does not self-balance when adding or deleting values
 *       (e.g., no AVL or Red-Black balancing).</li>
 *   <li>Duplicates are ignored (insertion of an existing value has no effect).</li>
 * </ul>
 *
 * @param <T> the element type stored in this tree; must implement {@link Comparable}
 */
public final class BinarySearchTree<T extends Comparable<? super T>> {

    /**
     * Creates an empty BST.
     */
    public BinarySearchTree() {
        // empty tree
    }

    /**
     * A node in the binary search tree.
     *
     * <p>Each node stores a key of type {@code U}, plus references to
     * its left and right children.</p>
     *
     * @param <U> the type of the key stored in the node
     */
    private static final class Node<U> {
        U key;
        Node<U> left, right;

        /**
         * Constructs a new node with the given key.
         *
         * @param k the key value to store in this node
         */
        Node(U k) { this.key = k; }
    }

    /** The root node of the tree. */
    private Node<T> root;

    /**
     * Checks if the tree is empty.
     *
     * @return {@code true} if the tree contains no elements; {@code false} otherwise
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Inserts a new key into the tree.
     *
     * <p>If the key already exists, the insertion is ignored.</p>
     *
     * @param key the key to insert (must be non-null)
     * @throws NullPointerException if {@code key} is null
     */
    public void add(T key) {
        Objects.requireNonNull(key, "key");
        root = insert(root, key);
    }

    /**
     * Recursive helper method for insertion.
     *
     * @param n   the current subtree root
     * @param key the key to insert
     * @return the updated subtree root
     */
    private Node<T> insert(Node<T> n, T key) {
        if (n == null) return new Node<>(key);
        int cmp = key.compareTo(n.key);
        if (cmp < 0)       n.left = insert(n.left, key);
        else if (cmp > 0)  n.right = insert(n.right, key);
        return n;
    }

    /**
     * Determines if the tree contains the given key.
     *
     * @param key the key to search for
     * @return {@code true} if the key is found; {@code false} otherwise
     */
    public boolean contains(T key) {
        Node<T> n = root;
        while (n != null) {
            int cmp = key.compareTo(n.key);
            if (cmp == 0) return true;
            n = (cmp < 0) ? n.left : n.right;
        }
        return false;
    }

    /**
     * Deletes a key from the tree, if present.
     *
     * @param key the key to delete
     */
    public void delete(T key) {
        root = deleteRec(root, key);
    }

    /**
     * Recursive helper method for deletion.
     *
     * <p>Cases handled:
     * <ul>
     *   <li>If the node is null, the key was not found.</li>
     *   <li>If the key is smaller, recurse into the left subtree.</li>
     *   <li>If the key is larger, recurse into the right subtree.</li>
     *   <li>If the key is found:
     *     <ul>
     *       <li>No left child → return right child.</li>
     *       <li>No right child → return left child.</li>
     *       <li>Two children → replace this node with its in-order successor
     *           (minimum key in the right subtree).</li>
     *     </ul>
     *   </li>
     * </ul>
     * </p>
     *
     * @param n   the current subtree root
     * @param key the key to delete
     * @return the updated subtree root
     */
    private Node<T> deleteRec(Node<T> n, T key) {
        if (n == null) return null;
        int cmp = key.compareTo(n.key);
        if (cmp < 0) {
            n.left = deleteRec(n.left, key);
        } else if (cmp > 0) {
            n.right = deleteRec(n.right, key);
        } else {
            // Found the node to delete
            if (n.left == null)  return n.right;
            if (n.right == null) return n.left;
            // Two children: replace with successor
            Node<T> succ = min(n.right);
            n.key = succ.key;
            n.right = deleteRec(n.right, succ.key);
        }
        return n;
    }

    /**
     * Finds the node with the minimum key in the given subtree.
     *
     * @param n the root of the subtree
     * @return the node containing the minimum key
     */
    private Node<T> min(Node<T> n) {
        while (n.left != null) n = n.left;
        return n;
    }

    /**
     * The available traversal orders for the tree.
     */
    public enum Traversal {
        /** In-order traversal: Left → Node → Right. */
        InOrder,
        /** Pre-order traversal: Node → Left → Right. */
        PreOrder,
        /** Post-order traversal: Left → Right → Node. */
        PostOrder
    }

    /**
     * Traverses the tree in the specified order and returns the keys as a list.
     *
     * @param t the traversal order (in-order, pre-order, or post-order)
     * @return a list of keys in the specified order
     */
    public List<T> traverse(Traversal t) {
        List<T> out = new ArrayList<>();
        switch (t) {
            case InOrder  -> inOrder(root, out);
            case PreOrder -> preOrder(root, out);
            case PostOrder-> postOrder(root, out);
        }
        return out;
    }

    /**
     * In-order traversal (Left → Node → Right).
     *
     * @param n   the current node
     * @param out the list to collect results
     */
    private void inOrder(Node<T> n, List<T> out) {
        if (n == null) return;
        inOrder(n.left, out);
        out.add(n.key);
        inOrder(n.right, out);
    }

    /**
     * Pre-order traversal (Node → Left → Right).
     *
     * @param n   the current node
     * @param out the list to collect results
     */
    private void preOrder(Node<T> n, List<T> out) {
        if (n == null) return;
        out.add(n.key);
        preOrder(n.left, out);
        preOrder(n.right, out);
    }

    /**
     * Post-order traversal (Left → Right → Node).
     *
     * @param n   the current node
     * @param out the list to collect results
     */
    private void postOrder(Node<T> n, List<T> out) {
        if (n == null) return;
        postOrder(n.left, out);
        postOrder(n.right, out);
        out.add(n.key);
    }

    /**
     * Builds a balanced BST containing all integers in the range
     * {@code [minVal, maxVal]} inclusive.
     *
     * @param minVal the minimum value in the range
     * @param maxVal the maximum value in the range
     * @return a new balanced BST containing the range of values
     */
    public static BinarySearchTree<Integer> fromInclusiveRange(int minVal, int maxVal) {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        bst.root = buildBalanced(minVal, maxVal);
        return bst;
    }

    /**
     * Recursively builds a balanced BST from a range of integers.
     *
     * <p>Base case: if {@code minVal > maxVal}, return {@code null}.</p>
     * <p>Recursive step: choose the midpoint as the root, build left and right subtrees
     * from the corresponding halves of the range.</p>
     *
     * @param minVal the minimum value in the range
     * @param maxVal the maximum value in the range
     * @return the root node of the constructed balanced BST, or {@code null} if the range is empty
     */
    private static Node<Integer> buildBalanced(int minVal, int maxVal) {
        if (minVal > maxVal) return null;
        int mid = minVal + (maxVal - minVal) / 2;
        Node<Integer> r = new Node<>(mid);
        r.left  = buildBalanced(minVal, mid - 1);
        r.right = buildBalanced(mid + 1, maxVal);
        return r;
    }
}
