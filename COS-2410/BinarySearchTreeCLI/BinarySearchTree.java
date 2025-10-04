/**
 * References:
 * [GFG-AVL] GeeksforGeeks. (n.d.). AVL Tree program in Java. Retrieved October 5, 2025, from
 *           https://www.geeksforgeeks.org/java/avl-tree-program-in-java/
 */

package BinarySearchTreeCLI;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * An AVL (self-balancing) binary search tree (BST) implementation.
 *
 * <p>This tree supports creation, insertion, deletion, and traversals
 * (in-order, pre-order, post-order). After each insertion or deletion,
 * the tree automatically rebalances using the AVL rules so that the
 * height difference (balance factor) of any node is in {-1, 0, 1}.</p>
 *
 * <p>Notes:
 * <ul>
 *   <li>Duplicates are ignored (inserting an existing value has no effect).</li>
 *   <li>All operations assume keys are non-null and mutually comparable.</li>
 * </ul>
 *
 *
 * @param <T> the element type stored in this tree; must implement {@link Comparable}
 */
public final class BinarySearchTree<T extends Comparable<? super T>> {

    /** Creates an empty AVL tree. */
    public BinarySearchTree() { }

    /**
     * A node in the AVL tree.
     *
     * <p>Stores a key, left/right children, and a cached height (leaf = 1).</p>
     */
    private final class Node {
        T key;
        Node left, right;
        int height = 1;

        /** Creates a node with key {@code k}. */
        Node(T k) { this.key = k; }
    }

    /** The root node of the tree (may be {@code null} for an empty tree). */
    private Node root;

    /**
     * Indicates whether the tree has no elements.
     *
     * @return {@code true} if the tree contains no elements; {@code false} otherwise
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Inserts a key into the tree (duplicates are ignored) and rebalances on the path.
     *
     * @param key the key to insert (must be non-null)
     * @throws NullPointerException if {@code key} is {@code null}
     */
    public void add(T key) {
        Objects.requireNonNull(key, "key");
        root = insert(root, key);
    }

    /**
     * Deletes a key from the tree if present and rebalances on the path.
     *
     * @param key the key to delete
     */
    public void delete(T key) {
        root = deleteRec(root, key);
    }

    /**
     * Determines whether the given key exists in the tree.
     *
     * @param key the key to search for
     * @return {@code true} if the key is found; {@code false} otherwise
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

    /* ===================== AVL helpers ===================== */

    /**
     * Returns the height of a node, treating {@code null} as 0.
     *
     * @param n the node whose height to read (may be {@code null})
     * @return 0 if {@code n} is {@code null}; otherwise the cached node height
     */
    private int height(Node n) {
        return (n == null) ? 0 : n.height;
    }

    /**
     * Recomputes and stores the height of {@code n} from its children.
     *
     * @param n the node to update (must not be {@code null})
     */
    private void updateHeight(Node n) {
        n.height = 1 + Math.max(height(n.left), height(n.right));
    }

    /**
     * Computes the AVL balance factor for a node.
     *
     * <p>Defined as {@code height(left) - height(right)}.
     * Valid AVL nodes keep this in the range [-1, 0, 1].</p>
     *
     * @param n the node (may be {@code null})
     * @return the balance factor, or 0 if {@code n} is {@code null}
     */
    private int balanceFactor(Node n) {
        return (n == null) ? 0 : height(n.left) - height(n.right);
    }

