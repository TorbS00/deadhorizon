package gg.deadhorizon.api.player;

import java.util.UUID;

public interface PlayerDataStore {

    void saveLocation(UUID playerId, double x, double y, double z, float yaw, float pitch);

    PlayerSnapshot loadSnapshot(UUID playerId);

    boolean hasSnapshot(UUID playerId);

}
