package dev.ukry.gkits.utils.time;

public class TimeFormat {

    public static String DD(long seconds) {
        return String.valueOf(seconds / 86400);
    }

    public static String HH(long seconds) {
        return String.valueOf((seconds % 86400) / 3600);
    }

    public static String HH1(long seconds) {
        return String.valueOf(seconds / 3600);
    }

    public static String MM(long seconds) {
        return String.valueOf((seconds % 3600) / 60);
    }

    public static String MM1(long seconds) {
        return String.valueOf(seconds / 60);
    }

    public static String SS(long seconds) {
        return String.valueOf(seconds % 60);
    }
}