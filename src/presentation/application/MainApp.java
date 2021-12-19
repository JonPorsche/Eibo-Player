package presentation.application;

import business.service.MP3Player;
import business.service.PlaylistManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import presentation.scenes.playerview.PlayerViewController;
import presentation.scenes.playlistview.PlaylistViewController;
import presentation.scenes.startview.StartViewController;
import presentation.scenes.controlview.ControlViewController;
import presentation.scenes.topview.TopViewController;
import java.io.*;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.Properties;

public class MainApp extends Application {

    public Properties defaultProp = null;
    public Properties appProp = null;
    private PlaylistManager playlistManager = new PlaylistManager();
    private MP3Player player;
    private Map<String, Pane> scenes;
    private Stage primaryStage;
    private Pane topPane;
    private Pane centerPane;
    private Pane bottomPane;
    private BorderPane rootBorderPane;
    private double windowWidth;
    private double windowHeight;
    public static String playlistPath;

    // CONTROLLERS
    private TopViewController topViewController;
    private PlayerViewController playerViewController;
    private PlaylistViewController playlistViewController;
    private ControlViewController controlViewController;
    private StartViewController startViewController;

    public static void main(String[] args) {
        new Thread(() -> launch(args)).start();
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            startControllers();
            loadScenes();
            setInitialView();
            setRootBorderPanes();

            Scene scene = new Scene(rootBorderPane, windowWidth, windowHeight);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

            this.primaryStage = primaryStage;
            primaryStage.setScene(scene);
            primaryStage.setTitle("iTues");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startControllers() {
        topViewController = new TopViewController(this);
        playerViewController = new PlayerViewController(this, player);
        playlistViewController = new PlaylistViewController(this, playlistManager.playlist, player);
        controlViewController = new ControlViewController(this, player);
        startViewController = new StartViewController(this);
    }

    private void loadScenes() {
        scenes = new HashMap<String, Pane>();
        scenes.put("TopView", topViewController.getRootView());
        scenes.put("PlayerView", playerViewController.getRootView());
        scenes.put("PlaylistView", playlistViewController.getRootView());
        scenes.put("ControlView", controlViewController.getRootView());
        scenes.put("StartView", startViewController.getRootView());
    }

    private void setInitialView() {
        topPane = scenes.get("TopView");
        centerPane = scenes.get("PlayerView");
        bottomPane = scenes.get("ControlView");
    }

    private void setRootBorderPanes() {
        rootBorderPane = new BorderPane();
        rootBorderPane.setTop(topPane);
        rootBorderPane.setCenter(centerPane);
        rootBorderPane.setBottom(bottomPane);
    }

    public void switchCenterPane(String scene) {
        if (scenes.containsKey(scene)) {
            setCenterPane(scenes.get(scene));
            rootBorderPane.setCenter(centerPane);
        }
    }

    public void setCenterPane(Pane centerPane) {
        this.centerPane = centerPane;
    }

    @Override
    public void init() {
        loadInitialProperties();

        try {
            playlistManager.playlist = playlistManager.getPlaylistFromM3U(playlistPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.player = new MP3Player(playlistManager.playlist);

        loadNextProperties();
    }

    private void loadInitialProperties(){
        try {
            defaultProp = new Properties();
            FileInputStream finDefault = new FileInputStream("./properties/default.xml");
            defaultProp.loadFromXML(finDefault);

            appProp = new Properties(defaultProp);
            FileInputStream finIndividual = new FileInputStream("./properties/individual.xml");
            appProp.loadFromXML(finIndividual);

            playlistPath = appProp.getProperty("playlist-path");

        } catch (InvalidPropertiesFormatException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadNextProperties(){
        try {
            defaultProp = new Properties();
            FileInputStream finDefault = new FileInputStream("./properties/default.xml");
            defaultProp.loadFromXML(finDefault);

            appProp = new Properties(defaultProp);
            FileInputStream finIndividual = new FileInputStream("./properties/individual.xml");
            appProp.loadFromXML(finIndividual);

            windowWidth = Double.valueOf(appProp.getProperty("window_width"));
            windowHeight = Double.valueOf(appProp.getProperty("window_height"));
            player.setVolume(Float.valueOf(appProp.getProperty("volume")));
        } catch (InvalidPropertiesFormatException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        appProp.put("window_width", Double.toString(primaryStage.getWidth()));
        appProp.put("window_height", Double.toString(primaryStage.getHeight()));
        appProp.put("volume", Float.toString(player.getVolume()));
        appProp.put("playlist-path", playlistPath);
        try {
            FileOutputStream fos = new FileOutputStream("./properties/individual.xml");
            appProp.storeToXML(fos, "Individual Properties");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(1);
    }
}
