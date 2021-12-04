package business.data;

public class Track {

    private long id;
    private String title;
    private long length;
    private String albumTitle;
    private String artist;
    private String songFilePath;

    public Track(long id, String title, long length, String albumTitle, String artist, String songFilePath) {
        this.id = id;
        this.title = title;
        this.length = length;
        this.albumTitle = albumTitle;
        this.artist = artist;
        this.songFilePath = songFilePath;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

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

    public String getSongFilePath() {
        return songFilePath;
    }

    public void setSongFilePath(String songFilePath) {
        this.songFilePath = songFilePath;
    }
}
