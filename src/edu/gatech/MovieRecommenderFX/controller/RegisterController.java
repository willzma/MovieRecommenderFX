package edu.gatech.MovieRecommenderFX.controller;

import edu.gatech.MovieRecommenderFX.Main;
import edu.gatech.MovieRecommenderFX.model.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private Button registerButton;
    @FXML private Button cancelButton;
    @FXML private Text messageDialog;
    @FXML private ButtonBar buttonBar;
    @FXML private Button closeButton;

    private static SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        assert nameField != null : "id=\"nameField\" was not injected: check your FXML file 'register.fxml'.";
        assert emailField != null : "id=\"emailField\" was not injected: check your FXML file 'register.fxml'.";
        assert usernameField != null : "id=\"usernameField\" was not injected: check your FXML file 'register.fxml'.";
        assert passwordField != null : "id=\"passwordField\" was not injected: check your FXML file 'register.fxml'.";
        assert registerButton != null : "id=\"registerButton\" was not injected: check your FXML file 'register.fxml'.";
        assert cancelButton != null : "id=\"cancelButton\" was not injected: check your FXML file 'register.fxml'.";
        assert messageDialog != null : "id=\"messageDialog\" was not injected: check your FXML file 'register.fxml'.";
        assert buttonBar != null : "id=\"buttonBar\" was not injected: check your FXML file 'register.fxml'.";
        assert closeButton != null : "id=\"closeButton\" was not injected: check your FXML file 'register.fxml'.";

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
        registerButton.setOnMouseClicked(event -> {
            if ("".equals(nameField.getText()) || "".equals(emailField.getText())
                    || "".equals(usernameField.getText()) || "".equals(passwordField.getText())) {
                messageDialog.setFill(Color.RED);
                messageDialog.setText("Please fill out every field to continue.");
            } else if (Main.getAllUsers().containsKey(usernameField.getText())) {
                messageDialog.setFill(Color.RED);
                messageDialog.setText("Sorry, that username is already taken.");
            } else {
                messageDialog.setFill(Color.GREEN);
                messageDialog.setText("Account registered.");
                User tempUser = new User(nameField.getText(), emailField.getText(), usernameField.getText(), Main.getDigest(passwordField.getText()), sdf.format(new Date()));
                tempUser.setStatus("Active");
                Main.setCurrentUser(tempUser);
                Main.getDBReference().child("users").child(tempUser.getUsername()).setValue(tempUser.toMap());
                Main.getAllUsers().put(tempUser.getUsername(), tempUser);
                Main.loadingCallback("dashboard.fxml");
            }
        });

        cancelButton.setOnMouseClicked(event -> Main.loadingCallback("login.fxml"));
    }
}
