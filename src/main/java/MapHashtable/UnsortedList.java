//package MapHashtable;
//
//import java.util.*;
//
//public class UnsortedList<K, V> extends AbstractMap<K, V> {
//
//    private Set<Map.Entry<K, V>> entrySet;
//
//    public UnsortedList() {
//        entrySet = new HashSet<>(); // 使用 HashSet 来存储键值对，这将不会保持顺序
//    }
//
//    @Override
//    public Set<Map.Entry<K, V>> entrySet() {
//        return entrySet;  // 返回存储键值对的集合
//    }
//
//    @Override
//    public int size() {
//        return entrySet().size();
//    }
//
//    @Override
//    public boolean containsKey(Object key) {
//        for (Map.Entry<K,V> entry : entrySet()) {
//            if (Objects.equals(key, entry.getKey()))
//                return true;
//        }
//        return false;
//    }
//
//    @Override
//    public boolean containsValue(Object value) {
//        for (Map.Entry<K,V> entry : entrySet()) {
//            if (Objects.equals(value, entry.getValue()))
//                return true;
//        }
//        return false;
//    }
//
//    @Override
//    public V get(Object key) {
//        for (Map.Entry<K,V> entry : entrySet()) {
//            if (Objects.equals(key, entry.getKey()))
//                return entry.getValue();
//        }
//        return null;
//    }
//
//    @Override
//    public V put(K key, V value) {
//        // 先检查是否已存在该键
//        for (Map.Entry<K, V> entry : entrySet) {
//            if (entry.getKey().equals(key)) {
//                // 如果已存在该键，更新其值
//                V oldValue = entry.getValue();
//                entry.setValue(value);
//                return oldValue;
//            }
//        }
//
//        // 如果键不存在，则新增键值对
//        entrySet.add(new MapEntry<>(key, value));
//        return null;
//    }
//
//    @Override
//    public V remove(Object key) {
//        Iterator<Entry<K,V>> i = entrySet().iterator();
//        Map.Entry<K,V> correctEntry = null;
//        while (correctEntry == null && i.hasNext()) {
//            Map.Entry<K,V> entry = i.next();
//            if (Objects.equals(key, entry.getKey())) {
//                correctEntry = entry;
//            }
//        }
//
//        V oldValue = null;
//        if (correctEntry != null) {
//            oldValue = correctEntry.getValue();
//            i.remove();
//        }
//        return oldValue;
//    }
//
//    @Override
//    public void putAll(Map<? extends K, ? extends V> m) {
//        for (Map.Entry<? extends K, ? extends V> e : m.entrySet())
//            put(e.getKey(), e.getValue());
//    }
//
//    @Override
//    public void clear() {
//        entrySet().clear();
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (o == this)
//            return true;
//
//        if (!(o instanceof Map))
//            return false;
//        Map<?,?> m = (Map<?,?>) o;
//        if (m.size() != size())
//            return false;
//
//        try {
//            for (Map.Entry<K,V> e : entrySet()) {
//                K key = e.getKey();
//                V value = e.getValue();
//                if (value == null) {
//                    if (!(m.get(key)==null && m.containsKey(key)))
//                        return false;
//                } else {
//                    if (!value.equals(m.get(key)))
//                        return false;
//                }
//            }
//        } catch (ClassCastException | NullPointerException unused) {
//            return false;
//        }
//
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        int h = 0;
//        for (Map.Entry<K,V> entry : entrySet())
//            h += entry.hashCode();
//        return h;
//    }
//
//    @Override
//    public String toString() {
//        Iterator<Map.Entry<K,V>> i = entrySet().iterator();
//        if (! i.hasNext())
//            return "{}";
//
//        StringBuilder sb = new StringBuilder();
//        sb.append('{');
//        for (;;) {
//            Map.Entry<K,V> e = i.next();
//            K key = e.getKey();
//            V value = e.getValue();
//            sb.append('<');
//            sb.append(key   == this ? "(this Map)" : key);
//            sb.append(',');
//            sb.append(value == this ? "(this Map)" : value);
//            sb.append('>');
//            if (! i.hasNext())
//                return sb.append('}').toString();
//            sb.append(',').append(' ');
//        }
//    }
//
//    public static void main(String[] args) {
//        // 测试未排序的 Map 实现
//        UnsortedList<Integer, String> map = new UnsortedList<>();
//        UnsortedList<Integer, String> map1 = new UnsortedList<>();
//
//        map.put(1, "one");
//        map.put(2, "two");
//        map.put(3, "three");
//        map1.putAll(map);
//
//        System.out.println("size(): " + map.size());
//        System.out.println("isEmpty(): " + map.isEmpty());
//        System.out.println("containsKey(1): " + map.containsKey(1));
//        System.out.println("containsKey(4): " + map.containsKey(4));
//        System.out.println("containsValue(\"one\"): " + map.containsValue("one"));
//        System.out.println("containsValue(\"four\"): " + map.containsValue("four"));
//        System.out.println("get(1): " + map.get(1));
//        System.out.println("remove(3): " + map.remove(3));
//
//        // 显示所有键值对（顺序不保证）
//        for (Map.Entry<Integer, String> entry : map1.entrySet()) {
//            System.out.println(entry.getKey() + " = " + entry.getValue());
//        }
//        System.out.println("map1 is empty: " + map1.isEmpty());
//        map1.clear();
//        System.out.println("after clear map1 is empty: " + map1.isEmpty());
//
//        // 更新现有键的值
//        map.put(2, "two two");
//        System.out.println("After updating 2:");
//
//        // 显示所有键值对（顺序不保证）
//        for (Map.Entry<Integer, String> entry : map.entrySet()) {
//            System.out.println(entry.getKey() + " = " + entry.getValue());
//        }
//
//        System.out.println("keySet(): " + map.keySet());
//        System.out.println("values(): " + map.values());
//
//        System.out.println("map equals map1:" + map.equals(map1));
//        System.out.println("hashCode(): " + map.hashCode());
//        System.out.println("map.toString(): " + map.toString());
//    }
//}