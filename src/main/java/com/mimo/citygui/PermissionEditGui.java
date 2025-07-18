package com.mimo.citygui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.mimo.City;
import com.mimo.Permissions;
import com.mimo.api.gui.AbstractInventoryGui;

import net.kyori.adventure.text.Component;

class PermissionEditGui extends AbstractInventoryGui {
    private final Player targetPlayer;
    private final Player editor;
    private final Permissions perms;
    @SuppressWarnings("unused")
    private final City city;

    public PermissionEditGui(Player editor, Player targetPlayer) {
        super(editor, Component.text("Edit Permissions: " + targetPlayer.getName()), 3);
        this.targetPlayer = targetPlayer;
        this.editor = editor;
        this.city = City.getCityByPlayer(targetPlayer);
        this.perms = City.playerPermissionsHashMap.get(targetPlayer);
        show();
    }

    @Override
    protected ItemStack[] items() {
        // Layout: 1 row, each permission as a toggleable item
        // 0: Back, 2-7: Permissions
        addItem(0, 2, Material.BARRIER);
        addPermissionToggle(2, "Block Break", perms.hasBlockBreakPermission, Material.IRON_PICKAXE);
        addPermissionToggle(3, "Block Place", perms.hasBlockPlacePermission, Material.GRASS_BLOCK);
        addPermissionToggle(4, "Interact", perms.hasInteractPermission, Material.LEVER);
        addPermissionToggle(5, "Claim", perms.hasClaimPermission, Material.MAP);
        addPermissionToggle(6, "Kick", perms.hasKickPermission, Material.BOOK);
        addPermissionToggle(7, "Treasure Chamber", perms.hasTreasureChamberPermission, Material.CHEST);
        return new ItemStack[0];
    }

    private void addPermissionToggle(int x, String name, boolean value, Material mat) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(name));
        meta.lore(java.util.List.of(
            Component.text("Current: " + (value ? "Enabled" : "Disabled")),
            Component.text("Click to toggle")
        ));
        item.setItemMeta(meta);
        addItem(x, 1, item);
    }

    @Override
    public void clickCallback(InventoryClickEvent event) {
        event.setCancelled(true);
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null) return;
        Material type = clicked.getType();
        if (type == Material.BARRIER) {
            new CityPlayerInfoGui(editor, targetPlayer).show();
            return;
        }
        // Toggle permissions
        boolean changed = false;
        if (type == Material.IRON_PICKAXE) {
            perms.hasBlockBreakPermission = !perms.hasBlockBreakPermission;
            changed = true;
        } else if (type == Material.GRASS_BLOCK) {
            perms.hasBlockPlacePermission = !perms.hasBlockPlacePermission;
            changed = true;
        } else if (type == Material.LEVER) {
            perms.hasInteractPermission = !perms.hasInteractPermission;
            changed = true;
        } else if (type == Material.MAP) {
            perms.hasClaimPermission = !perms.hasClaimPermission;
            changed = true;
        } else if (type == Material.BOOK) {
            perms.hasKickPermission = !perms.hasKickPermission;
            changed = true;
        } else if (type == Material.CHEST) {
            perms.hasTreasureChamberPermission = !perms.hasTreasureChamberPermission;
            changed = true;
        }
        if (changed) {
            City.playerPermissionsHashMap.put(targetPlayer, perms);
            show(); // Refresh GUI
        }
    }

    @Override
    protected void inventoryCloseCallback(InventoryCloseEvent event) {
        // No action needed
    }
}