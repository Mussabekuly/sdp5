package kz.playlist.strategy;

import kz.playlist.Track;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * "Smart" shuffle: tracks are shuffled, but the same artist is never played
 * twice in a row whenever that is possible. Each step takes a track from the
 * artist with the most remaining tracks, skipping the artist that just played.
 */
public class ArtistSpreadShuffle implements PlaybackOrderStrategy {

    private static final Comparator<Deque<Track>> MOST_REMAINING_FIRST =
            Comparator.comparingInt(Deque<Track>::size).reversed();

    private final Random random;

    public ArtistSpreadShuffle(Random random) {
        this.random = random;
    }

    @Override
    public List<Track> arrange(List<Track> tracks) {
        PriorityQueue<Deque<Track>> waitingArtists = new PriorityQueue<>(MOST_REMAINING_FIRST);
        waitingArtists.addAll(shuffledTracksPerArtist(tracks));

        List<Track> result = new ArrayList<>(tracks.size());
        Deque<Track> lastPlayedArtist = new ArrayDeque<>();

        while (!waitingArtists.isEmpty()) {
            Deque<Track> nextArtist = waitingArtists.poll();
            result.add(nextArtist.poll());
            putBackIfHasTracks(waitingArtists, lastPlayedArtist);
            lastPlayedArtist = nextArtist;
        }
        // Only one artist can be left here, so their tracks inevitably play back to back.
        result.addAll(lastPlayedArtist);
        return result;
    }

    private Collection<Deque<Track>> shuffledTracksPerArtist(List<Track> tracks) {
        List<Track> shuffled = new ArrayList<>(tracks);
        Collections.shuffle(shuffled, random);
        return shuffled.stream()
                .collect(Collectors.groupingBy(Track::artist,
                        Collectors.toCollection(() -> (Deque<Track>) new ArrayDeque<Track>())))
                .values();
    }

    private static void putBackIfHasTracks(PriorityQueue<Deque<Track>> queue, Deque<Track> artistTracks) {
        if (!artistTracks.isEmpty()) {
            queue.add(artistTracks);
        }
    }

    @Override
    public String name() {
        return "Artist spread shuffle";
    }
}
