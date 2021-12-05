package business.service;

import business.data.Playlist;
import business.data.Track;
import de.hsrm.mi.eibo.simpleplayer.SimpleAudioPlayer;
import de.hsrm.mi.eibo.simpleplayer.SimpleMinim;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class MP3Player {

    private SimpleMinim minim;
    private static SimpleAudioPlayer audioPlayer;
    public int currentTrackIndex = 0;
    public boolean isPlaying;
    private SimpleIntegerProperty currentTime;
    private SimpleIntegerProperty remainingTime;
    private SimpleObjectProperty<Track> track;
    public Playlist playlist;

    public MP3Player(Playlist playlist) {
        this.playlist = playlist;
        currentTime = new SimpleIntegerProperty();
        remainingTime = new SimpleIntegerProperty();
        track = new SimpleObjectProperty<Track>();
    }

    public void play(String title) {
        audioPlayer = minim.loadMP3File("music/" + title + ".mp3");
        new Thread() {
            @Override
            public void run() {
                audioPlayer.play();
            }
        }.start();
    }

    public void play() {

        minim = new SimpleMinim();

        setTrack(playlist.getTracks().get(currentTrackIndex));

        int duration = track.get().getDuration(); // used further to calculate remaining time
        int numberOfTracks = playlist.numberOfTracks(); // used further to test if there are remaining Tracks

        String trackPath = track.get().getTrackFilePath();
        audioPlayer = minim.loadMP3File(trackPath);

        new Thread() {
            @Override
            public void run() {
                new Thread() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                System.out.println("Thread was interrupted, Failed to complete operation");
                            }
                            currentTime.setValue(audioPlayer.position());
                            remainingTime.setValue(duration - currentTime.get());
                        }
                    }
                }.start();
                isPlaying = true;
                audioPlayer.play(currentTime.getValue());

                if (isPlaying & hasNextTrack()) skipNext();
                else isPlaying = false;
            }

            private boolean hasNextTrack(){
                if(currentTrackIndex < numberOfTracks - 1) return true;
                return false;
            }
        }.start();
    }

    public void pause() {
        isPlaying = false;
        currentTime.setValue(audioPlayer.position());
        audioPlayer.pause();
    }

    public void volume(String gain) {
        float newGain = Float.parseFloat(gain);
        audioPlayer.setGain(newGain);
    }

    public void skipNext() {
        currentTrackIndex++;
        currentTime.setValue(0);
        play();
    }

    public int getCurrentTime() {
        return currentTime.get();
    }

    public final SimpleIntegerProperty currentTimeProperty() {
        return this.currentTime;
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
}

