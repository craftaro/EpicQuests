package com.craftaro.epicrpg.tasks;

import com.craftaro.epicrpg.EpicRPG;
import com.craftaro.epicrpg.Region.ActiveView;
import com.craftaro.epicrpg.Region.Region;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class VisualizeTask extends BukkitRunnable {
    private static VisualizeTask instance;
    private static EpicRPG plugin;
    private static final Random random = new Random();
    int radius;

    public VisualizeTask(EpicRPG plug) {
        plugin = plug;
        this.radius = Bukkit.getServer().getViewDistance();
    }

    public static VisualizeTask startTask(EpicRPG plug) {
        plugin = plug;
        if (instance == null) {
            instance = new VisualizeTask(plugin);
            instance.runTaskTimerAsynchronously(plugin, 60, 10);
        }

        return instance;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            ActiveView view = plugin.getSelectionManager().getActiveView(player);
            if (view != null) {
                particleTick(player, view.getRegion());
            }
        }
    }

    void particleTick(Player player, Region region) {
        final Location playerLocation = player.getLocation();
        final World world = playerLocation.getWorld();
        // start and stop chunk coordinates
        int startY = playerLocation.getBlockY() + 1;
        int cxi = (playerLocation.getBlockX() >> 4) - this.radius, cxn = cxi + this.radius * 2;
        int czi = (playerLocation.getBlockZ() >> 4) - this.radius, czn = czi + this.radius * 2;
        // loop through the chunks to find applicable ones
        for (int cx = cxi; cx < cxn; ++cx) {
            for (int cz = czi; cz < czn; ++cz) {
                // sanity check
                if (!world.isChunkLoaded(cx, cz)) {
                    continue;
                }


                if (region.getPos1().getBlockX() >> 4 == cx
                        && region.getPos1().getBlockZ() >> 4 == cz ||
                        region.getPos2().getBlockX() >> 4 == cx
                                && region.getPos2().getBlockZ() >> 4 == cz) {
                    showRegionParticles(player, region);
                }
            }
        }
    }

    void showRegionParticles(Player player, Region region) {
        Location point1 = region.getPos1();
        Location point2 = region.getPos2();
        Vector max = Vector.getMaximum(point1.toVector(), point2.toVector());
        Vector min = Vector.getMinimum(point1.toVector(), point2.toVector());
        for (int i = min.getBlockX(); i <= max.getBlockX(); ++i) {
            for (int j = min.getBlockY(); j <= max.getBlockY(); ++j) {
                for (int k = min.getBlockZ(); k <= max.getBlockZ(); ++k) {
                    // show about 1/5 of the blocks per tick
                    boolean show = random.nextFloat() < .2;
                    if (!show) {
                        continue;
                    }

                    Block block = region.getPos1().getWorld().getBlockAt(i, j, k);

                    final Location loc = block.getLocation().add(.5, 1.5, .5);
                    player.spawnParticle(Particle.VILLAGER_HAPPY, loc, 0, 0, 0, 0, 1);
                }
            }
        }
    }
}
