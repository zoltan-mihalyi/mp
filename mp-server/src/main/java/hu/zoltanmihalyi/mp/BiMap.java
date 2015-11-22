package hu.zoltanmihalyi.mp;

import java.util.HashMap;
import java.util.Map;

public class BiMap<K, V> {
    private Map<K, V> map = new HashMap<>();
    private Map<V, K> inverseMap = new HashMap<>();

    public void put(K k, V v) {
        map.put(k, v);
        inverseMap.put(v, k);
    }

    public void removeKey(K k) {
        V v = map.get(k);
        if (v != null) {
            map.remove(k);
            inverseMap.remove(v);
        }
    }

    public void removeValue(V v) {
        K k = inverseMap.get(v);
        if (k != null) {
            inverseMap.remove(v);
            map.remove(k);
        }
    }

    public boolean containsKey(K k) {
        return map.containsKey(k);
    }

    public boolean containsValue(V v) {
        return inverseMap.containsKey(v);
    }

    public V getValue(K k) {
        return map.get(k);
    }

    public K getKey(V userRoom) {
        return inverseMap.get(userRoom);
    }

    public int size() {
        return map.size();
    }
}
