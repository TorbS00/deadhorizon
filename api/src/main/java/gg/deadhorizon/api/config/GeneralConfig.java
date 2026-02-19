package gg.deadhorizon.api.config;

public record GeneralConfig(
        String serverName,
        int maxPlayers,
        boolean debugMode
) {}