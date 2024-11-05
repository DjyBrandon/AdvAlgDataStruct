import java.util.*;
import java.io.PrintStream;

/**
 * An implementation of Adelson-Velskii&Landis Tree
 *
 * @author Brandon
 */
public class AVL<K extends Comparable<K>, V> implements SortedMap<K, V> {

    //---------------- nested MapEntry class ----------------

    /**
     * A concrete implementation of the Entry interface to be used within a Map implementation.
     * Represents a key-value pair in the AVL tree.
     */
    protected static class MapEntry<K, V> implements Entry<K, V> {
        private K k;  // key
        private V v;  // value

        public MapEntry(K key, V value) {
            k = key;
            v = value;
        }

        // Public methods of the Entry interface
        public K getKey() {
            return k;
        }

        public V getValue() {
            return v;
        }

        // Setters and utility methods
        protected void setKey(K key) {
            k = key;
        }

        public V setValue(V value) {
            V old = v;
            v = value;
            return old;
        }

        // Equality and hash code implementations
        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry))
                return false;
            Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
            return Objects.equals(k, e.getKey()) &&
                    Objects.equals(v, e.getValue());
        }

        public int hashCode() {
            return Objects.hashCode(k) ^ Objects.hashCode(v);
        }

        /**
         * Returns string representation (for debugging only)
         */
        public String toString() {
            return "<" + k + ", " + v + ">";
        }
    }
    //----------- end of nested MapEntry class -----------


    //-------------- nested Position interface ----------------
    public interface Position<E> {
        /**
         * Returns the element stored at this position.
         *
         * @return the stored element
         * @throws IllegalStateException if position no longer valid
         */
        E getElement() throws IllegalStateException;
    }
    //----------- end of nested Position interface -----------


    //---------------- nested BalanceableBinaryTree class ----------------

    /**
     * A specialized version of the LinkedBinaryTree class with additional mutators
     * to support binary search tree operations, and a specialized node class that includes
     * an auxiliary instance variable for balancing data.
     */
    protected static class BalanceableBinaryTree<K, V> {
        //-------------- nested BSTNode class --------------

        /**
         * Nested static class for a binary tree node.
         * Represents a node in the AVL tree containing an entry, parent, left child, right child, and height.
         */
        protected static class BSTNode<K, V> implements Position<Entry<K, V>> {
            private Entry<K, V> element;  // The entry stored in the node
            private BSTNode<K, V> parent;  // The parent node
            private BSTNode<K, V> left;    // Left child node
            private BSTNode<K, V> right;   // Right child node
            private int height;             // Height of the node

            public BSTNode(Entry<K, V> e, BSTNode<K, V> parent, BSTNode<K, V> left, BSTNode<K, V> right) {
                element = e;
                this.parent = parent;
                this.left = left;
                this.right = right;
                height = 1;  // Initial height of a new node is 1
            }

            public Entry<K, V> getElement() {
                return element;
            }

            public BSTNode<K, V> getParent() {
                return parent;
            }

            public BSTNode<K, V> getLeft() {
                return left;
            }

            public BSTNode<K, V> getRight() {
                return right;
            }

            public void setElement(Entry<K, V> e) {
                element = e;
            }

            public void setParent(BSTNode<K, V> parent) {
                this.parent = parent;
            }

            public void setLeft(BSTNode<K, V> left) {
                this.left = left;
            }

            public void setRight(BSTNode<K, V> right) {
                this.right = right;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }
        }
        //----------- end of nested Node class -----------

        private BSTNode<K, V> root;  // Root node of the tree
        private int size;             // Number of nodes in the tree

        public BalanceableBinaryTree() {
            root = null;  // Initialize root to null
            size = 0;     // Initialize size to 0
        }

        // Creates a new node
        public BSTNode<K, V> createNode(Entry<K, V> e, BSTNode<K, V> parent, BSTNode<K, V> left, BSTNode<K, V> right) {
            return new BSTNode<>(e, parent, left, right);
        }

        public BSTNode<K, V> root() {
            return root;  // Returns the root node
        }

        // Returns the parent of a given node
        public BSTNode<K, V> parent(BSTNode<K, V> p) {
            return p.getParent();
        }

        public BSTNode<K, V> left(BSTNode<K, V> p) {
            return p.getLeft();  // Returns the left child
        }

        public BSTNode<K, V> right(BSTNode<K, V> p) {
            return p.getRight();  // Returns the right child
        }

        public boolean isInternal(BSTNode<K, V> p) {
            return (p.getLeft() != null || p.getRight() != null);  // Checks if the node has at least one child
        }

        public boolean isExternal(BSTNode<K, V> p) {
            return (p.getLeft() == null && p.getRight() == null);  // Checks if the node is a leaf
        }

        public boolean isRoot(BSTNode<K, V> p) {
            return p == root;  // Checks if the node is the root
        }

        public void set(BSTNode<K, V> p, Entry<K, V> e) {
            p.setElement(e);  // Sets the entry of the node
        }

        public int size() {
            return size;  // Returns the size of the tree
        }

        // Adds a root node to the tree
        public void addRoot(Entry<K, V> e) {
            if (e == null) {
                throw new IllegalArgumentException("Entry cannot be null");
            }
            if (root == null) {
                root = createNode(e, null, null, null);
                size++;  // Increment size
            } else {
                System.out.println("Root already exists!");
            }
        }

        // Adds a left child to a node
        public void addLeft(BSTNode<K, V> p, Entry<K, V> e) {
            BSTNode<K, V> child = createNode(e, p, null, null);
            p.setLeft(child);
            size++;  // Increment size
        }

        // Adds a right child to a node
        public void addRight(BSTNode<K, V> p, Entry<K, V> e) {
            BSTNode<K, V> child = createNode(e, p, null, null);
            p.setRight(child);
            size++;  // Increment size
        }

        // Performs a rotation on the tree to maintain balance
        public void rotate(Position<Entry<K, V>> p) {
            BSTNode<K, V> x = (BSTNode<K, V>) p;
            BSTNode<K, V> y = x.getParent();
            BSTNode<K, V> z = y.getParent();

            if (z == null) {
                root = x;  // x becomes the new root
                x.setParent(null);
            } else if (y == z.getLeft()) {
                z.setLeft(x);
                x.setParent(z);
            } else {
                z.setRight(x);
                x.setParent(z);
            }

            if (x == y.getLeft()) {
                y.setLeft(x.getRight());
                if (x.getRight() != null) x.getRight().setParent(y);
                x.setRight(y);
            } else {
                y.setRight(x.getLeft());
                if (x.getLeft() != null) x.getLeft().setParent(y);
                x.setLeft(y);
            }
            y.setParent(x);
            updateHeight(y);  // Update heights after rotation
            updateHeight(x);
        }

        // Restructures the tree based on the node's position
        public Position<Entry<K, V>> restructure(Position<Entry<K, V>> x) {
            BSTNode<K, V> bx = (BSTNode<K, V>) x;
            BSTNode<K, V> by = parent(bx);
            BSTNode<K, V> bz = parent(by);

            if ((bx == right(by)) == (by == right(bz))) {
                rotate(by);  // Perform a single rotation
                return by;
            } else {
                rotate(bx);  // Perform a double rotation
                rotate(bx);
                return bx;
            }
        }

        // Updates the height of a node
        private void updateHeight(BSTNode<K, V> node) {
            node.setHeight(1 + Math.max(height(left(node)), height(right(node))));
        }

        // Returns the height of a node
        private int height(BSTNode<K, V> node) {
            return (node == null) ? 0 : node.getHeight();
        }

        // Rebalances the tree after an insertion
        public void rebalanceInsert(BSTNode<K, V> p) {
            updateHeight(p);
            while (p != null) {
                updateHeight(p);
                int balanceFactor = height(left(p)) - height(right(p));
                if (Math.abs(balanceFactor) > 1) {
                    p = (BSTNode<K, V>) restructure(tallerChild(tallerChild(p)));
                }
                p = parent(p);
            }
        }

        // Returns the taller child of a node
        private BSTNode<K, V> tallerChild(BSTNode<K, V> p) {
            int leftHeight = height(left(p));
            int rightHeight = height(right(p));
            if (leftHeight > rightHeight) return left(p);
            else if (leftHeight < rightHeight) return right(p);
            else return isRoot(p) ? left(p) : (p == left(parent(p)) ? left(p) : right(p));
        }
    }
    //---------------- end of BalanceableBinaryTree class ----------------

    private BalanceableBinaryTree<K, V> tree = new BalanceableBinaryTree<>();  // The AVL tree

    @Override
    public int size() {
        return tree.size();  // Returns the size of the AVL tree
    }

    @Override
    public boolean isEmpty() {
        return tree.size() == 0;  // Checks if the tree is empty
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;  // Checks if the key exists in the tree
    }

    @Override
    public boolean containsValue(Object value) {
        for (Entry<K, V> entry : entrySet()) {
            if (Objects.equals(entry.getValue(), value)) {
                return true;  // Checks if any entry has the specified value
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        if (!(key instanceof Comparable)) {
            throw new IllegalArgumentException("Key must be comparable");
        }
        @SuppressWarnings("unchecked")
        K k = (K) key;
        BalanceableBinaryTree.BSTNode<K, V> node = tree.root();
        while (node != null) {
            int cmp = k.compareTo(node.getElement().getKey());
            if (cmp < 0) {
                node = node.getLeft();  // Move to the left child
            } else if (cmp > 0) {
                node = node.getRight();  // Move to the right child
            } else {
                return node.getElement().getValue();  // Return the value if the key matches
            }
        }
        return null;  // Return null if the key is not found
    }

    @Override
    public V put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        MapEntry<K, V> newEntry = new MapEntry<>(key, value);
        BalanceableBinaryTree.BSTNode<K, V> node = tree.root();
        if (node == null) {
            tree.addRoot(newEntry);  // Add the first node as root
            return null;
        }
        while (true) {
            int cmp = key.compareTo(node.getElement().getKey());
            if (cmp < 0) {
                if (node.getLeft() == null) {
                    tree.addLeft(node, newEntry);
                    tree.rebalanceInsert(node.getLeft());
                    return null;
                }
                node = node.getLeft();
            } else if (cmp > 0) {
                if (node.getRight() == null) {
                    tree.addRight(node, newEntry);
                    tree.rebalanceInsert(node.getRight());
                    return null;
                }
                node = node.getRight();
            } else {
                V oldValue = node.getElement().getValue();
                node.setElement(newEntry);  // Update the existing value
                return oldValue;  // Return the old value
            }
        }
    }

    @Override
    public V remove(Object key) {
        return null;  // Removal not implemented
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());  // Add all entries from the given map
        }
    }

    @Override
    public void clear() {
        tree = new BalanceableBinaryTree<>();  // Clear the tree by creating a new instance
    }

    public void postOrderTraversal(PrintStream out) {
        postOrderTraversal(tree.root(), out);  // Start post-order traversal from the root
    }

    // Helper method for post-order traversal
    private void postOrderTraversal(BalanceableBinaryTree.BSTNode<K, V> node, PrintStream out) {
        if (node == null) return;  // Base case: if node is null, return
        postOrderTraversal(node.getLeft(), out);  // Traverse left subtree
        postOrderTraversal(node.getRight(), out);  // Traverse right subtree
        out.println(node.getElement().getKey());  // Print the key of the current node
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> entries = new TreeSet<>(Entry.comparingByKey());
        fillEntrySet(tree.root(), entries);  // Fill the entry set using in-order traversal
        return entries;  // Return the set of entries
    }

    // Helper method to fill the entry set
    private void fillEntrySet(BalanceableBinaryTree.BSTNode<K, V> p, Set<Entry<K, V>> entries) {
        if (p != null) {
            fillEntrySet(tree.left(p), entries);  // Traverse left subtree
            entries.add(p.getElement());  // Add the current entry
            fillEntrySet(tree.right(p), entries);  // Traverse right subtree
        }
    }

    @Override
    public Comparator<? super K> comparator() {
        return null;  // No comparator provided
    }

    @Override
    public K firstKey() {
        BalanceableBinaryTree.BSTNode<K, V> node = tree.root();
        if (node == null) throw new NoSuchElementException();
        while (node.getLeft() != null) {
            node = node.getLeft();  // Traverse to the leftmost node
        }
        return node.getElement().getKey();  // Return the key of the leftmost node
    }

    @Override
    public K lastKey() {
        BalanceableBinaryTree.BSTNode<K, V> node = tree.root();
        if (node == null) throw new NoSuchElementException();
        while (node.getRight() != null) {
            node = node.getRight();  // Traverse to the rightmost node
        }
        return node.getElement().getKey();  // Return the key of the rightmost node
    }

    @Override
    public Set<K> keySet() {
        return null;  // Key set not implemented
    }

    @Override
    public Collection<V> values() {
        return null;  // Values collection not implemented
    }

    @Override
    public SortedMap<K, V> subMap(K fromKey, K toKey) {
        throw new UnsupportedOperationException("subMap not implemented");
    }

    @Override
    public SortedMap<K, V> headMap(K toKey) {
        throw new UnsupportedOperationException("headMap not implemented");
    }

    @Override
    public SortedMap<K, V> tailMap(K fromKey) {
        throw new UnsupportedOperationException("tailMap not implemented");
    }

    // Main method for testing the AVL tree
    public static void main(String[] args) {
        AVL<Integer, String> avl = new AVL<>();
        try (Scanner scanner = new Scanner(System.in)) {
            if (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] keys = line.split(",");
                for (String key : keys) {
                    avl.put(Integer.parseInt(key.trim()), null);  // Add keys to the AVL tree
                }
            }
            avl.postOrderTraversal(System.out);  // Print the keys in post-order traversal
        }
    }
}