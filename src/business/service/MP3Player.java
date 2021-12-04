package business.service;

import business.data.Playlist;
import ddf.minim.AudioPlayer;
import de.hsrm.mi.eibo.simpleplayer.SimpleAudioPlayer;
import de.hsrm.mi.eibo.simpleplayer.SimpleMinim;

public class MP3Player {

    private SimpleMinim minim;
    private static SimpleAudioPlayer audioPlayer;
    private int currentTrack = 0;
    public boolean isPlaying;
    public int position = 0;
    Playlist playlist;

    public MP3Player(Playlist playlist) {
        this.playlist = playlist;
    }

    public void play(String title) {
        audioPlayer = minim.loadMP3File("music/" + title + ".mp3");
        new Thread(){
            @Override
            public void run() {
                audioPlayer.play();
            }
        }.start();
    }

    public void play(){
        minim = new SimpleMinim();
        String currentTrackPath = playlist.getTracks().get(currentTrack).getSongFilePath();
        audioPlayer = minim.loadMP3File(currentTrackPath);
        new Thread(){
            @Override
            public void run() {
                isPlaying = true;
                audioPlayer.play(position);
            }
        }.start();

    }

    public void pause() {
        isPlaying = false;
        audioPlayer.pause();
        position = audioPlayer.position();
    }

    public void volume(String gain){
        float newGain = Float.parseFloat(gain);
        System.out.println(newGain);
        audioPlayer.setGain(newGain);
    }

    public void skipNext(){
        position = 0;
        currentTrack++;
        audioPlayer.play();
    }
}

