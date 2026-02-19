# DeadHorizon

**DEAD HORIZON — The Last WarZ**

A post-apocalyptic zombie survival gamemode for Paper 1.21.4, inspired by McBrawl's McWarZ. Built with clean architecture principles to demonstrate professional Java development beyond the Minecraft ecosystem.

---

## Module Structure

```
deadhorizon/
├── common        ← Pure utilities. No domain knowledge. No dependencies.
├── api           ← Interfaces, records, events. The CONTRACT everything codes against.
├── core          ← Game logic. Where the actual brain lives. Zero Bukkit imports.
├── data          ← SQL, persistence, repositories. Zero Bukkit imports.
├── bootstrap     ← THE ONLY MODULE WITH BUKKIT. Listeners, adapters, plugin entry point.
└── testing       ← Fakes and test utilities shared across modules.
```

### Dependency Flow

```
                 common          ← depends on NOTHING
                   ↑
                  api            ← depends on common
                ↗     ↖
            core       data      ← both depend on api (NOT on each other)
               ↖     ↗
              bootstrap          ← depends on everything, provides Bukkit glue
```

Dependencies only point UPWARD. Never downward. Never sideways between core and data.

---

## Module Details

### common

**Purpose:** Generic utilities reusable in any Java project. Nothing game-specific.

**Contains:**
- `math/` — Vec3, Vec2, BoundingBox (no Bukkit vectors)
- `collection/` — WeightedPool (generic weighted random selection)
- `config/` — ConfigReader abstraction
- `event/` — EventBus interface and SimpleEventBus implementation
- `result/` — Result<T, E> type for error handling without exceptions
- `scheduling/` — TaskScheduler interface (abstracts away Bukkit scheduler)

**Rules:**
- Zero external dependencies (only SLF4J for logging)
- No game logic, no domain concepts
- Everything here could be copy-pasted into a completely different project

**Package:** `gg.deadhorizon.common.*`

---

### api

**Purpose:** Defines the game's domain model and contracts. Interfaces, records, enums, sealed types, and domain events. This is what other modules (and potential third-party addons) code against.

**Contains:**
- `player/` — GamePlayer interface, PlayerStats record, PlayerState enum
- `item/` — GameItem sealed interface, ConsumableItem, WeaponItem, PlaceableItem, ToolItem, AmmoItem records
- `weapon/` — GunDefinition, GunState, FireMode, DamageResult
- `zone/` — Zone, Town, ZoneProvider
- `loot/` — LootTable interface, LootEntry, LootContext
- `zombie/` — ZombieType, ZombieSpawnRule
- `world/` — WorldAdapter interface, BlockPosition record, BreakableBlock enum
- `event/` — DomainEvent marker interface, PlayerKillEvent, GunFiredEvent, etc.
- `registry/` — ItemRegistry, GunRegistry, PlayerRegistry interfaces

**Rules:**
- NO implementations (only interfaces, records, enums, sealed types)
- NO Bukkit imports
- NO logic beyond simple validation in records
- If you're writing an `if` statement, it probably belongs in core

**Package:** `gg.deadhorizon.api.*`

---

### core

**Purpose:** All game logic lives here. Services, handlers, managers, state machines. This is the brain of the plugin — and it has no idea Minecraft exists.

**Contains:**
- `game/` — GameSession (top-level state machine), GameState, GameConfig
- `player/` — PlayerManager, PlayerStatsTracker
- `item/` — ConsumableHandler, PlaceableHandler, ToolHandler
- `combat/` — GunMechanics, DamageCalculator, HitDetection
- `zone/` — ZoneManager, MapProtection
- `loot/` — LootTableImpl, LootTableRegistry, ChestPopulator
- `zombie/` — ZombieSpawner, ZombieAI
- `registry/` — ItemRegistryImpl, GunRegistryImpl

**Rules:**
- NO Bukkit imports. EVER. This is the most important rule.
- Depends on api interfaces, never on bootstrap adapters
- All classes accept dependencies via constructor injection
- Must be fully unit-testable without a server running
- This is where features are implemented as packages (NOT as separate modules)

**Package:** `gg.deadhorizon.core.*`

