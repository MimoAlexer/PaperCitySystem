package com.mimo.api.bossbar;

import com.mimo.api.AbstractPersonalBossbar;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class TimedBossbar extends AbstractPersonalBossbar {

    private final Plugin plugin;
    private int taskId = -1;

    public TimedBossbar(Plugin plugin, String title, BossbarColor color, BossbarStyle style, long duration) {
        super(title, color, style);
        this.plugin = plugin;
        startTimer(duration);
    }

    public TimedBossbar(Plugin plugin, String title, long duration) {
        super(title);
        this.plugin = plugin;
        startTimer(duration);
    }

    private void startTimer(long duration) {
        if (taskId != -1) {
            Bukkit.getScheduler().cancelTask(taskId);
        }

        taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this::remove, duration * 20L);
    }

    public void cancelTimer() {
        if (taskId != -1) {
            Bukkit.getScheduler().cancelTask(taskId);
            taskId = -1;
        }
    }

    @Override
    public void remove() {
        cancelTimer();
        super.remove();
    }
}
