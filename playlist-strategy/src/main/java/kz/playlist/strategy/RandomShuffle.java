package kz.playlist.strategy;

import kz.playlist.Track;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/** Plays tracks in a uniformly random order (Fisher–Yates shuffle). */
public class RandomShuffle implements PlaybackOrderStrategy {

    private final Random random;

    public RandomShuffle(Random random) {
        this.random = random;
    }

    @Override
    public List<Track> arrange(List<Track> tracks) {
        List<Track> shuffled = new ArrayList<>(tracks);
        Collections.shuffle(shuffled, random);
        return shuffled;
    }

    @Override
    public String name() {
        return "Random shuffle";
    }
}