---

### data

**Purpose:** Everything related to persistence. Database connections, SQL queries, mappers.

**Contains:**
- `repository/` — PlayerRepository, StatsRepository interfaces
- `sql/` — SqlPlayerRepository, SqlStatsRepository, ConnectionProvider, SchemaManager
- `mapper/` — PlayerMapper, StatsMapper (ResultSet → domain objects)

**Rules:**
- NO Bukkit imports
- Depends on api for domain types, never on core
- Uses HikariCP for connection pooling
- All database operations are async-safe

**Package:** `gg.deadhorizon.data.*`

---

### bootstrap

**Purpose:** The Bukkit adapter layer. This is the ONLY module that knows Minecraft exists. It translates between Bukkit's world and your domain.

**Contains:**
- `DeadHorizonPlugin.java` — extends JavaPlugin, the entry point. Wires all services together in onEnable().
- `adapter/` — BukkitGamePlayer, BukkitWorldAdapter, BukkitTaskScheduler, BukkitPlayerRegistry (implements api interfaces using Bukkit)
- `listener/` — Bukkit event listeners that delegate to core services
- `command/` — Command executors
- `item/` — BukkitItemFactory (GameItem → ItemStack with PDC/NBT)
- `config/` — BukkitConfigLoader (reads plugin YAML files)

**Rules:**
- This is the ONLY module that imports org.bukkit
- Listeners are THIN — they translate Bukkit events to domain calls, nothing more
- No game logic here. If you're writing an `if` about game rules, it belongs in core.
- The onEnable() method is the composition root — it wires all dependencies together

**Package:** `gg.deadhorizon.bootstrap.*`

---

### testing

**Purpose:** Shared test utilities, fakes, and test doubles used by other modules' tests.

**Contains:**
- `fake/` — FakeGamePlayer, FakeWorldAdapter, FakeEventBus, FakeTaskScheduler

**Rules:**
- Only provides test infrastructure
- Implements api interfaces with in-memory fakes
- No real logic, no assertions — those go in the actual test classes in each module

**Package:** `gg.deadhorizon.testing.*`

---

## Adding a New Feature

Example: Adding grapple hooks.

1. **api** → Add `GrappleHookItem` to the `GameItem` sealed interface. Add `PlayerGrappleEvent` domain event.
2. **core** → Create `core/item/GrappleHandler.java` with the pull physics, cooldown logic, etc.
3. **bootstrap** → Add a case in `ItemUseListener` for the grapple item. Wire `GrappleHandler` in `DeadHorizonPlugin.onEnable()`.
4. **config** → Add grapple hook definition to items YAML.

No new modules. No restructuring. Just new classes in existing packages.

---

## Build Commands

```bash
# Build everything
./gradlew build

# Build the plugin JAR (lands in bootstrap/build/libs/DeadHorizon.jar)
./gradlew :bootstrap:shadowJar

# Run tests
./gradlew test

# Run only core tests
./gradlew :core:test

# Clean
./gradlew clean
```

---

## Key Architecture Decisions

### Why not one module per feature?
Features (guns, zones, loot, zombies) cross-cut each other. Guns need players, players need zones, loot needs guns AND zones. Splitting by feature creates circular dependencies. Splitting by architectural layer keeps dependencies flowing one direction.

### Why abstract away Bukkit?
- Core game logic is unit-testable without spinning up a server
- Demonstrates understanding of hexagonal architecture (ports & adapters)
- Theoretically portable to Fabric, Sponge, or any other platform
- Shows professional software engineering, not just "Bukkit plugin development"

### Why constructor injection over static singletons?
- Explicit dependencies — you can see what a class needs by reading its constructor
- Testable — pass in fakes during testing
- No hidden global state
- Standard practice in enterprise Java

---

## Tech Stack

- **Java 21** — records, sealed types, pattern matching
- **Paper 1.21.4** — modern Minecraft server API
- **Gradle + Kotlin DSL** — build system with version catalog
- **Shadow plugin** — fat JAR for deployment
- **HikariCP** — database connection pooling
- **SLF4J** — logging facade
- **JUnit 5 + Mockito** — testing
