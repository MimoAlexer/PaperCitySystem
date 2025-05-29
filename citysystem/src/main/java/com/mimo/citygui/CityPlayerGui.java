package com.mimo.citygui;

import com.mimo.City;
import com.mimo.gui.BasicInventoryGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public class CityPlayerGui extends BasicInventoryGui {
    public CityPlayerGui(Player player) {
        super(player, "Players of " + City.getCityByPlayer(player).getName());
    }

    @Override
    protected ItemStack[] items() {
        addItem(9, 3, new ItemStack(Material.BARRIER).getType(), null);
        for (int col = 0; col < 10; col++) {
            addItem(col, 0, new ItemStack(Material.GRAY_STAINED_GLASS_PANE).getType(), null);
            addItem(col, 6, new ItemStack(Material.GRAY_STAINED_GLASS_PANE).getType(), null);
        }
        List<Player> members = City.getCityByPlayer(player).getPlayers();
        for (int i = 0; i < members.size(); i++) {
            Player member = members.get(i);
            ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            if (meta != null) {
                meta.setOwningPlayer(member);
                meta.displayName(Component.text(member.getName()));
                head.setItemMeta(meta);
            }
            int col = i % 10;
            int row = 3 + (i / 10);
            addItem(col, row, head.getType(), head);
        }
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
