package business.service;

import de.hsrm.mi.prog.util.StaticScanner;

public class KeyboardController {

    private static final String PLAY = "play";
    private static final String PAUSE = "pause";
    private MP3Player mp3Player = new MP3Player();
    private PlaylistManager playlistManager = new PlaylistManager();

    public void start(){
        String input = StaticScanner.nextString();
        String parts[] = input.split(" ", 2);
        // System.out.println(String.format("cr: %s, cdr: %s", parts[0], parts[1]));

        switch (parts[0]){
            case PLAY:
                mp3Player.play(parts[1]);
                break;
            case PAUSE:
                mp3Player.pause();
                break;
        }
    }
}
