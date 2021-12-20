package business.service;

import business.data.Playlist;
import business.data.Track;
import de.hsrm.mi.eibo.simpleplayer.SimpleAudioPlayer;
import de.hsrm.mi.eibo.simpleplayer.SimpleMinim;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import presentation.scenes.playlistview.PlaylistViewController;
import java.util.ArrayList;
import java.util.Collections;

public class MP3Player {

    public SimpleMinim minim;
    private static SimpleAudioPlayer audioPlayer;
    public ObservableList<Track> tracksObservable;
    public ArrayList<Track> tracks;
    private SimpleBooleanProperty isPlaying;
    private SimpleBooleanProperty isMuted;
    private SimpleBooleanProperty isLooping;
    private SimpleBooleanProperty isShuffling;
    private SimpleBooleanProperty isFirstTrack;
    private SimpleIntegerProperty playheadPosition;
    private SimpleIntegerProperty trackNumber;
    private SimpleIntegerProperty remainingTime;
    private SimpleObjectProperty<Track> track;
    private SimpleFloatProperty volume;
    private int duration;
    public Playlist playlist;
    private boolean isTrackLoaded;
    private static Thread playThread;
    private int numberOfTracks;

    public MP3Player(Playlist playlist) {
        this.playlist = playlist;
        tracks = playlist.getTracks();
        tracksObservable = FXCollections.observableArrayList(PlaylistManager.trackList);
        playheadPosition = new SimpleIntegerProperty();
        trackNumber = new SimpleIntegerProperty(0);
        remainingTime = new SimpleIntegerProperty();
        track = new SimpleObjectProperty<Track>();
        volume = new SimpleFloatProperty();
        isLooping = new SimpleBooleanProperty();
        isShuffling = new SimpleBooleanProperty();
        isPlaying = new SimpleBooleanProperty();
        isFirstTrack = new SimpleBooleanProperty(true);
        isMuted = new SimpleBooleanProperty();
        numberOfTracks = playlist.numberOfTracks();
        minim = new SimpleMinim();
    }

