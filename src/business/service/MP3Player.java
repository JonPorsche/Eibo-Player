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
        position = new SimpleIntegerProperty();
        remainingTime = new SimpleIntegerProperty();
        track = new SimpleObjectProperty<Track>();
        isPlaying = new SimpleBooleanProperty();
        isFirstTrack = new SimpleBooleanProperty(true);
        numberOfTracks = playlist.numberOfTracks();
        minim = new SimpleMinim();
    }

    public void play() {
        if(isTrackLoaded) {
            playThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("+++ Play thread started +++");
                    Thread currentTimeThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("+++ Current Time thread started +++");
                            while (true) {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    System.out.println("+++ Current Time thread says: Another thread woke me up +++\n");
                                    return;
                                }
                                position.setValue(audioPlayer.position());
                                remainingTime.setValue(duration - position.get());
                            }
                        }
                    });
                    currentTimeThread.start();

                    isPlaying.set(true);
                    audioPlayer.play(position.getValue());
                    currentTimeThread.interrupt();

                    if (isPlaying.get() && !isLastTrack()) {
                        playNext();
                    } else {
                        isPlaying.setValue(false);
                    }
                }
            });
            playThread.start();
        } else{
            loadTrack();
            play();
        }
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

    public void loadTrack() {
        setTrack(playlist.getTracks().get(trackNumber));
        duration = track.get().getDuration(); // used further to calculate remaining time
        String trackPath = track.get().getTrackFilePath();
        audioPlayer = minim.loadMP3File(trackPath);

        System.out.println("\n### " + trackNumber + " - " + track.get().getTitle() + " - " + track.get().getArtist() + " ###\n");

        isTrackLoaded = true;
    }

    private boolean isLastTrack() {
        if (trackNumber == numberOfTracks - 1){
            System.out.println("*** Is last track ***\n");
            return true;
        }
        return false;
    }

    public void pause() {
        isPlaying.set(false);
        audioPlayer.pause();
        position.setValue(audioPlayer.position());

    }

    public void volume(String gain) {
        float newGain = Float.parseFloat(gain);
        audioPlayer.setGain(newGain);
    }

    public void skipNext() {
        if(!isLastTrack()) {
            trackNumber++;
            position.setValue(0);
            if (isPlaying.get()) {
                System.out.println("+++ is playing when skip next pressed +++");
                audioPlayer.pause();
                System.out.println("Player is playing = " + audioPlayer.isPlaying());
                // kill the running play thread?
                loadTrack();
                play();
            } else {
                loadTrack();
            }
        } else {
            System.out.println("!!! Cannot skip !!!");
            return;
        }
    }

    public void playNext() {
        position.setValue(0);
        trackNumber++;
        loadTrack();
        play();
    }

    public int getPosition() {
        return position.get();
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
}

