package edu.gatech.MovieRecommenderFX.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class MovieProfileController implements Initializable {

    @FXML private ImageView imageView;
    @FXML private TextArea textArea;
    @FXML private ListView listView;
    @FXML private Text titleText;
    @FXML private Text ratedText;
    @FXML private Text runtimeText;
    @FXML private Text genreText;
    @FXML private Text releasedText;
    @FXML private Text imdbText;
    @FXML private Text descriptionText;
    @FXML private Text reviewText;
    @FXML private ButtonBar starButtonBar;
    @FXML private Button submitButton;

    @FXML private ButtonBar buttonBar;
    @FXML private Button closeButton;
    @FXML private ProgressIndicator progressIndicator;
    @FXML private Text loadingText;

    private ArrayList<Node> visibleItems;

    private Stage stage;
    private static double xOffset = 0;
    private static double yOffset = 0;

    private String title;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        assert imageView != null : "id=\"imageView\" was not injected: check your FXML file 'movie_profile.fxml'.";
        assert textArea != null : "id=\"textArea\" was not injected: check your FXML file 'movie_profile.fxml'.";
        assert listView != null : "id=\"listView\" was not injected: check your FXML file 'movie_profile.fxml'.";
        assert titleText != null : "id=\"titleText\" was not injected: check your FXML file 'movie_profile.fxml'.";
        assert ratedText != null : "id=\"ratedText\" was not injected: check your FXML file 'movie_profile.fxml'.";
        assert runtimeText != null : "id=\"runtimeText\" was not injected: check your FXML file 'movie_profile.fxml'.";
        assert genreText != null : "id=\"genreText\" was not injected: check your FXML file 'movie_profile.fxml'.";
        assert releasedText != null : "id=\"releasedText\" was not injected: check your FXML file 'movie_profile.fxml'.";
        assert imdbText != null : "id=\"imdbText\" was not injected: check your FXML file 'movie_profile.fxml'.";
        assert descriptionText != null : "id=\"descriptionText\" was not injected: check your FXML file 'movie_profile.fxml'.";
        assert reviewText != null : "id=\"reviewText\" was not injected: check your FXML file 'movie_profile.fxml'.";
        assert starButtonBar != null : "id=\"starButtonBar\" was not injected: check your FXML file 'movie_profile.fxml'.";
        assert submitButton != null : "id=\"submitButton\" was not injected: check your FXML file 'movie_profile.fxml'.";
        assert buttonBar != null : "id=\"buttonBar\" was not injected: check your FXML file 'movie_profile.fxml'.";
        assert closeButton != null : "id=\"closeButton\" was not injected: check your FXML file 'movie_profile.fxml'.";
        assert progressIndicator != null : "id=\"progressIndicator\" was not injected: check your FXML file 'movie_profile.fxml'.";
        assert loadingText != null : "id=\"loadingText\" was not injected: check your FXML file 'movie_profile.fxml'.";

        visibleItems = new ArrayList<>();
        visibleItems.addAll(Arrays.asList(imageView, textArea, listView, titleText, ratedText,
                runtimeText, genreText, releasedText, imdbText, descriptionText, reviewText,
                starButtonBar, submitButton));

        // initialize control logic
        buttonBar.setOnMousePressed(event -> {
            xOffset = stage.getX() - event.getScreenX();
            yOffset = stage.getY() - event.getScreenY();
        });
        buttonBar.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + xOffset);
            stage.setY(event.getScreenY() + yOffset);
        });
        closeButton.setOnMouseClicked(event -> {
            stage.close();
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
    }

    public void setItemsVisibility(boolean b) {
        for (Node n : visibleItems) {
            n.setVisible(b);
        }
    }

    public void sendStage(Stage stage) {
        this.stage = stage;
    }

    public void sendTitle(String title) {
        this.title = title;
    }
}
