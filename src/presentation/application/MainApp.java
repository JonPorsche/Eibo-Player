package presentation.application;

import business.service.KeyboardController;
import business.service.PlaylistManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import presentation.scenes.playerview.PlayerView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainApp extends Application {

    Map<String, Pane> scenes;

    public static void main(String[] args) {

        // Load playlist
        new Thread(() -> {
            try {
                PlaylistManager playlistManager = new PlaylistManager();
                playlistManager.getAllTracks("/Users/jonporsche/Documents/Dev Projects.nosync/eibo_test1/music");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        // GUI
        new Thread(() -> launch(args)).start();

        KeyboardController controller = new KeyboardController();
    }

    /* Erzeugung des eigentlichen Views bzw. der Controller */
    @Override
    public void start(Stage primaryStage) {
        try {
            scenes = new HashMap<String, Pane>();

            // Definition der Screens/Scenes
            Pane view = new PlayerView();
            scenes.put("PlayerView", view);

            // Erzeuge Wurzel-Element, dem alle Elemente hinzugefügt werden
            Pane root = scenes.get("PlayerView");

            /*  erzeuge die Szene mit dem Wurzel-Element und setze die Szene
            als aktuelle darzustellende Szene für die Bühne
         */
            Scene scene = new Scene(root, 400, 450);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

            primaryStage.setScene(scene);

            /*  es muss explizit gesagt werden , dass das Fenster sichtbar
            gemacht werden soll
         */
            primaryStage.setTitle("Player");
            primaryStage.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
