package pl.org.revolut.app.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class StorageService<K,V> {
    private final Map<K,V> map = new HashMap<>();

    protected StorageService(){ }

    synchronized protected final List<V> getAll(){
        List<V> valueList =  map.values().parallelStream().collect(Collectors.toList());
        return Collections.unmodifiableList(valueList);
    }

    synchronized protected final V getItem(final K key)
    {
        return map.get(key);
    }

    synchronized protected final void addOrUpdateItem(final K key, final V value)
    {
        map.put(key,value);
    }

    synchronized protected final V remove(final K key)
    {
        return map.remove(key);
    }
}
