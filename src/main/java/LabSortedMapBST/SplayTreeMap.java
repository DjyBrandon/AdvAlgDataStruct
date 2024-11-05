package LabSortedMapBST;

import LabMapHashtables.Entry;

import java.util.Comparator;

/**
 * An implementation of a sorted map using a splay tree.
 *
 * @author Michael T. Goodrich
 * @author Roberto Tamassia
 * @author Michael H. Goldwasser
 */
public class SplayTreeMap<K, V> extends TreeMap<K, V> {

    /**
     * Constructs an empty map using the natural ordering of keys.
     */
    public SplayTreeMap() {
        super();
    }

    /**
     * Constructs an empty map using the given comparator to order keys.
     *
     * @param comp comparator defining the order of keys in the map
     */
    public SplayTreeMap(Comparator<K> comp) {
        super(comp);
    }

    /**
     * Utility used to rebalance after a map operation.
     */
    private void splay(Position<Entry<K, V>> p) {
        while (!isRoot(p)) {
            Position<Entry<K, V>> parent = parent(p);
            Position<Entry<K, V>> grand = parent(parent);
            if (grand == null)                                          // zig case
                rotate(p);
            else if ((parent == left(grand)) == (p == left(parent))) {  // zig-zig case
                rotate(parent);      // move PARENT upward
                rotate(p);           // then move p upward
            } else {                                                    // zig-zag case
                rotate(p);           // move p upward
                rotate(p);           // move p upward again
            }
        }
    }

    /**
     * Overrides the TreeMap rebalancing hook that is called after a node access.
     */
    @Override
    protected void rebalanceAccess(Position<Entry<K, V>> p) {
        if (isExternal(p)) p = parent(p);
        if (p != null) splay(p);
    }

    /**
     * Overrides the TreeMap rebalancing hook that is called after an insertion.
     */
    @Override
    protected void rebalanceInsert(Position<Entry<K, V>> p) {
        splay(p);
    }

    /**
     * Overrides the TreeMap rebalancing hook that is called after a deletion.
     */
    @Override
    protected void rebalanceDelete(Position<Entry<K, V>> p) {
        if (!isRoot(p)) splay(parent(p));
    }

    /**
     * Prints the map using pre-order traversal
     */
    public void printMap() {
        printSubtree(root());  // Print from the root node
    }

    /**
     * Helper method for pre-order traversal.
     */
    private void printSubtree(Position<Entry<K, V>> p) {
        if (isExternal(p)) return;
        System.out.println("Node: " + p.getElement().getKey() + " = " + p.getElement().getValue());
        printSubtree(left(p));   // Traverse left subtree
        printSubtree(right(p));  // Traverse right subtree
    }

    public static void main(String[] args) {
        // 创建一个 SplayTreeMap 对象
        SplayTreeMap<Integer, String> map = new SplayTreeMap<>();

        // 插入一些元素并测试 rebalanceInsert
        System.out.println("Testing rebalanceInsert:");
        map.put(5, "Five");
        map.put(3, "Three");
        map.put(7, "Seven");

        // 打印树中的元素
        map.printMap();

        // 测试 rebalanceAccess
        System.out.println("\nTesting rebalanceAccess:");
        map.get(3); // 访问元素 3，触发重平衡

        // 打印树中的元素
        map.printMap();

        // 测试 rebalanceDelete
        System.out.println("\nTesting rebalanceDelete:");
        map.remove(5); // 删除元素 5，触发重平衡

        // 打印树中的元素
        map.printMap();

        // 测试 splay 方法
        System.out.println("\nTesting splay:");
        map.splay(map.root()); // 测试 splay 方法直接操作根节点

        // 打印树中的元素
        map.printMap();
    }
}