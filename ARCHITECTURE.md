# Architecture

This document describes the internal architecture of DeadHorizon for contributors and developers.

## Module Structure

```
deadhorizon/
├── common        ← Pure utilities. No domain knowledge. No external dependencies.
├── api           ← Interfaces, records, events. The contract everything codes against.
├── core          ← Game logic. The brain of the plugin. Zero Bukkit imports.
├── data          ← SQL, persistence, repositories. Zero Bukkit imports.
├── bootstrap     ← THE ONLY MODULE WITH BUKKIT. Listeners, adapters, plugin entry point.
└── testing       ← Fakes and test utilities shared across module tests.
```

## Dependency Flow

```
                 common          depends on NOTHING
                   ↑
                  api            depends on common
                ↗     ↖
            core       data      both depend on api (NOT on each other)
               ↖     ↗
              bootstrap          depends on everything, provides Bukkit glue
```

Dependencies only flow upward. Never downward. Never sideways between core and data.

## Module Responsibilities

### common

Generic utilities reusable in any Java project.

Contains math types (Vec3, BoundingBox), collection utilities (WeightedPool), the EventBus interface, TaskScheduler abstraction, and Result types.

Rules:
- Zero game-specific logic
- Only external dependency is SLF4J for logging
- Everything here could be extracted into a standalone library

### api

Defines the game's domain model and contracts through interfaces, records, enums, and sealed types.

Contains GamePlayer, GameItem (sealed hierarchy), Zone, GunDefinition, LootTable, WorldAdapter, and all domain events.

Rules:
- No implementations, only contracts
- No Bukkit imports
- No logic beyond simple record validation

### core

All game logic. Services, handlers, managers, state machines.

Contains ZoneManager, GunMechanics, DamageCalculator, ConsumableHandler, LootTableImpl, MapManagerImpl, and all feature implementations.

Rules:
- Zero Bukkit imports
- All dependencies received via constructor injection
- Must be fully unit-testable without a running server
- Features are organized as packages, not modules

### data

Everything related to persistence.

Contains SQL repositories, HikariCP connection pooling, schema migration, and ResultSet-to-domain mappers.

Rules:
- Zero Bukkit imports
- Depends on api for domain types, never on core
- All database operations are async-safe

### bootstrap

The Bukkit adapter layer and composition root.

Contains DeadHorizonPlugin (entry point), Bukkit adapters (BukkitGamePlayer, BukkitWorldAdapter), event listeners, command executors, config loaders, and item factories.

Rules:
- Only module that imports org.bukkit
- Listeners are thin, they translate Bukkit events into domain calls
- No game logic, if you're writing game rules, it belongs in core
- The onEnable() method wires all dependencies together

### testing

Shared test infrastructure.

Contains fake implementations of api interfaces (FakeGamePlayer, FakeWorldAdapter, FakeEventBus) used by test classes across modules.

Rules:
- Only provides test doubles
- No assertions, those go in each module's own test classes

## Design Principles

**Hexagonal Architecture** — Core game logic defines ports (interfaces). The bootstrap module provides adapters (Bukkit implementations). The domain never knows which platform it runs on.

**Constructor Injection** — No static singletons. Every service declares its dependencies explicitly in its constructor. This makes dependencies visible and testing trivial.

**Immutability by Default** — Records for data, List.of() for collections, unmodifiable maps. Mutable state is explicit and contained.

**Domain Events** — Internal EventBus decouples features. A PlayerKillEvent can trigger stat tracking, bounty systems, and kill feeds without those systems knowing about each other.

**Data-Driven Configuration** — Zones, loot tables, gun stats, and item definitions are all loaded from YAML config files. No hardcoded game values.

## Adding a New Feature

1. **api** — Define the domain model (records, interfaces, events)
2. **core** — Implement the logic (handlers, services)
3. **bootstrap** — Wire Bukkit glue (listener cases, onEnable wiring)
4. **config** — Add YAML definitions if needed

No new modules. No restructuring. New classes in existing packages.

## Build

```bash
./gradlew build                    # Build everything
./gradlew :bootstrap:shadowJar     # Plugin JAR → bootstrap/build/libs/DeadHorizon.jar
./gradlew test                     # All tests
./gradlew :core:test               # Core tests only
./gradlew clean                    # Clean all build directories
```
