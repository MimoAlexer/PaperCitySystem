package com.mimo.citygui;

import com.mimo.City;
import com.mimo.CityTypes;
import com.mimo.api.gui.AbstractInventoryGui;
import com.mimo.api.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class CityLvlGui extends AbstractInventoryGui {
    private final City city;

    public CityLvlGui(Player player) {
        super(player, Component.text("City Level Progress"), 3);
        this.city = City.getCityByPlayer(player);
    }

    @Override
    protected ItemStack[] items() {
        CityTypes[] types = CityTypes.values();
        int exp = city.getExp();
        for (int i = 0; i < types.length; i++) {
            CityTypes type = types[i];
            int neededExp = getExpForType(type);
            boolean reached = exp >= neededExp;
            Material mat = reached ? Material.GREEN_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE;
            ItemStack pane = new ItemBuilder(mat)
                .name(type.getName())
                .lore(java.util.List.of("Exp needed: " + neededExp))
                .build();
            addItem(i + 2, 1, pane);
        }
        return new ItemStack[0];
    }

    private int getExpForType(CityTypes type) {
        return switch (type) {
            case CITY -> 4000;
            case TOWN -> 2000;
            case VILLAGE -> 1000;
            case HAMLET -> 500;
            case SETTLEMENT -> 0;
        };
    }

    @Override
    protected void clickCallback(InventoryClickEvent event) {
    }

    @Override
    protected void inventoryCloseCallback(InventoryCloseEvent event) {
    }
} 