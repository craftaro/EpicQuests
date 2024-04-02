package com.songoda.epicquests.Region;

import org.bukkit.Location;

public class Region {
    private Location pos1;
    private Location pos2;

    public Region(Location pos1, Location pos2) {
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    public Location getPos1() {
        return this.pos1;
    }

    public void setPos1(Location pos1) {
        this.pos1 = pos1;
    }

    public Location getPos2() {
        return this.pos2;
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2;
    }

    public String serialize() {
        return this.pos1.getWorld().getName() + ";" + this.pos1.getBlockX() + ";" + this.pos1.getBlockY() + ";" + this.pos1.getBlockZ() + ";" + this.pos2.getBlockX() + ";" + this.pos2.getBlockY() + ";" + this.pos2.getBlockZ();
    }

    public static Region deserialize(String serialized) {
        String[] parts = serialized.split(";");
        return new Region(new Location(null, Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3])), new Location(null, Integer.parseInt(parts[4]), Integer.parseInt(parts[5]), Integer.parseInt(parts[6])));
    }
}
