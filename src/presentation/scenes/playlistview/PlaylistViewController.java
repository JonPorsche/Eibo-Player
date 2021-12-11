package presentation.scenes.playlistview;

import business.data.Playlist;
import business.data.Track;
import business.service.MP3Player;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import presentation.application.MainApp;
import presentation.scenes.playerview.PlayerViewController;
import javafx.scene.control.Button;

import java.util.ArrayList;

public class PlaylistViewController {

    private Button closeButton;
    private MainApp application;
    private MP3Player player;
    private Playlist playlist;
    private Pane rootView;
    private VBox trackListContainer;
    private ObservableList<Track> trackListModel = FXCollections.observableArrayList();
    private ListView<Track> trackListView;

    public PlaylistViewController(MainApp application, Playlist playlist, MP3Player player){
        this.application = application;
        this.playlist = playlist;
        this.player = player;

        PlaylistView mainView = new PlaylistView();

        this.closeButton = mainView.closeButton;
        this.trackListContainer = mainView.trackListContainer;
        this.trackListView = mainView.trackListView;

        rootView = mainView;
        initialize();
    }

    public void initialize(){
        closeButton.setOnAction(event -> application.switchScene("PlayerView"));

        //trackListContainer.getChildren().add(trackListView);
        //rootView.getChildren().add(trackListView);
        trackListView.getStyleClass().add("container");
        trackListContainer.getStyleClass().add("container");

        trackListView.setCellFactory(new Callback<ListView<Track>, ListCell<Track>>() {
            @Override
            public ListCell<Track> call(ListView<Track> param) {
                return new TrackCell();
            }
        });

        trackListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Track>() {
            @Override
            public void changed(ObservableValue<? extends Track> observable, Track oldTrack, Track newTrack) {
                System.out.println(newTrack);
            }
        });

        trackListModel.clear();
        ArrayList<Track> tracks = playlist.getTracks();
        System.out.println(tracks.toString());
        trackListModel.setAll(tracks);
        trackListModel = trackListView.getItems();
    }

    public Pane getRootView() {
        return rootView;
    }
}
