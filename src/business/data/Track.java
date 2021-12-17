package business.data;

import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicInteger;

public class Track {

    private int id;
    private String title;
    private int duration;
    private String albumTitle;
    private String artist;
    private String trackFilePath;
    private String coverFilePath;
    private byte[] albumImage;
    private static AtomicInteger ID_GENERATOR = new AtomicInteger(0);


    public byte[] getAlbumImage() {
        return albumImage;
    }

    public Track(String title, int duration, String albumTitle, String artist, String trackFilePath, byte[] albumImage) {
        this.id = ID_GENERATOR.getAndIncrement();
        this.title = title;
        this.duration = duration;
        this.albumTitle = albumTitle;
        this.artist = artist;
        this.trackFilePath = trackFilePath;
        this.coverFilePath = "/Users/jonporsche/Documents/Dev Projects.nosync/eibo_test1/src/presentation/assets/album_covers/" + artist + " - " + albumTitle + ".jpg";
        this.albumImage = albumImage;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration(){return duration;}

    public String getAlbumTitle() {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTrackFilePath() {
        return trackFilePath;
    }

    public void setTrackFilePath(String trackFilePath) {
        this.trackFilePath = trackFilePath;
    }

    public String getCoverFilePath() {
        return coverFilePath;
    }

    @Override
    public String toString() {
        return "Track{" +
                "id=" + id +
                '}';
    }
}
