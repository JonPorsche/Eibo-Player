package business.data;

import java.util.ArrayList;
import java.util.Date;

public class Playlist {

    private long id;
    private String title;
    private Date creationDate;
    private String coverFile;
    private ArrayList<Track> tracks;

    public Playlist(ArrayList<Track> tracks) {
        this.tracks = tracks;
    }

    public Playlist(long id, String title, Date creationDate, String coverFile) {
        this.id = id;
        this.title = title;
        this.creationDate = creationDate;
        this.coverFile = coverFile;
    }

    public int numberOfTracks(){
        return tracks.size();
    }

    public ArrayList<Track> getTracks() {
        return tracks;
    }
}
