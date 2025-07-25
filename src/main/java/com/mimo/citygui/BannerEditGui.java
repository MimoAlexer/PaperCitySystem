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
import org.bukkit.inventory.meta.BannerMeta;

public class BannerEditGui extends AbstractInventoryGui {
    private final City city;
    private Material selectedColor;

    public BannerEditGui(Player player, City city) {
        super(player, Component.text("Edit City Banner"));
        this.city = city;
        this.selectedColor = city.getBanner() != null ? city.getBanner().getType() : Material.WHITE_BANNER;
    }

    @Override
    protected ItemStack[] items() {
        Material[] colors = {
                Material.WHITE_BANNER, Material.BLACK_BANNER, Material.RED_BANNER,
                Material.BLUE_BANNER, Material.GREEN_BANNER, Material.YELLOW_BANNER
        };
        for (int i = 0; i < colors.length; i++) {
            ItemStack banner = new ItemStack(colors[i]);
            BannerMeta meta = (BannerMeta) banner.getItemMeta();
            meta.displayName(Component.text(colors[i].name().replace("_BANNER", ""), NamedTextColor.AQUA));
            banner.setItemMeta(meta);
            addItem(i + 1, 2, banner);
        }
        ItemStack confirm = new ItemStack(Material.LIME_WOOL);
        confirm.getItemMeta().displayName(Component.text("Confirm", NamedTextColor.GREEN));
        addItem(3, 4, confirm);
        ItemStack cancel = new ItemStack(Material.RED_WOOL);
        cancel.getItemMeta().displayName(Component.text("Cancel", NamedTextColor.RED));
        addItem(5, 4, cancel);
        return new ItemStack[0];
    }

    @Override
    public void clickCallback(InventoryClickEvent event) {
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null) return;
        Material type = clicked.getType();
        if (type.name().endsWith("BANNER")) {
            selectedColor = type;
            player.sendMessage(Component.text("Selected color: " + type.name().replace("_BANNER", ""), NamedTextColor.YELLOW));
            new BannerEditGui(player, city).show();
        } else if (type == Material.LIME_WOOL) {
            // Confirm
            // CONFIRM TAKE THE SHOT TAKE THE SHOT!!!
            ItemStack newBanner = new ItemStack(selectedColor);
            city.setBanner(newBanner, player);
            player.sendMessage(Component.text("City banner updated!", NamedTextColor.GREEN));
            new CityMainGui(player).show();
        } else if (type == Material.RED_WOOL) {
            new CityMainGui(player).show();
        }
    }

    @Override
    protected void inventoryCloseCallback(InventoryCloseEvent event) {
    }
} 