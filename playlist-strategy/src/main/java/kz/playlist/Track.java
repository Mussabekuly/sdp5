package kz.playlist;

import java.time.Duration;

public record Track(String title, String artist, Duration length, int playCount) {

    @Override
    public String toString() {
        return String.format("%-22s %-16s %d:%02d  plays: %d",
                title, artist, length.toMinutes(), length.toSecondsPart(), playCount);
    }
}
