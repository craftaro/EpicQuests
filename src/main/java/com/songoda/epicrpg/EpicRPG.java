package com.songoda.epicrpg;

import com.songoda.core.SongodaPlugin;
import com.songoda.core.commands.CommandManager;
import com.songoda.core.configuration.Config;
import com.songoda.core.gui.GuiManager;
import com.songoda.epicrpg.Region.SelectionManager;
import com.songoda.epicrpg.commands.*;
import com.songoda.epicrpg.dialog.DialogManager;
import com.songoda.epicrpg.listeners.BlockListeners;
import com.songoda.epicrpg.listeners.EntityListeners;
import com.songoda.epicrpg.listeners.InteractListeners;
import com.songoda.epicrpg.listeners.ItemListeners;
import com.songoda.epicrpg.settings.Settings;
import com.songoda.epicrpg.storage.json.JsonStorage;
import com.songoda.epicrpg.story.StoryManager;
import com.songoda.epicrpg.story.contender.ContendentManager;
import com.songoda.epicrpg.story.quest.action.ActionManager;
import com.songoda.epicrpg.tasks.QuestTask;
import com.songoda.epicrpg.tasks.RegionTask;
import com.songoda.epicrpg.tasks.VisualizeTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import java.util.List;

public class EpicRPG extends SongodaPlugin {

    private static EpicRPG INSTANCE;

    private final GuiManager guiManager = new GuiManager(this);
    private StoryManager storyManager;
    private ContendentManager contendentManager;
    private ActionManager actionManager;
    private CommandManager commandManager;
    private DialogManager dialogManager;
    private SelectionManager selectionManager;

    private JsonStorage jsonStorage;

    private QuestTask questTask;

    public static EpicRPG getInstance() {
        return INSTANCE;
    }

    @Override
    public void onPluginLoad() {
        INSTANCE = this;
    }

    @Override
    public void onPluginEnable() {

        // Setup Config
        Settings.setupConfig();
        this.setLocale(Settings.LANGUGE_MODE.getString(), false);

        // Init Managers
        storyManager = new StoryManager();
        contendentManager = new ContendentManager(storyManager);
        actionManager = new ActionManager(this);
        dialogManager = new DialogManager();
        selectionManager = new SelectionManager();

        // Init Commands
        this.commandManager = new CommandManager(this);
        this.commandManager.addMainCommand("EpicRPG")
                .addSubCommands(new CommandAdmin(this),
                        new CommandResetPlayer(this),
                        new CommandPartyCreate(this),
                        new CommandPartyDisband(this),
                        new CommandPartyInvite(this, contendentManager),
                        new CommandPartyLeave(this),
                        new CommandResetPlayer(this),
                        new CommandQuestLog(this));

        // Init Listeners
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new InteractListeners(this), this);
        pluginManager.registerEvents(new ItemListeners(this), this);
        pluginManager.registerEvents(new EntityListeners(this), this);
        pluginManager.registerEvents(new BlockListeners(this), this);

        // Enable Auto Saving
        int timeout = 15 * 60 * 20;
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            jsonStorage.saveStories();
            jsonStorage.saveActions();
            jsonStorage.savePlayers();
            jsonStorage.saveDialogs();
        }, timeout, timeout);

        // Load from file
        getDataFolder().mkdir();
        this.jsonStorage = new JsonStorage(this);
        jsonStorage.loadStories();
        jsonStorage.loadActions();
        jsonStorage.loadPlayers();
        jsonStorage.loadDialogs();

        // Start Tasks
        questTask = QuestTask.startTask(this);
        RegionTask.startTask(this);
        VisualizeTask.startTask(this);
    }

    @Override
    public void onPluginDisable() {
        jsonStorage.saveStories();
        jsonStorage.saveActions();
        jsonStorage.savePlayers();
        jsonStorage.saveDialogs();
        questTask.flush();
    }

    @Override
    public void onConfigReload() {
    }

    @Override
    public List<Config> getExtraConfig() {
        return null;
    }

    public SelectionManager getSelectionManager() {
        return selectionManager;
    }

    public StoryManager getStoryManager() {
        return storyManager;
    }

    public ContendentManager getContendentManager() {
        return contendentManager;
    }

    public ActionManager getActionManager() {
        return actionManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public GuiManager getGuiManager() {
        return guiManager;
    }

    public DialogManager getDialogManager() {
        return dialogManager;
    }

    public QuestTask getQuestTask() {
        return questTask;
    }
}
