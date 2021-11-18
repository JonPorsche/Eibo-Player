package presentation.scenes.playerview;

import business.service.MP3Player;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class PlayerViewController {
    Label titleLabel;
    Label artistLabel;
    ImageView coverView;
    Button playButton;
    private PlayerView rootView;
    MP3Player player;

    public PlayerViewController() {
        this.rootView = new PlayerView();
        this.player = new MP3Player();
        this.titleLabel = rootView.titleLabel;
        this.artistLabel = rootView.artistLabel;
        this.coverView = rootView.coverView;
        this.playButton = rootView.playButton;
        initialize();
    }

    public void initialize(){
        playButton.addEventHandler(ActionEvent.ACTION,
                event -> player.play()
        );
    }

    public PlayerView getRootView() {
        return rootView;
    }
}
