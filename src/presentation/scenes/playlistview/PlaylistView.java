package presentation.scenes.playlistview;

import business.data.Track;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class PlaylistView extends BorderPane {

    VBox trackListContainer = new VBox();
    ListView<Track> trackListView = new ListView<>();

    public PlaylistView(){
        this.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        this.getStyleClass().add("container");
        this.setCenter(trackListView);
    }
}
