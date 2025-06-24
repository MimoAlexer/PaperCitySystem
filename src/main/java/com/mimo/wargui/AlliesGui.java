package com.mimo.wargui;

import com.mimo.City;
import com.mimo.War;
import com.mimo.api.gui.AbstractInventoryGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class AlliesGui extends AbstractInventoryGui {
    // TODO: WIP
    public AlliesGui(Player player, Component title) {
        super(player, title);
    }

    @Override
    protected ItemStack[] items() {
        List<ItemStack> results = new ArrayList<>();
        for (War war : City.getCityByPlayer(player).getWars()) {
            for (City ally : war.getAttackerAllies()) {
                ItemStack item = new ItemStack(Material.PAPER);
                ItemMeta meta = item.getItemMeta();
                meta.displayName(Component.text(ally.getName()));
                item.setItemMeta(meta);
                results.add(item);
            }
        }
        return results.toArray(new ItemStack[0]);
    }

    @Override
    protected void clickCallback(InventoryClickEvent event) {

    }

    @Override
    protected void inventoryCloseCallback(InventoryCloseEvent event) {

    }
}
