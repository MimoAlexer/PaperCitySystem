package com.mimo.api.bossbar;

import com.mimo.api.AbstractPersonalBossbar;

public class SimpleBossbar extends AbstractPersonalBossbar {

    public SimpleBossbar(String title, BossbarColor color, BossbarStyle style) {
        super(title, color, style);
    }

    public SimpleBossbar(String title) {
        super(title);
    }
}
