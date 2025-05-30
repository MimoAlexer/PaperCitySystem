package com.mimo.citygui;

import com.mimo.shared.gui.AbstractInventoryGui;
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
        for (int col = 0; col < 10; col++) {
            addItem(col, 0, new ItemStack(Material.GRAY_STAINED_GLASS_PANE).getType());
            addItem(col, 6, new ItemStack(Material.GRAY_STAINED_GLASS_PANE).getType());
        }
        addItem(8, 6, Material.BARRIER);
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        if (meta != null) {
            meta.setOwningPlayer(infoPlayer);
            meta.displayName(Component.text(infoPlayer.getName()));
            head.setItemMeta(meta);
        }
        addItem(4, 3, head);
        ItemStack infoExp = new ItemStack(Material.EXPERIENCE_BOTTLE);
        infoExp.getItemMeta().displayName(Component.text(infoPlayer.getTotalExperience() + " XP"));
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
