package edu.gatech.MovieRecommenderFX.controller;

import edu.gatech.MovieRecommenderFX.Main;
import edu.gatech.MovieRecommenderFX.model.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button registerButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Text messageDialog;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        assert nameField != null : "id=\"nameField\" was not injected: check your FXML file 'register.fxml'.";
        assert emailField != null : "id=\"emailField\" was not injected: check your FXML file 'register.fxml'.";
        assert usernameField != null : "id=\"usernameField\" was not injected: check your FXML file 'register.fxml'.";
        assert passwordField != null : "id=\"passwordField\" was not injected: check your FXML file 'register.fxml'.";
        assert registerButton != null : "id=\"registerButton\" was not injected: check your FXML file 'register.fxml'.";
        assert cancelButton != null : "id=\"cancelButton\" was not injected: check your FXML file 'register.fxml'.";
        assert messageDialog != null : "id=\"messageDialog\" was not injected: check your FXML file 'register.fxml'.";

        // initialize button logic
        registerButton.setOnMouseClicked(event -> {
            if ("".equals(nameField.getText()) || "".equals(emailField.getText())
                    || "".equals(usernameField.getText()) || "".equals(passwordField.getText())) {
                messageDialog.setFill(Color.RED);
                messageDialog.setText("Please fill out every field to continue.");
            } else if (Main.allUsers.containsKey(usernameField.getText())) {
                messageDialog.setFill(Color.RED);
                messageDialog.setText("Sorry, that username is already taken.");
            } else {
                messageDialog.setFill(Color.GREEN);
                messageDialog.setText("Account registered.");
                for (int i = 0; i < 10000; i++) { }
                User tempUser = new User(nameField.getText(), emailField.getText(), usernameField.getText(), Main.getDigest(passwordField.getText()));
                tempUser.setStatus("Active");
                Main.currentUser = tempUser;
                Main.USER_TABLE.child(tempUser.getUsername()).setValue(tempUser.toMap());
                Main.allUsers.put(tempUser.getUsername(), tempUser);
                Main.loadingCallback("dashboard.fxml");
            }
        });

        cancelButton.setOnMouseClicked(event -> Main.loadingCallback("login.fxml"));
    }
}
