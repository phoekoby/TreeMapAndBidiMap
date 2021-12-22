package ru.vsu.map.bidimap;

import ru.vsu.map.treemap.SimpleTreeMap;
import java.util.Collection;
import java.util.Map;


public class SimpleDualBidiMap<Key extends Comparable<Key>, Value extends Comparable<Value>> implements BidiMap<Key, Value> {
    Map<Key, Value> normalMap;

    Map<Value, Key> reverseMap;

    BidiMap<Value, Key> inverseBidiMap;


    public SimpleDualBidiMap() {
        normalMap = new SimpleTreeMap<>();
        reverseMap = new SimpleTreeMap<>();
    }
    protected SimpleDualBidiMap(Map<Key,Value> map, Map<Value,Key> reverseMap, BidiMap<Value, Key> inverseBidiMap) {
        this.normalMap=map;
        this.reverseMap=reverseMap;
        this.inverseBidiMap=inverseBidiMap;
    }
    protected SimpleDualBidiMap<Value,Key> createBidiMap(Map<Value,Key> map, Map<Key,Value> reverseMap, BidiMap<Key, Value> inverseBidiMap){
        return new SimpleDualBidiMap<>(map, reverseMap, inverseBidiMap);
    }

    @Override
    public Value put(Key key, Value value) {
        if (normalMap.containsKey(key)) {
            reverseMap.remove(normalMap.get(key));
        }
        if (reverseMap.containsKey(value)) {
            normalMap.remove(reverseMap.get(value));
        }
        final Value obj = normalMap.put(key, value);
        reverseMap.put(value, key);
        return obj;
    }

    @Override
    public Key getKey(Object value) {
        return reverseMap.get(value);
    }
    @Override
    public Value getValue(Object key) {
        return normalMap.get(key);
    }

    @Override
    public Key removeValue(Object value) {
        Key key = null;
        if (reverseMap.containsKey(value)) {
            key = reverseMap.remove(value);
            normalMap.remove(key);
        }
        return key;
    }

    @Override
    public Value removeKey(Object key) {
        Value value = null;
        if (normalMap.containsKey(key)) {
            value = normalMap.remove(key);
            reverseMap.remove(key);
        }
        return value;
    }

    @Override
    public BidiMap<Value, Key> inverseBidiMap() {
        if (inverseBidiMap == null) {
            inverseBidiMap = createBidiMap(reverseMap, normalMap, this);
        }
        return inverseBidiMap;
    }

    @Override
    public Collection<Value> values() {
        return normalMap.values();
    }
}
