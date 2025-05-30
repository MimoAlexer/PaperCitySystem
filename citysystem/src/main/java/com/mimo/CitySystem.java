package com.mimo;

import com.mimo.commands.CommandManager;
import com.mimo.shared.gui.InventoryGuiManager;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class CitySystem extends JavaPlugin implements Listener {
    public static CitySystem INSTANCE = null;

    public CitySystem() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("CitySystem has been enabled!");
        LifecycleEventManager<Plugin> manager = this.getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            new CommandManager(commands);
        });
        new InventoryGuiManager(this);
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("CitySystem has been disabled!");
    }
}
