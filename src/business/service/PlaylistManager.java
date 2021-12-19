package business.service;

import business.data.Playlist;
import business.data.Track;
import com.mpatric.mp3agic.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.DirectoryChooser;
import presentation.application.MainApp;
import presentation.scenes.playlistview.PlaylistViewController;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistManager {

    public static ArrayList<Track> trackList = new ArrayList<Track>();
    public static ObservableList<Track> tracksObservable;
    public static Playlist playlist;
    private static String directoryPath;

    public Playlist getPlaylistFromM3U(String sDir) throws IOException {

        File[] faFiles = new File(sDir).listFiles(); // load files of dir into array
        File m3uFile = new File("./playlists/playlist.m3u"); // create new M3U playlist file
        FileOutputStream fOutStream = new FileOutputStream(m3uFile); // output stream to write to the file
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fOutStream)); // output stream to write to the file

        String path;

        for (File file : faFiles) {
            if (file.getName().matches("^(.*?)")) {
                path = file.getAbsolutePath();
                bw.write(path);
                bw.newLine();
                trackList.add(loadTrackInfo(path));
            }
            if (file.isDirectory()) {
                getPlaylistFromM3U(file.getAbsolutePath());
            }
        }
        bw.close();

        playlist = new Playlist(trackList);
        tracksObservable = FXCollections.observableArrayList(trackList);

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
                duration = (int) mp3File.getLengthInSeconds() * 1000;
                albumTitle = id3v2Tag.getAlbum();
                albumImage = id3v2Tag.getAlbumImage();
            } else {
                System.out.println("The mp3 file does not exists or does not have a ID3v1Tag");
            }
        } catch (UnsupportedTagException | InvalidDataException | IOException e) {
            System.err.println("File not found.");
            e.printStackTrace();
        }
        return new Track(title, duration, albumTitle, artist, songFilePath, albumImage);
    }

    public static void selectDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("src"));
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                File selectedDirectory = directoryChooser.showDialog(null);
                directoryChooser.setTitle("Open mp3 folder");
                if(selectedDirectory != null) {

                    directoryPath = selectedDirectory.getAbsolutePath();
                    trackList.clear();
                    List<File> files = new ArrayList<>();
                    loadPlaylist(directoryPath, files);
                    playlist.setTracks(trackList);
                    playlist.numberOfTracks();
                    PlaylistViewController.trackListModel.clear();
                    PlaylistViewController.trackListModel.addAll(playlist.getTracks());
                }
            }
        });
    }

    public static void loadPlaylist(String directoryName, List<File> files) {

        File directory = new File(directoryName);
        MainApp.playlistPath = directoryName;

        // Get all files from a directory.
        File[] fList = directory.listFiles();
        String filePath;

        if (fList != null) {
            for (File file : fList) {
                if (file.isFile() && file.getName().matches(".*[0-9]+.*\\.(mp3)$")) {
                    files.add(file);
                    filePath = file.getAbsolutePath();
                    trackList.add(loadTrackInfo(filePath));
                } else if (file.isDirectory()) {
                    loadPlaylist(file.getAbsolutePath(), files);
                }
            }
        }
    }

    public static Playlist getPlaylist() {
        return playlist;
    }
}
