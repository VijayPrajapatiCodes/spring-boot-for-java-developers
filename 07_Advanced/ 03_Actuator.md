# Spring Boot Caching

## 📌 What is Caching?

Caching is a technique used to temporarily store frequently accessed data in memory so that future requests can be served much faster.

Instead of fetching the same data repeatedly from the database or an external service, the application returns the cached data.

> **Caching improves application performance by reducing response time and database load.**

---

# Why Do We Need Caching?

Without caching:

Client
↓
Application
↓
Database
↓
Response

Every request hits the database.

Problems:

- Slow response time
- High database load
- Increased CPU usage
- Poor scalability

With caching:

Client
↓
Application
↓
Cache (Memory)
↓
Database (Only if cache miss)

Benefits:

- Faster response
- Reduced database queries
- Better performance
- Improved scalability

---

# How Caching Works

### First Request

Client

↓

Application

↓

Database

↓

Store Data in Cache

↓

Response

---

### Second Request

Client

↓

Application

↓

Cache

↓

Response

No database call is made.

---

# Types of Cache

## 1. In-Memory Cache

Stores data in application memory.

Examples:

- ConcurrentHashMap
- Caffeine
- Ehcache

Suitable for:

- Single application

---

## 2. Distributed Cache

Shared cache across multiple servers.

Examples:

- Redis
- Hazelcast
- Memcached

Suitable for:

- Microservices
- Cloud applications

---

# Spring Boot Cache

Spring Boot provides built-in caching support.

Enable caching using:

```java
@EnableCaching
@SpringBootApplication
public class SpringBootApplication {
}
```

---

# Cache Annotations

Spring Boot provides several cache annotations.

## @Cacheable

Stores method results in cache.

```java
@Cacheable("products")
public Product getProduct(Long id){
    return repository.findById(id).orElseThrow();
}
```

First call:

Database

Second call:

Cache

---

## @CachePut

Updates both database and cache.

```java
@CachePut(value = "products", key = "#product.id")
public Product update(Product product){
    return repository.save(product);
}
```

---

## @CacheEvict

Removes cache data.

```java
@CacheEvict(value = "products", key = "#id")
public void delete(Long id){
    repository.deleteById(id);
}
```

---

## @Caching

Used when multiple cache operations are required together.

Example:

```java
@Caching(
    put = {
        @CachePut(value="products", key="#product.id")
    },
    evict = {
        @CacheEvict(value="allProducts", allEntries=true)
    }
)
```

---

# Cache Manager

Spring Boot uses CacheManager to manage caches.

Example:

```java
@Bean
public CacheManager cacheManager(){
    return new ConcurrentMapCacheManager("products");
}
```

---

# Popular Cache Providers

| Cache | Best For |
|--------|----------|
| ConcurrentMap | Learning |
| Caffeine | High Performance |
| Ehcache | Enterprise |
| Redis | Distributed Cache |
| Hazelcast | Cluster Applications |

---

# Cache Flow

Client

↓

Spring Boot

↓

Cache

↓

Database

↓

Cache Updated

↓

Client

---

# Advantages

- Faster Response Time
- Better Performance
- Lower Database Load
- Reduced Network Calls
- Better User Experience
- Improved Scalability

---

# Disadvantages

- Cache Synchronization
- Memory Consumption
- Stale Data
- Additional Configuration

---

# Cache Eviction

Cached data should be removed when:

- Data is updated
- Data is deleted
- Cache expires
- Memory is full

---

# Real World Examples

## E-Commerce

Product Details

Frequently accessed product data is cached.

---

## Banking

Exchange Rates

Rates are cached for a short duration.

---

## Weather Application

Weather API responses are cached.

---

## Social Media

User Profile

Frequently viewed profiles are cached.

---

# Interview Questions

### What is caching?

Caching is the process of storing frequently accessed data in memory to improve application performance.

---

### Why is caching used?

To reduce database calls and improve response time.

---

### What is @Cacheable?

Stores method results in cache.

---

### What is @CachePut?

Updates cache whenever data changes.

---

### What is @CacheEvict?

Removes data from cache.

---

### Which cache is commonly used in Spring Boot production applications?

Redis is one of the most commonly used distributed caches.

---

### Difference between Cache and Database

| Cache | Database |
|--------|----------|
| Temporary Storage | Permanent Storage |
| Very Fast | Slower than Cache |
| Memory Based | Disk Based |
| Used for Performance | Used for Persistence |

---

# Best Practices

- Cache only frequently accessed data.
- Do not cache sensitive information.
- Configure cache expiration.
- Use Redis for distributed systems.
- Remove stale cache regularly.
- Monitor cache hit and miss ratios.

---

# Summary

Spring Boot Caching improves application performance by storing frequently used data in memory. It reduces database load, decreases response time, and provides a better user experience. Spring Boot supports caching through annotations like **@Cacheable**, **@CachePut**, and **@CacheEvict**, while providers such as Redis and Caffeine are commonly used in production environments.