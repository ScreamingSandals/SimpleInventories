package org.screamingsandals.simpleinventories.bukkit.tasks;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.screamingsandals.simpleinventories.bukkit.SimpleInventoriesBukkit;
import org.screamingsandals.simpleinventories.tasks.Task;

@Data
@RequiredArgsConstructor
public class BukkitTaskImpl implements Task {
    protected Runnable task;
    protected long delay = 0L;
    @Setter(AccessLevel.NONE)
    protected BukkitTask bukkitTask;

    @Override
    public void start() {
        if (bukkitTask == null) {
            bukkitTask = Bukkit.getScheduler().runTaskLater(SimpleInventoriesBukkit.getPlugin(), task, delay);
        }
    }

    @Override
    public void cancel() {
        if (bukkitTask != null) {
            if (!bukkitTask.isCancelled()) {
                bukkitTask.cancel();
                bukkitTask = null;
            }
        }
    }

    @Override
    public boolean isCancelled() {
        return bukkitTask != null && bukkitTask.isCancelled();
    }
}
