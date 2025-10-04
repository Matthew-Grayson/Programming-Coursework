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
    public BinarySearchTree() { }

    /**
     * A node in the AVL tree.
     *
     * <p>Each node stores a key, pointers to left and right children, and its
     * cached {@code height}. For null children the height is treated as 0.</p>
     *
     * @param <U> the node key type
     */
    private static final class Node<U> {
        U key;
        Node<U> left, right;
        /** Height of the node (leaf = 1). */
        int height = 1;

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
     * <p>If the key already exists, the insertion is ignored. The tree will be
     * rebalanced along the insertion path per the AVL rules.</p>
     *
     * @param key the key to insert (must be non-null)
     * @throws NullPointerException if {@code key} is null
     */
    public void add(T key) {
        Objects.requireNonNull(key, "key");
        root = insert(root, key);
    }

    /**
     * Deletes a key from the tree, if present.
     *
     * <p>The tree is rebalanced along the deletion path.</p>
     *
     * @param key the key to delete
     */
    public void delete(T key) {
        root = deleteRec(root, key);
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
     * Returns the height of a node, treating {@code null} as height 0.
     */
    private static int height(Node<?> n) {
        return (n == null) ? 0 : n.height;
    }

    /**
     * Recomputes the cached {@link Node#height} from its children.
     */
    private static <U> void updateHeight(Node<U> n) {
        n.height = 1 + Math.max(height(n.left), height(n.right));
    }

    /**
     * Computes the AVL balance factor for a node.
     *
     * <p>Defined as {@code height(left) - height(right)}. Valid AVL nodes
     * must keep this in the range [-1, 1].</p>
     */
    private static int balanceFactor(Node<?> n) {
        return (n == null) ? 0 : height(n.left) - height(n.right);
    }

    /**
     * Right rotation (single rotation) around {@code y}.
     *
     * @param y subtree root to rotate
     * @return new subtree root
     */
    private static <U> Node<U> rotateRight(Node<U> y) {
        Node<U> x  = y.left;
        Node<U> T2 = x.right;

        x.right = y;
        y.left  = T2;

        updateHeight(y);
        updateHeight(x);
        return x;
    }

    /**
     * Left rotation (single rotation) around {@code x}.
     *
     * @param x subtree root to rotate
     * @return new subtree root
     */
    private static <U> Node<U> rotateLeft(Node<U> x) {
        Node<U> y  = x.right;
        Node<U> T2 = y.left;

        y.left  = x;
        x.right = T2;

        updateHeight(x);
        updateHeight(y);
        return y;
    }

    /**
     * Rebalances a node after an insertion, using the inserted {@code key}
     * to decide between single and double rotations for the ambiguous cases.
     *
     * @param n subtree root
     * @param key key being inserted
     * @return balanced subtree root
     */
    private Node<T> rebalanceAfterInsert(Node<T> n, T key) {
        int bf = balanceFactor(n);

        if (bf > 1) {
            System.out.println("Rebalancing node " + n.key + "...");
            if (key.compareTo(n.left.key) < 0) {
                return rotateRight(n);
            } else {
                n.left = rotateLeft(n.left);
                return rotateRight(n);
            }
        }
        if (bf < -1) {
            System.out.println("Rebalancing node " + n.key + "...");
            if (key.compareTo(n.right.key) > 0) {
                return rotateLeft(n);
            } else {
                n.right = rotateRight(n.right);
                return rotateLeft(n);
            }
        }
        return n; // already balanced
    }

    /**
     * Rebalances a node after a deletion. Because the deleted key may no longer
     * exist, we decide rotations using the children's balance factors.
     *
     * @param n subtree root
     * @return balanced subtree root
     */
    private Node<T> rebalanceAfterDelete(Node<T> n) {
        int bf = balanceFactor(n);

        if (bf > 1) {
            System.out.println("Rebalancing node " + n.key + "...");
            if (balanceFactor(n.left) >= 0) {
                return rotateRight(n);
            } else {
                n.left = rotateLeft(n.left);
                return rotateRight(n);
            }
        }
        if (bf < -1) {
            System.out.println("Rebalancing node " + n.key + "...");
            if (balanceFactor(n.right) <= 0) {
                return rotateLeft(n);
            } else {
                n.right = rotateRight(n.right);
                return rotateLeft(n);
            }
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
    private Node<T> insert(Node<T> n, T key) {
        if (n == null) return new Node<>(key);

        int cmp = key.compareTo(n.key);
        if (cmp < 0) {
            n.left = insert(n.left, key);
        } else if (cmp > 0) {
            n.right = insert(n.right, key);
        } else {
            return n; // ignore duplicates
        }

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
    private Node<T> deleteRec(Node<T> n, T key) {
        if (n == null) return null;
        int cmp = key.compareTo(n.key);
        if (cmp < 0) {
            n.left = deleteRec(n.left, key);
        } else if (cmp > 0) {
            n.right = deleteRec(n.right, key);
        } else {
            if (n.left == null)  return n.right;
            if (n.right == null) return n.left;

            Node<T> succ = min(n.right);
            n.key = succ.key;
            n.right = deleteRec(n.right, succ.key);
        }

        updateHeight(n);
        return rebalanceAfterDelete(n);
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
     * <p>The result is a valid AVL tree because heights are computed bottom-up.</p>
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
     * Recursively builds a balanced BST from a range of integers and
     * initializes node heights.
     *
     * @param minVal the minimum value in the range
     * @param maxVal the maximum value in the range
     * @return the root node of the constructed balanced AVL tree,
     * or {@code null} if the range is empty
     */
    private static Node<Integer> buildBalanced(int minVal, int maxVal) {
        if (minVal > maxVal) return null;
        int mid = minVal + (maxVal - minVal) / 2;
        Node<Integer> r = new Node<>(mid);
        r.left  = buildBalanced(minVal, mid - 1);
        r.right = buildBalanced(mid + 1, maxVal);
        // initialize height bottom-up
        r.height = 1 + Math.max(height(r.left), height(r.right));
        return r;
    }
}
