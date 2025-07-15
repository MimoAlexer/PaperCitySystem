package com.mimo.citygui;

import com.mimo.City;
import com.mimo.api.gui.AbstractInventoryGui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class TreasureChamberGui extends AbstractInventoryGui {
    private final City city;
    private boolean isOwnerView;

    public TreasureChamberGui(Player player) {
        super(player, Component.text("Schatzkammer - " + City.getCityByPlayer(player).getName(), NamedTextColor.GOLD), 6);
        this.city = City.getCityByPlayer(player);
        this.isOwnerView = city.getOwner().equals(player);
        
        if (!city.canAccessTreasureChamber(player)) {
            player.sendMessage(Component.text("You don't have permission to access the treasure chamber!", NamedTextColor.RED));
            return;
        }
    }

    @Override
    protected ItemStack[] items() {
        if (!city.canAccessTreasureChamber(player)) {
            return new ItemStack[0];
        }

        // Copy items from the city's treasure chamber
        ItemStack[] treasureItems = city.getTreasureChamberContents();
        for (int i = 0; i < Math.min(treasureItems.length, 45); i++) {
            if (treasureItems[i] != null) {
                inventory.setItem(i, treasureItems[i].clone());
            }
        }

        // Add control buttons in the bottom row
        addBorderButtons();
        
        return new ItemStack[0];
    }

    private void addBorderButtons() {
        // Info button
        ItemStack infoButton = new ItemStack(Material.BOOK);
        ItemMeta infoMeta = infoButton.getItemMeta();
        infoMeta.displayName(Component.text("Treasure Chamber Info", NamedTextColor.AQUA));
        infoMeta.lore(Arrays.asList(
            Component.text("City: " + city.getName(), NamedTextColor.GRAY),
            Component.text("Owner: " + city.getOwner().getName(), NamedTextColor.GRAY),
            Component.text("", NamedTextColor.GRAY),
            Component.text("This is your city's treasure chamber.", NamedTextColor.YELLOW),
            Component.text("Store valuable items here for", NamedTextColor.YELLOW),
            Component.text("your city members to share.", NamedTextColor.YELLOW)
        ));
        infoButton.setItemMeta(infoMeta);
        addItem(0, 5, infoButton);

        // Deposit all button (for convenience)
        if (isOwnerView) {
            ItemStack depositButton = new ItemStack(Material.CHEST);
            ItemMeta depositMeta = depositButton.getItemMeta();
            depositMeta.displayName(Component.text("Quick Deposit", NamedTextColor.GREEN));
            depositMeta.lore(Arrays.asList(
                Component.text("Click to deposit all items", NamedTextColor.GRAY),
                Component.text("from your inventory!", NamedTextColor.GRAY)
            ));
            depositButton.setItemMeta(depositMeta);
            addItem(3, 5, depositButton);
        }

        // Close button
        ItemStack closeButton = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = closeButton.getItemMeta();
        closeMeta.displayName(Component.text("Close", NamedTextColor.RED));
        closeButton.setItemMeta(closeMeta);
        addItem(8, 5, closeButton);

        // Fill remaining bottom row with glass panes
        for (int i = 1; i < 8; i++) {
            if (i != 3 || !isOwnerView) {
                addItem(i, 5, Material.GRAY_STAINED_GLASS_PANE);
            }
        }
    }

    @Override
    protected void clickCallback(InventoryClickEvent event) {
        Player clicker = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();
        int slot = event.getSlot();

        // Handle control buttons
        if (slot >= 45) { // Bottom row (control buttons)
            event.setCancelled(true);
            
            if (isItemStackClicked(Material.BARRIER, event)) {
                clicker.closeInventory();
                return;
            }
            
            if (isItemStackClicked(Material.BOOK, event)) {
                // Info button - do nothing, just display info
                return;
            }
            
            if (isOwnerView && isItemStackClicked(Material.CHEST, event)) {
                depositAllItems(clicker);
                return;
            }
            
            return;
        }

        // Handle treasure chamber slots (0-44)
        if (slot < 45) {
            // Allow interaction with treasure chamber
            if (!city.canAccessTreasureChamber(clicker)) {
                event.setCancelled(true);
                clicker.sendMessage(Component.text("You don't have permission to access the treasure chamber!", NamedTextColor.RED));
                return;
            }
            
            // Sync with city's treasure chamber inventory
            syncWithTreasureChamber();
        }
    }

    private void depositAllItems(Player player) {
        Inventory playerInventory = player.getInventory();
        int deposited = 0;
        
        for (int i = 0; i < 36; i++) { // Player inventory slots (excluding armor/offhand)
            ItemStack item = playerInventory.getItem(i);
            if (item != null && !item.getType().isAir()) {
                if (city.addToTreasureChamber(item.clone())) {
                    playerInventory.setItem(i, null);
                    deposited++;
                }
            }
        }
        
        if (deposited > 0) {
            player.sendMessage(Component.text("Deposited " + deposited + " stacks into the treasure chamber!", NamedTextColor.GREEN));
            refreshInventory();
        } else {
            player.sendMessage(Component.text("No items to deposit or treasure chamber is full!", NamedTextColor.YELLOW));
        }
    }

    private void syncWithTreasureChamber() {
        // Sync the first 45 slots with the city's treasure chamber
        for (int i = 0; i < 45; i++) {
            ItemStack guiItem = inventory.getItem(i);
            city.getTreasureChamber().setItem(i, guiItem);
        }
    }

    private void refreshInventory() {
        // Clear and reload the inventory
        inventory.clear();
        items();
    }

    @Override
    protected void inventoryCloseCallback(InventoryCloseEvent event) {
        // Sync any changes back to the city's treasure chamber when closing
        syncWithTreasureChamber();
        
        player.sendMessage(Component.text("Treasure chamber saved!", NamedTextColor.GREEN));
    }
}
