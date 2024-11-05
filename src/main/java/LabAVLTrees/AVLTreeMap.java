package LabAVLTrees;

import LabMapHashtables.Entry;
import LabSortedMapBST.Position;
import LabSortedMapBST.TreeMap;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * An implementation of a sorted map using an AVL tree.
 *
 * @author Eric Zamore
 * @author Michael T. Goodrich
 * @author Roberto Tamassia
 * @author Michael H. Goldwasser
 */
public class AVLTreeMap<K, V> extends TreeMap<K, V> {

    /**
     * Constructs an empty map using the natural ordering of keys.
     */
    public AVLTreeMap() {
        super();
    }

    /**
     * Constructs an empty map using the given comparator to order keys.
     *
     * @param comp comparator defining the order of keys in the map
     */
    public AVLTreeMap(Comparator<K> comp) {
        super(comp);
    }

    /**
     * Returns the height of the given tree position.
     */
    protected int height(Position<Entry<K, V>> p) {
        return tree.getAux(p);
    }

    /**
     * Recomputes the height of the given position based on its children's heights.
     */
    protected void recomputeHeight(Position<Entry<K, V>> p) {
        tree.setAux(p, 1 + Math.max(height(left(p)), height(right(p))));
    }

    /**
     * Returns whether a position has balance factor between -1 and 1 inclusive.
     */
    protected boolean isBalanced(Position<Entry<K, V>> p) {
        return Math.abs(height(left(p)) - height(right(p))) <= 1;
    }

    /**
     * Returns a child of p with height no smaller than that of the other child.
     */
    protected Position<Entry<K, V>> tallerChild(Position<Entry<K, V>> p) {
        if (height(left(p)) > height(right(p))) return left(p);     // clear winner
        if (height(left(p)) < height(right(p))) return right(p);    // clear winner
        // equal height children; break tie while matching parent's orientation
        if (isRoot(p)) return left(p);                 // choice is irrelevant
        if (p == left(parent(p))) return left(p);      // return aligned child
        else return right(p);
    }

    /**
     * Utility used to rebalance after an insert or removal operation. This traverses the
     * path upward from p, performing a trinode restructuring when imbalance is found,
     * continuing until balance is restored.
     */
    protected void rebalance(Position<Entry<K, V>> p) {
        int oldHeight, newHeight;
        do {
            oldHeight = height(p);                       // not yet recalculated if internal
            if (!isBalanced(p)) {                        // imbalance detected
                // perform trinode restructuring, setting p to resulting root,
                // and recompute new local heights after the restructuring
                p = restructure(tallerChild(tallerChild(p)));
                recomputeHeight(left(p));
                recomputeHeight(right(p));
            }
            recomputeHeight(p);
            newHeight = height(p);
            p = parent(p);
        } while (oldHeight != newHeight && p != null);
    }

    /**
     * Overrides the TreeMap rebalancing hook that is called after an insertion.
     */
    @Override
    protected void rebalanceInsert(Position<Entry<K, V>> p) {
        rebalance(p);
    }

    /**
     * Overrides the TreeMap rebalancing hook that is called after a deletion.
     */
    @Override
    protected void rebalanceDelete(Position<Entry<K, V>> p) {
        if (!isRoot(p))
            rebalance(parent(p));
    }

    /**
     * Ensure that current tree structure is valid AVL (for debug use only).
     */
    private boolean sanityCheck() {
        for (Position<Entry<K, V>> p : tree.positions()) {
            if (isInternal(p)) {
                if (p.getElement() == null)
                    System.out.println("VIOLATION: Internal node has null entry");
                else if (height(p) != 1 + Math.max(height(left(p)), height(right(p)))) {
                    System.out.println("VIOLATION: AVL unbalanced node with key " + p.getElement().getKey());
                    dump();
                    return false;
                }
            }
        }
        return true;
    }

    // 在 AVLTreeMap 类中添加 postOrderTraversal 方法
    public void postOrderTraversal(Position<Entry<K, V>> p, List<K> result) {
        if (p != null && isInternal(p)) {
            // 递归遍历左子树
            postOrderTraversal(left(p), result);
            // 递归遍历右子树
            postOrderTraversal(right(p), result);
            // 添加当前节点的键
            result.add(p.getElement().getKey());
        }
    }

    public static void main(String[] args) {
        // 创建一个 AVLTreeMap 实例
        AVLTreeMap<Integer, String> map = new AVLTreeMap<>();
        List<Integer> keys = new ArrayList<>();

        // 从 input.txt 中读取输入
        // BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line = reader.readLine();
            String[] values = line.split(",");
            for (String value : values) {
                keys.add(Integer.parseInt(value.trim()));
            }
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
            return;
        }

        // 按照读取的顺序插入键值对
        for (int key : keys) {
            map.put(key, "Value" + key);
        }

        // 使用后序遍历获取结果
        List<Integer> result = new ArrayList<>();
        map.postOrderTraversal(map.root(), result);

        // 将结果写入 output.txt 文件
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
            // 按行写入后序遍历的结果
            for (int i = 0; i < result.size(); i++) {
                writer.write(result.get(i).toString());
                if (i < result.size() - 1) {  // 只在非最后一行写入换行符
                    writer.write("\n");
                }
            }
        } catch (IOException e) {
            System.err.println("Error writing to output file: " + e.getMessage());
        }

        System.out.println("Results have been written to output.txt");
    }
}
