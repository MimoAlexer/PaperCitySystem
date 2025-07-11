package com.mimo;

import com.mimo.api.gui.InventoryGuiManager;
import com.mimo.commands.CommandManager;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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
        // Add Sound Manager
        // I actually dont know if this is needed, but I will leave it here for now
        // (I need to read the wiki XD)
        getLogger().info("CitySystem has been enabled!");
        LifecycleEventManager<Plugin> manager = this.getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            new CommandManager(commands);
        });
        new InventoryGuiManager(this);
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        for (City city : City.playerCityHashMap.values()) {
            if (!(city.getChunks().equals(event.getBlock().getChunk()))) {
                return;
            }
            if (!(city.getPlayers().contains(player) && City.playerPermissionsHashMap.get(player).hasBlockBreakPermission)) {
                player.sendMessage("You don't have permission to break blocks in this city!");
                return;
            }
        }
    }

    // TODO: add more events for wars, claims, etc
    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        for (City city : City.playerCityHashMap.values()) {
            if (!(city.getChunks().equals(event.getClickedBlock().getChunk()))) {
                return;
            }
            // TODO: add that when a city is at war with another city the attacker can open dors and chests
            if (!(city.getPlayers().contains(player) && City.playerPermissionsHashMap.get(player).hasInteractPermission)) {
                player.sendMessage("You don't have permission to interact with blocks in this city!");
                return;
            }
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("CitySystem has been disabled!");
    }
}