    public void play() {

        minim = new SimpleMinim();

        if (isTrackLoaded) {
            playThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Thread currentTimeThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (true) {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    return;
                                }
                                playheadPosition.setValue(audioPlayer.position());
                                remainingTime.setValue(duration - playheadPosition.get());
                            }
                        }
                    });
                    currentTimeThread.start();

                    isPlaying.set(true);
                    audioPlayer.setGain(volume.getValue());
                    audioPlayer.play(playheadPosition.getValue());
                    currentTimeThread.interrupt();

                    if (trackIsFinished()) {
                        if (isLooping()) loop();
                        else skipNext();
                    }
                }
            });
            playThread.start();
        } else {
            loadTrack();
            play();
        }
    }

    private boolean trackIsFinished() {
        if (playheadPosition.getValue() > duration - 1000) return true;
        return false;
    }

    public void loadTrack() {
        minim.stop();
        setTrack(playlist.getTracks().get(trackNumber.getValue()));
        duration = track.get().getDuration(); // used further to calculate remaining time
        String trackPath = track.get().getTrackFilePath();
        audioPlayer = minim.loadMP3File(trackPath);
        volume(volume.getValue());

        isTrackLoaded = true;
    }

    private boolean isLastTrack() {
        if (trackNumber.getValue() == numberOfTracks - 1) return true;
        return false;
    }

    private void firstTrack() {
        if (trackNumber.getValue() == 0) setFirstTrack(true);
        else setFirstTrack(false);
    }

    public void pause() {
        minim.stop();
        isPlaying.set(false);
        audioPlayer.pause();
        playheadPosition.setValue(audioPlayer.position());
    }

    public void volume(float gain) {
        audioPlayer.shiftGain(audioPlayer.getGain(), gain, 500);
    }

    public void skipNext() {
        minim.stop();
        if (!isLastTrack()) {
            trackNumber.setValue(trackNumber.getValue() + 1);
            playheadPosition.setValue(0);
            if (isPlaying.get()) {
                audioPlayer.pause();
                playheadPosition.setValue(0);
                loadTrack();
                play();
            } else loadTrack();
        } else return;
    }

    public boolean isFirstTrack() {
        return isFirstTrack.get();
    }

    public void setFirstTrack(boolean isFirstTrack) {
        this.isFirstTrack.set(isFirstTrack);
    }

    public void skipBack() {
        minim.stop();
        firstTrack();
        if (!isFirstTrack.getValue()) {
            if (isPlaying.get()) {
                audioPlayer.pause();
                if (playheadPosition.get() < 2000) {
                    trackNumber.setValue(trackNumber.getValue() - 1);
                    playheadPosition.setValue(0);
                    loadTrack();
                    play();
                } else {
                    playheadPosition.setValue(0);
                    play();
                }
            } else {
                if (playheadPosition.get() == 0) {
                    trackNumber.setValue(trackNumber.getValue() - 1);
                    loadTrack();
                } else {
                    playheadPosition.setValue(0);
                }
            }
        } else {
            if (isPlaying.get()) {
                audioPlayer.pause();
                playheadPosition.setValue(0);
                loadTrack();
                play();
            } else {
                playheadPosition.setValue(0);
            }
        }
    }

    public void loop() {
        minim.stop();
        audioPlayer.pause();
        playheadPosition.setValue(0);
        loadTrack();
        play();
    }

    public void shuffle() {
        Collections.shuffle(tracks);
        PlaylistViewController.trackListModel.clear();
        PlaylistViewController.trackListModel.addAll(tracks);
        if (!isPlaying()) {
            minim.stop();
            playheadPosition.setValue(0);
            loadTrack();
        }
    }

    public void resetTrackOrder() {

        // Bubble Sort
        for (int pass = 1; pass <= tracks.size() - 1; pass++) {
            for (int compare = 1; compare <= tracks.size() - pass; compare++) {
                Track first = tracks.get(compare - 1);
                Track second = tracks.get(compare);
                if (first.getId() > second.getId()) {
                    Collections.swap(tracks, compare - 1, compare);
                }
            }
        }
        PlaylistManager.playlist.setTracks(PlaylistManager.trackList);
        PlaylistViewController.trackListModel.clear();
        PlaylistViewController.trackListModel.addAll(tracks);
    }

    public void mute() {
        audioPlayer.mute();
        setVolume(0);
    }

    public int getPlayheadPosition() {
        return playheadPosition.get();
    }

    public void setPlayheadPosition(int playheadPosition) {
        this.playheadPosition.set(playheadPosition);
    }

    public final SimpleIntegerProperty playheadPositionProperty() {
        return this.playheadPosition;
    }

    public int getTrackNumber() {
        return trackNumber.get();
    }

    public void setTrackNumber(int trackNumber) {
        this.trackNumber.set(trackNumber);
    }

    public final SimpleIntegerProperty trackNumberProperty() {
        return this.trackNumber;
    }

    public float getVolume() {
        return volume.get();
    }

    public void setVolume(float volume) {
        this.volume.set(volume);
    }

    public final SimpleIntegerProperty remainingTimeProperty() {
        return this.remainingTime;
    }

    public Track getTrack() {
        return track.get();
    }

    public void setTrack(Track track) {
        this.track.set(track);
    }

    public final SimpleObjectProperty<Track> trackProperty() {
        return this.track;
    }

    public SimpleBooleanProperty isPlayingProperty() {
        return isPlaying;
    }

    public boolean isPlaying() {
        return isPlaying.get();
    }

    public SimpleBooleanProperty isLoopingProperty() {
        return isLooping;
    }

    public boolean isLooping() {
        return isLooping.get();
    }

    public void setIsLooping(boolean isLooping) {
        this.isLooping.set(isLooping);
    }

    public SimpleBooleanProperty isShufflingProperty() {
        return isShuffling;
    }

    public boolean isShuffling() {
        return isShuffling.get();
    }

    public void setIsShuffling(boolean isShuffling) {
        this.isShuffling.set(isShuffling);
    }

    public SimpleBooleanProperty isMutedProperty() {
        return isMuted;
    }

    public boolean isMuted() {
        return isMuted.get();
    }

    public void setIsMuted(boolean isMuted) {
        this.isMuted.set(isMuted);
    }

    public boolean isTrackLoaded() {
        return isTrackLoaded;
    }

    public void unmute() {
        audioPlayer.unmute();
    }

    public int getTrackNumber(Track track) {
        for (int i = 0; i < numberOfTracks; i++) {
            if (tracks.get(i).getTitle() == track.getTitle()) {
                trackNumber.setValue(i);
            }
        }
        return trackNumber.getValue();
    }

    public void setNumberOfTracks(int numberOfTracks) {
        this.numberOfTracks = numberOfTracks;
    }

    public static String formatTime(int milliseconds) {
        int seconds = milliseconds / 1000;
        int hours = seconds / 3600;
        int minutes = (seconds - (3600 * hours)) / 60;
        int seg = seconds - ((hours * 3600) + (minutes * 60));

        return String.format("%01d", minutes) + ":" + String.format("%02d", seg);

    }
}

