package ru.vsu.map.bidimap;

import java.util.Collection;

public interface BidiMap<Key extends Comparable<Key>, Value extends Comparable<Value>> {
    void put(Key key, Value value);
    Key getKey(Object value);
    Value getValue(Object key);
    Key removeValue(Object value);
    Value removeKey(Object key);
    BidiMap<Value, Key> inverseBidiMap();
    Collection<Value> values();
    Collection<Key> keys();
    boolean isEmpty();
    int size();
}
