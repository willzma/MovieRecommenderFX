package edu.gatech.MovieRecommenderFX.controller;

import edu.gatech.MovieRecommenderFX.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DialogController implements Initializable {

    @FXML private Text dialogText;
    @FXML private ButtonBar choiceButtonBar;
    @FXML private Button yesButton;
    @FXML private Button noButton;
    @FXML private ImageView imageView;

    @FXML private ButtonBar buttonBar;
    @FXML private Button closeButton;

    // Window variables
    private Stage stage;
    private double xOffset = 0;
    private double yOffset = 0;

    private Stage owner;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        assert dialogText != null : "id=\"dialogText\" was not injected: check your FXML file 'dialog.fxml'.";
        assert choiceButtonBar != null : "id=\"choiceButtonBar\" was not injected: check your FXML file 'dialog.fxml'.";
        assert yesButton != null : "id=\"yesButton\" was not injected: check your FXML file 'dialog.fxml'.";
        assert noButton != null : "id=\"noButton\" was not injected: check your FXML file 'dialog.fxml'.";
        assert buttonBar != null : "id=\"buttonBar\" was not injected: check your FXML file 'dialog.fxml'.";
        assert closeButton != null : "id=\"closeButton\" was not injected: check your FXML file 'dialog.fxml'.";

        imageView.setImage(new Image(Main.getInstance().getClass().getResourceAsStream("view\\icons\\errorAlert.png")));

        // default control logic
        yesButton.setOnMouseClicked(event -> {
            stage.close();
            owner.close();
            Main.getTabPane().getSelectionModel().select(3);
        });

        noButton.setOnMouseClicked(event -> stage.close());

        buttonBar.setOnMousePressed(event -> {
            xOffset = stage.getX() - event.getScreenX();
            yOffset = stage.getY() - event.getScreenY();
        });
        buttonBar.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + xOffset);
            stage.setY(event.getScreenY() + yOffset);
        });
        closeButton.setOnMouseClicked(event -> stage.close());
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
    }

    public Text getDialogText() { return dialogText; }
    public ButtonBar getChoiceButtonBar() { return choiceButtonBar; }
    public Button getYesButton() { return yesButton; }
    public Button getNoButton() { return noButton; }

    public void setDialogText(Text dialogText) { this.dialogText = dialogText; }
    public void setChoiceButtonBar(ButtonBar choiceButtonBar) { this.choiceButtonBar = choiceButtonBar; }
    public void setYesButton(Button yesButton) { this.yesButton = yesButton; }
    public void setNoButton(Button noButton) { this.noButton = noButton; }

    public void sendOwner(Stage stage) { this.owner = stage; }
    public void sendStage(Stage stage) { this.stage = stage; }
}
