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

    public void numberOfTracks(){
        System.out.println("Number of tracks = " + tracks.size());
    }

    public void trackInfos(){
        System.out.println("\nPlaylist:");
        for(int i=0; i < tracks.size(); i++) {
            System.out.println(tracks.get(i).getArtist() + " - " + tracks.get(i).getTitle() + " - " + tracks.get(i).getSoundFile());
        }
        System.out.println("\n");
    }
}
