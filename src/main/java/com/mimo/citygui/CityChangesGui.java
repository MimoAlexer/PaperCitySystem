package com.mimo.citygui;

import com.mimo.City;
import com.mimo.api.gui.AbstractInventoryGui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class CityChangesGui extends AbstractInventoryGui {
    public CityChangesGui(Player player) {
        super(player, Component.text("Recent Changes"));
    }

    @Override
    protected ItemStack[] items() {
        City city = City.getCityByPlayer(player);
        if (city == null) return new ItemStack[0];
        List<City.CityChange> changes = city.getChanges();
        int row = 2;
        int col = 2;
        // bs code here!!!!
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(java.time.ZoneId.systemDefault());
        for (int i = 0; i < Math.min(changes.size(), 12); i++) {
            City.CityChange change = changes.get(i);
            ItemStack paper = new ItemStack(Material.PAPER);
            var meta = paper.getItemMeta();
            meta.displayName(Component.text(change.type, NamedTextColor.GOLD));
            meta.lore(List.of(
                    Component.text(change.description, NamedTextColor.YELLOW),
                    Component.text("By: " + (change.actor != null ? change.actor : "System"), NamedTextColor.AQUA),
                    Component.text(fmt.format(change.timestamp), NamedTextColor.GRAY)
            ));
            paper.setItemMeta(meta);
            addItem(col, row, paper);
            row++;
            if (row > 4) {
                row = 2;
                col++;
            }
        }
        addItem(8, 5, Material.BARRIER);
        return new ItemStack[0];
    }

    @Override
    public void clickCallback(InventoryClickEvent event) {
        event.setCancelled(true);
        if (isItemStackClicked(Material.BARRIER, event)) new CityMainGui(player).show();
    }

    @Override
    protected void inventoryCloseCallback(InventoryCloseEvent event) {
    }
} 