package business.service;

import business.data.Playlist;
import business.data.Track;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.*;
import java.util.ArrayList;

public class PlaylistManager {

    public ArrayList<Track> tracks = new ArrayList<Track>();
    public Playlist playlist;

    public Playlist getPlaylist(String sDir) throws IOException {

        File[] faFiles = new File(sDir).listFiles(); // load files of dir into array
        File m3uFile = new File("/Users/jonporsche/Documents/Dev Projects.nosync/eibo_test1/playlists/playlist.m3u"); // create new M3U playlist file
        FileOutputStream fOutStream = new FileOutputStream(m3uFile); // output stream to write to the file
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fOutStream)); // output stream to write to the file

        // TODO write file header

        String path;

        for (File file : faFiles) {
            if (file.getName().matches("^(.*?)")) {
                path = file.getAbsolutePath();
                bw.write(path);
                bw.newLine();
                tracks.add(loadTrackInfo(path));
            }
            if (file.isDirectory()) {
                getPlaylist(file.getAbsolutePath());
            }
        }
        bw.close();
        Playlist playlist = new Playlist(tracks);
        playlist.numberOfTracks();
        playlist.trackInfos();
        return playlist;
    }

    public Track loadTrackInfo(String songFilePath) {

        String title = null;
        long length = 0;
        String albumTitle = null;
        String artist = null;

        try {
            Mp3File mp3File = new Mp3File(songFilePath);
            if (mp3File.hasId3v1Tag()) {
                ID3v1 id3v1Tag = mp3File.getId3v1Tag();
                artist = id3v1Tag.getArtist();
                title = id3v1Tag.getTitle();
                length = mp3File.getLengthInSeconds();
                albumTitle = id3v1Tag.getAlbum();
            } else {
                System.out.println("The mp3 file does not exists or does not have a ID3v1Tag");
            }
        } catch (UnsupportedTagException | InvalidDataException | IOException e) {
            System.err.println("File not found.");
            e.printStackTrace();
        }
        return new Track(1, title, length, albumTitle, artist, songFilePath);
    }
}
