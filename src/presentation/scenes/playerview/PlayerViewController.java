package presentation.scenes.playerview;

import business.data.Playlist;
import business.service.MP3Player;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class PlayerViewController {
    Label titleLabel;
    Label artistLabel;
    ImageView coverView;
    Button playButton;
    Button skipNextButton;
    Pane rootView;
    MP3Player player;

    public PlayerViewController(MP3Player player) {

        this.player = player;

        PlayerView mainView = new PlayerView();

        this.titleLabel = mainView.titleLabel;
        this.artistLabel = mainView.artistLabel;
        this.coverView = mainView.coverView;
        this.playButton = mainView.playButton;
        this.skipNextButton = mainView.skipNextButton;

        rootView = mainView;

        initialize();
    }

    public void initialize(){
        playButton.setOnAction(event -> startPlayer());
        skipNextButton.setOnAction(event -> skipNext());
    }

    public Pane getRootView() {
        return rootView;
    }

    private void startPlayer(){
        if (player.isPlaying) {
            playButton.setId("play-btn");
            player.pause();
        } else {
            playButton.setId("pause-btn");
            player.play();
        }
    }

    private void skipNext() {
        player.skipNext();
    }
}
