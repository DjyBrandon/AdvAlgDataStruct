////package Test;
////
////import java.util.*;
////
////public class UnsortedTableMap<K, V> extends AbstractMap<K, List<V>> {
////
////    private ArrayList<MapEntry<K, List<V>>> table = new ArrayList<>();
////
////    public UnsortedTableMap() {
////    }
////
////    private int findIndex(Object key) {
////        int n = table.size();
////        for (int j = 0; j < n; j++) {
////            if (table.get(j).getKey().equals(key)) {
////                return j;
////            }
////        }
////        return -1;
////    }
////
////    public int size() {
////        return table.size();
////    }
////
////    @Override
////    public List<V> get(Object key) {
////        int j = findIndex(key);
////        if (j == -1) return null;
////        return table.get(j).getValue();
////    }
////
////    @Override
////    public List<V> put(K key, List<V> value) {
////        int j = findIndex(key);
////        if (j == -1) {
////            table.add(new MapEntry<>(key, value));
////            return null;
////        } else {
////            return table.get(j).setValue(value);
////        }
////    }
////
////    public List<V> remove(Object key) {
////        int j = findIndex(key);
////        if (j == -1) return null;
////        return table.remove(j).getValue();
////    }
////
////    private class EntryIterator implements Iterator<Entry<K, List<V>>> {
////        private int j = 0;
////
////        public boolean hasNext() {
////            return j < table.size();
////        }
////
////        public Entry<K, List<V>> next() {
////            if (j == table.size()) throw new NoSuchElementException();
////            return table.get(j++);
////        }
////
////        public void remove() {
////            throw new UnsupportedOperationException();
////        }
////    }
////
////    private class EntryIterable extends AbstractSet<Entry<K, List<V>>> {
////        public Iterator<Entry<K, List<V>>> iterator() {
////            return new EntryIterator();
////        }
////
////        public int size() {
////            return table.size();
////        }
////    }
////
////    @Override
////    public Set<Entry<K, List<V>>> entrySet() {
////        return new EntryIterable();
////    }
////}
//
//package Test;
//
//import java.util.*;
//
//public class UnsortedTableMap<K, V> extends AbstractMap<K, V> {
//
//    private ArrayList<MapEntry<K, V>> table = new ArrayList<>();
//
//    public UnsortedTableMap() {
//    }
//
//    private int findIndex(Object key) {
//        int n = table.size();
//        for (int j = 0; j < n; j++) {
//            if (table.get(j).getKey().equals(key)) {
//                return j;
//            }
//        }
//        return -1;
//    }
//
//    public int size() {
//        return table.size();
//    }
//
//    @Override
//    public boolean containsKey(Object key) {
//        return findIndex(key) != -1;
//    }
//
//    @Override
//    public boolean containsValue(Object value) {
//        for (MapEntry<K, V> entry : table) {
//            if (entry.getValue().equals(value)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    @Override
//    public V get(Object key) {
//        int j = findIndex(key);
//        if (j == -1) return null;
//        return table.get(j).getValue();
//    }
//
//    @Override
//    public V put(K key, V value) {
//        int j = findIndex(key);
//        if (j == -1) {
//            table.add(new MapEntry<>(key, value));
//            return null;
//        } else
//            return table.get(j).setValue(value);
//    }
//
//    public V remove(Object key) {
//        int j = findIndex(key);
//        int n = size();
//        if (j == -1) return null;
//        V answer = table.get(j).getValue();
//        if (j != n - 1) {
//            table.set(j, table.get(n - 1));
//        }
//        table.remove(n - 1);
//        return answer;
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
//    private class EntryIterator implements Iterator<Entry<K, V>> {
//        private int j = 0;
//
//        public boolean hasNext() {
//            return j < table.size();
//        }
//
//        public Entry<K, V> next() {
//            if (j == table.size()) throw new NoSuchElementException();
//            return table.get(j++);
//        }
//
//        public void remove() {
//            throw new UnsupportedOperationException();
//        }
//    }
//
//    private class EntryIterable extends AbstractSet<Entry<K, V>> {
//        public Iterator<Entry<K, V>> iterator() {
//            return new EntryIterator();
//        }
//
//        @Override
//        public int size() {
//            return table.size();
//        }
//    }
//
//    @Override
//    public Set<Entry<K, V>> entrySet() {
//        return new EntryIterable();
//    }
//
//    public static void main(String[] args) {
//        UnsortedTableMap<Integer, String> map = new UnsortedTableMap<>();
//
//        map.put(1, "one");
//        map.put(2, "two");
//        System.out.println("size: " + map.size());
//        System.out.println("containsKey(1): " + map.containsKey(1));
//        System.out.println("get(1): " + map.get(1));
//
//        // 显示所有键值对（顺序不保证）
////        for (Map.Entry<Integer, String> entry : map.entrySet()) {
////            System.out.println(entry.getKey() + " = " + entry.getValue());
////        }
//    }
//}