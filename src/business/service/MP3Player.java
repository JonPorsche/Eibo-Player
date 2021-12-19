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

    private SimpleMinim minim;
    private static SimpleAudioPlayer audioPlayer;
    public ObservableList<Track> tracksObservable;
    public ArrayList<Track> tracks;
    private ArrayList<Track> shuffledTracks;
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
    private int numberOfTracks; // used further to test if there are remaining Tracks

    public MP3Player(Playlist playlist) {
        this.playlist = playlist;
        tracks = playlist.getTracks();
        tracksObservable = FXCollections.observableArrayList(PlaylistManager.trackList);
        playheadPosition = new SimpleIntegerProperty();
        trackNumber = new SimpleIntegerProperty(0);
        remainingTime = new SimpleIntegerProperty();
        track = new SimpleObjectProperty<Track>();
        volume = new SimpleFloatProperty(0);
        isLooping = new SimpleBooleanProperty();
        isShuffling = new SimpleBooleanProperty();
        isPlaying = new SimpleBooleanProperty();
        isFirstTrack = new SimpleBooleanProperty(true);
        isMuted = new SimpleBooleanProperty();
        numberOfTracks = playlist.numberOfTracks();
        minim = new SimpleMinim();
        System.out.println("+++ MP3Player: minim instance started.");
    }

    public void play() {
        minim = new SimpleMinim();
        System.out.println("+++ play: new minim instance started.");
        if (isTrackLoaded) {
            System.out.println("+++ play: track number " + trackNumber.getValue() + " is loaded");
            playThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("+++ playThread: Started.");
                    Thread currentTimeThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("+++ currentTimeThread: Started.");
                            while (true) {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    System.out.println("+++ currentTimeThread: Another thread woke me up +++");
                                    return;
                                }
                                playheadPosition.setValue(audioPlayer.position());
                                remainingTime.setValue(duration - playheadPosition.get());
                            }
                        }
                    });
                    currentTimeThread.start();

                    isPlaying.set(true);
                    System.out.println("+++ play: initial gain = " + audioPlayer.getGain());
                    audioPlayer.setGain(volume.getValue());
                    audioPlayer.play(playheadPosition.getValue());
                    System.out.println("+++ playThread: Player status = " + audioPlayer.isPlaying());
                    currentTimeThread.interrupt();
                    System.out.println("+++ playThread: position = " + playheadPosition.get());
                    System.out.println("+++ playThread: duration = " + duration);

