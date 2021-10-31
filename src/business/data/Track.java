package business.data;

public class Track {

    private long id;
    private String title;
    private int length;
    private String albumTitle;
    private String interpret;
    private String soundFile;

    public Track(long id, String title, int length, String albumTitle, String interpret, String soundFile) {
        this.id = id;
        this.title = title;
        this.length = length;
        this.albumTitle = albumTitle;
        this.interpret = interpret;
        this.soundFile = soundFile;
    }
}
