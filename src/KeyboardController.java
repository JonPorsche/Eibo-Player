
public class KeyboardController {

    private MP3Player mp3Player = new MP3Player();

    public void start(){
        String test = StaticScanner.nextString();
        switch (test){
            case "play":
                mp3Player.play();
                break;
            case "pause":
                mp3Player.pause();
                break;
        }
    }
}
