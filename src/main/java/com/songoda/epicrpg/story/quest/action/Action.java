package com.songoda.epicrpg.story.quest.action;

import com.songoda.epicrpg.data.ActionDataStore;
import com.songoda.epicrpg.story.quest.Objective;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;

public interface Action {

    String getType();

    List<String> getDescription(ActionDataStore actionDataStore);

    void onInteract(PlayerInteractEvent event, ActiveAction activeAction);

    boolean onInteractWithEntity(PlayerInteractAtEntityEvent event, ActiveAction activeAction);

    void onPickup(EntityPickupItemEvent event, ActiveAction activeAction);

    void onDrop(PlayerDropItemEvent event, ActiveAction activeAction);

    void onEntityKill(EntityDeathEvent event, ActiveAction activeAction);

    ActiveAction setup(Player player, Objective objective);
}
