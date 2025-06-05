package com.mimo.api.bossbar;

import com.mimo.api.AbstractPersonalBossbar;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class ProgressBossbar extends AbstractPersonalBossbar {

    private final Plugin plugin;
    private int taskId = -1;
    private long totalTime;
    private long remainingTime;
    private Runnable completionAction;

    public ProgressBossbar(Plugin plugin, String title, BossbarColor color, BossbarStyle style, long duration) {
        super(title, color, style);
        this.plugin = plugin;
        this.totalTime = duration * 20L;
        this.remainingTime = this.totalTime;
        setProgress(1.0);
        startProgress();
    }

    public ProgressBossbar(Plugin plugin, String title, long duration) {
        this(plugin, title, BossbarColor.GREEN, BossbarStyle.SOLID, duration);
    }

    private void startProgress() {
        if (taskId != -1) {
            Bukkit.getScheduler().cancelTask(taskId);
        }

        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (remainingTime <= 0) {
                stopProgress();
                if (completionAction != null) {
                    completionAction.run();
                }
                return;
            }

            remainingTime--;
            double progress = (double) remainingTime / totalTime;
            setProgress(progress);
        }, 1L, 1L);
    }

    public void stopProgress() {
        if (taskId != -1) {
            Bukkit.getScheduler().cancelTask(taskId);
            taskId = -1;
        }
    }

    public ProgressBossbar onComplete(Runnable action) {
        this.completionAction = action;
        return this;
    }

    public ProgressBossbar reset() {
        stopProgress();
        this.remainingTime = this.totalTime;
        setProgress(1.0);
        startProgress();
        return this;
    }

    public ProgressBossbar setDuration(long duration) {
        stopProgress();
        this.totalTime = duration * 20L;
        this.remainingTime = this.totalTime;
        setProgress(1.0);
        startProgress();
        return this;
    }

    @Override
    public void remove() {
        stopProgress();
        super.remove();
    }
}
