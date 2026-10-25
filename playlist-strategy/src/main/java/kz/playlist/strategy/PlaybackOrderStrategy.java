package kz.playlist.strategy;

import kz.playlist.Track;

import java.util.List;

/**
 * Strategy interface: decides in which order the tracks of a playlist are played.
 * Implementations must not modify the given list and must return a new one.
 */
public interface PlaybackOrderStrategy {

    List<Track> arrange(List<Track> tracks);

    String name();
}
