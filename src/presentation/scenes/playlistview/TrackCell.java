package presentation.scenes.playlistview;

import business.data.Track;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TrackCell extends ListCell<Track> {

    private HBox listItemBox;
    private VBox infoBox;
    private Label title;
    private Label artist;
    private Label album;

    public TrackCell() {
        // View definition
        listItemBox = new HBox();
        infoBox = new VBox();
        title = new Label();
        artist = new Label();
        album = new Label();
        //listItemBox.getStyleClass().add("border-to-test-orange");

        infoBox.getChildren().addAll(title, artist, album);
        listItemBox.getChildren().add(infoBox);
    }

    protected void updateItem(Track item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        setGraphic(null);

        if (!empty) {
            title.setText(item.getTitle());
            artist.setText(item.getArtist());
            album.setText(item.getAlbumTitle());

            this.setGraphic(listItemBox);
        }
    }
}
