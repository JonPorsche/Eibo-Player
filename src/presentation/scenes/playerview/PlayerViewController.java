package presentation.scenes.playerview;

import business.service.MP3Player;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class PlayerViewController {
    Label titleLabel;
    Label artistLabel;
    ImageView coverView;
    Button playButton;
    Pane rootView;
    MP3Player player;

    public PlayerViewController(MP3Player player) {

        this.player = player;

        PlayerView mainView = new PlayerView();

        this.titleLabel = mainView.titleLabel;
        this.artistLabel = mainView.artistLabel;
        this.coverView = mainView.coverView;
        this.playButton = mainView.playButton;

        rootView = mainView;

        initialize();
    }

    public void initialize(){
        playButton.setOnAction(event -> startPlayer());
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
}
