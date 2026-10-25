package kz.playlist.strategy;

import kz.playlist.Track;

import java.util.Comparator;
import java.util.List;

/**
 * Plays short tracks first — handy for a quick break.
 * Added after the other strategies without changing any existing class
 * (Open/Closed principle demonstration).
 */
public class ShortestFirst implements PlaybackOrderStrategy {

    @Override
    public List<Track> arrange(List<Track> tracks) {
        return tracks.stream()
                .sorted(Comparator.comparing(Track::length))
                .toList();
    }

    @Override
    public String name() {
        return "Shortest first";
    }
}
