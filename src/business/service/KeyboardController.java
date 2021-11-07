package business.service;

import de.hsrm.mi.prog.util.StaticScanner;

public class KeyboardController{

    private static final String PLAY = "play";
    private static final String PAUSE = "pause";
    private static final String VOLUME = "volume";

    private MP3Player mp3Player = new MP3Player();
    private StaticScanner scanner = new StaticScanner();

    public KeyboardController() {
        this.start();
    }

    public void start() {
        String input = scanner.nextString();
        String[] commands = input.split(" ", 2);

        switch (commands.length) {
            case 1:
                switch (commands[0]) {
                    case PLAY:
                        mp3Player.play();
                        start();
                        break;
                    case PAUSE:
                        mp3Player.pause();
                        start();
                        break;
                }
            case 2:
                switch (commands[0]) {
                    case PLAY:
                        mp3Player.play(commands[1]);
                        start();
                        break;
                    case VOLUME:
                        mp3Player.volume(commands[1]);
                        start();
                        break;
                }
        }
    }

/*    public void start() {
        MP3Player mp3Player = new MP3Player();
        String input;
        String[] commands;
        StaticScanner scanner = new StaticScanner();

        do {
            input = scanner.nextString();
            commands = input.split(" ", 2);
            switch (commands.length) {
                case 1:
                    switch (commands[0]) {
                        case PLAY:
                            mp3Player.play("01 Bring Mich Nach Hause");
                            break;
                        case PAUSE:
                            mp3Player.pause();
                            break;
                    }
                case 2:
                    switch (commands[0]) {
                        case PLAY:
                            mp3Player.play(commands[1]);
                            break;
                        case VOLUME:
                            mp3Player.volume(commands[1]);
                            break;
                    }
            }
        } while (true);
    }*/
}
