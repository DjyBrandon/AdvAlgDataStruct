import java.io.PrintStream;
import java.util.*;

/**
 * An implementation of a sorted map using an AVL tree.
 * This class provides methods to insert, balance, and traverse the AVL tree.
 *
 * @param <K> the type of keys maintained by this map, which must be comparable
 * @param <V> the type of mapped values
 * @author Brandon
 */
public class AVL1<K extends Comparable<K>, V> extends AbstractMap<K, V> implements SortedMap<K, V> {

    // AVL tree node class
    protected static class AVLNode<K, V> {
        K key; // The key of the node
        V value; // The value associated with the key
        int height; // The height of the node
        AVLNode<K, V> left, right; // Left and right children

        // Constructor to create a new node
        AVLNode(K key, V value) {
            this.key = key;
            this.value = value;
            this.height = 1; // Initial height is set to 1
        }
    }

    private AVLNode<K, V> root; // Root of the AVL tree

    @Override
    public V put(K key, V value) {
        root = insert(root, key, value); // Insert the key-value pair and update the root
        return value; // Return the inserted value
    }

    /**
     * Inserts a key-value pair into the AVL tree.
     *
     * @param node The current node
     * @param key The key to insert
     * @param value The value to associate with the key
     * @return The new root of the subtree
     */
    private AVLNode<K, V> insert(AVLNode<K, V> node, K key, V value) {
        if (node == null) return new AVLNode<>(key, value); // Create a new node if current node is null

        int cmp = key.compareTo(node.key); // Compare the key with the current node's key
        if (cmp < 0) {
            node.left = insert(node.left, key, value); // Insert in the left subtree
        } else if (cmp > 0) {
            node.right = insert(node.right, key, value); // Insert in the right subtree
        } else {
            node.value = value; // Update value if key already exists
            return node; // Return the current node
        }

        return balance(node); // Balance the node if necessary
    }

    /**
     * Balances the AVL tree at the given node.
     *
     * @param node The node to balance
     * @return The new root of the subtree
     */
    private AVLNode<K, V> balance(AVLNode<K, V> node) {
        updateHeight(node); // Update the height of the node
        int balance = getBalance(node); // Calculate the balance factor

        // Left heavy case
        if (balance > 1) {
            if (getBalance(node.left) < 0) {
                node.left = rotateLeft(node.left); // Left-right case
            }
            return rotateRight(node); // Left-left case
        }
        // Right heavy case
        if (balance < -1) {
            if (getBalance(node.right) > 0) {
                node.right = rotateRight(node.right); // Right-left case
            }
            return rotateLeft(node); // Right-right case
        }
        return node; // Already balanced
    }

    // Updates the height of the node
    private void updateHeight(AVLNode<K, V> node) {
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right)); // Update height based on children's heights
    }

    // Returns the height of the node, or 0 if the node is null
    private int getHeight(AVLNode<K, V> node) {
        return node == null ? 0 : node.height; // Return the height of the node
    }

    // Returns the balance factor of the node
    private int getBalance(AVLNode<K, V> node) {
        return node != null ? getHeight(node.left) - getHeight(node.right) : 0; // Calculate balance factor
    }

    // Performs a right rotation on the subtree rooted at the specified node
    private AVLNode<K, V> rotateRight(AVLNode<K, V> node) {
        AVLNode<K, V> leftChild = node.left; // leftChild is the left child of the node
        node.left = leftChild.right; // leftChild's right child becomes node's left child
        leftChild.right = node; // node becomes leftChild's right child
        updateHeight(node); // Update height of node
        return leftChild; // Return new root
    }

    // Performs a left rotation on the subtree rooted at the specified node
    private AVLNode<K, V> rotateLeft(AVLNode<K, V> node) {
        AVLNode<K, V> rightChild = node.right; // rightChild is the right child of the node
        node.right = rightChild.left; // rightChild's left child becomes node's right child
        rightChild.left = node; // node becomes rightChild's left child
        updateHeight(node); // Update height of node
        return rightChild; // Return new root
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> entries = new TreeSet<>(Entry.comparingByKey()); // Create a sorted set for entries
        inOrderTraversal(root, entries); // Populate the entry set using in-order traversal
        return entries; // Return the set of entries
    }

    // In-order traversal to populate the entries set
    private void inOrderTraversal(AVLNode<K, V> node, Set<Entry<K, V>> entries) {
        if (node != null) {
            inOrderTraversal(node.left, entries); // Traverse left subtree
            entries.add(new AbstractMap.SimpleEntry<>(node.key, node.value)); // Add current node to entries
            inOrderTraversal(node.right, entries); // Traverse right subtree
        }
    }

    // Post-order traversal to output keys
    private void postOrderTraversal(AVLNode<K, V> node, PrintStream out) {
        if (node != null) {
            postOrderTraversal(node.left, out); // Traverse left subtree
            postOrderTraversal(node.right, out); // Traverse right subtree
            out.println(node.key); // Output the key
        }
    }

    // Helper method to perform post-order traversal starting from the root
    private void postOrderTraversal() {
        postOrderTraversal(root, System.out); // Print keys in post-order
    }

    @Override
    public Comparator<? super K> comparator() {
        return null; // No custom comparator; use natural order
    }



    @Override
    public K firstKey() {
        return firstEntry().getKey(); // Return the first key
    }

    @Override
    public K lastKey() {
        return lastEntry().getKey(); // Return the last key
    }

    @Override
    public SortedMap<K, V> subMap(K fromKey, K toKey) {
        throw new UnsupportedOperationException("SubMap not supported yet"); // Not implemented
    }

    @Override
    public SortedMap<K, V> headMap(K toKey) {
        throw new UnsupportedOperationException("HeadMap not supported yet"); // Not implemented
    }

    @Override
    public SortedMap<K, V> tailMap(K fromKey) {
        throw new UnsupportedOperationException("TailMap not supported yet"); // Not implemented
    }

    // Returns the first entry in the map
    public Entry<K, V> firstEntry() {
        AVLNode<K, V> node = root; // Start from the root
        if (node == null) return null; // Return null if the tree is empty
        while (node.left != null) {
            node = node.left; // Traverse to the leftmost node
        }
        return new AbstractMap.SimpleEntry<>(node.key, node.value); // Return the first entry
    }

    // Returns the last entry in the map
    public Entry<K, V> lastEntry() {
        AVLNode<K, V> node = root; // Start from the root
        if (node == null) return null; // Return null if the tree is empty
        while (node.right != null) {
            node = node.right; // Traverse to the rightmost node
        }
        return new AbstractMap.SimpleEntry<>(node.key, node.value); // Return the last entry
    }

    public static void main(String[] args) {
        AVL1<Integer, String> avl = new AVL1<>(); // Create a new AVL tree

        try (Scanner scanner = new Scanner(System.in)) {
            if (scanner.hasNextLine()) {
                String line = scanner.nextLine(); // Read input line
                String[] keys = line.split(","); // Split keys by comma
                for (String key : keys) {
                    avl.put(Integer.parseInt(key.trim()), null); // Insert keys into the tree
                }
            }

            avl.postOrderTraversal(); // Output keys in post-order
        }
    }
}