    /**
     * Performs a right rotation around {@code y}.
     *
     * @param y the subtree root to rotate
     * @return the new subtree root after rotation
     */
    private Node rotateRight(Node y) {
        System.out.println("Rebalancing tree by rotating nodes around " + y.key + " to the right...");
        Node x  = y.left;
        Node T2 = x.right;
        x.right = y;
        y.left  = T2;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    /**
     * Performs a left rotation around {@code x}.
     *
     * @param x the subtree root to rotate
     * @return the new subtree root after rotation
     */
    private Node rotateLeft(Node x) {
        System.out.println("Rebalancing tree by rotating nodes around " + x.key + " to the left...");
        Node y  = x.right;
        Node T2 = y.left;
        y.left  = x;
        x.right = T2;
        updateHeight(x);
        updateHeight(y);
        return y;
    }

    /**
     * Rebalances a node after insertion.
     *
     * <p>Uses the inserted {@code key} to disambiguate LL/LR/RR/RL cases.</p>
     *
     * Source: [GFG-AVL]
     *
     * @param n the (possibly unbalanced) subtree root
     * @param key the key that was inserted
     * @return the balanced subtree root
     */
    private Node rebalanceAfterInsert(Node n, T key) {
        int bf = balanceFactor(n);
        if (bf > 1) {
            if (key.compareTo(n.left.key) < 0) return rotateRight(n); // LL
            n.left = rotateLeft(n.left); return rotateRight(n);       // LR
        }
        if (bf < -1) {
            if (key.compareTo(n.right.key) > 0) return rotateLeft(n); // RR
            n.right = rotateRight(n.right); return rotateLeft(n);     // RL
        }
        return n;
    }

    /**
     * Rebalances a node after deletion.
     *
     * <p>Since the deleted key may no longer be available, this method
     * uses child balance factors to select LL/LR/RR/RL rotations.</p>
     *
     * Source: [GFG-AVL]
     *
     * @param n the (possibly unbalanced) subtree root
     * @return the balanced subtree root
     */
    private Node rebalanceAfterDelete(Node n) {
        int bf = balanceFactor(n);
        if (bf > 1) {
            if (balanceFactor(n.left) >= 0) return rotateRight(n); // LL
            n.left = rotateLeft(n.left); return rotateRight(n);    // LR
        }
        if (bf < -1) {
            if (balanceFactor(n.right) <= 0) return rotateLeft(n); // RR
            n.right = rotateRight(n.right); return rotateLeft(n);  // RL
        }
        return n;
    }

    /* ===================== Insert / Delete ===================== */

    /**
     * Recursive AVL insertion.
     *
     * @param n the current subtree root (may be {@code null})
     * @param key the key to insert
     * @return the updated (and possibly rotated) subtree root
     */
    private Node insert(Node n, T key) {
        if (n == null) return new Node(key);
        int cmp = key.compareTo(n.key);
        if (cmp < 0) n.left  = insert(n.left,  key);
        else if (cmp > 0)  n.right = insert(n.right, key);
        else return n; // duplicate
        updateHeight(n);
        return rebalanceAfterInsert(n, key);
    }

    /**
     * Recursive AVL deletion.
     *
     * <p>Rebalances the subtree on the way back up after removal.</p>
     *
     * @param n   the current subtree root (may be {@code null})
     * @param key the key to delete
     * @return the updated (and possibly rotated) subtree root
     */
    private Node deleteRec(Node n, T key) {
        if (n == null) return null;
        int cmp = key.compareTo(n.key);
        if (cmp < 0) {
            n.left = deleteRec(n.left, key);
        } else if (cmp > 0) {
            n.right = deleteRec(n.right, key);
        } else {
            if (n.left == null)  return n.right;
            if (n.right == null) return n.left;
            Node succ = min(n.right);
            n.key = succ.key;
            n.right = deleteRec(n.right, succ.key);
        }
        updateHeight(n);
        return rebalanceAfterDelete(n);
    }

    /**
     * Returns the node with the minimum key in the given subtree.
     *
     * @param n the subtree root (must not be {@code null})
     * @return the node containing the minimum key in that subtree
     */
    private Node min(Node n) {
        while (n.left != null) n = n.left;
        return n;
    }

    /* ===================== Traversals ===================== */

    /**
     * Available traversal orders for the tree.
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
     * @return a list of keys in the specified order (empty if the tree is empty)
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
     * @param n   the current node (may be {@code null})
     * @param out the list to collect visited keys
     */
    private void inOrder(Node n, List<T> out) {
        if (n == null) return;
        inOrder(n.left, out);
        out.add(n.key);
        inOrder(n.right, out);
    }

    /**
     * Pre-order traversal (Node → Left → Right).
     *
     * @param n   the current node (may be {@code null})
     * @param out the list to collect visited keys
     */
    private void preOrder(Node n, List<T> out) {
        if (n == null) return;
        out.add(n.key);
        preOrder(n.left, out);
        preOrder(n.right, out);
    }

    /**
     * Post-order traversal (Left → Right → Node).
     *
     * @param n   the current node (may be {@code null})
     * @param out the list to collect visited keys
     */
    private void postOrder(Node n, List<T> out) {
        if (n == null) return;
        postOrder(n.left, out);
        postOrder(n.right, out);
        out.add(n.key);
    }

    /* ===================== Balanced factory (Integers) ===================== */

    /**
     * Builds a perfectly balanced AVL tree from a sorted, distinct list in O(n).
     * <p>No insertions or rotations are performed; nodes are linked bottom-up and
     * heights are computed as the recursion unwinds.</p>
     *
     * @param sortedDistinct a sorted list of distinct keys
     * @param <E> element type
     * @return a new AVL tree containing all elements in {@code sortedDistinct}
     * @throws NullPointerException if the list or any element is null
     */
    public static <E extends Comparable<? super E>>
    BinarySearchTree<E> fromSortedList(List<E> sortedDistinct) {
        Objects.requireNonNull(sortedDistinct, "sortedDistinct");
        BinarySearchTree<E> bst = new BinarySearchTree<>();
        bst.root = bst.buildBalancedFromList(sortedDistinct, 0, sortedDistinct.size() - 1);
        return bst;
    }

    /** Internal bottom-up builder: links nodes and sets heights (O(n)). */
    private Node buildBalancedFromList(List<T> a, int lo, int hi) {
        if (lo > hi) return null;
        int mid = (lo + hi) >>> 1;
        T key = Objects.requireNonNull(a.get(mid), "list element");
        Node r = new Node(key);
        r.left  = buildBalancedFromList(a, lo, mid - 1);
        r.right = buildBalancedFromList(a, mid + 1, hi);
        r.height = 1 + Math.max(height(r.left), height(r.right));
        return r;
    }


    /**
     * Builds a balanced AVL tree with all integers in the inclusive range
     * {@code [minVal, maxVal]} in O(n) time and without rotations.
     *
     * @param minVal the minimum value (inclusive)
     * @param maxVal the maximum value (inclusive)
     * @return a new AVL tree containing every integer in the range
     * @throws IllegalArgumentException if {@code maxVal < minVal}
     */
    public static BinarySearchTree<Integer> fromInclusiveRange(int minVal, int maxVal) {
        if (maxVal < minVal) {
            throw new IllegalArgumentException("maxVal < minVal");
        }
        int n = maxVal - minVal + 1;
        List<Integer> vals = new ArrayList<>(n);
        for (int v = minVal; v <= maxVal; v++) vals.add(v);
        return BinarySearchTree.fromSortedList(vals);
    }


    /**
     * Recursively inserts values in median-first order into {@code bst} to produce
     * a balanced AVL tree for the range {@code [minVal, maxVal]}.
     *
     * @param bst     the tree to populate
     * @param minVal  the minimum integer value (inclusive)
     * @param maxVal  the maximum integer value (inclusive)
     */
    private static void buildBalanced(BinarySearchTree<Integer> bst, int minVal, int maxVal) {
        if (minVal > maxVal) return;
        int mid = minVal + (maxVal - minVal) / 2;
        bst.add(mid); // AVL insert keeps the tree balanced
        buildBalanced(bst, minVal, mid - 1);
        buildBalanced(bst, mid + 1, maxVal);
    }
}
