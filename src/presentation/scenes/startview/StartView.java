package presentation.scenes.startview;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class StartView extends BorderPane {
    Label loadPlaylistLabel;

    public StartView(){
        loadPlaylistLabel = new Label("Load mp3 files");
    }
}
