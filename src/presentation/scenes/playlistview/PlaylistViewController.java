package presentation.scenes.playlistview;

import business.data.Playlist;
import business.data.Track;
import business.service.MP3Player;
import business.service.PlaylistManager;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import presentation.application.MainApp;
import presentation.scenes.playerview.PlayerViewController;
import javafx.scene.control.Button;

import java.util.ArrayList;

public class PlaylistViewController {

    private Button playlistAddButton;
    private Button closeButton;
    private MainApp application;
    private MP3Player player;
    private Playlist playlist;
    private Pane rootView;
    private VBox trackListContainer;
    public static ObservableList<Track> trackListModel = FXCollections.observableArrayList();


    private ListView<Track> trackListView;

    public PlaylistViewController(MainApp application, Playlist playlist, MP3Player player){
        this.application = application;
        this.playlist = playlist;
        this.player = player;

        PlaylistView mainView = new PlaylistView();

        this.playlistAddButton = mainView.playlistAddButton;
        this.closeButton = mainView.closeButton;
        this.trackListContainer = mainView.trackListContainer;
        this.trackListView = mainView.trackListView;

        rootView = mainView;
        initialize();
    }

    public void initialize(){

        trackListContainer.getStyleClass().add("container");
        VBox.setMargin(trackListView, new Insets(5,0,5,0));

        closeButton.setOnAction(event -> application.switchScene("PlayerView"));

        playlistAddButton.setOnAction(event -> {
            PlaylistManager.selectDirectory();
        });

        trackListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent click) {
                if(click.getClickCount() == 2){
                    Track selectedTrack = trackListView.getSelectionModel().getSelectedItem();
                    player.getTrackNumber(selectedTrack);

                    if(!player.isPlaying() && player.isFirstTrack()){
                        player.setPosition(0);
                        player.loadTrack();
                        player.play();
                    } else {
                        player.pause();
                        player.setPosition(0);
                        player.loadTrack();
                        player.play();
                    }
                }
            }
        });

        // set how a cell will look like
        trackListView.setCellFactory(new Callback<ListView<Track>, ListCell<Track>>() {
            @Override
            public ListCell<Track> call(ListView<Track> param) {
                return new TrackCell();
            }
        });

        // recognizing the selected track
        trackListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Track>() {
            @Override
            public void changed(ObservableValue<? extends Track> observable, Track oldTrack, Track newTrack) {
                System.out.println(newTrack);
/*                player.getTrackNumber(newTrack);
                player.loadTrack();*/
            }
        });

        ArrayList<Track> tracks = playlist.getTracks();
        System.out.println("+++ PlaylistViewController.initialize: tracks = " + tracks.toString());

        /* Set content to be displayed.
        *  ObservableList is a collection that is capable of notifying UI controls
        *  when objects are added, updated and removed.
        * */

        trackListModel = trackListView.getItems();
        System.out.println("+++ PlaylistViewController.initialize: trackListModel = trackListView.getItems() = " + trackListModel.toString());
        //trackListModel.clear();
        trackListModel.addAll(PlaylistManager.trackList);
        System.out.println("+++ PlaylistViewController.initialize: trackListModel.addAll(PlaylistManager.trackList) = " + trackListModel.toString());

        trackListModel.addListener(new ListChangeListener<Track>() {
            @Override
            public void onChanged(Change<? extends Track> c) {
                System.out.println("+++ PlaylistController.initialize: trackList changed!");
                player.pause();
                player.setPosition(0);
                player.setTrackNumber(0);
                player.loadTrack();
                player.setNumberOfTracks(playlist.numberOfTracks());
            }
        });

        trackListModel.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                System.out.println("+++ PlaylistViewController.initialize: List invalidated");
            }
        });

        PlaylistManager.tracksObservable.addListener(new ListChangeListener<Track>() {
            @Override
            public void onChanged(Change<? extends Track> c) {
                System.out.println("+++ PlaylistViewController.initialize: List changed");

            }
        });
    }

    public Pane getRootView() {
        return rootView;
    }

    public void setTrackListView(ListView<Track> trackListView) {
        this.trackListView = trackListView;
    }
}
