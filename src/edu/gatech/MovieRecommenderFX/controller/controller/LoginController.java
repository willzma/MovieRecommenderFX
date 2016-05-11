package edu.gatech.MovieRecommenderFX.controller.controller;

import edu.gatech.MovieRecommenderFX.controller.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button registerButton;
    @FXML
    private Text messageDialog;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        assert usernameField != null : "id=\"usernameField\" was not injected: check your FXML file 'login.fxml'.";
        assert passwordField != null : "id=\"passwordField\" was not injected: check your FXML file 'login.fxml'.";
        assert loginButton != null : "id=\"loginButton\" was not injected: check your FXML file 'login.fxml'.";
        assert cancelButton != null : "id=\"cancelButton\" was not injected: check your FXML file 'login.fxml'.";
        assert registerButton != null : "id=\"registerButton\" was not injected: check your FXML file 'login.fxml'.";
        assert messageDialog != null : "id=\"messageDialog\" was not injected: check your FXML file 'login.fxml'.";

        // initialize button logic
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
                    for (int i = 0; i < 10000; i++) { }
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
