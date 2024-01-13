package com.craftaro.epicrpg;

import com.craftaro.core.SongodaCore;
import com.craftaro.core.SongodaPlugin;
import com.craftaro.core.commands.CommandManager;
import com.craftaro.core.configuration.Config;
import com.craftaro.core.gui.GuiManager;
import com.craftaro.epicrpg.Region.SelectionManager;
import com.craftaro.epicrpg.commands.CommandAdmin;
import com.craftaro.epicrpg.commands.CommandPartyCreate;
import com.craftaro.epicrpg.commands.CommandPartyDisband;
import com.craftaro.epicrpg.commands.CommandPartyInvite;
import com.craftaro.epicrpg.commands.CommandPartyKick;
import com.craftaro.epicrpg.commands.CommandPartyLeave;
import com.craftaro.epicrpg.commands.CommandQuestLog;
import com.craftaro.epicrpg.commands.CommandResetPlayer;
import com.craftaro.epicrpg.dialog.DialogManager;
import com.craftaro.epicrpg.listeners.BlockListeners;
import com.craftaro.epicrpg.listeners.EntityListeners;
import com.craftaro.epicrpg.listeners.InteractListeners;
import com.craftaro.epicrpg.listeners.ItemListeners;
import com.craftaro.epicrpg.settings.Settings;
import com.craftaro.epicrpg.storage.json.JsonStorage;
import com.craftaro.epicrpg.story.StoryManager;
import com.craftaro.epicrpg.story.contender.ContendentManager;
import com.craftaro.epicrpg.story.quest.action.ActionManager;
import com.craftaro.epicrpg.tasks.QuestTask;
import com.craftaro.epicrpg.tasks.RegionTask;
import com.craftaro.epicrpg.tasks.VisualizeTask;
import com.craftaro.third_party.com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import java.util.List;

public class EpicRPG extends SongodaPlugin {
    private final GuiManager guiManager = new GuiManager(this);
    private StoryManager storyManager;
    private ContendentManager contendentManager;
    private ActionManager actionManager;
    private CommandManager commandManager;
    private DialogManager dialogManager;
    private SelectionManager selectionManager;

    private JsonStorage jsonStorage;

    private QuestTask questTask;

    /**
     * @deprecated Use {@link #getPlugin(Class)} instead
     */
    @Deprecated
    public static EpicRPG getInstance() {
        return getPlugin(EpicRPG.class);
    }

    @Override
    public void onPluginLoad() {
    }

    @Override
    public void onPluginEnable() {
        // Run Songoda Updater
        SongodaCore.registerPlugin(this, 364, XMaterial.COMPASS);

        // Setup Config
        Settings.setupConfig();
        this.setLocale(Settings.LANGUGE_MODE.getString(), false);

        // Init Managers
        this.storyManager = new StoryManager();
        this.contendentManager = new ContendentManager(this.storyManager);
        this.actionManager = new ActionManager(this);
        this.dialogManager = new DialogManager();
        this.selectionManager = new SelectionManager();

        // Init Commands
        this.commandManager = new CommandManager(this);
        this.commandManager.addMainCommand("EpicRPG")
                .addSubCommands(new CommandAdmin(this),
                        new CommandResetPlayer(this),
                        new CommandPartyCreate(this),
                        new CommandPartyDisband(this),
                        new CommandPartyInvite(this, this.contendentManager),
                        new CommandPartyKick(this),
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
            this.jsonStorage.saveStories();
            this.jsonStorage.saveActions();
            this.jsonStorage.savePlayers();
            this.jsonStorage.saveDialogs();
        }, timeout, timeout);

        // Start Tasks
        this.questTask = QuestTask.startTask(this);
        RegionTask.startTask(this);
        VisualizeTask.startTask(this);
    }

    @Override
    public void onPluginDisable() {
        this.jsonStorage.saveStories();
        this.jsonStorage.saveActions();
        this.jsonStorage.savePlayers();
        this.jsonStorage.saveDialogs();
        this.questTask.flush();
    }

    @Override
    public void onDataLoad() {
        // Load from file
        getDataFolder().mkdir();
        this.jsonStorage = new JsonStorage(this);
        this.jsonStorage.loadStories();
        this.jsonStorage.loadActions();
        this.jsonStorage.loadPlayers();
        this.jsonStorage.loadDialogs();

    }

    @Override
    public void onConfigReload() {
    }

    @Override
    public List<Config> getExtraConfig() {
        return null;
    }

    public SelectionManager getSelectionManager() {
        return this.selectionManager;
    }

    public StoryManager getStoryManager() {
        return this.storyManager;
    }

    public ContendentManager getContendentManager() {
        return this.contendentManager;
    }

    public ActionManager getActionManager() {
        return this.actionManager;
    }

    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    public GuiManager getGuiManager() {
        return this.guiManager;
    }

    public DialogManager getDialogManager() {
        return this.dialogManager;
    }

    public QuestTask getQuestTask() {
        return this.questTask;
    }
}
