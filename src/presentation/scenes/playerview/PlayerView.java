package presentation.scenes.playerview;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import presentation.uicomponents.ImageViewPane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class PlayerView extends BorderPane {

    // TOP CONTROLS
    HBox topBox = new HBox();
    Button playlistButton = new Button();

    // MIDDLE BOX
    VBox middleBox = new VBox();

    // TITLES
    VBox titlesBox = new VBox();
    Label titleLabel;
    Label artistLabel;

    // COVER
    ImageView coverView;

    // BOTTOM BOX
    VBox bottomBox = new VBox();

    // TIME CONTROL
    VBox timeControlBox = new VBox();
    Slider timeSlider = new Slider();
    HBox timeLabels = new HBox();
    HBox remainingTimeBox = new HBox();
    Label currentTimeLabel;
    Label remainingTimeLabel;

    // CONTROLS
    HBox controlBox = new HBox();
    Button loopingButton = new Button();
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
        this.getStyleClass().add("container");

        topControls();
        titles();
        timeControl();
        controls();
        volumeControls();

        middleBox.getChildren().addAll(titlesBox, cover());
        middleBox.setAlignment(Pos.CENTER);

        bottomBox.getChildren().addAll(timeControlBox, controlBox, volumeControls);
        bottomBox.setAlignment(Pos.CENTER);

        this.setTop(topBox);
        this.setCenter(middleBox);
        this.setBottom(bottomBox);
    }

    private void topControls() {
        // Style
        playlistButton.getStyleClass().addAll("small-button", "button");
        playlistButton.setId("playlist-btn");
        topBox.setAlignment(Pos.CENTER_RIGHT);
        HBox.setMargin(playlistButton, new Insets(0,0,0,8));

        // Add elements
        topBox.getChildren().add(playlistButton);
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
        VBox.setMargin(artistLabel, new Insets(8,0,30,0));

        // Add elements
        titlesBox.getChildren().addAll(titleLabel, artistLabel);
    }

    private ImageViewPane cover() {
        // Create elements
        coverView = new ImageView();

        try {
            coverView.setImage(new Image(new FileInputStream("/Users/jonporsche/Documents/Dev Projects.nosync/eibo_test1/src/presentation/assets/album_covers/Dummy Cover.jpg")));
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
        currentTimeLabel = new Label("0:00");
        remainingTimeLabel = new Label("-3:08");

        // Style
        timeControlBox.setId("time-control");
        timeControlBox.setMaxWidth(400);
        VBox.setMargin(timeControlBox, new Insets(20,0,0,0));
        VBox.setMargin(timeLabels, new Insets(8,0,0,0));

        currentTimeLabel.getStyleClass().addAll("time-labels", "main-text");
        remainingTimeLabel.getStyleClass().addAll("time-labels", "main-text");

        timeSlider.getStyleClass().addAll("pretty-slider", "time-slider");

        remainingTimeBox.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(remainingTimeBox, Priority.ALWAYS ); // To make the remaining time label move to the right when the screen gets wider

        // Add elements
        remainingTimeBox.getChildren().add(remainingTimeLabel);

        timeLabels.getChildren().addAll(
                currentTimeLabel,
                remainingTimeBox
        );

        timeControlBox.getChildren().addAll(
                timeSlider,
                timeLabels
        );
    }

    private void controls() {
        // Style
        controlBox.setId("control-box");

        loopingButton.getStyleClass().clear();
        loopingButton.getStyleClass().addAll("small-button", "button");
        loopingButton.setId("repeat-btn");
        System.out.println("... classes = " + loopingButton.getStyleClass());

        skipBackButton.getStyleClass().addAll("small-button", "button");
        skipBackButton.setId("skip-back-btn");

        playButton.getStyleClass().addAll("big-button", "button");
        playButton.setId("play-btn");

        skipNextButton.getStyleClass().addAll("small-button", "button");
        skipNextButton.setId("skip-next-btn");

        shuffleButton.getStyleClass().clear();
        shuffleButton.getStyleClass().addAll("small-button", "button");
        shuffleButton.setId("shuffle-btn");

        HBox.setMargin(loopingButton, new Insets(0,31,0,0));
        HBox.setMargin(skipBackButton, new Insets(0,25,0,0));
        HBox.setMargin(playButton, new Insets(0,4,0,4));
        HBox.setMargin(skipNextButton, new Insets(0,0,0,25));
        HBox.setMargin(shuffleButton, new Insets(0,0,0,31));
        VBox.setMargin(controlBox, new Insets(0,0,8,0));

        // Add elements
        controlBox.getChildren().addAll(
                loopingButton,
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

        volumeButton.getStyleClass().addAll("small-button", "button", "volume-btn-max");

        volumeSlider.setPrefWidth(185);
        volumeSlider.setMax(10);
        volumeSlider.setMin(-10);
        volumeSlider.getStyleClass().add("pretty-slider");
        HBox.setMargin(volumeSlider, new Insets(0,4,0,4));

        // Add elements
        volumeControls.getChildren().addAll(
                volumeButton,
                volumeSlider
        );
    }
}
