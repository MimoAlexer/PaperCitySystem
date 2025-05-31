package com.mimo.citygui;

import com.mimo.shared.gui.GenericConfirmationGui;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.function.BiFunction;

@Deprecated
public class test extends GenericConfirmationGui {
    // WIP
    public test(Player player, Component title, BiFunction<Player, Boolean, Void> function) {
        super(player, title);
    }

    @Override
    public void onConfirm(Player player) {

    }

    @Override
    public void onCancel(Player player) {

    }
}
