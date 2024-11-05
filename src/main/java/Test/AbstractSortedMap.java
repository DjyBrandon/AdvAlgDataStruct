//package Test;
//
//import java.util.*;
//
//public abstract class AbstractSortedMap<K, V> extends AbstractMap<K, V> implements SortedMap<K, V> {
//    private Comparator<K> comp;
//
//    protected AbstractSortedMap() {
//        this.comp = null; // Use natural ordering
//    }
//
//    protected AbstractSortedMap(Comparator<K> comp) {
//        this.comp = comp;
//    }
//
//    protected int compare(K key1, K key2) {
//        if (comp == null) {
//            return ((Comparable<K>) key1).compareTo(key2);
//        } else {
//            return comp.compare(key1, key2);
//        }
//    }
//
//    protected int compare(K key, Map.Entry<K, V> entry) {
//        return compare(key, entry.getKey());
//    }
//
//    public Comparator<? super K> comparator() {
//        return comp;
//    }
//
//    // Abstract methods that subclasses like SortedTableMap must implement
//    public abstract Entry<K, V> firstEntry();
//
//    public abstract Entry<K, V> lastEntry();
//
//    public abstract Entry<K, V> ceilingEntry(K key);
//
//    public abstract Entry<K, V> floorEntry(K key);
//
//    public abstract Entry<K, V> lowerEntry(K key);
//
//    public abstract Entry<K, V> higherEntry(K key);
//
//    public abstract Set<Entry<K, V>> entrySet();
//
//    public abstract SortedMap<K, V> subMap(K fromKey, K toKey);
//}
