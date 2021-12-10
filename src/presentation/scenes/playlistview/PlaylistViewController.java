package presentation.scenes.playlistview;

import business.data.Playlist;
import business.service.MP3Player;
import javafx.scene.layout.Pane;
import presentation.application.MainApp;
import presentation.scenes.playerview.PlayerViewController;
import javafx.scene.control.Button;

public class PlaylistViewController {

    private Button closeButton;
    private MainApp application;
    private MP3Player player;
    private Pane rootView;

    public PlaylistViewController(MainApp application, MP3Player player){
        this.application = application;
        this.player = player;

        PlaylistView mainView = new PlaylistView();

        this.closeButton = mainView.closeButton;

        rootView = mainView;
        initialize();
    }

    public void initialize(){
        closeButton.setOnAction(event -> application.switchScene("PlayerView"));
    }

    public Pane getRootView() {
        return rootView;
    }
}
