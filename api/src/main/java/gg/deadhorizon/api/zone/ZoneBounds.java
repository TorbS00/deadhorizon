package gg.deadhorizon.api.zone;

public record ZoneBounds(
        int minX,
        int minZ,
        int maxX,
        int maxZ
) {

    public ZoneBounds {
        if (minX > maxX) {
            int temp = minX;
            minX = maxX;
            maxX = temp;
        }
        if (minZ > maxZ) {
            int temp = minZ;
            minZ = maxZ;
            maxZ = temp;
        }
    }

    public boolean contains(int x, int z) {
        return x >= minX && x <= maxX && z >= minZ && z <= maxZ;
    }
}