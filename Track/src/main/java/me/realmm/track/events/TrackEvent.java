package me.realmm.track.events;

import me.realmm.track.tracker.Tracker;
import net.jamesandrew.realmlib.listener.RealmEvent;
import org.bukkit.event.Cancellable;

public class TrackEvent extends RealmEvent implements Cancellable {

    private final Tracker tracker;
    private boolean cancelled = false;

    public TrackEvent(Tracker tracker) {
        this.tracker = tracker;
    }

    public Tracker getTracker() {
        return tracker;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
