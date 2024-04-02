package com.songoda.epicquests.Region;

public class ActiveView {
    private final Region region;
    private final long start;

    public ActiveView(Region region) {
        this.region = region;
        this.start = System.currentTimeMillis();
    }

    public Region getRegion() {
        return this.region;
    }

    public long getStart() {
        return this.start;
    }
}
