import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache<K, V> {
    private final int maxSize;
    private final Map<K, V> cache;

    public LRUCache(int maxSize) {
        this.maxSize = maxSize;
        this.cache = new LinkedHashMap<>(maxSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > LRUCache.this.maxSize;
            }
        };
    }

    // Add an item to the cache
    public synchronized void put(K key, V value) {
        cache.put(key, value);
    }

    // Retrieve an item from the cache
    public synchronized V get(K key) {
        return cache.get(key);
    }

    // Remove an item from the cache
    public synchronized V remove(K key) {
        return cache.remove(key);
    }

    // Clear the cache
    public synchronized void clear() {
        cache.clear();
    }

    // Get the current size of the cache
    public synchronized int size() {
        return cache.size();
    }

    // Check if the cache contains a key
    public synchronized boolean containsKey(K key) {
        return cache.containsKey(key);
    }

    // Print cache contents for debugging
    public synchronized void printCache() {
        System.out.println("Cache contents: " + cache);
    }
}
