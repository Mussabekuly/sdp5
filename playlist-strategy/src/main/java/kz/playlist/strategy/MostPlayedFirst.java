package kz.playlist.strategy;

import kz.playlist.Track;

import java.util.Comparator;
import java.util.List;

/** Plays the user's favourite tracks (highest play count) first. */
public class MostPlayedFirst implements PlaybackOrderStrategy {

    @Override
    public List<Track> arrange(List<Track> tracks) {
        return tracks.stream()
                .sorted(Comparator.comparingInt(Track::playCount).reversed())
                .toList();
    }

    @Override
    public String name() {
        return "Most played first";
    }
}
