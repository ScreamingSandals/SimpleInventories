package org.screamingsandals.simpleinventories.tasks;

public interface RepeatingTask extends Task {
    long getPeriod();

    void setPeriod(long period);
}
