# Assignment #5 — Strategy Pattern: Playlist Playback Order

A music playlist can be played in different orders: sequentially, shuffled,
"smart"-shuffled so the same artist never plays twice in a row, favourites first,
or shortest tracks first. Each order is a separate, interchangeable algorithm
selected at runtime — a natural fit for the **Strategy** pattern.

## Structure

| Pattern role        | Class                                                                 |
|---------------------|-----------------------------------------------------------------------|
| Strategy interface  | `PlaybackOrderStrategy` — `List<Track> arrange(List<Track> tracks)`    |
| Concrete strategies | `SequentialOrder`, `RandomShuffle`, `ArtistSpreadShuffle`, `MostPlayedFirst`, `ShortestFirst` |
| Context             | `Playlist` — holds a strategy, delegates ordering, `setOrderStrategy()` switches it at runtime |
| Client              | `Main` — creates strategies, selects one by mode name via a `Map` lookup |
| Domain object       | `Track` (record)                                                       |

```
           Main (Client)
             │ chooses & injects
             ▼
  Playlist (Context) ──has-a──► «interface» PlaybackOrderStrategy
                                   ▲   ▲   ▲   ▲   ▲
       SequentialOrder ────────────┘   │   │   │   └──── ShortestFirst
       RandomShuffle ──────────────────┘   │   └──────── MostPlayedFirst
       ArtistSpreadShuffle ────────────────┘
```

## Run

```bash
javac -d out $(find src -name "*.java")
java -cp out kz.playlist.Main               # demo of all modes
java -cp out kz.playlist.Main most-played   # pick a mode: sequential | shuffle | artist-spread | most-played | shortest
```

Or with Maven: `mvn compile exec:java -Dexec.args="shuffle"`, or just open the folder in IntelliJ IDEA and run `Main`.

## Clean Code principles applied

1. **Polymorphism instead of conditionals.** Neither `Playlist` nor `Main` contains
   `if`/`switch` on the strategy type. `Playlist.playbackQueue()` simply calls
   `orderStrategy.arrange(...)`; the client picks a strategy with
   `strategiesByMode.get(mode)`. A new mode never adds a new branch.

2. **Meaningful, intention-revealing names.** Every class says what algorithm it is:
   `MostPlayedFirst`, `ShortestFirst`, `ArtistSpreadShuffle`. Methods and variables
   describe intent too: `arrange`, `playbackQueue`, `setOrderStrategy`,
   `lastPlayedArtist`, `putBackIfHasTracks`, `MOST_REMAINING_FIRST`.

3. **Single Responsibility Principle.** Each strategy implements exactly one ordering
   algorithm and nothing else (no printing, no storage). `Playlist` only stores tracks
   and delegates; printing/formatting of a track lives in `Track.toString()`; wiring
   and selection live in the client.

4. **Context is algorithm-agnostic (Dependency Inversion).** `Playlist` depends only
   on the `PlaybackOrderStrategy` abstraction and imports no concrete strategy.
   It does not know whether the order is random, sorted, or grouped by artist.

5. **Open/Closed — easy extensibility.** `ShortestFirst` was added as a new class plus
   one `put(...)` line in the client's registry; no existing strategy or the context
   was modified.

6. **Small functions, one level of abstraction.** `ArtistSpreadShuffle.arrange()` reads
   as a short high-level algorithm; the details are extracted into
   `shuffledTracksPerArtist()` and `putBackIfHasTracks()`.

7. **No side effects / immutability.** Strategies receive an immutable copy and return
   a new list, so switching strategies never corrupts the playlist's original order.
   `Track` is an immutable `record`.

8. **Dependencies are injected, not hidden.** Random-based strategies receive a
   `Random` through the constructor, so the demo is reproducible (fixed seed) and the
   strategies are easy to test.

9. **Fail fast.** `setOrderStrategy` rejects `null`; an unknown mode name produces a
   clear `IllegalArgumentException` listing the available modes.
