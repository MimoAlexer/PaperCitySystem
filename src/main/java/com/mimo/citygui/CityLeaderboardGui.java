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

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.HashMap;

public class CityLeaderboardGui extends AbstractInventoryGui {
    private final HashMap<ItemStack, City> itemStackCityMap = new HashMap<>();
    public CityLeaderboardGui(Player player) {
        super(player, Component.text("City Leaderboard"));
    }

    @Override
    protected ItemStack[] items() {
        List<City> sortedCities = City.cityArrayList.stream()
                .sorted(Comparator.comparingInt(City::getExp).reversed())
                .collect(Collectors.toList());
        int row = 2;
        int col = 2;
        int rank = 1;
        for (City city : sortedCities) {
            ItemStack paper = new ItemStack(Material.PAPER);
            var meta = paper.getItemMeta();
            meta.displayName(Component.text("#" + rank + " " + city.getName(), NamedTextColor.GOLD));
            meta.lore(List.of(
                    Component.text("EXP: " + city.getExp(), NamedTextColor.YELLOW),
                    Component.text("Owner: " + city.getOwner().getName(), NamedTextColor.AQUA)
            ));
            paper.setItemMeta(meta);
            addItem(col, row, paper);
            itemStackCityMap.put(paper, city);
            row++;
            rank++;
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
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null) return;
        City city = itemStackCityMap.get(clicked);
        if (city != null) {
            new CityMainGui(player, city).show();
        }
    }

    @Override
    protected void inventoryCloseCallback(InventoryCloseEvent event) {
    }
} 