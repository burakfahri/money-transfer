package pl.com.revolut.common.service;

import pl.com.revolut.exception.NullParameterException;
import pl.com.revolut.common.utils.impl.ServiceUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public abstract class StorageService<K,V> {
    private final Map<K,V> map = new ConcurrentHashMap<>();


    protected StorageService(){ }

    protected final List<V> getAll(){
        synchronized (map) {
            List<V> valueList = map.values().parallelStream().collect(Collectors.toList());
            return Collections.unmodifiableList(valueList);
        }
    }

    protected final V getItem(final K key) throws NullParameterException {
        ServiceUtils.checkParameters(key);
        return map.get(key);
    }

    protected final void addOrUpdateItem(final K key, final V value) throws NullParameterException {
        ServiceUtils.checkParameters(key,value);
        map.put(key,value);
    }

    protected final V remove(final K key) throws NullParameterException {
        ServiceUtils.checkParameters(key);
        return map.remove(key);
    }
}
