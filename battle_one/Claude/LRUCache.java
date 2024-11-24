import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class LRUCache<K, V> {
    private final int maxSize;
    private final ConcurrentHashMap<K, V> cache;
    private final LinkedHashMap<K, Long> accessOrder;
    private final ReentrantReadWriteLock lock;
    
    /**
     * Creates a new LRU cache with the specified maximum size.
     * @param maxSize the maximum number of entries the cache can hold
     * @throws IllegalArgumentException if maxSize is less than 1
     */
    public LRUCache(int maxSize) {
        if (maxSize < 1) {
            throw new IllegalArgumentException("Cache size must be greater than 0");
        }
        
        this.maxSize = maxSize;
        this.cache = new ConcurrentHashMap<>();
        this.accessOrder = new LinkedHashMap<>();
        this.lock = new ReentrantReadWriteLock();
    }
    
    /**
     * Adds a key-value pair to the cache. If the key already exists, the value is updated.
     * If adding would exceed the maximum size, the least recently used entry is removed.
     * @param key the key to add
     * @param value the value to add
     * @throws NullPointerException if key or value is null
     */
    public void put(K key, V value) {
        if (key == null || value == null) {
            throw new NullPointerException("Key and value cannot be null");
        }
        
        lock.writeLock().lock();
        try {
            // Update access time
            accessOrder.put(key, System.nanoTime());
            cache.put(key, value);
            
            // Remove least recently used item if we're over capacity
            if (cache.size() > maxSize) {
                K lruKey = accessOrder.entrySet()
                    .stream()
                    .min(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElseThrow();
                    
                remove(lruKey);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * Retrieves a value from the cache and updates its access time.
     * @param key the key to look up
     * @return an Optional containing the value if found, or empty if not found
     * @throws NullPointerException if key is null
     */
    public Optional<V> get(K key) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }
        
        lock.readLock().lock();
        try {
            V value = cache.get(key);
            if (value != null) {
                // Update access time under a write lock
                lock.readLock().unlock();
                lock.writeLock().lock();
                try {
                    accessOrder.put(key, System.nanoTime());
                    lock.readLock().lock();
                } finally {
                    lock.writeLock().unlock();
                }
            }
            return Optional.ofNullable(value);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * Removes an entry from the cache.
     * @param key the key to remove
     * @return true if the entry was removed, false if it didn't exist
     * @throws NullPointerException if key is null
     */
    public boolean remove(K key) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }
        
        lock.writeLock().lock();
        try {
            accessOrder.remove(key);
            return cache.remove(key) != null;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * Returns the current size of the cache.
     * @return the number of entries in the cache
     */
    public int size() {
        lock.readLock().lock();
        try {
            return cache.size();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * Removes all entries from the cache.
     */
    public void clear() {
        lock.writeLock().lock();
        try {
            cache.clear();
            accessOrder.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }
}
