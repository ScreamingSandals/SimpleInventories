package org.screamingsandals.simpleinventories.bukkit.tasks;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.Bukkit;
import org.screamingsandals.simpleinventories.bukkit.SimpleInventoriesBukkit;
import org.screamingsandals.simpleinventories.tasks.RepeatingTask;

@Data
@EqualsAndHashCode(callSuper = true)
public class BukkitRepeatingTask extends BukkitTaskImpl implements RepeatingTask {
    private long period = 20L;

    @Override
    public void start() {
        if (bukkitTask == null) {
            bukkitTask = Bukkit.getScheduler().runTaskTimer(SimpleInventoriesBukkit.getPlugin(), task, delay, period);
        }
    }
}
