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
        System.out.println("+++ MP3Player: minim instance started.");
    }

    public void play() {
        minim = new SimpleMinim();
        System.out.println("+++ play: new minim instance started.");
        if(isTrackLoaded) {
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
                    audioPlayer.play(position.getValue());
                    System.out.println("+++ playThread: Player status = " +  audioPlayer.isPlaying());
                    currentTimeThread.interrupt();
                    System.out.println("+++ playThread: position = " + position.get());
                    System.out.println("+++ playThread: duration = " + duration);

                    if (position.getValue()>duration-1000) skipNext();
                }
            });
            playThread.start();
        } else{
            System.out.println("+++ play: track is not loaded");
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
        minim.stop();
        setTrack(playlist.getTracks().get(trackNumber));
        duration = track.get().getDuration(); // used further to calculate remaining time
        String trackPath = track.get().getTrackFilePath();
        audioPlayer = minim.loadMP3File(trackPath);

        System.out.println("+++ loadTrack: Loaded track " + trackNumber + " - " + track.get().getTitle() + " - " + track.get().getArtist());
        isTrackLoaded = true;
    }

    private boolean isLastTrack() {
        if (trackNumber == numberOfTracks - 1){
            System.out.println("+++ isLastTrack: Track number = " + trackNumber);
            return true;
        }
        System.out.println("+++ isLastTrack: Is not the last track.");
        return false;
    }

    public void pause() {
        minim.stop();
        System.out.println("+++ pause: Paused.");
        isPlaying.set(false);
        audioPlayer.pause();
        position.setValue(audioPlayer.position());

    }

    public void volume(String gain) {
        float newGain = Float.parseFloat(gain);
        audioPlayer.setGain(newGain);
    }

    public void skipNext() {
        minim.stop();
        if(!isLastTrack()) {
            trackNumber++;
            System.out.println("+++ skipNext: It was not the last track.");
            position.setValue(0);
            if (isPlaying.get()) {
                System.out.println("+++ skipNext: It was playing when skip next pressed.");
                audioPlayer.pause();
                // kill the running play thread?
                System.out.println("+++ skipNext: Load and play track number " + trackNumber);
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

    public void playNext() {
        minim.stop();
        audioPlayer.pause();
        position.setValue(0);
        trackNumber++;
        System.out.println("+++ playNext: Load and play the track number " + trackNumber);
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

