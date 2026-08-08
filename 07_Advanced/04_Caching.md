# Spring Boot Caching

## What is Cache?

Cache is a temporary storage that stores frequently accessed data in memory so that repeated database calls can be avoided.

It helps improve application performance by returning data much faster than fetching it from the database every time.

---

# Why Do We Need Cache?

Without Cache

Client
↓
Spring Boot
↓
Database
↓
Response

Every request hits the database.

Problems:

- High Database Load
- Slow Response Time
- Increased Server Load
- Poor Performance

---

With Cache

Client
↓
Spring Boot
↓
Cache

If Cache Hit
↓
Response

If Cache Miss
↓
Database
↓
Store into Cache
↓
Response

Benefits:

- Faster Response
- Less Database Load
- Better Performance
- Reduced Server Cost

---

# Cache vs Database

| Cache | Database |
|--------|----------|
| Temporary Storage | Permanent Storage |
| Very Fast | Comparatively Slow |
| Stored in Memory (RAM) | Stored on Disk |
| Used for Performance | Used for Data Persistence |

---

# Real Life Examples

Examples of data that rarely changes:

- Country List
- State List
- Railway Stations
- Airport Codes
- Product Categories
- Currency List

These are ideal candidates for caching.

---

# Cache Hit

Data already exists in cache.

Flow:

Client
↓
Cache
↓
Response

Database is NOT called.

---

# Cache Miss

Data is not available in cache.

Flow:

Client
↓
Cache
↓
Database
↓
Store into Cache
↓
Response

---

# Spring Boot Cache

Spring Boot provides built-in caching support using annotations.

Enable caching:

```java
@EnableCaching
@SpringBootApplication
public class SpringbootLearningApplication {

}
```

---

# @Cacheable

Stores method result in cache.

Example

```java
@Cacheable(value = "products", key = "#id")
public Product getProduct(Long id){

    System.out.println("Fetching From Database...");

    return repository.findById(id).orElseThrow();
}
```

First Request

Database Called

Second Request

Returned from Cache

---

# @CachePut

Updates both Database and Cache.

```java
@CachePut(value = "products", key = "#product.id")
public Product updateProduct(Product product){

    return repository.save(product);
}
```

Used when data is updated.

---

# @CacheEvict

Removes data from cache.

```java
@CacheEvict(value = "products", key = "#id")
public void deleteProduct(Long id){

    repository.deleteById(id);
}
```

---

# Remove Entire Cache

```java
@CacheEvict(value = "products", allEntries = true)
```

Deletes every cached product.

---

# @Caching

Allows multiple cache operations in one method.

```java
@Caching(
    put = {
        @CachePut(value = "products", key = "#product.id")
    },
    evict = {
        @CacheEvict(value = "categories", allEntries = true)
    }
)
```

---

# Cache Key

Default

```java
@Cacheable("products")
```

Custom

```java
@Cacheable(value = "products", key = "#id")
```

or

```java
@Cacheable(value = "products", key = "#product.id")
```

---

# Default Cache Manager

Spring Boot uses

ConcurrentMapCacheManager

Data is stored inside JVM Memory.

---

# Limitation of Spring Default Cache

- Cache is stored in server RAM.
- Cache is lost after application restart.
- Cannot be shared between multiple servers.
- Suitable for learning and small projects.

---

# Redis Cache

Redis is a production-ready distributed cache.

Advantages:

- Extremely Fast
- Distributed Cache
- Shared Across Multiple Servers
- Supports TTL
- Production Ready

---

# Spring Cache vs Redis

| Spring Cache | Redis |
|--------------|--------|
| JVM Memory | Redis Server |
| Lost on Restart | Persistent Until Expiry |
| Single Server | Multiple Servers |
| Learning | Production |

---

# Cache Flow

Read Request

Client
↓
Cache
↓
Hit → Response

Miss
↓
Database
↓
Store into Cache
↓
Response

---

Update Request

Client
↓
Database
↓
Cache Update

---

Delete Request

Client
↓
Database
↓
Cache Delete

---

# Best Use Cases

- Product Details
- Product Categories
- Country List
- State List
- User Profile
- Configuration Data
- Frequently Viewed Products
- Dashboard Statistics

---

# Avoid Caching

Avoid caching data that changes very frequently, such as:

- Bank Balance
- OTP
- Payment Status
- Live Stock Prices
- Real-time Auction Data

If such data is cached, stale data may be returned unless the cache is updated or evicted properly.

---

# Advantages

- Improves Performance
- Reduces Database Load
- Faster Response Time
- Better User Experience
- Reduces Server Cost
- Easy Integration

---

# Disadvantages

- Consumes Memory
- Cache Invalidation Complexity
- Stale Data Risk
- Additional Configuration for Production

---

# Interview Questions

### What is Cache?

Temporary storage that keeps frequently accessed data in memory for faster retrieval.

---

### What is Cache Hit?

Requested data is found in cache, so the database is not accessed.

---

### What is Cache Miss?

Requested data is not found in cache, so the database is queried and the result is stored in cache.

---

### Difference between Cache and Database?

Cache is temporary and fast, whereas the database is permanent and comparatively slower.

---

### What does @Cacheable do?

Stores the result of a method in cache and returns cached data on subsequent requests.

---

### What does @CachePut do?

Updates both the database and the cache.

---

### What does @CacheEvict do?

Removes specific or all entries from the cache.

---

### Where is Spring Boot default cache stored?

In JVM Memory using ConcurrentMapCacheManager.

---

### Which cache is used in production?

Redis.

---

### Why is Redis preferred?

Because it is distributed, very fast, supports TTL, and can be shared across multiple application instances.

---

# Summary

- Cache stores frequently accessed data in memory.
- Reduces database calls.
- Improves application performance.
- @EnableCaching enables caching.
- @Cacheable reads from cache.
- @CachePut updates cache.
- @CacheEvict removes cache.
- Spring Default Cache is suitable for learning.
- Redis is preferred for production environments.