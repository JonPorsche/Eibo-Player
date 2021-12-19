package presentation.scenes.controlview;

import business.service.MP3Player;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import presentation.application.MainApp;

public class ControlViewController {
    private MainApp application;
    private Button loopingButton;
    private Button shuffleButton;
    private Button playButton;
    private Button skipNextButton;
    private Button skipBackButton;
    private Button volumeButton;
    private Slider volumeSlider;
    private Pane rootView;
    private MP3Player player;

    public ControlViewController(MainApp application, MP3Player player){
        this.application = application;
        this.player = player;

        ControlView controlView = new ControlView();

        this.loopingButton = controlView.loopingButton;
        this.shuffleButton = controlView.shuffleButton;
        this.playButton = controlView.playButton;
        this.skipNextButton = controlView.skipNextButton;
        this.skipBackButton = controlView.skipBackButton;
        this.volumeButton = controlView.volumeButton;
        this.volumeSlider = controlView.volumeSlider;

        rootView = controlView;
        initialize();
    }

    public void initialize(){
        handleControlsClickEvents();
        handlePlayPauseStatusChanges();
        handleLoopingStatusChanges();
        handleShuffleStatusChanges();
        handleMuteStatusChanges();
        handleVolumeStatusChanges();
    }

    private void handleControlsClickEvents(){
        volumeButton.setOnAction(event -> mute());
        loopingButton.setOnAction(event -> loop());
        shuffleButton.setOnAction(event -> shuffle());
        playButton.setOnAction(event -> startPlayer());
        skipNextButton.setOnAction(event -> skipNext());
        skipBackButton.setOnAction(event -> skipBack());
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

    private void handlePlayPauseStatusChanges(){
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
    }

    private void handleLoopingStatusChanges(){
        player.isLoopingProperty().addListener((observable, oldValue, newValue) -> {
            if (player.isLooping()) {
                loopingButton.getStyleClass().add("button-on");
                System.out.println("+++ player: looping = true");
                System.out.println("... classes = " + loopingButton.getStyleClass());
            } else {
                loopingButton.getStyleClass().remove("button-on");
                System.out.println("+++ player: looping = false");
                System.out.println("... classes = " + loopingButton.getStyleClass());
            }
        });
    }

    private void handleShuffleStatusChanges(){
        player.isShufflingProperty().addListener((observable, oldValue, newValue) -> {
            if (player.isShuffling()) {
                shuffleButton.getStyleClass().add("button-on");
                System.out.println("+++ player: shuffle = true");
                System.out.println("... classes = " + shuffleButton.getStyleClass());
            } else {
                shuffleButton.getStyleClass().remove("button-on");
                System.out.println("+++ player: shuffle = false");
                System.out.println("... classes = " + shuffleButton.getStyleClass());
            }
        });
    }

    private void handleMuteStatusChanges(){
        player.isMutedProperty().addListener((observable, oldValue, newValue) -> {
            if(player.isMuted()){
                volumeButton.getStyleClass().clear();
                volumeButton.getStyleClass().addAll("small-button", "button", "volume-btn-off", "button-on");
            } else {
                volumeButton.getStyleClass().clear();
                volumeButton.getStyleClass().addAll("small-button", "button", "volume-btn-min", "button-off");
            }
        });
    }

    private void handleVolumeStatusChanges(){
        volumeSlider.valueProperty().addListener((observable, oldVolume, newVolume) ->
            volume(newVolume.floatValue())
        );
    }

    public Pane getRootView() {
        return rootView;
    }
}
