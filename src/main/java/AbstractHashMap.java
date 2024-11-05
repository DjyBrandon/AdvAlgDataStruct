//import java.util.ArrayList;
//import java.util.AbstractMap;
//import java.util.Map;
//import java.util.Set;
//import java.util.Random;
//
///**
// * An abstract base class supporting Map implementations that use hash
// * tables with MAD (Multiply-Add-Divide) compression.
// *
// * The base class provides the following means of support:
// * 1) Support for calculating hash values with MAD compression.
// * 2) Support for resizing table when load factor reaches 1/2.
// *
// * Subclass is responsible for providing abstract methods:
// *   createTable(), bucketGet(h,k), bucketPut(h,k,v),
// *   bucketRemove(h,k), and entrySet(),
// * and for accurately maintaining the protected member, n,
// * to reflect changes within bucketPut and bucketRemove.
// *
// * @author Michael T. Goodrich
// * @author Roberto Tamassia
// * @author Michael H. Goldwasser
// */
//public abstract class AbstractHashMap<K, V> extends AbstractMap<K, V> {
//
//    protected int n = 0; // Number of entries in the map
//    protected int capacity; // Capacity of the hash table
//    private int prime; // Prime factor used for compression
//    private long scale, shift; // Scaling and shifting factors
//
//    /** Creates a hash table with the given capacity and prime factor. */
//    public AbstractHashMap(int cap, int p) {
//        prime = p;
//        capacity = cap;
//        Random rand = new Random();
//        scale = rand.nextInt(prime - 1) + 1; // scale from 1 to p-1
//        shift = rand.nextInt(prime); // shift from 0 to p-1
//        createTable(); // Create the table with given capacity
//    }
//
//    /** Creates a hash table with the given capacity and prime factor 109345121. */
//    public AbstractHashMap(int cap) {
//        this(cap, 109345121); // Default prime factor
//    }
//
//    /** Creates a hash table with capacity 17 and prime factor 109345121. */
//    public AbstractHashMap() {
//        this(17); // Default capacity
//    }
//
//    /** Returns the number of entries in the map. */
//    @Override
//    public int size() {
//        return n;
//    }
//
//    /** Tests whether the map is empty. */
//    @Override
//    public boolean isEmpty() {
//        return size() == 0;
//    }
//
//    /** Returns the value associated with the specified key, or null if no such entry exists. */
//    @Override
//    public V get(Object key) {
//        return bucketGet(hashValue(key), (K) key);
//    }
//
//    /** Associates the given value with the given key in the map. */
//    @Override
//    public V put(K key, V value) {
//        V answer = bucketPut(hashValue(key), key, value);
//        if (n > capacity / 2) // If load factor exceeds 0.5, resize
//            resize(2 * capacity - 1); // Resize to twice the capacity
//        return answer;
//    }
//
//    /** Removes the entry with the specified key and returns its value, or null if no such entry. */
//    @Override
//    public V remove(Object key) {
//        return bucketRemove(hashValue(key), (K) key);
//    }
//
//    /** Applies MAD compression to the hash code. */
//    private int hashValue(Object key) {
//        return (int) ((Math.abs(key.hashCode() * scale + shift) % prime) % capacity);
//    }
//
//    /** Resizes the hash table to the new capacity and rehashes all entries. */
//    private void resize(int newCap) {
//        ArrayList<Map.Entry<K, V>> buffer = new ArrayList<>(n);
//        for (Map.Entry<K, V> e : entrySet()) {
//            buffer.add(e);
//        }
//        capacity = newCap;
//        createTable(); // Recreate the table with updated capacity
//        n = 0; // Reset size and reinsert entries
//        for (Map.Entry<K, V> e : buffer) {
//            put(e.getKey(), e.getValue());
//        }
//    }
//
//    // Abstract methods that subclasses must implement
//
//    /** Creates an empty table with length equal to the current capacity. */
//    protected abstract void createTable();
//
//    /**
//     * Returns value associated with key k in bucket with hash value h.
//     * If no such entry exists, returns null.
//     */
//    protected abstract V bucketGet(int h, K k);
//
//    /**
//     * Associates key k with value v in bucket with hash value h,
//     * returning the previously associated value, if any.
//     */
//    protected abstract V bucketPut(int h, K k, V v);
//
//    /**
//     * Removes entry having key k from bucket with hash value h,
//     * returning the previously associated value, if found.
//     */
//    protected abstract V bucketRemove(int h, K k);
//}
