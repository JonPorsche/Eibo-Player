package presentation.uicomponents;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ControlView extends VBox {
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

    public ControlView(){
        setControlViewStyle();
        setPlayControls();
        setVolumeControls();

        this.getChildren().addAll(controlBox, volumeControls);
        this.setAlignment(Pos.CENTER);
    }

    private void setControlViewStyle() {
        this.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        this.getStyleClass().addAll("container", "bkg");
    }

    private void setPlayControls() {
        setPlayControlsStyle();
        playControlsAddElements();
    }

    private void setPlayControlsStyle(){
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
    }

    private void playControlsAddElements(){
        controlBox.getChildren().addAll(
                loopingButton,
                skipBackButton,
                playButton,
                skipNextButton,
                shuffleButton
        );
    }

    private void setVolumeControls(){
        setVolumeControlsStyle();
        volumeControlsAddElements();
    }

    private void setVolumeControlsStyle(){
        volumeControls.setId("volume-controls");
        volumeControls.setAlignment(Pos.CENTER);

        volumeButton.getStyleClass().addAll("small-button", "button", "volume-btn-max");

        volumeSlider.setPrefWidth(185);
        volumeSlider.setMax(10);
        volumeSlider.setMin(-10);
        volumeSlider.getStyleClass().add("pretty-slider");
        HBox.setMargin(volumeSlider, new Insets(0,4,0,4));
    }

    private void volumeControlsAddElements(){
        volumeControls.getChildren().addAll(
                volumeButton,
                volumeSlider
        );
    }
}
