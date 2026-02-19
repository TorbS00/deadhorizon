package gg.deadhorizon.bootstrap.adapter;


import gg.deadhorizon.api.map.WorldAdapter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

public class BukkitWorldAdapter implements WorldAdapter {

    private final JavaPlugin plugin;

    public BukkitWorldAdapter(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean templateExists(String templateWorldName) {
        Path templatePath = plugin.getServer().getWorldContainer().toPath().resolve(templateWorldName);
        return Files.exists(templatePath) && Files.exists(templatePath.resolve("level.dat"));
    }

    @Override
    public void copyTemplate(String templateWorldName, String activeWorldName) throws IOException {
        Path source = plugin.getServer().getWorldContainer().toPath().resolve(templateWorldName);
        Path target = plugin.getServer().getWorldContainer().toPath().resolve(activeWorldName);

        // Delete existing active world if present
        if (Files.exists(target)) {
            deleteDirectory(target);
        }

        copyDirectory(source, target);

        // Remove session.lock so the server can load it
        Path sessionLock = target.resolve("session.lock");
        Files.deleteIfExists(sessionLock);

        // Remove uid.dat so the world gets a fresh UID
        Path uidDat = target.resolve("uid.dat");
        Files.deleteIfExists(uidDat);
    }

    @Override
    public boolean loadWorld(String worldName) {
        WorldCreator creator = new WorldCreator(worldName);
        World world = Bukkit.createWorld(creator);
        return world != null;
    }

    @Override
    public void broadcast(String message) {
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(message));
    }

    @Override
    public void kickAll(String message) {
        Bukkit.getOnlinePlayers().forEach(player -> player.kick(
                net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
                        .legacySection().deserialize(message)
        ));
    }

    @Override
    public void stopServer() {
        Bukkit.shutdown();
    }

    @Override
    public Collection<UUID> getOnlinePlayers() {
        return Bukkit.getOnlinePlayers().stream()
                .map(org.bukkit.entity.Player::getUniqueId)
                .collect(Collectors.toList());
    }

    private void copyDirectory(Path source, Path target) throws IOException {
        Files.walkFileTree(source, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Files.createDirectories(target.resolve(source.relativize(dir)));
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.copy(file, target.resolve(source.relativize(file)),
                        StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private void deleteDirectory(Path path) throws IOException {
        Files.walkFileTree(path, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
