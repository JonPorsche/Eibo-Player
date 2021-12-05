package presentation.scenes.playerview;

import business.data.Track;
import business.service.MP3Player;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class PlayerViewController {
    Label titleLabel;
    Label artistLabel;
    ImageView coverView;
    Slider timeSlider;
    Button playButton;
    Button skipNextButton;
    Slider volumeSlider;
    Pane rootView;
    MP3Player player;
    Label currentTimeLabel;
    Label remainingTimeLabel;

    public PlayerViewController(MP3Player player) {

        this.player = player;

        PlayerView mainView = new PlayerView();

        this.titleLabel = mainView.titleLabel;
        this.artistLabel = mainView.artistLabel;
        this.coverView = mainView.coverView;
        this.timeSlider = mainView.timeSlider;
        this.playButton = mainView.playButton;
        this.skipNextButton = mainView.skipNextButton;
        this.volumeSlider = mainView.volumeSlider;
        this.currentTimeLabel = mainView.currentTimeLabel;
        this.remainingTimeLabel = mainView.remainingTimeLabel;

        rootView = mainView;
        initialize();
    }

    public void initialize() {
        playButton.setOnAction(event -> startPlayer());
        skipNextButton.setOnAction(event -> skipNext());

        currentTimeLabel.setText(formatTime(player.getCurrentTime()));

        // Just to keep a full declaration example:
        /*
        timeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                System.out.println(newValue.doubleValue();
            }
        });
         */

//        timeSlider.valueProperty().addListener((observable, oldValue, newValue) -> System.out.println(newValue.doubleValue()));
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> System.out.println(newValue.doubleValue()));

        player.currentTimeProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Platform.runLater(() -> currentTimeLabel.setText(formatTime(newValue.intValue())));
                timeSlider.valueProperty().set(newValue.intValue());
            }
        });

        player.remainingTimeProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) ->
                Platform.runLater(() -> remainingTimeLabel.setText("-" + formatTime(newValue.intValue()))));

        player.trackProperty().addListener(new ChangeListener<Track>() {
            @Override
            public void changed(ObservableValue<? extends Track> observable, Track oldValue, Track newValue) {
                Platform.runLater(() -> {
                    titleLabel.setText(player.getTrack().getTitle());
                    artistLabel.setText(player.getTrack().getArtist());
                    timeSlider.setMax(player.getTrack().getDuration());

                    try {
                        coverView.setImage(new Image(new FileInputStream(player.getTrack().getCoverFilePath())));
                    } catch (FileNotFoundException e) {
                        System.err.println("Cover file not found.");
                        //e.printStackTrace();
                    }
                });
            }
        });
    }

    public Pane getRootView() {
        return rootView;
    }

    private void startPlayer() {
        if (player.isPlaying) {
            playButton.setId("play-btn");
            player.pause();
        } else {
            playButton.setId("pause-btn");
            player.play();
        }
    }

    private void skipNext() {
        player.skipNext();
    }

    public String formatTime(int milliseconds) {
        int seconds = milliseconds / 1000;
        int hours = seconds / 3600;
        int minutes = (seconds - (3600 * hours)) / 60;
        int seg = seconds - ((hours * 3600) + (minutes * 60));

        return String.format("%01d", minutes) + ":" + String.format("%02d", seg);

    }
}
