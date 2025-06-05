package com.mimo.api.bossbar;

import com.mimo.api.AbstractPersonalBossbar;
import net.kyori.adventure.text.Component;

package com.mimo.api.bossbar;

import com.mimo.api.AbstractPersonalBossbar;
import net.kyori.adventure.text.Component;

public class SimpleBossbar extends AbstractPersonalBossbar {

    public SimpleBossbar(Component title, BossbarColor color, BossbarStyle style) {
        super(title, color, style);
    }

    public SimpleBossbar(Component title) {
        super(title);
    }

    public SimpleBossbar(String title, BossbarColor color, BossbarStyle style) {
        super(title, color, style);
    }

    public SimpleBossbar(String title) {
        super(title);
    }
}
