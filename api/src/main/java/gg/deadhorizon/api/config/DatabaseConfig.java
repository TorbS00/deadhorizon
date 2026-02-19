package gg.deadhorizon.api.config;

public record DatabaseConfig(
        String host,
        int port,
        String database,
        String username,
        String password,
        int maxPoolSize
) {

    public String jdbcUrl() {
        return "jdbc:mysql://" + host + ":" + port + "/" + database;
    }
}