<div align="center">

# ☠ DEAD HORIZON

### The Last WarZ

**The dead don't rest. Neither do the remaining survivors.**

[![Java](https://img.shields.io/badge/Java_21-ED8B00?style=flat&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Paper](https://img.shields.io/badge/Paper_1.21.4-252525?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAOCAYAAAAfSC3RAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAABfSURBVDhPY2AYBYMBMDIw/P8PYuMCjMgcEI0NMCFzQDQ2wIzMAQBuJTg0oGtEBkzIHBCNDUDdSpANJAKoW4mxkSAAJCcS4UZcNhKvCVkjDDAxoWkkApBhI9amUQAAZscfEVHFsqoAAAAASUVORK5CYII=&logoColor=white)](https://papermc.io/)
[![Gradle](https://img.shields.io/badge/Gradle-02303A?style=flat&logo=gradle&logoColor=white)](https://gradle.org/)
[![License](https://img.shields.io/badge/License-MIT-red?style=flat)](#license)

---

*A post-apocalyptic zombie survival gamemode for Minecraft.*
*Your last chance to finally end and win the WarZ.*

</div>

---

## The World Has Fallen

The infection spread faster than anyone expected. Cities crumbled. Governments collapsed. The military lines broke months ago. What remains are scattered survivors, abandoned outposts, and the endless dead.

**Dead Horizon** is a survival gamemode inspired by the legendary McWarZ from Brawl.com. Scavenge through towns, loot military outposts, arm yourself with guns and gear, and fight to survive against both the undead and the remaining factions who will stop at nothing to take what you have.

This is the last server. The last chance. When the horizon goes dark, there's no coming back.

## Features

**Zone System** — The map is divided into danger zones (1 through 6). The wilderness is zone 1. Towns contain higher zones with better loot, but greater risk. The deeper you go, the more you stand to gain, and lose.

**Guns & Combat** — Military-grade firearms with realistic mechanics. Damage falloff, accuracy, reloading, headshot multipliers. Every bullet counts.

**Loot & Scavenging** — Chests scattered across towns contain food, medical supplies, weapons, ammo, and rare tools. Higher zones yield rarer drops.

**Survival Items** — Barb wire to fortify positions. Wire cutters to breach them. Grapple hooks to reach what others can't. Food and bandages to stay alive one more hour.

**Zombies** — They're not the main threat. The other survivors are. But ignore the dead at your own risk.

**Map Resets** — The world resets on a 4-hour cycle. Your progress, inventory, and location persist. The map returns to its original state. Fresh loot. Fresh start. Same war.

## Architecture

This project is built with professional software engineering practices, not just as a Minecraft plugin, but as a demonstration of clean, testable, enterprise-grade Java.

```
common        Pure utilities. Zero dependencies. Zero domain knowledge.
api           Interfaces, records, events. The contract.
core          All game logic. Fully unit-testable. Zero Bukkit imports.
data          Persistence layer. SQL, repositories, connection pooling.
bootstrap     The only module that touches Bukkit. Thin adapter layer.
```

The core game logic compiles and runs without Minecraft on the classpath. Gun damage calculations, loot table rolls, zone resolution, and game state management are all pure Java, tested with JUnit 5, and completely decoupled from the platform.

For a detailed breakdown of the architecture, module responsibilities, and how to extend the project, see [ARCHITECTURE.md](ARCHITECTURE.md).

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 (records, sealed types, pattern matching) |
| Platform | Paper 1.21.4 |
| Build | Gradle with Kotlin DSL, version catalogs |
| Database | MySQL via HikariCP |
| Testing | JUnit 5, Mockito |
| Packaging | Shadow plugin (fat JAR with relocated dependencies) |

## Building

```bash
# Clone
git clone https://github.com/torbs00/deadhorizon.git
cd deadhorizon

# Build the plugin JAR
./gradlew :bootstrap:shadowJar

# Output: bootstrap/build/libs/DeadHorizon.jar

# Run tests
./gradlew test
```

Requires Java 21+.

## Project Status

> 🔴 **Early Development** — Not yet playable. Core systems under construction.

- [x] Project architecture & multi-module setup
- [x] Map reset system (template world → active world cycling)
- [x] Zone system (towns with danger levels 2-6)
- [x] Configuration system
- [ ] Loot tables & chest population
- [ ] Gun mechanics (firing, reloading, damage)
- [ ] Consumable items (food, medical)
- [ ] Placeable items (barb wire, traps)
- [ ] Tool items (wire cutters, grapple hooks)
- [ ] Zombie spawning & AI
- [ ] Player data persistence (SQL)
- [ ] Commands & admin tools

## License

This project is licensed under the [MIT License](LICENSE).

---

<div align="center">

*The horizon is dead. Are you?*

</div>
