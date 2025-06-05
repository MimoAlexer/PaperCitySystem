package com.mimo.api.bossbar;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BossbarManager {

    private final Plugin plugin;
    private final Map<String, PersonalBossbar> bossbars;

    public BossbarManager(Plugin plugin) {
        this.plugin = plugin;
        this.bossbars = new HashMap<>();
    }

    public PersonalBossbar createBossbar(String id, String title, BossbarColor color, BossbarStyle style) {
        PersonalBossbar bossbar = new SimpleBossbar(title, color, style);
        bossbars.put(id, bossbar);
        return bossbar;
    }

    public PersonalBossbar createBossbar(String id, String title) {
        return createBossbar(id, title, BossbarColor.PURPLE, BossbarStyle.SOLID);
    }

    public PersonalBossbar createTimedBossbar(String id, String title, BossbarColor color, BossbarStyle style, long duration) {
        PersonalBossbar bossbar = new TimedBossbar(plugin, title, color, style, duration);
        bossbars.put(id, bossbar);
        return bossbar;
    }

    public PersonalBossbar createTimedBossbar(String id, String title, long duration) {
        return createTimedBossbar(id, title, BossbarColor.PURPLE, BossbarStyle.SOLID, duration);
    }

    public ProgressBossbar createProgressBossbar(String id, String title, BossbarColor color, BossbarStyle style, long duration) {
        ProgressBossbar bossbar = new ProgressBossbar(plugin, title, color, style, duration);
        bossbars.put(id, bossbar);
        return bossbar;
    }

    public ProgressBossbar createProgressBossbar(String id, String title, long duration) {
        return createProgressBossbar(id, title, BossbarColor.GREEN, BossbarStyle.SOLID, duration);
    }

    public PersonalBossbar getBossbar(String id) {
        return bossbars.get(id);
    }

    public boolean removeBossbar(String id) {
        PersonalBossbar bossbar = bossbars.remove(id);
        if (bossbar != null) {
            bossbar.remove();
            return true;
        }
        return false;
    }

    public Set<PersonalBossbar> getAllBossbars() {
        return new HashSet<>(bossbars.values());
    }

    public Set<PersonalBossbar> getPlayerBossbars(Player player) {
        Set<PersonalBossbar> playerBossbars = new HashSet<>();
        for (PersonalBossbar bossbar : bossbars.values()) {
            if (bossbar.isVisible(player)) {
                playerBossbars.add(bossbar);
            }
        }
        return playerBossbars;
    }

    public void removeAllBossbars() {
        for (PersonalBossbar bossbar : new HashSet<>(bossbars.values())) {
            bossbar.remove();
        }
        bossbars.clear();
    }

    public void removePlayerBossbars(Player player) {
        for (PersonalBossbar bossbar : bossbars.values()) {
            bossbar.removePlayer(player);
        }
    }
}
