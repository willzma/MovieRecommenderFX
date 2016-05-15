package edu.gatech.MovieRecommenderFX.controller;

import edu.gatech.MovieRecommenderFX.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button cancelButton;
    @FXML private Button registerButton;
    @FXML private Text messageDialog;
    @FXML private ButtonBar buttonBar;
    @FXML private Button closeButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        assert usernameField != null : "id=\"usernameField\" was not injected: check your FXML file 'login.fxml'.";
        assert passwordField != null : "id=\"passwordField\" was not injected: check your FXML file 'login.fxml'.";
        assert loginButton != null : "id=\"loginButton\" was not injected: check your FXML file 'login.fxml'.";
        assert cancelButton != null : "id=\"cancelButton\" was not injected: check your FXML file 'login.fxml'.";
        assert registerButton != null : "id=\"registerButton\" was not injected: check your FXML file 'login.fxml'.";
        assert messageDialog != null : "id=\"messageDialog\" was not injected: check your FXML file 'login.fxml'.";
        assert buttonBar != null : "id=\"buttonBar\" was not injected: check your FXML file 'login.fxml'.";
        assert closeButton != null : "id=\"closeButton\" was not injected: check your FXML file 'login.fxml'.";

        // initialize button logic
        buttonBar.setOnMousePressed(Main::panePressed);
        buttonBar.setOnMouseDragged(Main::paneDragged);

        closeButton.setOnMouseClicked(event -> {
            Main.getSharedStage().close();
            Main.getInstance().stop();
        });
        closeButton.setOnMouseEntered(event -> closeButton.setStyle("-fx-background-radius: 12em;" +
                                                                    "-fx-min-width: 12px;" +
                                                                    "-fx-min-height: 12px;" +
                                                                    "-fx-max-width: 12px;" +
                                                                    "-fx-max-height: 12px;" +
                                                                    "-fx-background-color: #C14645;" +
                                                                    "-fx-background-insets: 0px;" +
                                                                    "-fx-border-color: #B03537;" +
                                                                    "-fx-border-radius: 50%;" +
                                                                    "-fx-padding: 0px;"));
        closeButton.setOnMouseExited(event -> closeButton.setStyle("-fx-background-radius: 12em;" +
                                                                    "-fx-min-width: 12px;" +
                                                                    "-fx-min-height: 12px;" +
                                                                    "-fx-max-width: 12px;" +
                                                                    "-fx-max-height: 12px;" +
                                                                    "-fx-background-color: #FF5C5C;" +
                                                                    "-fx-background-insets: 0px;" +
                                                                    "-fx-border-color: #E33E41;" +
                                                                    "-fx-border-radius: 50%;" +
                                                                    "-fx-padding: 0px;"));
        closeButton.setOnMousePressed(event -> closeButton.setStyle("-fx-background-radius: 12em;" +
                                                                    "-fx-min-width: 12px;" +
                                                                    "-fx-min-height: 12px;" +
                                                                    "-fx-max-width: 12px;" +
                                                                    "-fx-max-height: 12px;" +
                                                                    "-fx-background-color: #4E0002;" +
                                                                    "-fx-background-insets: 0px;" +
                                                                    "-fx-border-color: #4E0002;" +
                                                                    "-fx-border-radius: 50%;" +
                                                                    "-fx-padding: 0px;"));
        loginButton.setOnMouseClicked(event -> {
            if (!Main.allUsers.containsKey(usernameField.getText())) {
                messageDialog.setFill(Color.RED);
                messageDialog.setText("No user exists by that name.");
            } else if ("".equals(passwordField.getText())) {
                messageDialog.setFill(Color.RED);
                messageDialog.setText("Please enter a password.");
            } else {
                String username = usernameField.getText();
                String theirHash = Main.getDigest(passwordField.getText());

                if (theirHash.equals(Main.allUsers.get(username).getPasswordHash())) {
                    messageDialog.setFill(Color.GREEN);
                    messageDialog.setText("Login successful.");
                    for (int i = 0; i < 100000; i++) { }
                    Main.currentUser = Main.allUsers.get(username);
                    Main.loadingCallback("dashboard.fxml");
                } else {
                    messageDialog.setFill(Color.RED);
                    messageDialog.setText("The password you entered is incorrect.");
                }
            }
        });

        cancelButton.setOnMouseClicked(event -> Main.getInstance().stop());
        registerButton.setOnMouseClicked(event -> Main.loadingCallback("register.fxml"));
    }
}
