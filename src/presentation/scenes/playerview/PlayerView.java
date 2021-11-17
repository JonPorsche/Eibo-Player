package presentation.scenes.playerview;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import presentation.uicomponents.ImageViewPane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class PlayerView extends BorderPane {

    private Label titleLabel;
    private Label artistLabel;
    private ImageView coverView;

    public PlayerView() {
        VBox header = new VBox();

        // HEADER
        header.getStyleClass().add("bkg");
        header.setId("header");

        titleLabel = new javafx.scene.control.Label("title");
        titleLabel.getStyleClass().addAll("main-text", "text-color");

        artistLabel = new Label("artist");
        artistLabel.getStyleClass().addAll("second-text", "text-color");

        header.setAlignment(Pos.CENTER);

        header.getChildren().addAll(titleLabel, artistLabel);

        this.setTop(header);

        // COVER
        coverView = new ImageView();
        try {
            coverView.setImage(new Image(new FileInputStream("/Users/jonporsche/Documents/Dev Projects.nosync/eibo_test1/src/presentation/assets/album_covers/Iron Maiden - Fear Of The Dark.jpeg")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ImageViewPane pane = new ImageViewPane(coverView);
        this.setCenter(new ImageViewPane(coverView));

        // CONTROLS
        HBox controller = new HBox();
        controller.getStyleClass().add("bkg");
        controller.setId("controller");

        Button playButton = new Button();
        playButton.getStyleClass().add("control-button");
        playButton.setId("play");

        controller.getChildren().add(playButton);

        this.setBottom(controller);
    }
}
