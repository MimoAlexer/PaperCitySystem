package com.mimo.api;

import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class ItemBuilder {

    private ItemStack itemStack;

    public ItemBuilder(Material material) {
        itemStack = new ItemStack(material);
    }

    public ItemBuilder name(String name) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.itemName(Component.text(name));
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        itemStack.lore(lore.stream().map(Component::text).toList());
        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment, int level) {
        itemStack.addEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder enchants(Map<Enchantment, Integer> enchants) {
        itemStack.addUnsafeEnchantments(enchants);
        return this;
    }

    public ItemBuilder flags(ItemFlag... itemFlags) {
        itemStack.addItemFlags(itemFlags);
        return this;
    }

    public ItemStack build() {
        return itemStack;
    }
}
