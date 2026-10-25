package kz.playlist;

import kz.playlist.strategy.ArtistSpreadShuffle;
import kz.playlist.strategy.MostPlayedFirst;
import kz.playlist.strategy.PlaybackOrderStrategy;
import kz.playlist.strategy.RandomShuffle;
import kz.playlist.strategy.SequentialOrder;
import kz.playlist.strategy.ShortestFirst;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

/**
 * Client: builds the strategies, picks one by the mode the user asked for
 * and switches the playlist between them at runtime.
 * Strategy selection is a map lookup — no if/switch on the strategy type.
 */
public class Main {

    private static final long DEMO_SEED = 42;

    public static void main(String[] args) {
        Map<String, PlaybackOrderStrategy> strategiesByMode = availableStrategies(new Random(DEMO_SEED));

        Playlist playlist = new Playlist("Road trip", strategiesByMode.get("sequential"));
        addDemoTracks(playlist);

        for (PlaybackOrderStrategy strategy : strategiesByMode.values()) {
            playlist.setOrderStrategy(strategy);
            playlist.printPlaybackQueue();
        }

        String requestedMode = args.length > 0 ? args[0] : "artist-spread";
        playlist.setOrderStrategy(strategyFor(requestedMode, strategiesByMode));
        System.out.printf("%nUser requested mode \"%s\":", requestedMode);
        playlist.printPlaybackQueue();
    }

    private static Map<String, PlaybackOrderStrategy> availableStrategies(Random random) {
        Map<String, PlaybackOrderStrategy> strategies = new LinkedHashMap<>();
        strategies.put("sequential", new SequentialOrder());
        strategies.put("shuffle", new RandomShuffle(random));
        strategies.put("artist-spread", new ArtistSpreadShuffle(random));
        strategies.put("most-played", new MostPlayedFirst());
        strategies.put("shortest", new ShortestFirst());
        return strategies;
    }

    private static PlaybackOrderStrategy strategyFor(String mode, Map<String, PlaybackOrderStrategy> strategies) {
        PlaybackOrderStrategy strategy = strategies.get(mode);
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown mode '" + mode + "'. Available: " + strategies.keySet());
        }
        return strategy;
    }

    private static void addDemoTracks(Playlist playlist) {
        playlist.add(new Track("Bohemian Rhapsody", "Queen", Duration.ofSeconds(354), 120));
        playlist.add(new Track("Don't Stop Me Now", "Queen", Duration.ofSeconds(209), 95));
        playlist.add(new Track("Under Pressure", "Queen", Duration.ofSeconds(248), 40));
        playlist.add(new Track("Numb", "Linkin Park", Duration.ofSeconds(187), 150));
        playlist.add(new Track("In the End", "Linkin Park", Duration.ofSeconds(216), 88));
        playlist.add(new Track("Blinding Lights", "The Weeknd", Duration.ofSeconds(200), 210));
        playlist.add(new Track("Save Your Tears", "The Weeknd", Duration.ofSeconds(215), 33));
        playlist.add(new Track("Shape of You", "Ed Sheeran", Duration.ofSeconds(233), 64));
    }
}
