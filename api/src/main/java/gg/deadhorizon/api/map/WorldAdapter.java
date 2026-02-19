package gg.deadhorizon.api.map;

import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

public interface WorldAdapter {

    boolean templateExists(String templateWorldName);

    void copyTemplate(String templateWorldName, String activeWorldName) throws IOException;

    boolean loadWorld(String worldName);

    void broadcast(String message);

    void kickAll(String message);

    void stopServer();

    Collection<UUID> getOnlinePlayers();
}
