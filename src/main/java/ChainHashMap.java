//import java.util.AbstractMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.HashSet;
//
///**
// * Implements a hash table using the Map ADT, handling collisions using
// * Separate Chaining (each bucket is a LinkedList of entries).
// */
//public class ChainHashMap<K, V> extends AbstractHashMap<K, V> {
//
//    // Table where each bucket is a LinkedList to handle collisions
//    private List<Entry<K, V>>[] table;
//
//    // Constructors
//    public ChainHashMap(int capacity, int prime) {
//        super(capacity, prime);
//    }
//
//    public ChainHashMap(int capacity) {
//        super(capacity);
//    }
//
//    public ChainHashMap() {
//        super();
//    }
//
//    /**
//     * Creates an empty hash table with LinkedLists in each bucket
//     */
//    @Override
//    protected void createTable() {
//        table = (List<Entry<K, V>>[]) new LinkedList[capacity];
//        for (int i = 0; i < capacity; i++) {
//            table[i] = new LinkedList<>();
//        }
//    }
//
//    /**
//     * Retrieves the value associated with key k in the bucket with hash value h
//     */
//    @Override
//    protected V bucketGet(int h, K k) {
//        for (Entry<K, V> entry : table[h]) {
//            if (entry.getKey().equals(k)) {
//                return entry.getValue();
//            }
//        }
//        return null; // No entry found
//    }
//
//    /**
//     * Associates key k with value v in the bucket with hash value h
//     */
//    @Override
//    protected V bucketPut(int h, K k, V v) {
//        for (Entry<K, V> entry : table[h]) {
//            if (entry.getKey().equals(k)) {
//                V oldValue = entry.getValue();
//                entry.setValue(v); // Update the value
//                return oldValue;
//            }
//        }
//        // 注意修改这个
//        table[h].add(new SimpleEntry<>(k, v)); // Add a new entry if key not found
//        n++; // Increase the number of entries
//        return null;
//    }
//
//    /**
//     * Removes the entry with key k in the bucket with hash value h
//     */
//    @Override
//    protected V bucketRemove(int h, K k) {
//        for (Entry<K, V> entry : table[h]) {
//            if (entry.getKey().equals(k)) {
//                V oldValue = entry.getValue();
//                table[h].remove(entry); // Remove the entry
//                n--; // Decrease the number of entries
//                return oldValue;
//            }
//        }
//        return null; // No entry found
//    }
//
//    /**
//     * Returns a set of all the entries in the hash table
//     */
//    @Override
//    public Set<Map.Entry<K, V>> entrySet() {
//        Set<Map.Entry<K, V>> set = new HashSet<>();
//        for (List<Entry<K, V>> bucket : table) {
//            set.addAll(bucket); // Add all entries from each bucket
//        }
//        return set;
//    }
//
//    public static void main(String[] args) {
//        ChainHashMap<String, Integer> map = new ChainHashMap<>();
//
//        map.put("one", 1);
//        map.put("two", 2);
//        map.put("three", 3);
//
//        System.out.println("Get 'two': " + map.get("two")); // 输出 2
//        System.out.println("Size: " + map.size()); // 输出 3
//
//        map.remove("two");
//        System.out.println("Get 'two' after removal: " + map.get("two")); // 输出 null
//        System.out.println("Size after removal: " + map.size()); // 输出 2
//
//        map.put("four", 4);
//        map.put("five", 5);
//        map.put("six", 6);
//
//        System.out.println("Get 'four': " + map.get("four")); // 输出 4
//        System.out.println("Get 'six': " + map.get("six")); // 输出 6
//        System.out.println("Size: " + map.size()); // 输出 5
//
//        map.put("two", 8);
//        System.out.println("Get 'two' after update: " + map.get("two")); // 输出 null
//    }
//
//}
