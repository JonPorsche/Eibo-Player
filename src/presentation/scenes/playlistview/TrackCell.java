package presentation.scenes.playlistview;

import business.data.Track;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import presentation.uicomponents.ImageViewPane;

import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class TrackCell extends ListCell<Track> {

    private HBox listItemBox;
    private VBox infoBox;
    private Label title;
    private Label artist;
    private Label album;
    private ImageView coverImage;
    private ImageViewPane coverImageViewPane;

    public TrackCell() {
        // View definition
        listItemBox = new HBox();

        // COVER
        coverImage = new ImageView();
        coverImageViewPane = new ImageViewPane(coverImage);
        coverImageViewPane.setMaxSize(67,67);
        HBox.setMargin(coverImageViewPane, new Insets(5));

        // INFO BOX
        infoBox = new VBox();
        title = new Label();
        artist = new Label();
        album = new Label();
        infoBox.setMaxWidth(193);

        infoBox.getChildren().addAll(title, artist, album);
        listItemBox.getChildren().addAll(coverImageViewPane, infoBox);
    }

    protected void updateItem(Track item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        setGraphic(null);

        if (!empty) {
            coverImage.setImage(new Image(new ByteArrayInputStream(item.getAlbumImage())));
            title.setText(item.getTitle());
            artist.setText(item.getArtist());
            album.setText(item.getAlbumTitle());

            this.setGraphic(listItemBox);
        }
    }
}
