package com.mimo.api.bossbar;

import lombok.Getter;
import org.bukkit.boss.BarFlag;

@Getter
public enum BossbarFlag {
    CREATE_FOG(BarFlag.CREATE_FOG),
    DARKEN_SKY(BarFlag.DARKEN_SKY),
    PLAY_BOSS_MUSIC(BarFlag.PLAY_BOSS_MUSIC);

    private final BarFlag bukkitFlag;

    BossbarFlag(BarFlag bukkitFlag) {
        this.bukkitFlag = bukkitFlag;
    }

}
