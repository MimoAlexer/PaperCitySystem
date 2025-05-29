package com.mimo.citygui;

import com.mimo.gui.BasicInventoryGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class CityPlayerInfoGui extends BasicInventoryGui {
    private final Player infoPlayer;

    public CityPlayerInfoGui(Player player, Player infoPlayer) {
        this.infoPlayer = infoPlayer;
        super(player, infoPlayer.getName() + "'s Info");
    }

    @Override
    protected ItemStack[] items() {
        for (int col = 0; col < 10; col++) {
            addItem(col, 0, new ItemStack(Material.GRAY_STAINED_GLASS_PANE).getType(), null);
            addItem(col, 6, new ItemStack(Material.GRAY_STAINED_GLASS_PANE).getType(), null);
        }
        addItem(9, 6, Material.BARRIER, null);
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        if (meta != null) {
            meta.setOwningPlayer(infoPlayer);
            meta.displayName(Component.text(infoPlayer.getName()));
            head.setItemMeta(meta);
        }
        addItem(4, 3, null, head);
        ItemStack infoExp = new ItemStack(Material.EXPERIENCE_BOTTLE);
        infoExp.getItemMeta().displayName(Component.text(infoPlayer.getTotalExperience() + " XP"));
        addItem(6, 2, null, infoExp);
        return new ItemStack[0];
    }

    @Override
    public void clickCallback(InventoryClickEvent event) {
        event.setCancelled(true);
        switch (event.getCurrentItem()) {
            case ItemStack _ when isItemStackClicked(event, Material.BARRIER) -> {
                CityMainGui mainGui = new CityMainGui(player);
                mainGui.show();
            }
            case null -> {
            }
            default -> {
                event.setCancelled(true);
                return;
            }
        }
    }
}
