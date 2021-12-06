package business.service;

import business.data.Playlist;
import business.data.Track;
import de.hsrm.mi.eibo.simpleplayer.SimpleAudioPlayer;
import de.hsrm.mi.eibo.simpleplayer.SimpleMinim;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class MP3Player {

    private SimpleMinim minim;
    private static SimpleAudioPlayer audioPlayer;
    public int trackNumber = 0;
    private SimpleBooleanProperty isPlaying;
    private SimpleBooleanProperty isFirstTrack;
    private SimpleIntegerProperty currentTime;
    private SimpleIntegerProperty remainingTime;
    private SimpleObjectProperty<Track> track;
    public Playlist playlist;
    private Thread playThread;
    private int numberOfTracks; // used further to test if there are remaining Tracks

    public MP3Player(Playlist playlist) {
        this.playlist = playlist;
        currentTime = new SimpleIntegerProperty();
        remainingTime = new SimpleIntegerProperty();
        track = new SimpleObjectProperty<Track>();
        isPlaying = new SimpleBooleanProperty();
        isFirstTrack = new SimpleBooleanProperty(true);
        numberOfTracks = playlist.numberOfTracks();
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
        setTrack(playlist.getTracks().get(trackNumber));
        int duration = track.get().getDuration(); // used further to calculate remaining time
        String trackPath = track.get().getTrackFilePath();
        audioPlayer = minim.loadMP3File(trackPath);

        playThread = new Thread(new Runnable() {

            @Override
            public void run() {
                    System.out.println("Hello from Play thread");
                    Thread currentTimeThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("Hello from Current Time thread");
                            while (true) {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    System.out.println("Current Time thread says: Another thread woke me up");
                                    return;
                                }
                                currentTime.setValue(audioPlayer.position());
                                remainingTime.setValue(duration - currentTime.get());
                            }
                        }
                    });
                    currentTimeThread.start();

                    isPlaying.set(true);
                    audioPlayer.play(currentTime.getValue());
                    currentTimeThread.interrupt();

                    if (!isPlaying.get() && isLastTrack()) {
                        skipNext();
                    } else {
                        isPlaying.setValue(false);
                    }
                }
        });
        playThread.start();

/*        new Thread() {
            @Override
            public void run() {

                this.setName("Play Thread");
                System.out.println("Hello from " + this.getName());

                Thread currentTimeThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Hello from Current Time thread");
                        while (true) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                //Thread.currentThread().interrupt();
                                System.out.println("Another thread woke me up");
                                return;
                            }
                            currentTime.setValue(audioPlayer.position());
                            remainingTime.setValue(duration - currentTime.get());
                        }
                    }
                });

                currentTimeThread.start();

                isPlaying.set(true);
                audioPlayer.play(currentTime.getValue());
                currentTimeThread.interrupt();

                if (isPlaying.get() && !isLastTrack()) {
                    skipNext();
                } else {
                    isPlaying.setValue(false);
                }
            }

            private boolean isLastTrack() {
                if (currentTrackIndex == numberOfTracks - 1) return true;
                return false;
            }
        }.start();*/
    }

    private boolean isLastTrack() {
        if (trackNumber == numberOfTracks - 1) return true;
        return false;
    }

    public void pause() {
        isPlaying.set(false);
        currentTime.setValue(audioPlayer.position());
        audioPlayer.pause();
    }

    public void volume(String gain) {
        float newGain = Float.parseFloat(gain);
        audioPlayer.setGain(newGain);
    }

    public void skipNext() {
            audioPlayer.pause();
            trackNumber++;
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

    public SimpleBooleanProperty isPlayingProperty() {
        return isPlaying;
    }

    public boolean isPlaying() {
        return isPlaying.get();
    }

    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying.set(isPlaying);
    }

    public SimpleBooleanProperty isFirstTrackProperty(){
        return isFirstTrack;
    }

    void draw()
    {

    }
}

