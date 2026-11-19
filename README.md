# Foodmenu Cache API

A food menu REST API built to learn **application-level caching** with Spring Boot and Caffeine. The domain (cities, cuisines, restaurants, menu items, offers) is intentionally small, so the focus stays on *how* and *why* each cache is configured the way it is.

## Tech Stack

- Java 21, Spring Boot 4.1
- Spring Data JPA (Hibernate) + PostgreSQL
- Spring Cache abstraction + Caffeine
- Spring Boot Actuator (cache metrics)
- springdoc OpenAPI (Swagger UI)
- Lombok

## Domain Model

| Entity | Relationships |
|---|---|
| City | one city has many restaurants |
| Cuisine | many-to-many with Restaurant |
| Restaurant | many-to-one City, many-to-many Cuisine |
| MenuItem | many-to-one Restaurant (enum category, `BigDecimal` price) |
| Offer | many-to-one Restaurant (time-bound, `validFrom`/`validTill`) |

## Caching Design

Each cache has its own TTL and size limit (`CacheConfig`, using `SimpleCacheManager` so unknown cache names fail fast instead of silently creating unbounded caches).

| Cache | TTL | Max entries | Why |
|---|---|---|---|
| `cities` | 24 hours | 100 | almost never changes |
| `cuisines` | 24 hours | 100 | almost never changes |
| `restaurants` | 30 minutes | 1,000 | read-heavy, rating/address change occasionally |
| `menuItems` | 5 minutes | 5,000 | stale prices are costly; many filter/page key combinations |
| `activeOffers` | 1 minute | 1,000 | offers expire at exact times, so a short TTL limits staleness |

### What this project demonstrates

- `@Cacheable`, `@CachePut`, `@CacheEvict` and `@Caching` on service methods
- Caching DTOs (immutable records), never JPA entities
- Composite cache keys for filtered and paginated search, with key prefixes to avoid collisions
- Cascade eviction: City/Cuisine update clears `restaurants`; restaurant delete clears `menuItems` and `activeOffers`
- Why cache hits skip the method body (checks inside a cached method do not run)
- `expireAfterWrite` vs `expireAfterAccess`, and `maximumSize` eviction
- Hit/miss/eviction stats through Actuator metrics
- Cache stampede protection with `sync = true`
- What not to cache (admin-only, rarely read endpoints)

## API Overview

| Resource | Base path |
|---|---|
| Cities | `/api/cities` |
| Cuisines | `/api/cuisines` |
| Restaurants | `/api/restaurants` |
| Menu items | `/api/restaurants/{restaurantId}/menu-items` (supports `category`, `vegetarian`, `maxPrice`, pagination) |
| Offers | `/api/restaurants/{restaurantId}/offers`, `/offers/active` |
| City-wide active offers | `/api/offers/active?cityId=` |

Swagger UI: `http://localhost:8080/swagger-ui.html`

## Run Locally

1. Install PostgreSQL and create a database:
   ```sql
   CREATE DATABASE foodmenu_db;
   ```
2. Set your credentials in `src/main/resources/application.properties` (`spring.datasource.username` / `spring.datasource.password`).
3. Start the app:
   ```bash
   ./mvnw spring-boot:run
   ```

## Inspecting the Caches

Metrics are exposed through Actuator:

```
/actuator/caches
/actuator/metrics/cache.gets?tag=cache:menuItems&tag=result:hit
/actuator/metrics/cache.gets?tag=cache:menuItems&tag=result:miss
/actuator/metrics/cache.size?tag=cache:menuItems
/actuator/metrics/cache.evictions?tag=cache:menuItems
```

Hit ratio = hits / (hits + misses). Tip: with `spring.jpa.show-sql=true`, a cache hit is simply a request that produces no SQL in the console.

## Known Limitations

- Caffeine is **in-memory and per instance**. With several app instances, each keeps its own cache and evictions on one instance are not seen by the others. A distributed cache (for example Redis) solves this.
- List endpoints still have N+1 queries on a cache miss; caching hides this, it does not fix it.
- `activeOffers` can show an expired offer for up to its TTL; per-entry expiry would remove that window.
- `allEntries` eviction on menu and offer writes is simple but clears unrelated entries.