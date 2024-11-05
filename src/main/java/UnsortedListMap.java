//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//
//public class UnsortedListMap<K, V> extends AbstractMap<K, V> {
//
//    private Set<Map.Entry<K, V>> entrySet;
//
//    public UnsortedListMap() {
//        entrySet = new HashSet<>(); // 使用 HashSet 来存储键值对，这将不会保持顺序
//    }
//
//    @Override
//    public Set<Map.Entry<K, V>> entrySet() {
//        return entrySet;  // 返回存储键值对的集合
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
//        entrySet.add(new SimpleEntry<>(key, value));
//        return null;
//    }
//
//    public static void main(String[] args) {
//        // 测试未排序的 Map 实现
//        UnsortedListMap<String, Integer> map = new UnsortedListMap<>();
//
//        // 添加元素
//        map.put("one", 1);
//        map.put("two", 2);
//        map.put("three", 3);
//
//        // 显示所有键值对（顺序不保证）
//        for (Map.Entry<String, Integer> entry : map.entrySet()) {
//            System.out.println(entry.getKey() + " = " + entry.getValue());
//        }
//
//        // 更新现有键的值
//        map.put("two", 22);
//        System.out.println("After updating 'two':");
//
//        for (Map.Entry<String, Integer> entry : map.entrySet()) {
//            System.out.println(entry.getKey() + " = " + entry.getValue());
//        }
//    }
//}
