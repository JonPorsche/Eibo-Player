package presentation.scenes.playerview;

import business.data.Track;
import business.service.MP3Player;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import presentation.application.MainApp;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class PlayerViewController {
    private MainApp application;
    private Label titleLabel;
    private Label artistLabel;
    private ImageView coverView;
    private Slider timeSlider;
    private Pane rootView;
    private MP3Player player;
    private Label currentTimeLabel;
    private Label remainingTimeLabel;

    public PlayerViewController(MainApp application, MP3Player player) {
        this.application = application;
        this.player = player;

        PlayerView mainView = new PlayerView();

        this.titleLabel = mainView.titleLabel;
        this.artistLabel = mainView.artistLabel;
        this.coverView = mainView.coverView;
        this.timeSlider = mainView.timeSlider;
        this.currentTimeLabel = mainView.currentTimeLabel;
        this.remainingTimeLabel = mainView.remainingTimeLabel;

        rootView = mainView;
        initialize();
    }

    public void initialize() {
        setTimeLabels();
        setInfoLabels();
        setCoverImage();
        handlePlayheadPositionChanges();
        handleRemainingTimeChanges();
        handleTrackChanges();
    }

    private void setTimeLabels() {
        currentTimeLabel.setText(MP3Player.formatTime(player.getPlayheadPosition()));
        remainingTimeLabel.setText("-" + MP3Player.formatTime(player.playlist.getTracks().get(0).getDuration()));
    }

    private void setInfoLabels() {
        titleLabel.setText(player.playlist.getTracks().get(0).getTitle());
        artistLabel.setText(player.playlist.getTracks().get(0).getArtist());
    }

    private void setCoverImage(){
        try{
            coverView.setImage(new Image(new ByteArrayInputStream(player.playlist.getTracks().get(0).getAlbumImage())));
        } catch (NullPointerException e){
            BufferedImage bImage = null;
            try {
                bImage = ImageIO.read(new File("../../Dummy Cover.jpg"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                ImageIO.write(bImage, "jpg", bos );
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            byte [] dummyImage = bos.toByteArray();
            coverView.setImage(new Image(new ByteArrayInputStream(dummyImage)));

        }
    }

    private void handlePlayheadPositionChanges() {
        player.playheadPositionProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> currentTimeLabel.setText(MP3Player.formatTime(newValue.intValue())));
            timeSlider.valueProperty().set(newValue.intValue());
        });
    }

    private void handleRemainingTimeChanges() {
        player.remainingTimeProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) ->
                Platform.runLater(() -> remainingTimeLabel.setText("-" + MP3Player.formatTime(newValue.intValue()))));
    }

    private void handleTrackChanges() {
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
    }

    public Pane getRootView() {
        return rootView;
    }
}
