//package Test;
//
//import java.lang.reflect.Array;
//import java.util.*;
//
//public class ChainHashMap<K, V> extends AbstractHashMap<K, V> {
//
//    private UnsortedTableMap<K, V>[] table;
//
//    public ChainHashMap() {
//        super();
//    }
//
//    public ChainHashMap(int cap) {
//        super(cap);
//    }
//
//    public ChainHashMap(int cap, int p) {
//        super(cap, p);
//    }
//
//    protected void createTable() {
//        table = (UnsortedTableMap<K,V>[]) new UnsortedTableMap[capacity];
//    }
//
//    protected V bucketGet(int h, Object k) {
//        UnsortedTableMap<K, V> bucket = table[h];
//        if (bucket == null) return null;
//        return bucket.get(k);
//    }
//
//    protected V bucketPut(int h, K k, V v) {
//        UnsortedTableMap<K, V> bucket = table[h];
//        if (bucket == null) {
//            bucket = table[h] = new UnsortedTableMap<>();
//        }
//        int oldSize = bucket.size();
//        V answer = bucket.put(k, v);
//        n += (bucket.size() - oldSize);
//        return answer;
//    }
//
//    protected V bucketRemove(int h, Object k) {
//        UnsortedTableMap<K, V> bucket = table[h];
//        if (bucket == null) return null;
//        int oldSize = bucket.size();
//        V answer = bucket.remove(k);
//        n -= (oldSize - bucket.size());
//        return answer;
//    }
//
//    public Set<Entry<K, V>> entrySet() {
//        Set<Entry<K, V>> buffer = new HashSet<>();
//        for (int h = 0; h < capacity; h++) {
//            if (table[h] != null) {
//                for (Entry<K, V> entry : table[h].entrySet()) {
//                    buffer.add(entry);
//                }
//            }
//        }
//        return buffer;
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
//    public static void main(String[] args) {
//        ChainHashMap<Integer, String> chain = new ChainHashMap<>();
//
//        chain.put(158, "one");
//        chain.put(266, "two");
//        chain.put(158, "three");
//        System.out.println("size: " + chain.size());
//        // System.out.println("containsKey(1): " + chain.containsKey(1));
//        System.out.println("get(158): " + chain.get(158));
//    }
//}
