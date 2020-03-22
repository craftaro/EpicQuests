package com.songoda.epicrpg.tasks;

import com.songoda.core.utils.TextUtils;
import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.story.player.StoryPlayer;
import com.songoda.epicrpg.story.quest.ActiveQuest;
import com.songoda.epicrpg.story.quest.Objective;
import com.songoda.epicrpg.story.quest.Quest;
import com.songoda.epicrpg.story.quest.RemainingObjective;
import com.songoda.epicrpg.story.quest.action.ActiveAction;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

public class QuestTask extends BukkitRunnable {

    private static QuestTask instance;
    private static EpicRPG plugin;

    private Map<UUID, BossBar> activeBossBars = new HashMap<>();


    public QuestTask(EpicRPG plug) {
        plugin = plug;
    }

    public static QuestTask startTask(EpicRPG plug) {
        plugin = plug;
        if (instance == null) {
            instance = new QuestTask(plugin);
            instance.runTaskTimerAsynchronously(plugin, 0, 5);
        }

        return instance;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            plugin.getPlayerManager().discoverQuests(player);
            StoryPlayer storyPlayer = plugin.getPlayerManager().getPlayer(player);
            boolean updatedBossBar = false;
            for (ActiveQuest activeQuest : new HashSet<>(storyPlayer.getActiveQuests()).stream()
                    .sorted(Comparator.comparing(q -> !q.isFocused()))
                    .collect(Collectors.toCollection(LinkedHashSet::new))) {
                if (activeQuest == null) continue;
                Quest quest = plugin.getStoryManager().getEnabledQuest(activeQuest.getActiveQuest());

                if (quest == null || quest.getObjectives() == null || quest.getObjectives().size() == 0) continue;

                if (activeQuest.getRemainingObjectives().isEmpty()) {
                    player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1L, 1L);
                    storyPlayer.completeQuest(quest);
                    quest.giveRewards(player);
                    continue;
                }
                if (updatedBossBar) continue;
                updatedBossBar = true;

                Map.Entry<UUID, RemainingObjective> entry = activeQuest.getRemainingObjectives().entrySet().iterator().next();
                RemainingObjective remainingObjective = entry.getValue();
                Objective currentObjective = quest.getObjective(remainingObjective.getUniqueId());

                List<ActiveAction> activeActions = plugin.getActionManager().getActiveActionsByObjective(currentObjective);

                double current = activeActions.get(0).getAmount() == 1 ? activeActions.size() -
                        remainingObjective.getRemainingActions().size() : activeActions.get(0).getAmount() -
                        remainingObjective.getAmount(activeActions.get(0));
                int goal = activeActions.get(0).getAmount() == 1 ? activeActions.size() : activeActions.get(0).getAmount();

                String title = TextUtils.formatText("Objective: &d"
                        + currentObjective.getTitle());
                BossBar bossBar = activeBossBars.computeIfAbsent(player.getUniqueId(),
                        b -> Bukkit.createBossBar(title,
                                BarColor.PINK,
                                BarStyle.SOLID));
                bossBar.addPlayer(player);
                if (goal == 1 || goal == 0) {
                    bossBar.setTitle(title);
                    bossBar.setProgress(1);
                } else {
                    bossBar.setTitle(TextUtils.formatText(title + " &7(&d" + (int) current + "&7/&a" + goal + "&7)"));
                    bossBar.setProgress(current / goal);
                }
            }

            if (!updatedBossBar && activeBossBars.containsKey(player.getUniqueId())) {
                BossBar bossBar = activeBossBars.remove(player.getUniqueId());
                bossBar.removeAll();
            }
        }
    }

    public void flush() {
        for (BossBar bossBar : activeBossBars.values())
            bossBar.removeAll();
    }
}
