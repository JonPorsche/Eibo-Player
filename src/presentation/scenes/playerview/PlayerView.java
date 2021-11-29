package presentation.scenes.playerview;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import presentation.uicomponents.ImageViewPane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class PlayerView extends VBox {

    // TOP CONTROLS
    HBox topControlsBox = new HBox();
    Button playlistButton = new Button();

    // TITLES
    VBox titlesBox = new VBox();
    Label titleLabel;
    Label artistLabel;

    // COVER
    ImageView coverView;

    // TIME CONTROL
    VBox timeControlBox = new VBox();
    Slider durationSlider = new Slider();
    HBox timeLabels = new HBox();
    HBox remainingTimeBox = new HBox();
    Label currentTimeLabel;
    Label remainingTimeLabel;

    // CONTROLS
    HBox controlBox = new HBox();
    Button repeatButton = new Button();
    Button skipBackButton = new Button();
    Button playButton = new Button();
    Button skipNextButton = new Button();
    Button shuffleButton = new Button();

    // VOLUME CONTROLS
    HBox volumeControls = new HBox();
    Button volumeButton = new Button();
    Slider volumeSlider = new Slider();

    public PlayerView() {

        this.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        this.getStyleClass().add("bkg");
        this.setId("player-view");

        topControls();
        titles();
        timeControl();
        controls();
        volumeControls();

        this.getChildren().addAll(
                topControlsBox,
                titlesBox,
                cover(),
                timeControlBox,
                controlBox,
                volumeControls
        );
    }

    private void topControls() {
        // Style
        playlistButton.getStyleClass().addAll("small-button", "button-opacity");
        playlistButton.setId("playlist-btn");
        topControlsBox.setAlignment(Pos.CENTER_RIGHT);

        // Add elements
        topControlsBox.getChildren().add(playlistButton);
    }

    private void titles() {
        // Create elements
        titleLabel = new Label("Song Title");
        artistLabel = new Label("Artist");

        // Style
        titlesBox.setId("titles-box");
        titlesBox.setAlignment(Pos.CENTER);
        VBox.setMargin(titlesBox, new Insets(8,0,8,0));

        titleLabel.getStyleClass().add("main-text");
        titleLabel.setId("title");

        artistLabel.getStyleClass().add("main-text");
        artistLabel.setId("artist");
        VBox.setMargin(artistLabel, new Insets(8,0,0,0));

        // Add elements
        titlesBox.getChildren().addAll(titleLabel, artistLabel);
    }

    private ImageViewPane cover() {
        // Create elements
        coverView = new ImageView();

        try {
            coverView.setImage(new Image(new FileInputStream("/Users/jonporsche/Documents/Dev Projects.nosync/eibo_test1/src/presentation/assets/album_covers/Iron Maiden - Fear Of The Dark.jpeg")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        coverView.setPreserveRatio(true);

        ImageViewPane coverImageViewPane = new ImageViewPane(coverView);
        return coverImageViewPane;
    }

    /* Two HBoxes inside each other are used to be able
     * to flow the remaining time label to the right
     * */
    private void timeControl() {
        // Create elements
        currentTimeLabel = new Label("1:40");
        remainingTimeLabel = new Label("-3:08");

        // Style
        timeControlBox.setId("time-control");
        VBox.setMargin(timeControlBox, new Insets(20,0,0,0));
        VBox.setMargin(timeLabels, new Insets(4,0,0,0));

        currentTimeLabel.getStyleClass().addAll("time-labels", "main-text");
        remainingTimeLabel.getStyleClass().addAll("time-labels", "main-text");

        remainingTimeBox.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(remainingTimeBox, Priority.ALWAYS ); // To make the remaining time label move to the right when the screen gets wider

        // Add elements
        remainingTimeBox.getChildren().add(remainingTimeLabel);

        timeLabels.getChildren().addAll(
                currentTimeLabel,
                remainingTimeBox
        );

        timeControlBox.getChildren().addAll(
                durationSlider,
                timeLabels
        );
    }

    private void controls() {
        // Style
        controlBox.setId("control-box");

        repeatButton.getStyleClass().addAll("small-button", "button-opacity");
        repeatButton.setId("repeat-btn");

        skipBackButton.getStyleClass().addAll("small-button", "button-opacity");
        skipBackButton.setId("skip-back-btn");

        playButton.getStyleClass().addAll("big-button", "button-opacity");
        playButton.setId("play-btn");

        skipNextButton.getStyleClass().addAll("small-button", "button-opacity");
        skipNextButton.setId("skip-next-btn");

        shuffleButton.getStyleClass().addAll("small-button", "button-opacity");
        shuffleButton.setId("shuffle-btn");

        HBox.setMargin(repeatButton, new Insets(0,31,0,0));
        HBox.setMargin(skipBackButton, new Insets(0,25,0,0));
        HBox.setMargin(playButton, new Insets(0,4,0,4));
        HBox.setMargin(skipNextButton, new Insets(0,0,0,25));
        HBox.setMargin(shuffleButton, new Insets(0,0,0,31));
        VBox.setMargin(controlBox, new Insets(0,0,8,0));

        // Add elements
        controlBox.getChildren().addAll(
                repeatButton,
                skipBackButton,
                playButton,
                skipNextButton,
                shuffleButton
        );
    }

    private void volumeControls() {
        // Styles
        volumeControls.setId("volume-controls");
        volumeControls.setAlignment(Pos.CENTER);

        volumeButton.getStyleClass().addAll("small-button", "button-opacity");
        volumeButton.setId("volume-btn");

        volumeSlider.setPrefWidth(185);
        HBox.setMargin(volumeSlider, new Insets(0,4,0,4));

        // Add elements
        volumeControls.getChildren().addAll(
                volumeButton,
                volumeSlider
        );
    }
}
