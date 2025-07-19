package com.mimo.wargui;

import com.mimo.City;
import com.mimo.api.gui.AbstractInventoryGui;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.List;
import com.mimo.War;

public class CityWarGui extends AbstractInventoryGui {
    public CityWarGui(Player player) {
        super(player, Component.text("Wars of " + City.getCityByPlayer(player).getName() + "!"));
    }

    @Override
    protected ItemStack[] items() {
        List<ItemStack> results = new ArrayList<>();
        City city = City.getCityByPlayer(player);
        if (city == null) return results.toArray(new ItemStack[0]);
        for (War war : city.getWars()) {
            ItemStack item = new ItemStack(Material.SHIELD);
            ItemMeta meta = item.getItemMeta();
            meta.displayName(Component.text("War vs: " + war.getDefender().getName(), net.kyori.adventure.text.format.NamedTextColor.RED));
            meta.lore(List.of(
                Component.text("Type: " + war.getWarType().name(), net.kyori.adventure.text.format.NamedTextColor.AQUA),
                Component.text("Score: " + war.getAttackerScore() + " - " + war.getDefenderScore(), net.kyori.adventure.text.format.NamedTextColor.YELLOW),
                Component.text("Click to manage allies/enemies", net.kyori.adventure.text.format.NamedTextColor.GREEN)
            ));
            item.setItemMeta(meta);
            results.add(item);
        }
        return results.toArray(new ItemStack[0]);
    }

    @Override
    protected void clickCallback(InventoryClickEvent event) {
        event.setCancelled(true);
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return;
        if (clicked.getType() == Material.SHIELD) {
            // Open AlliesGui or EnemyGui for the selected war
            new AlliesGui(player, Component.text("Allies of " + City.getCityByPlayer(player).getName())).show();
        }
    }

    @Override
    protected void inventoryCloseCallback(InventoryCloseEvent event) {

    }
}
