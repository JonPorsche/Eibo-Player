package presentation.scenes.playlistview;

import business.data.Track;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PlaylistView extends BorderPane {

    // TOP CONTROLS
    HBox topControlsBox = new HBox();
    Button playlistAddButton = new Button();
    Button closeButton = new Button();

    // LIST VIEW
    VBox trackListContainer = new VBox();
    ListView<Track> trackListView = new ListView<Track>();

    public PlaylistView(){
        this.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        this.getStyleClass().add("container");

        topControls();
        trackListContainer();

        this.setTop(topControlsBox);
        this.setCenter(trackListView);
    }

    private void topControls() {
        // Style
        playlistAddButton.getStyleClass().addAll("small-button", "button");
        playlistAddButton.setId("playlist-add-btn");
        closeButton.getStyleClass().addAll("small-button", "button");
        closeButton.setId("close-btn");
        topControlsBox.setAlignment(Pos.CENTER_RIGHT);
        HBox.setMargin(closeButton, new Insets(0,0,0,8));

        // Add elements
        topControlsBox.getChildren().addAll(playlistAddButton, closeButton);
    }

    private void trackListContainer() {
        trackListContainer.getStyleClass().addAll("border-to-test");
        trackListView.getStyleClass().add("border-to-test-orange");
        VBox.setMargin(trackListContainer, new Insets(8,0,0,0));

        trackListContainer.getChildren().add(trackListView);
    }
}
