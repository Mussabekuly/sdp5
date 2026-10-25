package kz.playlist;

import kz.playlist.strategy.PlaybackOrderStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Context: stores the tracks and delegates the ordering to the current strategy.
 * It knows nothing about how any concrete strategy works.
 */
public class Playlist {

    private final String name;
    private final List<Track> tracks = new ArrayList<>();
    private PlaybackOrderStrategy orderStrategy;

    public Playlist(String name, PlaybackOrderStrategy initialStrategy) {
        this.name = name;
        setOrderStrategy(initialStrategy);
    }

    public void setOrderStrategy(PlaybackOrderStrategy orderStrategy) {
        this.orderStrategy = Objects.requireNonNull(orderStrategy, "orderStrategy");
    }

    public void add(Track track) {
        tracks.add(track);
    }

    public List<Track> playbackQueue() {
        return orderStrategy.arrange(List.copyOf(tracks));
    }

    public void printPlaybackQueue() {
        System.out.printf("%n=== %s | mode: %s ===%n", name, orderStrategy.name());
        List<Track> queue = playbackQueue();
        for (int position = 1; position <= queue.size(); position++) {
            System.out.printf("%2d. %s%n", position, queue.get(position - 1));
        }
    }
}
