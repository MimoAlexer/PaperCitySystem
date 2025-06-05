package com.mimo.api.bossbar;

import lombok.Getter;
import org.bukkit.boss.BarColor;

@Getter
public enum BossbarColor {
    PINK(BarColor.PINK),
    BLUE(BarColor.BLUE),
    RED(BarColor.RED),
    GREEN(BarColor.GREEN),
    YELLOW(BarColor.YELLOW),
    PURPLE(BarColor.PURPLE),
    WHITE(BarColor.WHITE);
    
    private final BarColor bukkitColor;

    BossbarColor(BarColor bukkitColor) {
        this.bukkitColor = bukkitColor;
    }

}
