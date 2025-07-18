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
            if (!city.getChunks().contains(event.getBlock().getChunk())) {
                continue;
            }
            boolean canBreak = city.getPlayers().contains(player) && City.playerPermissionsHashMap.get(player).hasBlockBreakPermission;
            boolean isAttacker = false;
            for (War war : city.getWars()) {
                if (war.getAttacker().getPlayers().contains(player) && war.getDefender().equals(city)) {
                    isAttacker = true;
                    break;
                }
            }
            if (!(canBreak || isAttacker)) {
                player.sendMessage("You don't have permission to break blocks in this city!");
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getClickedBlock() == null) return;
        for (City city : City.playerCityHashMap.values()) {
            if (!city.getChunks().contains(event.getClickedBlock().getChunk())) {
                continue;
            }
            boolean canInteract = city.getPlayers().contains(player) && City.playerPermissionsHashMap.get(player).hasInteractPermission;
            boolean isAttacker = false;
            for (War war : city.getWars()) {
                if (war.getAttacker().getPlayers().contains(player) && war.getDefender().equals(city)) {
                    isAttacker = true;
                    break;
                }
            }
            org.bukkit.Material type = event.getClickedBlock().getType();
            boolean isSpecialBlock = type.name().endsWith("_DOOR") || type.name().endsWith("_TRAPDOOR") || type.name().endsWith("_FENCE_GATE") || type.name().endsWith("_CHEST") || type.name().equals("BARREL") || type.name().equals("ENDER_CHEST") || type.name().equals("TRAPPED_CHEST");
            if (!(canInteract || (isAttacker && isSpecialBlock))) {
                player.sendMessage("You don't have permission to interact with blocks in this city!");
                event.setCancelled(true);
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
