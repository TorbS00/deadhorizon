package gg.deadhorizon.api.zone;

public enum ZoneLevel {

    WILDERNESS(1),
    ZONE_2(2),
    ZONE_3(3),
    ZONE_4(4),
    ZONE_5(5),
    ZONE_6(6);

    private final int level;

    ZoneLevel(int level) {
        this.level = level;
    }

    public int level() {
        return level;
    }

    public static ZoneLevel fromInt(int level) {
        return switch (level) {
            case 1 -> WILDERNESS;
            case 2 -> ZONE_2;
            case 3 -> ZONE_3;
            case 4 -> ZONE_4;
            case 5 -> ZONE_5;
            case 6 -> ZONE_6;
            default -> throw new IllegalArgumentException(
                    "Zone level must be between 1 and 6, got: " + level
            );
        };
    }

    public boolean isHigherThan(ZoneLevel other) {
        return this.level > other.level;
    }
}