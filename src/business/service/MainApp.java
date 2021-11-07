package business.service;

import business.data.Playlist;

import java.io.IOException;

public class MainApp {

    public static void main(String[] args) throws IOException {

        new Thread(() -> {
            try {
                PlaylistManager playlistManager = new PlaylistManager();
                playlistManager.getAllTracks("/Users/jonporsche/Documents/Dev Projects.nosync/eibo_test1/music");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        KeyboardController controller = new KeyboardController();

    }
}
