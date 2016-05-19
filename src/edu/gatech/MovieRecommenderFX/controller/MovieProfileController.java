package edu.gatech.MovieRecommenderFX.controller;

import com.firebase.client.Firebase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.gatech.MovieRecommenderFX.Main;
import edu.gatech.MovieRecommenderFX.model.Movie;
import edu.gatech.MovieRecommenderFX.model.Rating;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

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
    @FXML private HBox starButtonBar;
    @FXML private Button submitButton;

    @FXML private ButtonBar buttonBar;
    @FXML private Button closeButton;
    @FXML private ProgressIndicator progressIndicator;
    @FXML private Text loadingText;

    @FXML private ImageView star1;
    @FXML private ImageView star2;
    @FXML private ImageView star3;
    @FXML private ImageView star4;
    @FXML private ImageView star5;

    private ArrayList<Node> visibleItems;

    // Window variables
    private Stage stage;
    private double xOffset = 0;
    private double yOffset = 0;

    private int starsGiven = 0;

    private Gson gson;

    private static SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
    private String correspondingFormattedTitle;

    Map<String, String> jsonMap;

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
        assert starButtonBar != null : "id=\"starButtonBar\" was not injected: check your FXML file 'movie_profile.fxml'.";
        assert submitButton != null : "id=\"submitButton\" was not injected: check your FXML file 'movie_profile.fxml'.";
        assert buttonBar != null : "id=\"buttonBar\" was not injected: check your FXML file 'movie_profile.fxml'.";
        assert closeButton != null : "id=\"closeButton\" was not injected: check your FXML file 'movie_profile.fxml'.";
        assert progressIndicator != null : "id=\"progressIndicator\" was not injected: check your FXML file 'movie_profile.fxml'.";
        assert loadingText != null : "id=\"loadingText\" was not injected: check your FXML file 'movie_profile.fxml'.";
        assert star1 != null : "id=\"star1\" was not injected: check your FXML file 'movie_profile.fxml'.";
        assert star2 != null : "id=\"star2\" was not injected: check your FXML file 'movie_profile.fxml'.";
        assert star3 != null : "id=\"star3\" was not injected: check your FXML file 'movie_profile.fxml'.";
        assert star4 != null : "id=\"star4\" was not injected: check your FXML file 'movie_profile.fxml'.";
        assert star5 != null : "id=\"star5\" was not injected: check your FXML file 'movie_profile.fxml'.";

        star1.setImage(new Image(Main.getInstance().getClass().getResourceAsStream("view\\icons\\emptyStar.png")));
        star2.setImage(new Image(Main.getInstance().getClass().getResourceAsStream("view\\icons\\emptyStar.png")));
        star3.setImage(new Image(Main.getInstance().getClass().getResourceAsStream("view\\icons\\emptyStar.png")));
        star4.setImage(new Image(Main.getInstance().getClass().getResourceAsStream("view\\icons\\emptyStar.png")));
        star5.setImage(new Image(Main.getInstance().getClass().getResourceAsStream("view\\icons\\emptyStar.png")));

        gson = new Gson();

        visibleItems = new ArrayList<>();
        visibleItems.addAll(Arrays.asList(imageView, textArea, listView, titleText, ratedText, runtimeText,
                genreText, releasedText, imdbText, descriptionText, starButtonBar, submitButton));

        setItemsVisibility(false);

        // initialize control logic
        star1.setOnMouseEntered(event -> setIntStarBar(1));
        star2.setOnMouseEntered(event -> setIntStarBar(2));
        star3.setOnMouseEntered(event -> setIntStarBar(3));
        star4.setOnMouseEntered(event -> setIntStarBar(4));
        star5.setOnMouseEntered(event -> setIntStarBar(5));

        EventHandler<MouseEvent> exit = event -> setIntStarBar(starsGiven);

        star1.setOnMouseExited(exit);
        star2.setOnMouseExited(exit);
        star3.setOnMouseExited(exit);
        star4.setOnMouseExited(exit);
        star5.setOnMouseExited(exit);

        star1.setOnMouseClicked(event -> {
            if (starsGiven == 1) {
                starsGiven = 0;
                setEmptyStarBar();
            } else {
                setIntStarBar(1);
                starsGiven = 1;
            }
        });
        star2.setOnMouseClicked(event -> {
            if (starsGiven == 2) {
                starsGiven = 0;
                setEmptyStarBar();
            } else {
                setIntStarBar(2);
                starsGiven = 2;
            }
        });
        star3.setOnMouseClicked(event -> {
            if (starsGiven == 3) {
                starsGiven = 0;
                setEmptyStarBar();
            } else {
                setIntStarBar(3);
                starsGiven = 3;
            }
        });
        star4.setOnMouseClicked(event -> {
            if (starsGiven == 4) {
                starsGiven = 0;
                setEmptyStarBar();
            } else {
                setIntStarBar(4);
                starsGiven = 4;
            }
        });
        star5.setOnMouseClicked(event -> {
            if (starsGiven == 5) {
                starsGiven = 0;
                setEmptyStarBar();
            } else {
                setIntStarBar(5);
                starsGiven = 5;
            }
        });

        submitButton.setOnMouseClicked(event -> {
            if (Main.getCurrentUser().getProfile() != null) {
                if ("".equals(textArea.getText())) {
                    textArea.setPromptText("Your review appears to be blank. Please write something to post a review.");
                } else {
                    textArea.setPromptText("Add a review...");
                    String postDate = sdf.format(new Date());
                    listView.getItems().add(String.format("%s gave this film %d/5 on %s:\n%s", Main.getCurrentUser().getUsername(), starsGiven, postDate, textArea.getText()));
                    Firebase ref = Main.getDBReference().child("movies");
                    Movie m;

                    if (Main.getAllMovies().containsKey(correspondingFormattedTitle)) {
                        m = Main.getAllMovies().get(correspondingFormattedTitle);
                    } else {
                        m = new Movie(correspondingFormattedTitle);
                        m.setURL(jsonMap.get("Poster"));

                        Map<String, String> mMap = new HashMap<>();
                        mMap.put("title", m.getTitle());
                        mMap.put("averageRating", String.valueOf(m.getAverageRating()));
                        mMap.put("imgURL", m.getURL());

                        ref.child(m.getTitle()).setValue(mMap);
                        Main.getAllMovies().put(m.getTitle(), m);
                    }

                    Rating r = new Rating(starsGiven, textArea.getText(), Main.getCurrentUser(), postDate);
                    ref.child(m.getTitle()).child("ratings").push().setValue(r.toMap());
                    m.addRating(r);

                    Map<String, Object> rMap = new HashMap<>();
                    rMap.put("averageRating", String.valueOf(m.getAverageRating()));
                    ref.child(m.getTitle()).updateChildren(rMap);
                }
            } else {
                try {
                    Stage dialog = new Stage();
                    dialog.initStyle(StageStyle.TRANSPARENT);
                    dialog.initModality(Modality.APPLICATION_MODAL);
                    dialog.getIcons().add(new Image(Main.getInstance().getClass().getResourceAsStream("view\\icons\\icon.png")));
                    dialog.setResizable(false);

                    FXMLLoader fxmlLoader = new FXMLLoader(Main.getInstance().getClass().getResource("view\\dialog.fxml"));
                    Parent root = fxmlLoader.load();
                    Rectangle windowRect = new Rectangle(600, 100);
                    windowRect.setArcHeight(15.0);
                    windowRect.setArcWidth(15.0);
                    root.setClip(windowRect);

                    Scene scene = new Scene(root, 600, 100);
                    scene.setFill(Color.TRANSPARENT);

                    DialogController dc = fxmlLoader.getController();
                    dc.sendStage(dialog);
                    dc.sendOwner(stage);

                    dialog.setScene(scene);
                    dialog.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

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

    public void setIntStarBar(int stars) {
        switch (stars) {
            case 0: {
                setEmptyStarBar();
            } break;case 1: {
                star1.setImage(new Image(Main.getInstance().getClass().getResourceAsStream("view\\icons\\fullStar.png")));
                star2.setImage(new Image(Main.getInstance().getClass().getResourceAsStream("view\\icons\\emptyStar.png")));
                star3.setImage(new Image(Main.getInstance().getClass().getResourceAsStream("view\\icons\\emptyStar.png")));
                star4.setImage(new Image(Main.getInstance().getClass().getResourceAsStream("view\\icons\\emptyStar.png")));
                star5.setImage(new Image(Main.getInstance().getClass().getResourceAsStream("view\\icons\\emptyStar.png")));
            } break;case 2: {
                star1.setImage(new Image(Main.getInstance().getClass().getResourceAsStream("view\\icons\\fullStar.png")));
                star2.setImage(new Image(Main.getInstance().getClass().getResourceAsStream("view\\icons\\fullStar.png")));
                star3.setImage(new Image(Main.getInstance().getClass().getResourceAsStream("view\\icons\\emptyStar.png")));
                star4.setImage(new Image(Main.getInstance().getClass().getResourceAsStream("view\\icons\\emptyStar.png")));
                star5.setImage(new Image(Main.getInstance().getClass().getResourceAsStream("view\\icons\\emptyStar.png")));
            } break;case 3: {
                star1.setImage(new Image(Main.getInstance().getClass().getResourceAsStream("view\\icons\\fullStar.png")));
                star2.setImage(new Image(Main.getInstance().getClass().getResourceAsStream("view\\icons\\fullStar.png")));
                star3.setImage(new Image(Main.getInstance().getClass().getResourceAsStream("view\\icons\\fullStar.png")));
                star4.setImage(new Image(Main.getInstance().getClass().getResourceAsStream("view\\icons\\emptyStar.png")));
                star5.setImage(new Image(Main.getInstance().getClass().getResourceAsStream("view\\icons\\emptyStar.png")));
            } break;case 4: {
                star1.setImage(new Image(Main.getInstance().getClass().getResourceAsStream("view\\icons\\fullStar.png")));
                star2.setImage(new Image(Main.getInstance().getClass().getResourceAsStream("view\\icons\\fullStar.png")));
                star3.setImage(new Image(Main.getInstance().getClass().getResourceAsStream("view\\icons\\fullStar.png")));
                star4.setImage(new Image(Main.getInstance().getClass().getResourceAsStream("view\\icons\\fullStar.png")));
                star5.setImage(new Image(Main.getInstance().getClass().getResourceAsStream("view\\icons\\emptyStar.png")));
            } break;case 5: {
                star1.setImage(new Image(Main.getInstance().getClass().getResourceAsStream("view\\icons\\fullStar.png")));
                star2.setImage(new Image(Main.getInstance().getClass().getResourceAsStream("view\\icons\\fullStar.png")));
                star3.setImage(new Image(Main.getInstance().getClass().getResourceAsStream("view\\icons\\fullStar.png")));
                star4.setImage(new Image(Main.getInstance().getClass().getResourceAsStream("view\\icons\\fullStar.png")));
                star5.setImage(new Image(Main.getInstance().getClass().getResourceAsStream("view\\icons\\fullStar.png")));
            } break;
        }
    }

    public void setEmptyStarBar() {
        star1.setImage(new Image(Main.getInstance().getClass().getResourceAsStream("view\\icons\\emptyStar.png")));
        star2.setImage(new Image(Main.getInstance().getClass().getResourceAsStream("view\\icons\\emptyStar.png")));
        star3.setImage(new Image(Main.getInstance().getClass().getResourceAsStream("view\\icons\\emptyStar.png")));
        star4.setImage(new Image(Main.getInstance().getClass().getResourceAsStream("view\\icons\\emptyStar.png")));
        star5.setImage(new Image(Main.getInstance().getClass().getResourceAsStream("view\\icons\\emptyStar.png")));
    }

    public void setItemsVisibility(boolean b) {
        for (Node n : visibleItems) {
            n.setVisible(b);
        }
    }

    public void hideLoadingScreen() {
        progressIndicator.setVisible(false);
        loadingText.setVisible(false);
    }

    public void sendStage(Stage stage) { this.stage = stage; }

    public void sendTitle(String title) {
        Thread t = new Thread() {
            public void run() {
                try {
                    URL url = new URL("http://www.omdbapi.com/?t=" + title.substring(0, title.lastIndexOf(" (")).replaceAll(" ", "%20") + "&r=json&plot=short&tomatoes=true&type=movie");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                    jsonMap = gson.fromJson(reader, new TypeToken<HashMap<String, String>>(){}.getType());

                    Platform.runLater(() -> {
                        imageView.setImage(!"N/A".equals(jsonMap.get("Poster")) ? new Image(jsonMap.get("Poster"), 200, 300, false, false) :
                                new Image(Main.class.getResourceAsStream("view\\icons\\nopicture.png"), 200, 300, false, false));
                        titleText.setText(jsonMap.get("Title"));
                        ratedText.setText(jsonMap.get("Rated"));
                        runtimeText.setText(jsonMap.get("Runtime"));
                        genreText.setText(jsonMap.get("Genre"));
                        releasedText.setText(jsonMap.get("Released"));
                        imdbText.setText(String.format("IMDb: %s/10 from %s", jsonMap.get("imdbRating"), jsonMap.get("imdbVotes")));
                        descriptionText.setText(jsonMap.get("Plot"));

                        correspondingFormattedTitle = jsonMap.get("Title") + " (" + jsonMap.get("Year") + ")";

                        if (Main.getAllMovies().containsKey(correspondingFormattedTitle)) {
                            ArrayList<String> rList = new ArrayList<>();
                            for (Rating r : Main.getAllMovies().get(correspondingFormattedTitle).getRatings()) {
                                rList.add(String.format("%s gave this film %d/5 on %s:\n%s", r.getPoster().getUsername(), (int) r.getRating(), r.getDate(), r.getComment()));
                            }

                            ObservableList oList = FXCollections.observableArrayList(rList);
                            listView.setItems(oList);
                        } else {
                            Text t = new Text("This movie hasn't been reviewed yet. Be the first to review it.");
                            t.setWrappingWidth(210);
                            listView.setPlaceholder(t);
                        }

                        setItemsVisibility(true);
                        hideLoadingScreen();
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }; t.setDaemon(true);
        t.start();
    }
}
