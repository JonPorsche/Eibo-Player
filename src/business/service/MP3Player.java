package business.service;

import ddf.minim.AudioPlayer;
import de.hsrm.mi.eibo.simpleplayer.SimpleAudioPlayer;
import de.hsrm.mi.eibo.simpleplayer.SimpleMinim;

public class MP3Player {

    private SimpleMinim minim = new SimpleMinim();
    private static SimpleAudioPlayer audioPlayer;

    public void play(String title) {
        audioPlayer = minim.loadMP3File("music/" + title + ".mp3");
        audioPlayer.play();
    }

    public void pause() {
        audioPlayer.pause();
    }
}

