package me.realmm.track.tracker;

import net.md_5.bungee.api.chat.BaseComponent;

public class TrackMessage {

    private final Tracker tracker;
    private final BaseComponent[] components;
    private final boolean cancelled;

    public TrackMessage(Tracker tracker, BaseComponent[] components, boolean cancelled) {
        this.tracker = tracker;
        this.components = components;
        this.cancelled = cancelled;
    }

    public Tracker getTracker() {
        return tracker;
    }

    public BaseComponent[] getMessage() {
        return components;
    }

    public boolean isCancelled() {
        return cancelled;
    }



}
