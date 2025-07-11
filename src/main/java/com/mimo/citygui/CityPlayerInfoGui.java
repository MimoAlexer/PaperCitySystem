package com.mimo.citygui;

import com.mimo.api.gui.AbstractInventoryGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class CityPlayerInfoGui extends AbstractInventoryGui {
    private final Player infoPlayer;

    public CityPlayerInfoGui(Player player, Player infoPlayer) {
        super(player, Component.text(infoPlayer.getName() + "'s Info"));
        this.infoPlayer = infoPlayer;
    }

    @Override
    protected ItemStack[] items() {
        for (int col = 0; col < 9; col++) {
            addItem(col, 0, Material.GRAY_STAINED_GLASS_PANE);
            addItem(col, 5, Material.GRAY_STAINED_GLASS_PANE);
        }
        addItem(8, 5, Material.BARRIER);
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        if (meta != null) {
            meta.setOwningPlayer(infoPlayer);
            meta.displayName(Component.text(infoPlayer.getName()));
            head.setItemMeta(meta);
        }
        addItem(4, 3, head);
        ItemStack infoExp = new ItemStack(Material.EXPERIENCE_BOTTLE);
        var itemMeta = infoExp.getItemMeta();
        if (itemMeta != null) {
            itemMeta.displayName(Component.text(infoPlayer.getTotalExperience() + " XP"));
            infoExp.setItemMeta(itemMeta);
        }
        addItem(6, 2, infoExp);
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
