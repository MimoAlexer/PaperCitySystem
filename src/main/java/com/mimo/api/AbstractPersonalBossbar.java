package com.mimo.api;

import com.mimo.api.bossbar.BossbarColor;
import com.mimo.api.bossbar.BossbarFlag;
import com.mimo.api.bossbar.BossbarStyle;
import com.mimo.api.bossbar.PersonalBossbar;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractPersonalBossbar implements PersonalBossbar {

    @Getter
    private final BossBar bukkitBossBar;
    private final Set<Player> players;

    public AbstractPersonalBossbar(String title, BossbarColor color, BossbarStyle style) {
        this.bukkitBossBar = Bukkit.createBossBar("", color.getBukkitColor(), style.getBukkitStyle());
        this.bukkitBossBar.setTitle(title);
        this.players = new HashSet<>();
    }

    public AbstractPersonalBossbar(String title) {
        this(title, BossbarColor.PURPLE, BossbarStyle.SOLID);
    }

    @Override
    public PersonalBossbar setTitle(String title) {
        bukkitBossBar.setTitle(title);
        return this;
    }

    @Override
    public String getTitle() {
        return bukkitBossBar.getTitle();
    }

    @Override
    public PersonalBossbar setProgress(double progress) {
        if (progress < 0.0 || progress > 1.0) {
            throw new IllegalArgumentException("Progress must be between 0.0 and 1.0");
        }
        bukkitBossBar.setProgress(progress);
        return this;
    }

    @Override
    public double getProgress() {
        return bukkitBossBar.getProgress();
    }

    @Override
    public PersonalBossbar setColor(BossbarColor color) {
        bukkitBossBar.setColor(color.getBukkitColor());
        return this;
    }

    @Override
    public BossbarColor getColor() {
        for (BossbarColor color : BossbarColor.values()) {
            if (color.getBukkitColor() == bukkitBossBar.getColor()) {
                return color;
            }
        }
        return BossbarColor.PURPLE;
    }

    @Override
    public PersonalBossbar setStyle(BossbarStyle style) {
        bukkitBossBar.setStyle(style.getBukkitStyle());
        return this;
    }

    @Override
    public BossbarStyle getStyle() {
        for (BossbarStyle style : BossbarStyle.values()) {
            if (style.getBukkitStyle() == bukkitBossBar.getStyle()) {
                return style;
            }
        }
        return BossbarStyle.SOLID;
    }

    @Override
    public PersonalBossbar addFlag(BossbarFlag flag) {
        bukkitBossBar.addFlag(flag.getBukkitFlag());
        return this;
    }

    @Override
    public PersonalBossbar removeFlag(BossbarFlag flag) {
        bukkitBossBar.removeFlag(flag.getBukkitFlag());
        return this;
    }

    @Override
    public boolean hasFlag(BossbarFlag flag) {
        return bukkitBossBar.hasFlag(flag.getBukkitFlag());
    }

    @Override
    public PersonalBossbar addPlayer(Player player) {
        bukkitBossBar.addPlayer(player);
        players.add(player);
        return this;
    }

    @Override
    public PersonalBossbar removePlayer(Player player) {
        bukkitBossBar.removePlayer(player);
        players.remove(player);
        return this;
    }

    @Override
    public Set<Player> getPlayers() {
        return new HashSet<>(players);
    }

    @Override
    public boolean isVisible(Player player) {
        return players.contains(player);
    }

    @Override
    public PersonalBossbar addAllPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            addPlayer(player);
        }
        return this;
    }

    @Override
    public PersonalBossbar removeAllPlayers() {
        for (Player player : new HashSet<>(players)) {
            removePlayer(player);
        }
        return this;
    }

    @Override
    public PersonalBossbar setVisible(boolean visible) {
        return visible ? addAllPlayers() : removeAllPlayers();
    }

    @Override
    public void remove() {
        removeAllPlayers();
        bukkitBossBar.setVisible(false);
    }
}
