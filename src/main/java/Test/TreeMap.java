//package Test;
//
//import java.util.*;
//
//public class TreeMap<K, V> extends AbstractSortedMap<K, V> {
//
//    //---------------- nested BalanceableBinaryTree class ----------------
//
//    /**
//     * A specialized version of the LinkedBinaryTree class with
//     * additional mutators to support binary search tree operations, and
//     * a specialized node class that includes an auxiliary instance
//     * variable for balancing data.
//     */
//    protected static class BalanceableBinaryTree<K, V> extends LinkedBinaryTree<Entry<K, V>> {
//        //-------------- nested BSTNode class --------------
//        // this extends the inherited LinkedBinaryTree.Node class
//        protected static class BSTNode<E> extends Node<E> {
//            int aux = 0;
//
//            BSTNode(E e, Node<E> parent, Node<E> leftChild, Node<E> rightChild) {
//                super(e, parent, leftChild, rightChild);
//            }
//
//            public int getAux() {
//                return aux;
//            }
//
//            public void setAux(int value) {
//                aux = value;
//            }
//        } //--------- end of nested BSTNode class ---------
//
//        // positional-based methods related to aux field
//        public int getAux(Position<Entry<K, V>> p) {
//            return ((BSTNode<Entry<K, V>>) p).getAux();
//        }
//
//        public void setAux(Position<Entry<K, V>> p, int value) {
//            ((BSTNode<Entry<K, V>>) p).setAux(value);
//        }
//
//        // Override node factory function to produce a BSTNode (rather than a Node)
//        @Override
//        protected Node<Entry<K, V>> createNode(Entry<K, V> e, Node<Entry<K, V>> parent,
//                                               Node<Entry<K, V>> left, Node<Entry<K, V>> right) {
//            return new BSTNode<>(e, parent, left, right);
//        }
//
//        /**
//         * Relinks a parent node with its oriented child node.
//         */
//        private void relink(Node<Entry<K, V>> parent, Node<Entry<K, V>> child,
//                            boolean makeLeftChild) {
//            child.setParent(parent);
//            if (makeLeftChild)
//                parent.setLeft(child);
//            else
//                parent.setRight(child);
//        }
//
//        /**
//         * Rotates Position p above its parent.  Switches between these
//         * configurations, depending on whether p is a or p is b.
//         * <pre>
//         *          b                  a
//         *         / \                / \
//         *        a  t2             t0   b
//         *       / \                    / \
//         *      t0  t1                 t1  t2
//         * </pre>
//         * Caller should ensure that p is not the root.
//         */
//        public void rotate(Position<Entry<K, V>> p) {
//            Node<Entry<K, V>> x = validate(p);
//            Node<Entry<K, V>> y = x.getParent();        // we assume this exists
//            Node<Entry<K, V>> z = y.getParent();        // grandparent (possibly null)
//            if (z == null) {
//                root = x;                                // x becomes root of the tree
//                x.setParent(null);
//            } else
//                relink(z, x, y == z.getLeft());          // x becomes direct child of z
//            // now rotate x and y, including transfer of middle subtree
//            if (x == y.getLeft()) {
//                relink(y, x.getRight(), true);           // x's right child becomes y's left
//                relink(x, y, false);                     // y becomes x's right child
//            } else {
//                relink(y, x.getLeft(), false);           // x's left child becomes y's right
//                relink(x, y, true);                      // y becomes left child of x
//            }
//        }
//
//        /**
//         * Returns the Position that becomes the root of the restructured subtree.
//         * <p>
//         * Assumes the nodes are in one of the following configurations:
//         * <pre>
//         *     z=a                 z=c           z=a               z=c
//         *    /  \                /  \          /  \              /  \
//         *   t0  y=b             y=b  t3       t0   y=c          y=a  t3
//         *      /  \            /  \               /  \         /  \
//         *     t1  x=c         x=a  t2            x=b  t3      t0   x=b
//         *        /  \        /  \               /  \              /  \
//         *       t2  t3      t0  t1             t1  t2            t1  t2
//         * </pre>
//         * The subtree will be restructured so that the node with key b becomes its root.
//         * <pre>
//         *           b
//         *         /   \
//         *       a       c
//         *      / \     / \
//         *     t0  t1  t2  t3
//         * </pre>
//         * Caller should ensure that x has a grandparent.
//         */
//        public Position<Entry<K, V>> restructure(Position<Entry<K, V>> x) {
//            Position<Entry<K, V>> y = parent(x);
//            Position<Entry<K, V>> z = parent(y);
//            if ((x == right(y)) == (y == right(z))) {   // matching alignments
//                rotate(y);                                // single rotation (of y)
//                return y;                                 // y is new subtree root
//            } else {                                    // opposite alignments
//                rotate(x);                                // double rotation (of x)
//                rotate(x);
//                return x;                                 // x is new subtree root
//            }
//        }
//    }
//    //----------- end of nested BalanceableBinaryTree class -----------
//
//    protected BalanceableBinaryTree<K, V> tree = new BalanceableBinaryTree<>();
//
//    public TreeMap() {
//        super();
//        tree.addRoot(null);
//    }
//
//    public TreeMap(Comparator<K> comp) {
//        super(comp);
//        tree.addRoot(null);
//    }
//
//    public int size() {
//        return (tree.size() - 1) / 2;
//    }
//
//    @Override
//    public boolean containsKey(Object key) {
//        return false;
//    }
//
//    @Override
//    public boolean containsValue(Object value) {
//        return false;
//    }
//
//    private void expandExternal(Position<Entry<K, V>> p, Entry<K, V> entry) {
//        tree.set(p, entry);
//        tree.addLeft(p, null);
//        tree.addRight(p, null);
//    }
//
//    protected Position<Entry<K, V>> root() {
//        return tree.root();
//    }
//
//    protected Position<Entry<K, V>> parent(Position<Entry<K, V>> p) {
//        return tree.parent(p);
//    }
//
//    protected Position<Entry<K, V>> left(Position<Entry<K, V>> p) {
//        return tree.left(p);
//    }
//
//    protected Position<Entry<K, V>> right(Position<Entry<K, V>> p) {
//        return tree.right(p);
//    }
//
//    protected Position<Entry<K, V>> sibling(Position<Entry<K, V>> p) {
//        return tree.sibling(p);
//    }
//
//    protected boolean isRoot(Position<Entry<K, V>> p) {
//        return tree.isRoot(p);
//    }
//
//    protected boolean isExternal(Position<Entry<K, V>> p) {
//        return tree.isExternal(p);
//    }
//
//    protected boolean isInternal(Position<Entry<K, V>> p) {
//        return tree.isInternal(p);
//    }
//
//    protected void set(Position<Entry<K, V>> p, Entry<K, V> e) {
//        tree.set(p, e);
//    }
//
//    protected Entry<K, V> remove(Position<Entry<K, V>> p) {
//        return tree.remove(p);
//    }
//
//    protected void rotate(Position<Entry<K, V>> p) {
//        tree.rotate(p);
//    }
//
//    protected Position<Entry<K, V>> restructure(Position<Entry<K, V>> x) {
//        return tree.restructure(x);
//    }
//
//    private Position<Entry<K, V>> treeSearch(Position<Entry<K, V>> p, K key) {
//        if (isExternal(p)) {
//            return p;
//        }
//        int comp = compare(key, p.getElement());
//        if (comp == 0) {
//            return p;
//        } else if (comp < 0) {
//            return treeSearch(left(p), key);
//        } else {
//            return treeSearch(right(p), key);
//        }
//    }
//
//    public V get(Object key) throws IllegalArgumentException {
//        checkKey((K) key);
//        Position<Entry<K, V>> p = treeSearch(root(), (K) key);
//        rebalanceAccess(p);
//        if (isExternal(p)) {
//            return null;
//        }
//        return p.getElement().getValue();
//    }
//
//    public V put(K key, V value) throws IllegalArgumentException {
//        checkKey(key);
//        Entry<K, V> newEntry = new AbstractMap.MapEntry<>(key, value);
//        Position<Entry<K, V>> p = treeSearch(root(), key);
//        if (isExternal(p)) {
//            expandExternal(p, newEntry);
//            rebalanceAccess(p);
//            return null;
//        } else {
//            V old = p.getElement().getValue();
//            set(p, newEntry);
//            rebalanceAccess(p);
//            return old;
//        }
//    }
//
//    public V remove(Object key) throws IllegalArgumentException {
//        checkKey((K) key);
//        Position<Entry<K, V>> p = treeSearch(root(), (K) key);
//        if (isExternal(p)) {
//            rebalanceAccess(p);
//            return null;
//        } else {
//            V old = p.getElement().getValue();
//            if (isInternal(left(p)) && isInternal(right(p))) {
//                Position<Entry<K, V>> replacement = treeMax(left(p));
//                set(p, replacement.getElement());
//                p = replacement;
//            }
//            Position<Entry<K, V>> leaf = (isExternal(left(p)) ? left(p) : right(p));
//            Position<Entry<K, V>> sib = sibling(leaf);
//            remove(leaf);
//            remove(p);
//            rebalanceDelete(sib);
//            return old;
//        }
//    }
//
//    @Override
//    public void putAll(Map<? extends K, ? extends V> m) {
//
//    }
//
//    @Override
//    public void clear() {
//
//    }
//
//    protected void checkKey(K key) {
//        if (key == null) {
//            throw new IllegalArgumentException("Key cannot be null");
//        }
//    }
//
//
//    protected Position<Entry<K, V>> treeMax(Position<Entry<K, V>> p) {
//        Position<Entry<K, V>> walk = p;
//        while (isInternal(walk))
//            walk = right(walk);
//        return parent(walk);
//    }
//
//    public Entry<K, V> firstEntry() {
//        if (isEmpty()) {
//            return null;  // 如果 TreeMap 为空，返回 null
//        }
//
//        Position<Entry<K, V>> p = root();  // 从根节点开始
//        while (isInternal(left(p))) {      // 一直向左遍历，直到到达最左侧的内部节点
//            p = left(p);
//        }
//        return p.getElement();  // 返回最左侧节点的键值对
//    }
//
//    public Entry<K, V> lastEntry() {
//        if (isEmpty()) {
//            return null;
//        }
//        return treeMax(root()).getElement();
//    }
//
//    @Override
//    public Entry<K, V> ceilingEntry(K key) {
//        checkKey(key);  // 检查 key 的合法性
//        Position<Entry<K, V>> p = treeSearch(root(), key);  // 在树中搜索 key
//
//        // 如果找到的节点是内部节点（即 key 已存在），则直接返回该节点的元素
//        if (isInternal(p)) {
//            return p.getElement();
//        }
//
//        // 如果未找到，则需要从当前外部节点向上找到下一个大于等于 key 的节点
//        while (!isRoot(p)) {
//            if (p == left(parent(p))) {  // 如果 p 是其父节点的左子节点
//                return parent(p).getElement();  // 父节点即为下一个大于等于 key 的节点
//            }
//            p = parent(p);  // 向上移动
//        }
//
//        // 如果没有找到合适的节点，返回 null
//        return null;
//    }
//
//    public Entry<K, V> floorEntry(K key) throws IllegalArgumentException {
//        checkKey(key);
//        Position<Entry<K, V>> p = treeSearch(root(), key);
//        if (isInternal(p)) return p.getElement();
//        while (!isRoot(p)) {
//            if (p == right(parent(p))) {
//                return parent(p).getElement();
//            } else {
//                p = parent(p);
//            }
//        }
//        return null;
//    }
//
//    public Entry<K, V> lowerEntry(K key) throws IllegalArgumentException {
//        checkKey(key);
//        Position<Entry<K, V>> p = treeSearch(root(), key);
//        if (isInternal(p) && isInternal(left(p))) {
//            return treeMax(left(p)).getElement();
//        }
//        while (!isRoot(p)) {
//            if (p == right(parent(p))) {
//                return parent(p).getElement();
//            } else {
//                p = parent(p);
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public Entry<K, V> higherEntry(K key) {
//        checkKey(key);  // 检查 key 的合法性
//        Position<Entry<K, V>> p = treeSearch(root(), key);  // 在树中搜索 key
//
//        // 如果找到的节点是内部节点，查找右子树中的最小节点
//        if (isInternal(p)) {
//            if (isInternal(right(p))) {
//                p = right(p);
//                while (isInternal(left(p))) {
//                    p = left(p);
//                }
//                return p.getElement();
//            }
//        }
//
//        // 向上查找直到找到一个父节点，其键大于给定键
//        while (!isRoot(p)) {
//            if (p == left(parent(p))) {  // 如果 p 是父节点的左子节点
//                return parent(p).getElement();  // 返回父节点，它是比 key 大的最小节点
//            }
//            p = parent(p);  // 向上遍历树
//        }
//
//        // 如果没有找到大于给定键的节点，返回 null
//        return null;
//    }
//
//    @Override
//    public Set<Entry<K, V>> entrySet() {
//        Set<Entry<K, V>> buffer = new TreeSet<>(new Comparator<Entry<K, V>>() {
//            @Override
//            public int compare(Entry<K, V> o1, Entry<K, V> o2) {
//                return TreeMap.this.compare(o1.getKey(), o2.getKey()); // 比较 Entry 的键
//            }
//        });
//
//        for (Position<Entry<K, V>> p : tree.inorder()) {
//            if (isInternal(p)) {
//                buffer.add(p.getElement());
//            }
//        }
//        return buffer;
//    }
//
//    @Override
//    public SortedMap<K, V> subMap(K fromKey, K toKey) {
//        SortedMap<K, V> buffer = new java.util.TreeMap<>(comparator());
//        if (compare(fromKey, toKey) < 0) {
//            subMapRecurse(fromKey, toKey, root(), buffer);
//        }
//        return buffer;
//    }
//
//    @Override
//    public SortedMap<K, V> headMap(K toKey) {
//        // 创建一个新的 TreeMap 用于存储头部子映射
//        SortedMap<K, V> buffer = new java.util.TreeMap<>(comparator());
//
//        // 使用递归函数填充子映射，范围是从最小键到 toKey 之前的所有键
//        subMapRecurse(null, toKey, root(), buffer);
//
//        return buffer;
//    }
//
//    @Override
//    public SortedMap<K, V> tailMap(K fromKey) {
//        // 创建一个新的 TreeMap 用于存储尾部子映射
//        SortedMap<K, V> buffer = new java.util.TreeMap<>(comparator());
//
//        // 使用递归函数填充子映射，范围是从 fromKey 到最大键
//        subMapRecurse(fromKey, null, root(), buffer);
//
//        return buffer;
//    }
//
//    @Override
//    public K firstKey() {
//        // 如果树为空，抛出异常
//        if (isEmpty()) {
//            throw new NoSuchElementException("TreeMap is empty");
//        }
//
//        // 获取最左侧节点的键，即最小的键
//        Position<Entry<K, V>> p = root();
//        while (isInternal(left(p))) {
//            p = left(p);
//        }
//        return p.getElement().getKey();  // 返回最小的键
//    }
//
//    @Override
//    public K lastKey() {
//        // 如果树为空，抛出异常
//        if (isEmpty()) {
//            throw new NoSuchElementException("TreeMap is empty");
//        }
//
//        // 获取最右侧节点的键，即最大的键
//        return treeMax(root()).getElement().getKey();
//    }
//
//    private void subMapRecurse(K fromKey, K toKey, Position<Entry<K, V>> p, SortedMap<K, V> buffer) {
////        if (isInternal(p)) {
////            if (compare(fromKey, p.getElement()) < 0) {
////                subMapRecurse(fromKey, toKey, right(p), buffer);
////            } else {
////                subMapRecurse(fromKey, toKey, left(p), buffer);
////                if (compare(toKey, p.getElement()) < 0) {
////                    buffer.add(p.getElement());
////                    subMapRecurse(fromKey, toKey, right(p), buffer);
////                }
////            }
////        }
//        if (isInternal(p)) {
//            int cmpLow = (fromKey == null) ? 1 : compare(p.getElement().getKey(), fromKey);
//            int cmpHigh = (toKey == null) ? -1 : compare(p.getElement().getKey(), toKey);
//
//            // 如果当前节点的键大于等于 fromKey，处理左子树
//            if (cmpLow >= 0) {
//                subMapRecurse(fromKey, toKey, left(p), buffer);
//            }
//
//            // 如果当前节点的键在范围内，加入 buffer
//            if (cmpLow >= 0 && cmpHigh < 0) {
//                buffer.put(p.getElement().getKey(), p.getElement().getValue());
//            }
//
//            // 如果当前节点的键小于 toKey，处理右子树
//            if (cmpHigh < 0) {
//                subMapRecurse(fromKey, toKey, right(p), buffer);
//            }
//        }
//
//    }
//
//    // Stubs for balanced search tree operations (subclasses can override)
//
//    /**
//     * Rebalances the tree after an insertion of specified position.  This
//     * version of the method does not do anything, but it can be
//     * overridden by subclasses.
//     *
//     * @param p the position which was recently inserted
//     */
//    protected void rebalanceInsert(Position<Entry<K, V>> p) {
//    }
//
//    /**
//     * Rebalances the tree after a child of specified position has been
//     * removed.  This version of the method does not do anything, but it
//     * can be overridden by subclasses.
//     *
//     * @param p the position of the sibling of the removed leaf
//     */
//    protected void rebalanceDelete(Position<Entry<K, V>> p) {
//    }
//
//    /**
//     * Rebalances the tree after an access of specified position.  This
//     * version of the method does not do anything, but it can be
//     * overridden by a subclasses.
//     *
//     * @param p the Position which was recently accessed (possibly a leaf)
//     */
//    protected void rebalanceAccess(Position<Entry<K, V>> p) {
//    }
//
//    public static void main(String[] args) {
//        // 创建一个 TreeMap 实例
//        TreeMap<String, String> map = new TreeMap<>();
//
//        // 向 TreeMap 中插入键值对
//        map.put("Google", "Search Engine");
//        map.put("Apple", "Technology Company");
//        map.put("Microsoft", "Software Company");
//        map.put("Amazon", "E-commerce");
//        map.put("Facebook", "Social Media");
//
//        // 测试 get 方法
//        System.out.println("Get value for key 'Google': " + map.get("Google"));
//        System.out.println("Get value for key 'Apple': " + map.get("Apple"));
//
//        // 测试 subMap 方法
//        System.out.println("SubMap from 'A' to 'M': " + map.subMap("A", "M"));
//        System.out.println("SubMap from 'A' to 'Z': " + map.subMap("A", "Z"));
//
//        // 测试 remove 方法
//        map.remove("Facebook");
//        System.out.println("After removing 'Facebook': " + map.entrySet());
//
//        // 测试 firstEntry 和 lastEntry 方法
//        System.out.println("First Entry: " + map.firstEntry());
//        System.out.println("Last Entry: " + map.lastEntry());
//
//        // 测试 lowerEntry 和 floorEntry
//        System.out.println("Lower entry than 'Microsoft': " + map.lowerEntry("Microsoft"));
//        System.out.println("Floor entry for 'Amazon': " + map.floorEntry("Amazon"));
//
//        // 测试 ceilingEntry 和 higherEntry
//        System.out.println("Ceiling entry for 'Amazon': " + map.ceilingEntry("Amazon"));
//        System.out.println("Ceiling entry for 'Facebook': " + map.ceilingEntry("Facebook"));
//        System.out.println("Higher entry than 'Google': " + map.higherEntry("Google"));
//
//        // 测试 headMap 和 tailMap（假设你已实现这些方法）
//        System.out.println("HeadMap to 'Microsoft': " + map.headMap("Microsoft"));
//        System.out.println("TailMap from 'Amazon': " + map.tailMap("Amazon"));
//
//        // 测试 firstKey 和 lastKey
//        System.out.println("First Key: " + map.firstKey());
//        System.out.println("Last Key: " + map.lastKey());
//
//        // 检查完整的映射
//        System.out.println("Complete TreeMap: " + map.entrySet());
//    }
//
//
//}
