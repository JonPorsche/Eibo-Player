package presentation.application;

import business.service.MP3Player;
import business.service.PlaylistManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import presentation.scenes.playerview.PlayerViewController;
import presentation.scenes.playlistview.PlaylistViewController;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainApp extends Application {

    private static PlaylistManager playlistManager = new PlaylistManager();
    private MP3Player player;
    private Map<String, Pane> scenes;
    private Stage primaryStage;
    private static List<File> files = new ArrayList<>();

    public static void main(String[] args) {

        try {
            playlistManager.playlist = playlistManager.getPlaylistFromM3U("./music");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // GUI
        new Thread(() -> launch(args)).start();
    }

    /* Erzeugung des eigentlichen Views bzw. der Controller */
    @Override
    public void start(Stage primaryStage) {


        try {
            scenes = new HashMap<String, Pane>();

            // Definition der Screens/Scenes
            /*Pane view = new PlayerView();
            scenes.put("PlayerView", view);*/

            PlayerViewController playerViewController = new PlayerViewController(this, player);
            scenes.put("PlayerView", playerViewController.getRootView());

            PlaylistViewController playlistViewController = new PlaylistViewController(this, playlistManager.playlist, player);
            scenes.put("PlaylistView", playlistViewController.getRootView());

            // Erzeuge Wurzel-Element, dem alle Elemente hinzugefügt werden
            Pane root = scenes.get("PlayerView");

            /*  erzeuge die Szene mit dem Wurzel-Element und setze die Szene
            als aktuelle darzustellende Szene für die Bühne
         */
            Scene scene = new Scene(root, 450, 800);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

            this.primaryStage = primaryStage;
            primaryStage.setScene(scene);
            /*primaryStage.minWidthProperty().bind(scene.heightProperty().multiply(2));
            primaryStage.minHeightProperty().bind(scene.widthProperty().divide(2));*/

            /*  es muss explizit gesagt werden , dass das Fenster sichtbar
            gemacht werden soll
         */
            primaryStage.setTitle("iTues");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void switchScene(String scene) {
        if (scenes.containsKey(scene) ) {
            primaryStage.getScene().setRoot(scenes.get(scene));
        }
    }

    @Override
    public void init() {
/*
         in der Anwednung gibt es einen Player, der dann von allen
     * Seiten/Views bzw. deren Controllern aus erreichbar ist*/

        this.player = new MP3Player(playlistManager.playlist);


    }
}
