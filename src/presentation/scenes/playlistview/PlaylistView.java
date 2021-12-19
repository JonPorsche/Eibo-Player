package presentation.scenes.playlistview;

import business.data.Track;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import presentation.uicomponents.TopView;
import presentation.uicomponents.TopViewController;

public class PlaylistView extends BorderPane {

    VBox trackListContainer = new VBox();
    ListView<Track> trackListView = new ListView<>();

    public PlaylistView(){
        this.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        this.getStyleClass().add("container");
        this.setCenter(trackListView);
    }
}
