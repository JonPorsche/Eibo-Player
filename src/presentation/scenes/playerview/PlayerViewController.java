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

    private class PlayEventHandler implements EventHandler<ActionEvent> {

        boolean isPlaying;

        @Override
        public void handle(ActionEvent event) {
            if (!isPlaying) {
                isPlaying = true;
                player.play();
            } else {
                isPlaying = false;
                player.pause();
            }
        }

    }

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
        playButton.addEventHandler(ActionEvent.ACTION,
                event -> player.play()
        );
    }

    public Pane getRootView() {
        return rootView;
    }
}
