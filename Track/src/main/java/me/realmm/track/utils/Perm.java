package me.realmm.track.utils;

public final class Perm {

    public static final String TRACK = init("use"); //Can use TrackCommand
    public static final String TEMP = init("temp"); //Can use TempTracker
    public static final String MAIN = init("main"); //Can use MainTracker

    private static String init(String s) {
        return "track." + s;
    }

}
