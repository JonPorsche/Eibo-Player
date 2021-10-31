
public class KeyboardController {

    private static final String PLAY = "play";
    private static final String PAUSE = "pause";
    private MP3Player mp3Player = new MP3Player();

    public void start(){
        String test = StaticScanner.nextString();
        switch (test){
            case PLAY:
                mp3Player.play();
                break;
            case PAUSE:
                mp3Player.pause();
                break;
        }
    }
}
