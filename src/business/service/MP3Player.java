package business.service;

import ddf.minim.AudioPlayer;
import de.hsrm.mi.eibo.simpleplayer.SimpleAudioPlayer;
import de.hsrm.mi.eibo.simpleplayer.SimpleMinim;

public class MP3Player {

    private SimpleMinim minim = new SimpleMinim();
    private static SimpleAudioPlayer audioPlayer;

    public void play() {
        audioPlayer = minim.loadMP3File("music/02_Drei_Worte.mp3");
        audioPlayer.play();
    }

    public void pause() {
        audioPlayer.pause();
    }
}
