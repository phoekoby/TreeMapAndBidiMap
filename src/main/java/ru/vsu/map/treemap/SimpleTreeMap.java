package ru.vsu.map.treemap;

import java.util.*;

public class SimpleTreeMap<Key extends Comparable<Key>, Value> implements Map<Key,Value> {

    static class MapTreeEntry<Key extends Comparable<? super Key>, Value> implements Map.Entry<Key, Value>, Comparable<MapTreeEntry<Key, Value>> {

        public Key key;
        public Value value;

        public MapTreeEntry(Key key, Value value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public int compareTo(MapTreeEntry<Key, Value> o) {
            return this.key.compareTo(o.key);
        }

        @Override
        public Key getKey() {
            return key;
        }

        @Override
        public Value getValue() {
            return value;
        }

        @Override
        public Value setValue(Value value) {
            Value oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }
    private final RBTree<MapTreeEntry<Key, Value>> tree = new SimpleRedBlackTree<>();

    RBTree<MapTreeEntry<Key, Value>> getTree(){
        return tree;
    }
    @Override
    public int size() {
        return getTree().size();
    }

    @Override
    public boolean isEmpty() {
        return size() <= 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return getTree().contains(new MapTreeEntry<>((Key) key, null));
    }

    @Override
    public boolean containsValue(Object value) {
        for (MapTreeEntry<Key, Value> entry : getTree()) {
            if (entry.value.equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Value get(Object key) {
        MapTreeEntry<Key, Value> entry = getTree().get(new MapTreeEntry<>((Key) key, null));
        return (entry == null) ? null : entry.value;
    }

    @Override
    public Value put(Key key, Value value) {
        MapTreeEntry<Key, Value> entry = getTree().put(new MapTreeEntry<>(key, value));
        return entry == null ? null : entry.value;
    }

    @Override
    public Value remove(Object key) {
        MapTreeEntry<Key, Value> entry = getTree().remove(new MapTreeEntry<>((Key) key, null));
        return entry == null ? null : entry.value;
    }

    @Override
    public void putAll(Map<? extends Key, ? extends Value> m) {
        m.forEach((key, value) -> getTree().put(new MapTreeEntry<>(key, value)));
    }

    @Override
    public void clear() {
        getTree().clear();
    }

    @Override
    public Set<Key> keySet() {
        return new HashSet<>() {
            final Iterator<Map.Entry<Key, Value>> entryIterator = entrySet().iterator();

            @Override
            public int size() {
                return SimpleTreeMap.this.size();
            }

            @Override
            public Iterator<Key> iterator() {
                return new Iterator<>() {
                    @Override
                    public boolean hasNext() {
                        return entryIterator.hasNext();
                    }

                    @Override
                    public Key next() {
                        return entryIterator.next().getKey();
                    }

                };
            }

            // надо будет потом реализовать остальные методы
        };
    }

    @Override
    public Collection<Value> values() {
        return new HashSet<>() {
            final Iterator<Map.Entry<Key, Value>> entryIterator = entrySet().iterator();

            @Override
            public int size() {
                return SimpleTreeMap.this.size();
            }

            @Override
            public Iterator<Value> iterator() {
                return new Iterator<>() {
                    @Override
                    public boolean hasNext() {
                        return entryIterator.hasNext();
                    }

                    @Override
                    public Value next() {
                        return entryIterator.next().getValue();
                    }

                };
            }
        };
    }

    @Override
    public Set<Entry<Key, Value>> entrySet() {
        return new HashSet<>() {
            @Override
            public int size() {
                return SimpleTreeMap.this.size();
            }

            @Override
            public Iterator<Entry<Key, Value>> iterator() {
                return (Iterator<Entry<Key, Value>>) (Object) getTree().iterator();
            }

        };
    }
}
