package dev.ukry.gkits.utils.time;

public class TimeCounter {

    private long initTime = System.currentTimeMillis();

    public void update() {
        initTime = System.currentTimeMillis();
    }

    public String get() {
        return System.currentTimeMillis() - initTime + "ms";
    }

    @Override
    public String toString() {
        return get();
    }
}
