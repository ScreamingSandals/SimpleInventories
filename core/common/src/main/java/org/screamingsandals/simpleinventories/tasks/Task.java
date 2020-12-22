package org.screamingsandals.simpleinventories.tasks;

public interface Task {
    void start();

    long getDelay();

    void setDelay(long delay);

    void cancel();

    boolean isCancelled();
}
