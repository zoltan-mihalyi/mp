package hu.zoltanmihalyi.mp;

import org.junit.Test;

import static org.junit.Assert.*;

public class BiMapTest {
    @Test
    public void testContainsKey() {
        BiMap<String, Integer> map = new BiMap<>();
        map.put("1", 1);
        assertTrue(map.containsKey("1"));
    }

    @Test
    public void testContainsValue() {
        BiMap<String, Integer> map = new BiMap<>();
        assertFalse(map.containsValue(1));
        map.put("1", 1);
        assertTrue(map.containsValue(1));
    }

    @Test
    public void testGetValue() {
        BiMap<String, Integer> map = new BiMap<>();
        map.put("1", 1);
        assertEquals(Integer.valueOf(1), map.getValue("1"));
    }

    @Test
    public void testGetKey() {
        BiMap<String, Integer> map = new BiMap<>();
        map.put("1", 1);
        assertEquals("1", map.getKey(1));
    }

    @Test
    public void testRemoveValue() {
        BiMap<String, Integer> map = new BiMap<>();
        map.put("1", 1);
        map.removeValue(1);
        assertFalse(map.containsKey("1"));
        map.removeValue(1); //can be used twice

    }

    @Test
    public void testRemoveKey() {
        BiMap<String, Integer> map = new BiMap<>();
        map.put("1", 1);
        map.removeKey("1");
        assertFalse(map.containsKey("1"));
        map.removeKey("1"); //can be used twice
    }

    @Test
    public void testSize() {
        BiMap<String, Integer> map = new BiMap<>();
        assertEquals(0, map.size());
        map.put("1", 1);
        assertEquals(1, map.size());
        map.put("1", 1);
        assertEquals(1, map.size());
        map.removeKey("1");
        assertEquals(0, map.size());
    }
}
