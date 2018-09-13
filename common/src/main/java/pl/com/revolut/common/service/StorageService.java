package pl.com.revolut.common.service;

import pl.com.revolut.exception.NullParameterException;
import pl.com.revolut.common.utils.impl.ServiceUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * abstract storage service. Other service implementations would be drive from the Storage Service and
 * would use the methods derived from the storage service
 * @param <K> key of the map, must be unique in the system
 * @param <V> value of the map
 */
public abstract class StorageService<K,V> {
    private final Map<K,V> map = new ConcurrentHashMap<>();


    protected StorageService(){ }

    /**
     * @return all values in the map
     */
    protected final List<V> getAll(){
        synchronized (map) {
            List<V> valueList = map.values().parallelStream().collect(Collectors.toList());
            return Collections.unmodifiableList(valueList);
        }
    }

    /**
     *
     * @param key is identifier of the value
     * @return the value which has {@param key}
     * @throws NullParameterException if the {@param key} is null
     */
    protected final V getItem(final K key) throws NullParameterException {
        ServiceUtils.checkParameters(key);
        return map.get(key);
    }

    /**
     * add or update the item according to whether or not the value is present
     * @param key of the value
     * @param value the item in the store
     * @throws NullParameterException if the parameters are null
     */
    protected final void addOrUpdateItem(final K key, final V value) throws NullParameterException {
        ServiceUtils.checkParameters(key,value);
        map.put(key,value);
    }

    /**
     * remove the value which has key from the store
     * @param key of the value
     * @throws NullParameterException if the {@param key} is null
     */
    protected final V remove(final K key) throws NullParameterException {
        ServiceUtils.checkParameters(key);
        return map.remove(key);
    }
}
