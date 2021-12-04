package business.service;

import business.data.Playlist;
import de.hsrm.mi.eibo.simpleplayer.SimpleAudioPlayer;
import de.hsrm.mi.eibo.simpleplayer.SimpleMinim;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleIntegerProperty;

public class MP3Player {

    private SimpleMinim minim;
    private static SimpleAudioPlayer audioPlayer;
    private int currentTrack = 0;
    public boolean isPlaying;
    private SimpleIntegerProperty currentTime;
    Playlist playlist;

    public MP3Player(Playlist playlist) {
        this.playlist = playlist;
        currentTime = new SimpleIntegerProperty();
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
        String currentTrackPath = playlist.getTracks().get(currentTrack).getSongFilePath();
        audioPlayer = minim.loadMP3File(currentTrackPath);

        new Thread() {
            @Override
            public void run() {
                while (true) {
                    System.out.println(currentTime);
                    currentTime.setValue(audioPlayer.position());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                isPlaying = true;
                audioPlayer.play(currentTime.getValue());
            }
        }.start();
    }

    public void pause() {
        isPlaying = false;
        audioPlayer.pause();
    }

    public void volume(String gain) {
        float newGain = Float.parseFloat(gain);
        System.out.println(newGain);
        audioPlayer.setGain(newGain);
    }

    public void skipNext() {
        currentTime.setValue(0);
        currentTrack++;
        play();
    }

    public int getCurrentTime() {
        return currentTime.get();
    }

    public final SimpleIntegerProperty currentTimeProperty() {
        return this.currentTime;
    }
}

