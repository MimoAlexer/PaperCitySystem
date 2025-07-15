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
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class CityMainGui extends AbstractInventoryGui {
    public CityMainGui(Player player) {
        super(player, Component.text(City.getCityByPlayer(player).getName()));
    }

    @Override
    protected ItemStack[] items() {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta headMeta = head.getItemMeta();
        headMeta.displayName(Component.text("Players", NamedTextColor.BLUE));
        headMeta.lore(Arrays.asList(Component.text("Manage city members", NamedTextColor.GRAY)));
        head.setItemMeta(headMeta);
        
        ItemStack claim = new ItemStack(Material.BLACK_BANNER);
        ItemMeta claimMeta = claim.getItemMeta();
        claimMeta.displayName(Component.text("Claim", NamedTextColor.GREEN));
        claimMeta.lore(Arrays.asList(Component.text("Claim new territory", NamedTextColor.GRAY)));
        claim.setItemMeta(claimMeta);
        
        ItemStack treasureChamber = new ItemStack(Material.CHEST);
        ItemMeta treasureMeta = treasureChamber.getItemMeta();
        treasureMeta.displayName(Component.text("Schatzkammer", NamedTextColor.GOLD));
        treasureMeta.lore(Arrays.asList(
            Component.text("Access the treasure chamber", NamedTextColor.GRAY),
            Component.text("Store and share valuable items", NamedTextColor.GRAY)
        ));
        treasureChamber.setItemMeta(treasureMeta);
        
        addItem(1, 2, head);
        addItem(3, 2, claim);
        addItem(5, 2, treasureChamber);
        
        for (int i = 0; i < 9; i++) {
            addItem(i, 0, Material.GRAY_STAINED_GLASS_PANE);
            addItem(i, 5, Material.GRAY_STAINED_GLASS_PANE);
        }
        addItem(8, 5, Material.BARRIER);
        return new ItemStack[0];
    }

    @Override
    public void clickCallback(InventoryClickEvent event) {
        event.setCancelled(true);
        if (isItemStackClicked(Material.BARRIER, event)) {
            event.getWhoClicked().closeInventory();
        }
        if (isItemStackClicked(Material.PLAYER_HEAD, event)) {
            new CityPlayerGui(player).show();
        }
        if (isItemStackClicked(Material.BLACK_BANNER, event)) {
            new CityClaimGui(player).show();
        }
        if (isItemStackClicked(Material.CHEST, event)) {
            City city = City.getCityByPlayer(player);
            if (city.canAccessTreasureChamber(player)) {
                new TreasureChamberGui(player).show();
            } else {
                player.sendMessage(Component.text("You don't have permission to access the treasure chamber!", NamedTextColor.RED));
            }
        }
    }

    @Override
    protected void inventoryCloseCallback(InventoryCloseEvent event) {

    }
}
