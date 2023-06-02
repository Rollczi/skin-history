package dev.rollczi.liteskinhistory.bukkit.scheduler;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public class SchedulerBukkitImpl implements Scheduler {

    private final Plugin plugin;
    private final BukkitScheduler root;

    public SchedulerBukkitImpl(Plugin plugin) {
        this.plugin = plugin;
        this.root = plugin.getServer().getScheduler();
    }

    @Override
    public void async(Runnable runnable) {
        root.runTaskAsynchronously(plugin, runnable);
    }

    @Override
    public void sync(Runnable runnable) {
        root.runTask(plugin, runnable);
    }

}
