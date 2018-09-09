package pl.org.revolut.app.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class StoreageService<K,V> {
    private final Map<K,V> map = new HashMap<>();

    private StoreageService(){ }

    synchronized public final List<V> getAll(){
        List<V> valueList =  map.values().parallelStream().collect(Collectors.toList());
        return Collections.unmodifiableList(valueList);
    }

    synchronized public final V getItem(final K key)
    {
        return map.get(key);
    }

    synchronized public final void addOrUpdateItem(final K key, final V value)
    {
        map.put(key,value);
    }

    synchronized public final V remove(final K key)
    {
        return map.remove(key);
    }
}
