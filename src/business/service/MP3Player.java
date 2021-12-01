package business.service;

import ddf.minim.AudioPlayer;
import de.hsrm.mi.eibo.simpleplayer.SimpleAudioPlayer;
import de.hsrm.mi.eibo.simpleplayer.SimpleMinim;

public class MP3Player {

    private SimpleMinim minim;
    private static SimpleAudioPlayer audioPlayer;
    public boolean isPlaying;
    public int position = 0;

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
        audioPlayer = minim.loadMP3File("music/12 Fear Of The Dark.mp3");
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
}

