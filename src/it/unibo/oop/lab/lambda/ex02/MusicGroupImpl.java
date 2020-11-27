package it.unibo.oop.lab.lambda.ex02;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.stream.Stream;

/**
 *
 */
public final class MusicGroupImpl implements MusicGroup {

    private final Map<String, Integer> albums = new HashMap<>();
    private final Set<Song> songs = new HashSet<>();

    @Override
    public void addAlbum(final String albumName, final int year) {
        this.albums.put(albumName, year);
    }

    @Override
    public void addSong(final String songName, final Optional<String> albumName, final double duration) {
        if (albumName.isPresent() && !this.albums.containsKey(albumName.get())) {
            throw new IllegalArgumentException("invalid album name");
        }
        this.songs.add(new MusicGroupImpl.Song(songName, albumName, duration));
    }

    @Override
    public Stream<String> orderedSongNames() {
        final Stream<String> stream1 = 
                this.songs.stream()
                .map(song -> song.getSongName())
                .sorted();
        return stream1;
    }

    @Override
    public Stream<String> albumNames() {
        final Stream<String> stream2 = 
                this.albums.keySet().stream();
        return stream2;
    }

    @Override
    public Stream<String> albumInYear(final int year) {
        final Stream<String> stream3 =
                this.albums.entrySet().stream()
                .filter(ele -> ele.getValue().equals(year))
                .map(ele -> ele.getKey());
        return stream3;
    }

    @Override
    public int countSongs(final String albumName) {
        final int stream4 = 
                (int) this.songs.stream()
                .filter(song -> song.getAlbumName().isPresent())
                .filter(song -> song.getAlbumName().get().equals(albumName))
                .count();
        return stream4;
    }

    @Override
    public int countSongsInNoAlbum() {
        final int stream5 = 
                (int) this.songs.stream()
                .filter(song -> song.getAlbumName().isEmpty())
                .count();
        return stream5;
    }

    @Override
    public OptionalDouble averageDurationOfSongs(final String albumName) {
        final OptionalDouble stream6 = 
                this.songs.stream()
                .filter(song -> song.getAlbumName().isPresent())
                .filter(song -> song.getAlbumName().get().equals(albumName))
                .mapToDouble(song -> song.getDuration())
                .average();
        return stream6;

    }

    @Override
    public Optional<String> longestSong() {
        final Optional<String> stream7 =
                this.songs.stream()
                .max((song1, song2) -> (Double.compare(song1.getDuration(), song2.getDuration())))
                .map(d -> d.getSongName());
        return stream7;


    }

    @Override


    public Optional<String> longestAlbum() {
        Map<String, Double> mappa = new HashMap<>();
        for (String a : albums.keySet()) {
            mappa.put(a, songs.stream()
                              .filter(song -> song.getAlbumName().isPresent())
                              .filter(song -> song.getAlbumName().get().equals(a))
                              .mapToDouble(s -> s.getDuration()).sum());
            }
        return mappa.entrySet().stream()
                               .max((s, t) -> Double.compare(s.getValue(), t.getValue()))
                               .map(ele -> ele.getKey());
    }

    private static final class Song {

        private final String songName;
        private final Optional<String> albumName;
        private final double duration;
        private int hash;

        Song(final String name, final Optional<String> album, final double len) {
            super();
            this.songName = name;
            this.albumName = album;
            this.duration = len;
        }

        public String getSongName() {
            return songName;
        }

        public Optional<String> getAlbumName() {
            return albumName;
        }

        public double getDuration() {
            return duration;
        }

        @Override
        public int hashCode() {
            if (hash == 0) {
                hash = songName.hashCode() ^ albumName.hashCode() ^ Double.hashCode(duration);
            }
            return hash;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj instanceof Song) {
                final Song other = (Song) obj;
                return albumName.equals(other.albumName) && songName.equals(other.songName)
                        && duration == other.duration;
            }
            return false;
        }

        @Override
        public String toString() {
            return "Song [songName=" + songName + ", albumName=" + albumName + ", duration=" + duration + "]";
        }

    }

}
