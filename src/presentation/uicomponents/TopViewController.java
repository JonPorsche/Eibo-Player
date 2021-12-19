package presentation.uicomponents;

import business.service.PlaylistManager;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import presentation.application.MainApp;

public class TopViewController {

    private Button playlistAddButton;
    private Button playlistButton;
    private boolean playlistViewActive;
    private MainApp application;
    private Pane rootView;

    public TopViewController(MainApp application) {
        this.application = application;

        TopView topView = new TopView();
        this.playlistAddButton = topView.playlistAddButton;
        this.playlistButton = topView.playlistButton;
        this.playlistViewActive = false;

        rootView = topView;
        initialize();
    }

    public void initialize() {
        handlePlaylistAddBtnClick();
        handlePlaylistBtnClick();
    }

    private void handlePlaylistAddBtnClick() {
        playlistAddButton.setOnAction(event -> PlaylistManager.selectDirectory());
    }

    private void handlePlaylistBtnClick() {
        System.out.println("playlistViewActive = " + playlistViewActive);
        playlistButton.setOnAction(event -> {
            if (playlistViewActive) {
                application.switchCenterPane("PlayerView");
                playlistButton.getStyleClass().remove("close-btn");
                playlistButton.getStyleClass().add("playlist-btn");
                playlistViewActive = false;
            } else {
                application.switchCenterPane("PlaylistView");
                playlistButton.getStyleClass().remove("playlist-btn");
                playlistButton.getStyleClass().add("close-btn");
                playlistViewActive = true;
            }
        });
    }

    public Pane getRootView() {
        return rootView;
    }
}
