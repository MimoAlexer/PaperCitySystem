package com.mimo.api.bossbar;

import org.bukkit.entity.Player;

import java.util.Set;

// GOSH WHY DO COMPONENTS EXIST???
public interface PersonalBossbar {

    PersonalBossbar setTitle(String title);

    String getTitle();

    PersonalBossbar setProgress(double progress);

    double getProgress();

    PersonalBossbar setColor(BossbarColor color);

    BossbarColor getColor();

    PersonalBossbar setStyle(BossbarStyle style);

    BossbarStyle getStyle();

    PersonalBossbar addFlag(BossbarFlag flag);

    PersonalBossbar removeFlag(BossbarFlag flag);

    boolean hasFlag(BossbarFlag flag);

    PersonalBossbar addPlayer(Player player);

    PersonalBossbar removePlayer(Player player);

    Set<Player> getPlayers();

    boolean isVisible(Player player);

    PersonalBossbar addAllPlayers();

    PersonalBossbar removeAllPlayers();

    PersonalBossbar setVisible(boolean visible);

    void remove();

    org.bukkit.boss.BossBar getBukkitBossBar();
}
