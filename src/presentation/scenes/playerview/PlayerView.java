package presentation.scenes.playerview;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

    // MIDDLE BOX
    VBox middleBox = new VBox();

    // TITLES
    VBox titlesBox = new VBox();
    Label titleLabel = new Label();
    Label artistLabel = new Label();

    // COVER
    ImageView coverView;

    // TIME CONTROL
    VBox timeControlBox = new VBox();
    Slider timeSlider = new Slider();
    HBox timeLabels = new HBox();
    HBox remainingTimeBox = new HBox();
    Label currentTimeLabel = new Label();
    Label remainingTimeLabel = new Label();

    public PlayerView() {
        setPlayerViewStyle();
        setInfos();
        setTimeControl();

        middleBox.getChildren().addAll(titlesBox, setCoverImage(), timeControlBox);
        middleBox.setAlignment(Pos.CENTER);

        this.setCenter(middleBox);
    }

    private void setInfos() {
        setInfosStyles();
        infosAddElements();
    }

    private ImageViewPane setCoverImage() {
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
    private void setTimeControl() {
        setTimeControlStyle();
        timeControlAddElements();
    }

    private void setPlayerViewStyle(){
        this.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        this.getStyleClass().add("container");
    }

    private void setInfosStyles(){
        titlesBox.setId("titles-box");
        titlesBox.setAlignment(Pos.CENTER);
        VBox.setMargin(titlesBox, new Insets(8,0,8,0));

        titleLabel.getStyleClass().add("main-text");
        titleLabel.setId("title");

        artistLabel.getStyleClass().add("main-text");
        artistLabel.setId("artist");
        VBox.setMargin(artistLabel, new Insets(8,0,30,0));
    }

    private void infosAddElements(){
        titlesBox.getChildren().addAll(titleLabel, artistLabel);
    }

    private void setTimeControlStyle(){
        timeControlBox.setId("time-control");
        timeControlBox.setMaxWidth(400);
        VBox.setMargin(timeControlBox, new Insets(20,0,0,0));
        VBox.setMargin(timeLabels, new Insets(8,0,0,0));

        currentTimeLabel.getStyleClass().addAll("time-labels", "main-text");
        remainingTimeLabel.getStyleClass().addAll("time-labels", "main-text");

        timeSlider.getStyleClass().addAll("pretty-slider", "time-slider");

        remainingTimeBox.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(remainingTimeBox, Priority.ALWAYS ); // To make the remaining time label move to the right when the screen gets wider
    }

    private void timeControlAddElements(){
        remainingTimeBox.getChildren().add(remainingTimeLabel);
        timeLabels.getChildren().addAll( currentTimeLabel, remainingTimeBox);
        timeControlBox.getChildren().addAll(timeSlider, timeLabels);
    }
}
