package business.service;

import business.data.Playlist;
import business.data.Track;
import de.hsrm.mi.eibo.simpleplayer.SimpleAudioPlayer;
import de.hsrm.mi.eibo.simpleplayer.SimpleMinim;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Collections;

public class MP3Player {

    private SimpleMinim minim;
    private static SimpleAudioPlayer audioPlayer;
    public int trackNumber = 0;
    public ObservableList<Track> tracksObservable;
    public ArrayList<Track> tracks;
    private SimpleBooleanProperty isPlaying;
    private SimpleBooleanProperty isMuted;
    private SimpleBooleanProperty isLooping;
    private SimpleBooleanProperty isShuffling;
    private SimpleBooleanProperty isFirstTrack;
    private SimpleIntegerProperty position;
    private SimpleIntegerProperty remainingTime;
    private SimpleObjectProperty<Track> track;
    private int duration;
    public Playlist playlist;
    private boolean isTrackLoaded;
    private static Thread playThread;
    private int numberOfTracks; // used further to test if there are remaining Tracks

    public MP3Player(Playlist playlist) {
        this.playlist = playlist;
        tracks = playlist.getTracks();
        tracksObservable = FXCollections.observableArrayList(PlaylistManager.trackList);
        position = new SimpleIntegerProperty();
        remainingTime = new SimpleIntegerProperty();
        track = new SimpleObjectProperty<Track>();
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
            System.out.println("+++ play: track number " + trackNumber + " is loaded");
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
                                position.setValue(audioPlayer.position());
                                remainingTime.setValue(duration - position.get());
                            }
                        }
                    });
                    currentTimeThread.start();

                    isPlaying.set(true);
                    System.out.println("+++ play: initial gain = " + audioPlayer.getGain());
                    audioPlayer.play(position.getValue());
                    System.out.println("+++ playThread: Player status = " + audioPlayer.isPlaying());
                    currentTimeThread.interrupt();
                    System.out.println("+++ playThread: position = " + position.get());
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
        if (position.getValue() > duration - 1000) {
            System.out.println("+++ trackIsFinished: true");
            return true;
        }
        System.out.println("+++ trackIsFinished: false");
        return false;
    }

    public void loadTrack() {
        minim.stop();
        System.out.println("+++ MP3Player.loadTrack: tracks = " + playlist.getTracks().toString());
        setTrack(playlist.getTracks().get(trackNumber));
        duration = track.get().getDuration(); // used further to calculate remaining time
        String trackPath = track.get().getTrackFilePath();
        audioPlayer = minim.loadMP3File(trackPath);

        System.out.println("+++ loadTrack: Loaded track " + trackNumber + " - " + track.get().getTitle() + " - " + track.get().getArtist());
        isTrackLoaded = true;
    }

    private boolean isLastTrack() {
        if (trackNumber == numberOfTracks - 1) {
            System.out.println("+++ isLastTrack: Track number = " + trackNumber);
            return true;
        }
        System.out.println("+++ isLastTrack: Is not the last track.");
        return false;
    }

    private void firstTrack() {
        if (trackNumber == 0) {
            System.out.println("+++ firstTrack: Track number = " + trackNumber);
            setFirstTrack(true);
        }
        else {
            System.out.println("+++ firstTrack: Is not the first track.");
            setFirstTrack(false);
        }
    }

    public void pause() {
        minim.stop();
        System.out.println("+++ pause: Paused.");
        isPlaying.set(false);
        audioPlayer.pause();
        position.setValue(audioPlayer.position());

    }

    public void volume(float gain) {
        //audioPlayer.setGain(gain);
        audioPlayer.shiftGain(audioPlayer.getGain(),gain,500);
        System.out.println("+++ volume: " + gain);
    }

    public void skipNext() {
        minim.stop();
        if (!isLastTrack()) {
            trackNumber++;
            System.out.println("+++ skipNext: It was not the last track.");
            position.setValue(0);
            if (isPlaying.get()) {
                System.out.println("+++ skipNext: It was playing when skip next pressed.");
                audioPlayer.pause();
                // kill the running play thread?
                System.out.println("+++ skipNext: Load and play track number " + trackNumber);
                position.setValue(0);
                loadTrack();
                play();
            } else {
                System.out.println("+++ skipNext: It was not playing when skip next pressed.");
                System.out.println("+++ skipNext: Load track number " + trackNumber);

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
                if(position.get()< 2000){
                    trackNumber--;
                    position.setValue(0);
                    loadTrack();
                    play();
                } else {
                    position.setValue(0);
                    System.out.println("+++ skipBack: Load and play track number " + trackNumber);
                    //loadTrack();
                    play();
                }
            } else {
                if(position.get() == 0){
                    trackNumber--;
                    loadTrack();
                } else {
                    System.out.println("+++ skipBack: It was not playing when skip back pressed.");
                    System.out.println("+++ skipBack: Load track number " + trackNumber);
                    position.setValue(0);
                    //loadTrack();
                }
            }
        } else {
            if(isPlaying.get()){
                System.out.println("+++ skipBack: It was playing when skip back pressed.");
                audioPlayer.pause();
                position.setValue(0);
                System.out.println("+++ skipBack: Load and play track number " + trackNumber);
                loadTrack();
                play();
            }
            else{
                position.setValue(0);
                //System.out.println("!!! skipBack: Cannot skip!");
            }
        }
    }

    public void playNext() {
        minim.stop();
        audioPlayer.pause();
        position.setValue(0);
        trackNumber++;
        System.out.println("+++ playNext: Load and play the track number " + trackNumber);
        loadTrack();
        play();
    }

    public void loop() {
        minim.stop();
        audioPlayer.pause();
        position.setValue(0);
        loadTrack();
        play();
    }

    public void shuffle() {
        //isShuffling.set(true);
        ArrayList<Track> shuffledTracks = playlist.getTracks();
        Collections.shuffle(shuffledTracks);
        playlist.setTracks(shuffledTracks);
        shuffledTracks.toString();
        if (!isPlaying()) {
            minim.stop();
            position.setValue(0);
            trackNumber++;
            loadTrack();
        }
    }

    public void mute(){
        audioPlayer.mute();
    }

    public int getPosition() {
        return position.get();
    }

    public void setPosition(int position) {
        this.position.set(position);
    }

    public final SimpleIntegerProperty positionProperty() {
        return this.position;
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
        return isTrackLoaded;
    }

    public void setTrackLoaded(boolean trackLoaded) {
        isTrackLoaded = trackLoaded;
    }

    void draw() {

    }

    public void resetTrackOrder() {
        playlist.setTracks(PlaylistManager.trackList);
        System.out.println("+++ resetTrackOrder: " + PlaylistManager.trackList.toString());
    }

    public void unmute() {
        audioPlayer.unmute();
    }

    public int getTrackNumber(Track track) {
        for(int i = 0; i < numberOfTracks; i++){
            if(tracks.get(i).getTitle() == track.getTitle()){
                trackNumber = i;
            }
        }
        System.out.println("+++ getTrackNumber: " + trackNumber + " = " + tracks.get(trackNumber).toString());
        return trackNumber;
    }

    public void setTrackNumber(int trackNumber) {
        this.trackNumber = trackNumber;
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
}

