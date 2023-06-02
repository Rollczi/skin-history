package dev.rollczi.liteskinhistory.bukkit.scheduler;

public interface Scheduler {

    void async(Runnable runnable);

    void sync(Runnable runnable);

}
