//package Test;
//
//import java.util.*;
//
//public class SortedTableMap<K, V> extends AbstractSortedMap<K, V> {
//    private ArrayList<AbstractMap.MapEntry<K,V>> table = new ArrayList<>();
//
//    public SortedTableMap( ) { super( ); }
//
//    public SortedTableMap(Comparator<K> comp) { super(comp); }
//
//    private int findIndex(K key, int low, int high) {
//        if (high < low) return high + 1;
//        int mid = (low + high) / 2;
//        int comp = compare(key, table.get(mid));
//        if (comp == 0) {
//            return mid;
//        } else if (comp < 0) {
//            return findIndex(key, low, mid - 1);
//        } else {
//            return findIndex(key, mid + 1, high);
//        }
//    }
//
//    private int findIndex(Object key) { return findIndex((K) key, 0, table.size() - 1); }
//
//    public int size() { return table.size(); }
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
//    public V get(Object key) {
//        int j = findIndex(key);
//        if (j == size() || compare((K) key, table.get(j)) != 0) return null;
//        return table.get(j).getValue();
//    }
//
//    public V put(K key, V value) {
//        int j = findIndex(key);
//        if (j < size() && compare(key, table.get(j)) == 0) {
//            return table.get(j).setValue(value);
//        }
//        table.add(j, new AbstractMap.MapEntry<K,V>(key,value));
//        return null;
//    }
//
//    public V remove(Object key) {
//        int j = findIndex(key);
//        if (j == size() || compare((K) key, table.get(j)) != 0) return null;
//        return table.remove(j).getValue();
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
//    private Entry<K,V> safeEntry(int j) {
//        if (j < 0 || j >= table.size( )) return null;
//        return table.get(j);
//    }
//
//    public Entry<K,V> firstEntry( ) { return safeEntry(0); }
//
//    public Entry<K,V> lastEntry( ) { return safeEntry(table.size( )-1); }
//
//    public Entry<K,V> ceilingEntry(K key) {
//        return safeEntry(findIndex(key));
//    }
//
//    public Entry<K,V> floorEntry(K key) {
//        int j = findIndex(key);
//        if (j == size( ) || ! key.equals(table.get(j).getKey( ))) {
//            j--;
//        }
//        return safeEntry(j);
//    }
//
//    public Entry<K,V> lowerEntry(K key) {
//        return safeEntry(findIndex(key) - 1);
//    }
//
//    public Entry<K,V> higherEntry(K key) {
//        int j = findIndex(key);
//        if (j < size( ) && key.equals(table.get(j).getKey( ))) {
//            j++;
//        }
//        return safeEntry(j);
//    }
//
//    private SortedMap<K,V> snapshot(int startIndex, K stop) {
//        SortedMap<K,V> buffer = new java.util.TreeMap<>(comparator());
//        int j = startIndex;
//        while (j < table.size( ) && (stop == null || compare(stop, table.get(j)) > 0)) {
////            buffer.add(table.get(j++));
//            buffer.put(table.get(j).getKey(), table.get(j).getValue());
//            j++;
//        }
//        return buffer;
//    }
//
//    public Set<Entry<K, V>> entrySet() {
//        return snapshot(0, null).entrySet();
//    }
//
//    @Override
//    public SortedMap<K, V> subMap(K fromKey, K toKey) {
//        return snapshot(findIndex(fromKey), toKey);
//    }
//
//    @Override
//    public SortedMap<K, V> headMap(K toKey) {
//        int index = findIndex(toKey);
//        return snapshot(0, toKey);
//    }
//
//    @Override
//    public SortedMap<K, V> tailMap(K fromKey) {
//        int index = findIndex(fromKey);
//        return snapshot(index, null);
//    }
//
//    @Override
//    public K firstKey() {
//        if (table.isEmpty()) throw new NoSuchElementException("Map is empty");
//        return table.get(0).getKey();
//    }
//
//    @Override
//    public K lastKey() {
//        if (table.isEmpty()) throw new NoSuchElementException("Map is empty");
//        return table.get(table.size() - 1).getKey();
//    }
//
//
//    public static void main(String[] args) {
//        SortedTableMap<String, String> map = new SortedTableMap<>();
//        map.put("Google", "Search");
//        map.put("Apple", "iPhone");
//        map.put("Microsoft", "Windows");
//        map.put("Facebook", "Social");
//
//        System.out.println("First entry: " + map.firstEntry());
//        System.out.println("Last entry: " + map.lastEntry());
//        System.out.println("Submap from A to G: " + map.subMap("A", "G"));
//    }
//}
