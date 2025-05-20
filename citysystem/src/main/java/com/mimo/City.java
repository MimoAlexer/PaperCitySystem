package com.mimo;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.mimo.Gui.BasicInventoryGui;

public class City extends BasicInventoryGui {
    private final String name;
    private final String owner;
    private final Inventory inventory;

    public City(String name, String owner) {
        super(null, name);
        this.name = name;
        this.owner = owner;
        this.inventory = getInventory();
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    @Override
    protected ItemStack[] items() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'items'");
    }

    @Override
    public void clickCallback(InventoryClickEvent event) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'clickCallback'");
    }
    
    
}
