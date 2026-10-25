package kz.playlist.strategy;

import kz.playlist.Track;

import java.util.List;

/** Plays tracks exactly in the order they were added to the playlist. */
public class SequentialOrder implements PlaybackOrderStrategy {

    @Override
    public List<Track> arrange(List<Track> tracks) {
        return List.copyOf(tracks);
    }

    @Override
    public String name() {
        return "Sequential";
    }
}
