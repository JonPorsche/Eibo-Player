package business.service;

import business.data.Playlist;
import business.data.Track;
import com.mpatric.mp3agic.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import presentation.scenes.playlistview.PlaylistViewController;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.lang.Object.*;
import java.util.List;

public class PlaylistManager {

    public static ArrayList<Track> trackList = new ArrayList<Track>();
    public static ObservableList<Track> tracksObservable;
    public static Playlist playlist;
    private static String directoryPath;

    public Playlist getPlaylistFromM3U(String sDir) throws IOException {

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
                getPlaylistFromM3U(file.getAbsolutePath());
            }
        }
        bw.close();

        playlist = new Playlist(trackList);
        tracksObservable = FXCollections.observableArrayList(trackList);


        playlist.numberOfTracks();
        return playlist;
    }

    public static Playlist getPlaylistFromDirectory() {

        System.out.println("+++ PlaylistManager.getPlaylistFromDirectory: trackList = " + trackList.toString());
        trackList.clear();
        System.out.println("+++ PlaylistManager.getPlaylistFromDirectory: trackList = " + trackList.toString());

        File[] faFiles = new File(directoryPath).listFiles(); // load files of dir into array

        String filePath;

        for (File file : faFiles) {
            if (file.getName().matches(".*[0-9]+.*\\.(mp3)$")) {
                filePath = file.getAbsolutePath();
                trackList.add(loadTrackInfo(filePath));
            }
/*            if (file.isDirectory()) {
                getPlaylist(file.getAbsolutePath());
            }*/
        }
        System.out.println("+++ PlaylistManager.getPlaylistFromDirectory: trackList = " + trackList.toString());
        playlist.setTracks(trackList);
        tracksObservable = FXCollections.observableArrayList(trackList);

        playlist.numberOfTracks();
        return playlist;
    }

    public void listf(String directoryName, List<File> files) {
        File directory = new File(directoryName);
        //System.out.println("+++ PlaylistManager.listf: directory = " + directoryName);

        // Get all files from a directory.
        File[] fList = directory.listFiles();
        //System.out.println("+++ PlaylistManager.listf: fList: = " + fList.toString());

        if (fList != null) {
            //System.out.println("+++ PlaylistManager.listf: fList != null");

            for (File file : fList) {
                if (file.isFile() && file.getName().matches(".*[0-9]+.*\\.(mp3)$")) {
                    files.add(file);
                } else if (file.isDirectory()) {
                    listf(file.getAbsolutePath(), files);
                }
            }
        }
        System.out.println("+++ PlaylistManager.listf: The direcotry contains " + files.size() + " mp3 files");
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
        return new Track(1, title, duration, albumTitle, artist, songFilePath, albumImage);
    }

    public void openFile() {
        FileChooser fileChooser = new FileChooser();
        Platform.runLater(() -> fileChooser.showOpenDialog(null));
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
                    System.out.println("Directory choosed");

                    directoryPath = selectedDirectory.getAbsolutePath();
                    System.out.println("+++ PlaylistManager.selectDirectory: directoryPath = " + selectedDirectory.getAbsolutePath());
                    trackList.clear();
                    List<File> files = new ArrayList<>();
                    listf2(directoryPath, files);
                    System.out.println("+++ PlaylistManager.selectDirectory: trackList = " + trackList.toString());
                    playlist.setTracks(trackList);
                    playlist.numberOfTracks();
                    PlaylistViewController.trackListModel.clear();
                    PlaylistViewController.trackListModel.addAll(trackList);
                    System.out.println("+++ PlaylistManager.selectDirectory: trackList = " + trackList.toString());
                }
            }

        });
    }

    public static Playlist getPlaylistFromDirectory2() {

        System.out.println("+++ PlaylistManager.getPlaylistFromDirectory: trackList = " + trackList.toString());
        trackList.clear();
        System.out.println("+++ PlaylistManager.getPlaylistFromDirectory: trackList = " + trackList.toString());

        File[] faFiles = new File(directoryPath).listFiles(); // load files of dir into array

        String filePath;

        for (File file : faFiles) {
            if (file.getName().matches(".*[0-9]+.*\\.(mp3)$")) {
                filePath = file.getAbsolutePath();
                trackList.add(loadTrackInfo(filePath));
            }
/*            if (file.isDirectory()) {
                getPlaylist(file.getAbsolutePath());
            }*/
        }
        System.out.println("+++ PlaylistManager.getPlaylistFromDirectory: trackList = " + trackList.toString());
        playlist.setTracks(trackList);
        tracksObservable = FXCollections.observableArrayList(trackList);

        playlist.numberOfTracks();
        return playlist;
    }

    public static void listf2(String directoryName, List<File> files) {

        File directory = new File(directoryName);

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
                    listf2(file.getAbsolutePath(), files);
                }
            }
        }
        System.out.println("+++ PlaylistManager.listf2: The direcotry contains " + files.size() + " mp3 files");
    }

    public static Playlist getPlaylist() {
        return playlist;
    }
}
