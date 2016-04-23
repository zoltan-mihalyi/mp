package hu.zoltanmihalyi.mp;

import java.util.*;

public class MultiMap<K, V> {
    private Map<K, List<V>> map = new HashMap<>();

    public void put(K k, V v) {
        List<V> values = map.get(k);
        if (values != null) {
            values.add(v);
        } else {
            values = new ArrayList<>();
            values.add(v);
            map.put(k, values);
        }
    }

    public Set<Map.Entry<K, List<V>>> entrySet() {
        return map.entrySet();
    }
}