/*                    if (trackIsFinished() && !isLooping()) skipNext();
                    else if (trackIsFinished() && isLooping()) loop();*/

                    if (trackIsFinished()) {
                        if (isLooping()) loop();
                        else skipNext();
                    }
                }
            });
            playThread.start();
        } else {
            System.out.println("+++ play: track is not loaded");
            loadTrack();
            play();
        }
    }

    private boolean trackIsFinished() {
        if (playheadPosition.getValue() > duration - 1000) {
            System.out.println("+++ trackIsFinished: true");
            return true;
        }
        System.out.println("+++ trackIsFinished: false");
        return false;
    }

    public void loadTrack() {
        minim.stop();
        System.out.println("+++ MP3Player.loadTrack: tracks = " + playlist.getTracks().toString());
        setTrack(playlist.getTracks().get(trackNumber.getValue()));
        duration = track.get().getDuration(); // used further to calculate remaining time
        String trackPath = track.get().getTrackFilePath();
        audioPlayer = minim.loadMP3File(trackPath);
        volume(volume.getValue());

        System.out.println("+++ loadTrack: Loaded track with ID = " + track.get().getId() + " trackNumber = " + trackNumber.getValue() + " - " + track.get().getTitle() + " - " + track.get().getArtist());
        isTrackLoaded = true;
    }

    private boolean isLastTrack() {
        if (trackNumber.getValue() == numberOfTracks - 1) {
            System.out.println("+++ isLastTrack: Track number = " + trackNumber.getValue());
            return true;
        }
        System.out.println("+++ isLastTrack: Is not the last track.");
        return false;
    }

    private void firstTrack() {
        if (trackNumber.getValue() == 0) {
            System.out.println("+++ firstTrack: Track number = " + trackNumber.getValue());
            setFirstTrack(true);
        } else {
            System.out.println("+++ firstTrack: Is not the first track.");
            setFirstTrack(false);
        }
    }

    public void pause() {
        minim.stop();
        System.out.println("+++ pause: Paused.");
        isPlaying.set(false);
        audioPlayer.pause();
        playheadPosition.setValue(audioPlayer.position());

    }

    public void volume(float gain) {
        //audioPlayer.setGain(gain);
        audioPlayer.shiftGain(audioPlayer.getGain(), gain, 500);
        System.out.println("+++ volume: " + gain);
    }

    public void skipNext() {
        minim.stop();
        if (!isLastTrack()) {
            trackNumber.setValue(trackNumber.getValue() + 1);
            System.out.println("+++ skipNext: It was not the last track.");
            playheadPosition.setValue(0);
            if (isPlaying.get()) {
                System.out.println("+++ skipNext: It was playing when skip next pressed.");
                audioPlayer.pause();
                // kill the running play thread?
                System.out.println("+++ skipNext: Load and play track number " + trackNumber.getValue());
                playheadPosition.setValue(0);
                loadTrack();
                play();
            } else {
                System.out.println("+++ skipNext: It was not playing when skip next pressed.");
                System.out.println("+++ skipNext: Load track number " + trackNumber.getValue());

                loadTrack();
            }
        } else {
            System.out.println("!!! skipNext: Cannot skip!");
            return;
        }
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
            //trackNumber--;
            System.out.println("+++ skipBack: It was not the first track.");
            //position.setValue(0);
            if (isPlaying.get()) {
                System.out.println("+++ skipBack: It was playing when skip back pressed.");
                audioPlayer.pause();
                if (playheadPosition.get() < 2000) {
                    trackNumber.setValue(trackNumber.getValue() - 1);
                    playheadPosition.setValue(0);
                    loadTrack();
                    play();
                } else {
                    playheadPosition.setValue(0);
                    System.out.println("+++ skipBack: Load and play track number " + trackNumber.getValue());
                    //loadTrack();
                    play();
                }
            } else {
                if (playheadPosition.get() == 0) {
                    trackNumber.setValue(trackNumber.getValue() - 1);
                    loadTrack();
                } else {
                    System.out.println("+++ skipBack: It was not playing when skip back pressed.");
                    System.out.println("+++ skipBack: Load track number " + trackNumber.getValue());
                    playheadPosition.setValue(0);
                    //loadTrack();
                }
            }
        } else {
            if (isPlaying.get()) {
                System.out.println("+++ skipBack: It was playing when skip back pressed.");
                audioPlayer.pause();
                playheadPosition.setValue(0);
                System.out.println("+++ skipBack: Load and play track number " + trackNumber.getValue());
                loadTrack();
                play();
            } else {
                playheadPosition.setValue(0);
                //System.out.println("!!! skipBack: Cannot skip!");
            }
        }
    }

    public void playNext() {
        minim.stop();
        audioPlayer.pause();
        playheadPosition.setValue(0);
        trackNumber.setValue(trackNumber.getValue() + 1);
        System.out.println("+++ playNext: Load and play the track number " + trackNumber.getValue());
        loadTrack();
        play();
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
        for(int pass = 1; pass <= tracks.size() - 1; pass++){
            for(int compare = 1; compare <= tracks.size() - pass; compare++){
                Track first = tracks.get(compare - 1);
                Track second = tracks.get(compare);
                if(first.getId() > second.getId()){
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

    public final SimpleFloatProperty volumeProperty() {
        return this.volume;
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

    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying.set(isPlaying);
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

    public SimpleBooleanProperty isFirstTrackProperty() {
        return isFirstTrack;
    }

    public boolean isTrackLoaded() {
        System.out.println("+++ MP3Player.isTrackLoaded = " + isTrackLoaded);
        return isTrackLoaded;
    }

    public void setTrackLoaded(boolean trackLoaded) {
        isTrackLoaded = trackLoaded;
    }

    void draw() {

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
        System.out.println("+++ getTrackNumber: " + trackNumber.getValue() + " = " + tracks.get(trackNumber.getValue()).toString());
        return trackNumber.getValue();
    }

    public void setNumberOfTracks(int numberOfTracks) {
        this.numberOfTracks = numberOfTracks;
    }

    public ObservableList<Track> getTracksObservable() {
        return tracksObservable;
    }

    public void setTracksObservable(ObservableList<Track> tracksObservable) {
        this.tracksObservable = tracksObservable;
    }

    public static String formatTime(int milliseconds) {
        int seconds = milliseconds / 1000;
        int hours = seconds / 3600;
        int minutes = (seconds - (3600 * hours)) / 60;
        int seg = seconds - ((hours * 3600) + (minutes * 60));

        return String.format("%01d", minutes) + ":" + String.format("%02d", seg);

    }
}

