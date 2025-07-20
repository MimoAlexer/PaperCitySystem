package com.mimo.wargui;

import com.mimo.City;
import com.mimo.War;
import com.mimo.api.gui.AbstractInventoryGui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class EnemyGui extends AbstractInventoryGui {
    public EnemyGui(Player player, Component title) {
        super(player, title);
    }

    @Override
    protected ItemStack[] items() {
        List<ItemStack> results = new ArrayList<>();
        City city = City.getCityByPlayer(player);
        if (city == null || city.getWars().isEmpty()) return results.toArray(new ItemStack[0]);
        War war = city.getWars().get(0);
        for (City enemy : war.getDefenderAllies()) {
            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta meta = item.getItemMeta();
            meta.displayName(Component.text("Enemy: " + enemy.getName(), NamedTextColor.RED));
            meta.lore(List.of(Component.text("Click for info", NamedTextColor.GRAY)));
            item.setItemMeta(meta);
            results.add(item);
        }
        return results.toArray(new ItemStack[0]);
    }

    @Override
    protected void clickCallback(InventoryClickEvent event) {
        event.setCancelled(true);
        // Could show more info about the enemy city here
    }

    @Override
    protected void inventoryCloseCallback(InventoryCloseEvent event) {

    }
}
