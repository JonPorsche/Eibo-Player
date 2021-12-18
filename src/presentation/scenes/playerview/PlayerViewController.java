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
import presentation.application.MainApp;

import java.io.ByteArrayInputStream;

public class PlayerViewController {
    private MainApp application;
    private Button playlistButton;
    private Label titleLabel;
    private Label artistLabel;
    private ImageView coverView;
    private Slider timeSlider;
    private Button loopingButton;
    private Button shuffleButton;
    private Button playButton;
    private Button skipNextButton;
    private Button skipBackButton;
    private Button volumeButton;
    private Slider volumeSlider;
    private Pane rootView;
    private MP3Player player;
    private Label currentTimeLabel;
    private Label remainingTimeLabel;

    public PlayerViewController(MainApp application, MP3Player player) {
        this.application = application;
        this.player = player;

        PlayerView mainView = new PlayerView();

        this.playlistButton = mainView.playlistButton;
        this.titleLabel = mainView.titleLabel;
        this.artistLabel = mainView.artistLabel;
        this.coverView = mainView.coverView;
        this.timeSlider = mainView.timeSlider;
        this.loopingButton = mainView.loopingButton;
        this.shuffleButton = mainView.shuffleButton;
        this.playButton = mainView.playButton;
        this.skipNextButton = mainView.skipNextButton;
        this.skipBackButton = mainView.skipBackButton;
        this.volumeButton = mainView.volumeButton;
        this.volumeSlider = mainView.volumeSlider;
        this.currentTimeLabel = mainView.currentTimeLabel;
        this.remainingTimeLabel = mainView.remainingTimeLabel;

        rootView = mainView;
        initialize();
    }

    public void initialize() {
        playlistButton.setOnAction(event -> application.switchScene("PlaylistView"));
        volumeButton.setOnAction(event -> mute());
        loopingButton.setOnAction(event -> loop());
        shuffleButton.setOnAction(event -> shuffle());
        playButton.setOnAction(event -> startPlayer());
        skipNextButton.setOnAction(event -> skipNext());
        skipBackButton.setOnAction(event -> skipBack());
        currentTimeLabel.setText(formatTime(player.getPosition()));
        remainingTimeLabel.setText("-" + formatTime(player.playlist.getTracks().get(0).getDuration()));
        titleLabel.setText(player.playlist.getTracks().get(0).getTitle());
        artistLabel.setText(player.playlist.getTracks().get(0).getArtist());
        coverView.setImage(new Image(new ByteArrayInputStream(player.playlist.getTracks().get(0).getAlbumImage())));

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
//        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> System.out.println(newValue.doubleValue()));

        player.positionProperty().addListener(new ChangeListener<Number>() {
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
                    coverView.setImage(new Image(new ByteArrayInputStream(player.getTrack().getAlbumImage())));
                });
            }
        });

        player.isPlayingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (player.isPlaying()) {
                    playButton.setId("pause-btn");
                    playButton.getStyleClass().add("button-on");
                } else {
                    playButton.setId("play-btn");
                }
            }
        });

        player.isLoopingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (player.isLooping()) {
                    loopingButton.getStyleClass().add("button-on");
                    System.out.println("+++ player: looping = true");
                    System.out.println("... classes = " + loopingButton.getStyleClass());
                } else {
                    loopingButton.getStyleClass().remove("button-on");
                    System.out.println("+++ player: looping = false");
                    System.out.println("... classes = " + loopingButton.getStyleClass());
                }
            }
        });

        player.isShufflingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (player.isShuffling()) {
                    shuffleButton.getStyleClass().add("button-on");
                    System.out.println("+++ player: shuffle = true");
                    System.out.println("... classes = " + shuffleButton.getStyleClass());
                } else {
                    shuffleButton.getStyleClass().remove("button-on");
                    System.out.println("+++ player: shuffle = false");
                    System.out.println("... classes = " + shuffleButton.getStyleClass());
                }
            }
        });

        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldVolume, Number newVolume) {
                volume(newVolume.floatValue());
            }
        });

        player.isMutedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(player.isMuted()){
                    volumeButton.getStyleClass().clear();
                    volumeButton.getStyleClass().addAll("small-button", "button", "volume-btn-off", "button-on");
                } else {
                    volumeButton.getStyleClass().clear();
                    volumeButton.getStyleClass().addAll("small-button", "button", "volume-btn-min", "button-off");
                }
            }
        });
    }

    private void volume(float newVolume){
        if(!player.isTrackLoaded()){
            player.setVolume(newVolume);
        } else {
            player.volume(newVolume);
            player.setVolume(newVolume);
            if (newVolume >= 0) {
                volumeButton.getStyleClass().clear();
                volumeButton.getStyleClass().addAll("small-button", "button", "volume-btn-max");
            } else if (newVolume == -10) {
                mute();
            } else if (newVolume > -10) {
                unmute();
            } else {
                volumeButton.getStyleClass().clear();
                volumeButton.getStyleClass().addAll("small-button", "button", "volume-btn-min");
            }
        }
    }

    private void mute() {
        if(player.isMuted()) {
            player.setIsMuted(false);
            player.unmute();
        }
        else {
            player.setIsMuted(true);
            player.mute();
        }
    }

    public void unmute(){
        player.unmute();
        player.setIsMuted(false);
    }

    private void shuffle() {
        if (player.isShuffling()){
            player.setIsShuffling(false);
            player.resetTrackOrder();
        }
        else {
            player.setIsShuffling(true);
            player.shuffle();
        }
    }

    private void loop() {
        if (player.isLooping()) player.setIsLooping(false);
        else player.setIsLooping(true);
    }

    public Pane getRootView() {
        return rootView;
    }

    private void startPlayer() {
        if (player.isPlaying()) {
            player.pause();
        } else {
            player.play();
        }
    }

    private void skipNext() {
        System.out.println("+++ PlayerViewController.skipNext: Pressed.");
        player.skipNext();
    }

    private void skipBack() {
        System.out.println("+++ PlayerViewController.skipBack: Pressed.");
        player.skipBack();
    }

    public static String formatTime(int milliseconds) {
        int seconds = milliseconds / 1000;
        int hours = seconds / 3600;
        int minutes = (seconds - (3600 * hours)) / 60;
        int seg = seconds - ((hours * 3600) + (minutes * 60));

        return String.format("%01d", minutes) + ":" + String.format("%02d", seg);

    }
}
