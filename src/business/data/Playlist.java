package business.data;

import java.util.ArrayList;

public class Playlist {

    private ArrayList<Track> tracks;

    public Playlist(ArrayList<Track> tracks) {
        this.tracks = tracks;
    }

    public int numberOfTracks(){
        return tracks.size();
    }

    public ArrayList<Track> getTracks() {
        return tracks;
    }

    public void setTracks(ArrayList<Track> tracks) {
        this.tracks = tracks;
    }
}
