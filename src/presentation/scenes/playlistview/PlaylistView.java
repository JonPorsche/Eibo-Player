package presentation.scenes.playlistview;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PlaylistView extends VBox {

    // TOP CONTROLS
    HBox topControlsBox = new HBox();
    Button playlistAddButton = new Button();
    Button closeButton = new Button();

    public PlaylistView(){
        this.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        this.getStyleClass().add("container");

        topControls();

        this.getChildren().add(topControlsBox);
    }

    private void topControls() {
        // Style
        playlistAddButton.getStyleClass().addAll("small-button", "button-opacity");
        playlistAddButton.setId("playlist-add-btn");
        closeButton.getStyleClass().addAll("small-button", "button-opacity");
        closeButton.setId("close-btn");
        topControlsBox.setAlignment(Pos.CENTER_RIGHT);
        HBox.setMargin(closeButton, new Insets(0,0,0,8));

        // Add elements
        topControlsBox.getChildren().addAll(playlistAddButton, closeButton);
    }
}
