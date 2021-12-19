package presentation.scenes.playlistview;

import business.data.Track;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import presentation.uicomponents.TopView;
import presentation.uicomponents.TopViewController;

public class PlaylistView extends BorderPane {

    VBox trackListContainer = new VBox();
    ListView<Track> trackListView;

    public PlaylistView(){
        this.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        this.getStyleClass().add("container");

        trackListView = new ListView<>();
        trackListContainer();
        this.setCenter(trackListView);
    }

/*    private void topControls() {
        // Style
        playlistAddButton.getStyleClass().addAll("small-button", "button");
        playlistAddButton.setId("playlist-add-btn");
        closeButton.getStyleClass().addAll("small-button", "button");
        closeButton.setId("close-btn");
        topControlsBox.setAlignment(Pos.CENTER_RIGHT);
        HBox.setMargin(closeButton, new Insets(0,0,0,8));

        // Add elements
        topControlsBox.getChildren().addAll(playlistAddButton, closeButton);
    }*/

    private void trackListContainer() {
        trackListView.getStyleClass().add("pretty-list-view");
        BorderPane.setMargin(trackListView, new Insets(6,0,0,0));
        trackListContainer.getChildren().add(trackListView);
    }
}
