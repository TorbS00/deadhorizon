package gg.deadhorizon.api.config;

import gg.deadhorizon.api.map.MapConfig;

public record GameConfig(
        MapConfig map,
        DatabaseConfig database,
        GeneralConfig general
) {}