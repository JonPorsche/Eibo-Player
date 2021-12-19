package presentation.scenes.topview;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class TopView extends HBox {

    Button playlistAddButton = new Button();
    Button playlistButton = new Button();

    public TopView() {
        topViewStyle();
        this.getChildren().addAll(playlistAddButton, playlistButton);
    }

    private void topViewStyle() {
        this.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        this.getStyleClass().addAll("container", "bkg");
        this.setAlignment(Pos.CENTER_RIGHT);

        playlistAddButton.getStyleClass().addAll("small-button", "button");
        playlistAddButton.setId("playlist-add-btn");

        playlistButton.getStyleClass().addAll("small-button", "button", "playlist-btn");
        HBox.setMargin(playlistButton, new Insets(0, 0, 0, 8));
    }
}