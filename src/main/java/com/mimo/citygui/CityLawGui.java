package com.mimo.citygui;

import com.mimo.City;
import com.mimo.api.CityLawType;
import com.mimo.api.gui.AbstractInventoryGui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CityLawGui extends AbstractInventoryGui {
    private final City city;
    private final boolean isOwner;

    public CityLawGui(Player player) {
        super(player, Component.text("City Laws"));
        this.city = City.getCityByPlayer(player);
        this.isOwner = city != null && city.getOwner().equals(player);
    }

    @Override
    protected ItemStack[] items() {
        int y = 2;
        int x = 2;
        for (CityLawType law : CityLawType.values()) {
            boolean enabled = city != null && city.getLaws().contains(law);
            Material mat = enabled ? Material.LIME_WOOL : Material.RED_WOOL;
            ItemStack item = new ItemStack(mat);
            ItemMeta meta = item.getItemMeta();
            meta.displayName(Component.text(law.name(), NamedTextColor.AQUA));
            meta.lore(java.util.List.of(
                Component.text(enabled ? "Enabled" : "Disabled", enabled ? NamedTextColor.GREEN : NamedTextColor.RED),
                Component.text(isOwner ? "Click to toggle" : "Only the owner can edit", NamedTextColor.YELLOW)
            ));
            item.setItemMeta(meta);
            addItem(x, y, item);
            x++;
            if (x > 6) { x = 2; y++; }
        }
        addItem(8, 5, Material.BARRIER);
        return new ItemStack[0];
    }

    @Override
    public void clickCallback(InventoryClickEvent event) {
        if (isItemStackClicked(Material.BARRIER, event)) {
            event.getWhoClicked().closeInventory();
            return;
        }
        if (!isOwner) return;
        for (CityLawType law : CityLawType.values()) {
            boolean enabled = city.getLaws().contains(law);
            Material mat = enabled ? Material.LIME_WOOL : Material.RED_WOOL;
            if (isItemStackClicked(mat, event)) {
                if (enabled) {
                    city.getLaws().remove(law);
                    player.sendMessage(Component.text("Disabled law: " + law.name(), NamedTextColor.YELLOW));
                } else {
                    city.getLaws().add(law);
                    player.sendMessage(Component.text("Enabled law: " + law.name(), NamedTextColor.GREEN));
                }
                new CityLawGui(player).show();
                return;
            }
        }
    }

    @Override
    protected void inventoryCloseCallback(InventoryCloseEvent event) {
        // WHY IS IT ALWAYS CLOSING!!
        // STAY OPEN
        // THE PLAYER SHOULD NEVER CLOSE THE BEST GUI
        // Schizophrenia is hitting hard
    }
} 