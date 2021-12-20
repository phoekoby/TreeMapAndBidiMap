package ru.vsu.map.treemap;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class SimpleTreeMap<Key,Value> implements Map<Key,Value> {
    private int size = 0;
    
    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public Value get(Object key) {
        return null;
    }

    @Override
    public Value put(Key key, Value value) {
        return null;
    }

    @Override
    public Value remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends Key, ? extends Value> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<Key> keySet() {
        return null;
    }

    @Override
    public Collection<Value> values() {
        return null;
    }

    @Override
    public Set<Entry<Key, Value>> entrySet() {
        return null;
    }
}
