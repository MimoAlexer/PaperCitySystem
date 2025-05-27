package com.mimo.CityGui;

import com.mimo.Gui.BasicInventoryGui;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class CityPlayerInfoGui extends BasicInventoryGui {
    private Player infoPlayer;

    public CityPlayerInfoGui(Player player, Player infoPlayer) {
        this.infoPlayer = infoPlayer;
        super(player, infoPlayer.getName() + "'s Info");
    }

    @Override
    protected ItemStack[] items() {
        addItem(9, 3, Material.BARRIER);
        for (int col = 0; col < 10; col++) {
            addItem(col, 0, new ItemStack(Material.GRAY_STAINED_GLASS_PANE).getType());
            addItem(col, 6, new ItemStack(Material.GRAY_STAINED_GLASS_PANE).getType());
        }
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        if (meta != null) {
            meta.setOwningPlayer(infoPlayer);
            meta.setDisplayName(infoPlayer.getName()); // TODO: maybe is there a better way to display the name?
            head.setItemMeta(meta);
        }
        return new ItemStack[0];
    }

    @Override
    public void clickCallback(InventoryClickEvent event) {
        event.setCancelled(true);
        switch (event.getCurrentItem()) {
            case ItemStack _ when isItemStackClicked(event, Material.BARRIER) -> {
                MainGui mainGui = new MainGui(player);
                mainGui.show();
            }
            case null -> {}
            default -> {
                event.setCancelled(true);
                return;
            }
        }
    }
}
