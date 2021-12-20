package presentation.scenes.startview;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import presentation.application.MainApp;

public class StartViewController {
    private MainApp applicaton;
    private Label loadPlaylistLabel;
    private Pane rootView;

    public StartViewController(MainApp application){
        this.applicaton = application;

        StartView startView = new StartView();
        rootView = startView;
        initialize();
    }

    public void initialize(){

    }

    public Pane getRootView() {
        return rootView;
    }
}
