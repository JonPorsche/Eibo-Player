package business.service;

import business.data.Playlist;
import business.data.Track;
import com.mpatric.mp3agic.*;
import javafx.application.Platform;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.ArrayList;

public class PlaylistManager {

    public static final ArrayList<Track> trackList = new ArrayList<Track>();
    public static Playlist playlist;
    private static String directoryPath;

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
                trackList.add(loadTrackInfo(path));
            }
            if (file.isDirectory()) {
                getPlaylist(file.getAbsolutePath());
            }
        }
        bw.close();

        playlist = new Playlist(trackList);

        playlist.numberOfTracks();
        return playlist;
    }

    public static Playlist getPlaylistFromDirectory() {

        File[] faFiles = new File(directoryPath).listFiles(); // load files of dir into array

        String filePath;

        for (File file : faFiles) {
            if (file.getName().matches("^(.*?)")) {
                filePath = file.getAbsolutePath();
                trackList.add(loadTrackInfo(filePath));
            }
/*            if (file.isDirectory()) {
                getPlaylist(file.getAbsolutePath());
            }*/
        }

        playlist = new Playlist(trackList);

        playlist.numberOfTracks();
        return playlist;
    }

    private static Track loadTrackInfo(String songFilePath) {

        String title = null;
        int duration = 0;
        String albumTitle = null;
        String artist = null;
        byte[] albumImage = null;

        try {
            Mp3File mp3File = new Mp3File(songFilePath);
            if (mp3File.hasId3v2Tag()) {
                ID3v2 id3v2Tag = mp3File.getId3v2Tag();
                artist = id3v2Tag.getArtist();
                title = id3v2Tag.getTitle();
                duration = (int)mp3File.getLengthInSeconds() * 1000;
                albumTitle = id3v2Tag.getAlbum();
                albumImage = id3v2Tag.getAlbumImage();
            } else {
                System.out.println("The mp3 file does not exists or does not have a ID3v1Tag");
            }
        } catch (UnsupportedTagException | InvalidDataException | IOException e) {
            System.err.println("File not found.");
            e.printStackTrace();
        }
        return new Track(1, title, duration, albumTitle, artist, songFilePath, albumImage);
    }

    public void openFile(){
        FileChooser fileChooser = new FileChooser();
        Platform.runLater(()->fileChooser.showOpenDialog(null));
    }

    public static void selectDirectory(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("src"));
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                File selectedDirectory = directoryChooser.showDialog(null);
                directoryPath = selectedDirectory.getAbsolutePath();
                System.out.println(selectedDirectory.getAbsolutePath());
                getPlaylistFromDirectory();
            }
        });
    }
}
