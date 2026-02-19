package gg.deadhorizon.api.player;

public record PlayerSnapshot(
        double x,
        double y,
        double z,
        float yaw,
        float pitch
) {}
