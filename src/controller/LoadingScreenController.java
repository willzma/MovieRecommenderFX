package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressIndicator;

import java.net.URL;
import java.util.ResourceBundle;

public class LoadingScreenController implements Initializable {

    @FXML
    private ProgressIndicator loadingWheel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        assert loadingWheel != null : "id=\"loadingWheel\" was not injected: check your FXML file 'loading_screen.fxml'.";
    }
}
