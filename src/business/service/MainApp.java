package business.service;

import java.io.IOException;

public class MainApp {

    public static void main(String[] args) throws IOException {
        KeyboardController controller = new KeyboardController();

        PlaylistManager playlistManager = new PlaylistManager();
        playlistManager.getAllTracks("/Users/jonporsche/Documents/Dev Projects.nosync/eibo_test1/music");
    }
}
