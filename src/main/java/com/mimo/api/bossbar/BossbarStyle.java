package com.mimo.api.bossbar;

import lombok.Getter;
import org.bukkit.boss.BarStyle;

@Getter
public enum BossbarStyle {
    SOLID(BarStyle.SOLID),
    SEGMENTED_6(BarStyle.SEGMENTED_6),
    SEGMENTED_10(BarStyle.SEGMENTED_10),
    SEGMENTED_12(BarStyle.SEGMENTED_12),
    SEGMENTED_20(BarStyle.SEGMENTED_20);

    private final BarStyle bukkitStyle;

    BossbarStyle(BarStyle bukkitStyle) {
        this.bukkitStyle = bukkitStyle;
    }

}
