package edu.gatech.MovieRecommenderFX.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.gatech.MovieRecommenderFX.Main;
import edu.gatech.MovieRecommenderFX.model.Movie;
import edu.gatech.MovieRecommenderFX.model.Profile;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class DashboardController implements Initializable {

    // search movies FXML items
    @FXML private TextField searchField;
    @FXML private Button goButton;
    @FXML private GridPane gridPane;
    @FXML private ListView<MovieContainer> listView;
    @FXML private Text loadingText;
    @FXML private ProgressIndicator loadingWheel;
    @FXML private TabPane tabPane;
    @FXML private ButtonBar buttonBar;
    @FXML private Button closeButton;

    // profile FXML items
    @FXML private TextArea profileDescription;
    @FXML private Text memberSinceDate;
    @FXML private Text profileUsername;
    @FXML private Text profileName;
    @FXML private Text profileEmailAddress;
    @FXML private ImageView profilePicture;
    @FXML private Button profileSaveButton;
    @FXML private ComboBox profileMajorMenu;
    @FXML private Text profileMessage;

    @FXML private ListView<String> topRatedListView;
    @FXML private ListView<String> majorRatedListView;

    private Text listViewPlaceholder;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        assert searchField != null : "id=\"searchField\" was not injected: check your FXML file 'dashboard.fxml'.";
        assert goButton != null : "id=\"goButton\" was not injected: check your FXML file 'dashboard.fxml'.";
        assert gridPane != null : "id=\"gridPane\" was not injected: check your FXML file 'dashboard.fxml'.";
        assert listView != null : "id=\"listView\" was not injected: check your FXML file 'dashboard.fxml'.";
        assert loadingText != null : "id=\"loadingText\" was not injected: check your FXML file 'dashboard.fxml'.";
        assert loadingWheel != null : "id=\"loadingWheel\" was not injected: check your FXML file 'dashboard.fxml'.";
        assert tabPane != null : "id=\"tabPane\" was not injected: check your FXML file 'dashboard.fxml'.";

        assert profileDescription != null : "id=\"profileDescription\" was not injected: check your FXML file 'dashboard.fxml'.";
        assert memberSinceDate != null : "id=\"memberSinceDate\" was not injected: check your FXML file 'dashboard.fxml'.";
        assert profileUsername != null : "id=\"profileUsername\" was not injected: check your FXML file 'dashboard.fxml'.";
        assert profileName != null : "id=\"profileName\" was not injected: check your FXML file 'dashboard.fxml'.";
        assert profileEmailAddress != null : "id=\"profileEmailAddress\" was not injected: check your FXML file 'dashboard.fxml'.";
        assert profilePicture != null : "id=\"profilePicture\" was not injected: check your FXML file 'dashboard.fxml'.";
        assert profileSaveButton != null : "id=\"profileSaveButton\" was not injected: check your FXML file 'dashboard.fxml'.";
        assert profileMajorMenu != null : "id=\"profileMajorMenu\" was not injected: check your FXML file 'dashboard.fxml'.";
        assert profileMessage != null : "id=\"profileMessage\" was not injected: check your FXML file 'dashboard.fxml'.";

        assert topRatedListView != null : "id=\"topRatedListView\" was not injected: check your FXML file 'dashboard.fxml'.";
        assert majorRatedListView != null : "id=\"majorRatedListView\" was not injected: check your FXML file 'dashboard.fxml'.";

        assert buttonBar != null : "id=\"buttonBar\" was not injected: check your FXML file 'dashboard.fxml'.";
        assert closeButton != null : "id=\"closeButton\" was not injected: check your FXML file 'dashboard.fxml'.";

        listViewPlaceholder = new Text("Type a title into the search bar and press go to get started.");
        listViewPlaceholder.setFont(new Font("Ubuntu", 16));

        listView.setPlaceholder(listViewPlaceholder);

        Main.setTabPane(tabPane);

        // initialize control logic
        loadingText.setVisible(false);
        loadingWheel.setVisible(false);

        goButton.setOnMouseClicked(event -> {
            if (!"".equals(searchField.getText())) {
                listView.setVisible(false);
                loadingText.setText("Searching...");
                loadingText.setVisible(true);
                loadingWheel.setVisible(true);

                StringBuilder sb = new StringBuilder();

                try {
                    URL url = new URL("http://www.omdbapi.com/?s=" + searchField.getText().replaceAll(" ", "%20") + "&type=movie");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

                    String s;
                    while ((s = reader.readLine()) != null) {
                        sb.append(s);
                    }

                    String result = sb.toString();

                    if (result.contains("Error\":\"Movie")) {
                        loadingText.setText("No movies were found.");
                        loadingWheel.setVisible(false);
                    } else if (result.contains("Error")) {
                        loadingText.setText("Search terms must be at least 2 characters long.");
                        loadingWheel.setVisible(false);
                    } else {
                        result = result.substring(result.indexOf("["), result.lastIndexOf("]") + 1);

                        Gson g = new Gson();

                        List<HashMap<String, String>> jsonList = g.fromJson(result, new TypeToken<List<HashMap<String, String>>>(){}.getType());
                        ObservableList<MovieContainer> list = FXCollections.observableArrayList();

                        Thread t = new Thread() {
                            public void run() {
                                for (HashMap<String, String> jsonObject : jsonList) {
                                    HashMap<String, String> details = getDetailedInformation(jsonObject.get("Title"));

                                    MovieContainer mc = new MovieContainer((!"N/A".equals(jsonObject.get("Poster")) ? new Image(jsonObject.get("Poster"), 120, 180, false, false) :
                                                                                    new Image(Main.class.getResourceAsStream("view\\icons\\nopicture.png"), 120, 180, false, false)),
                                            jsonObject.get("Title") + " (" + jsonObject.get("Year") + ")",
                                            details.get("imdbRating"),
                                            details.get("imdbVotes"),
                                            details.get("Runtime"),
                                            details.get("tomatoMeter"),
                                            details.get("Metascore"),
                                            details.get("Plot"));
                                    if (!list.contains(mc)) list.add(mc);
                                }

                                Platform.runLater(() -> {
                                    listView = new ListView<>(list);
                                    listView.setPlaceholder(listViewPlaceholder);
                                    listView.setPrefHeight(390);
                                    listView.setMaxHeight(390);
                                    listView.setPrefWidth(720);
                                    listView.setMaxWidth(720);
                                    listView.setCellFactory(param -> new MovieCell());
                                    GridPane.setRowSpan(listView, 2);
                                    GridPane.setColumnSpan(listView, 4);

                                    loadingText.setVisible(false);
                                    loadingWheel.setVisible(false);

                                    gridPane.add(listView, 0, 3);
                                });
                            }
                        }; t.setDaemon(true);
                        t.start();
                    }
                } catch (IOException e) {
                    loadingText.setText("An error occurred.");
                    loadingWheel.setVisible(false);
                }
            }
        });

        tabPane.getSelectionModel().selectedIndexProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue.equals(1)) {
                topRatedListView.setPlaceholder(new Text("There are no ratings yet in the database. Be the first to review a movie!"));
                for (Movie m : Main.getAllMovies().values()) {
                    String mText = String.format("%s: %f from %d users", m.getTitle(), m.getAverageRating(), m.getRatings().size());
                    if (!topRatedListView.getItems().contains(mText)) {
                        topRatedListView.getItems().add(mText);
                    }
                }
            } else if (newValue.equals(2)) {
                if (Main.getCurrentUser().getProfile() == null) {
                    majorRatedListView.setPlaceholder(new Text("You need to make a profile to view movie ratings by major."));
                } else {
                    majorRatedListView.setPlaceholder(new Text(String.format("No %s majors have rated a movie yet. Be the first to review a movie!",
                            Main.getCurrentUser().getProfile().getMajor())));
                    for (Movie m : Main.getAllMovies().values()) {
                        String mText = String.format("%s: %f from %d users", m.getTitle(), m.getAverageMajorRating(Main.getCurrentUser().getProfile().getMajor()), m.getRatings().size());
                        if (!majorRatedListView.getItems().contains(mText)) {
                            majorRatedListView.getItems().add(mText);
                        }
                    }
                }
            }
        });

        // profile logic
        profileDescription.setPromptText("Tell us a little bit about yourself.");
        memberSinceDate.setText(String.format("Member since %s", Main.getCurrentUser().getMemberSince()));
        profileUsername.setText(String.format("Username: %s", Main.getCurrentUser().getUsername()));
        profileName.setText(String.format("Name: %s", Main.getCurrentUser().getName()));
        profileEmailAddress.setText(String.format("Email Address: %s", Main.getCurrentUser().getEmail()));
        profilePicture.setImage(new Image(Main.class.getResourceAsStream("view\\icons\\noProfilePicture.png"), 128, 128, false, false));
        profileMessage.setFill(Color.GREEN);
        profileMessage.setVisible(false);

        ArrayList<String> mList = new ArrayList<>(Arrays.asList("Computer Science", "Electrical Engineering", "Mechanical Engineering",
                "Industrial and Systems Engineering", "Mathematics", "Physics", "Chemistry", "Chemical Engineering"));
        ObservableList<String> list = FXCollections.observableArrayList(mList);
        profileMajorMenu.setItems(list);

        if (Main.getCurrentUser().getProfile() != null) {
            profileMajorMenu.getSelectionModel().select(Main.getCurrentUser().getProfile().getMajor());
            profileDescription.setText(Main.getCurrentUser().getProfile().getDescription());
        }

        profileSaveButton.setOnMouseClicked(event -> {
            if (profileMajorMenu.getSelectionModel().getSelectedItem() == null || "".equals(profileDescription.getText())) {
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
                    dc.getDialogText().setText("Are you sure you want to continue? You cannot post reviews without a description and major.");
                    dc.getYesButton().setOnMouseClicked(event1 -> dialog.close());

                    dialog.setScene(scene);
                    dialog.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                String selected = profileMajorMenu.getSelectionModel().getSelectedItem().toString();
                Main.getCurrentUser().setProfile(new Profile(selected, profileDescription.getText()));
                HashMap<String, Object> keyVal = new HashMap<>();
                keyVal.put("major", selected);
                keyVal.put("description", profileDescription.getText());
                Main.getDBReference().child("users").child(Main.getCurrentUser().getUsername()).updateChildren(keyVal);
                profileMessage.setVisible(true);
                FadeTransition ft = new FadeTransition(Duration.millis(500), profileMessage);
                ft.setDelay(Duration.millis(1000));
                ft.setFromValue(1.0);
                ft.setToValue(0.0);
                ft.play();
            }
        });

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
    }

    protected HashMap<String, String> getDetailedInformation(String title) {
        try {
            StringBuilder sb = new StringBuilder();
            URL url = new URL("http://www.omdbapi.com/?t=" + title.replaceAll(" ", "%20") + "&r=json&plot=short&tomatoes=true&type=movie");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

            String s;
            while ((s = reader.readLine()) != null) {
                sb.append(s);
            }

            Gson g = new Gson();

            HashMap<String, String> jsonMap = g.fromJson(sb.toString(), new TypeToken<HashMap<String, String>>(){}.getType());

            return jsonMap;
        } catch (IOException e) {
            System.out.println("not gonna happen");
        }

        return null;
    }

    private class MovieContainer {
        public Image image;
        public String title;
        public String imdbRating;
        public String imdbVotes;
        public String runtime;
        public String rotten;
        public String metascore;
        public String plot;

        public MovieContainer(Image image, String title, String imdbRating, String imdbVotes, String runtime, String rotten, String metascore, String plot) {
            this.image = image;
            this.title = title;
            this.imdbRating = imdbRating;
            this.imdbVotes = imdbVotes;
            this.runtime = runtime;
            this.rotten = rotten;
            this.metascore = metascore;
            this.plot = plot;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MovieContainer that = (MovieContainer) o;

            return title.equals(that.title);
        }

        @Override
        public int hashCode() { return title.hashCode(); }
    }

    private class MovieCell extends ListCell<MovieContainer> {
        private GridPane gridPane;
        private ImageView poster;
        private Text titleYear;
        private HBox hBox;
        private Text runtime;
        private Text IMDb;
        private Text rotten;
        private Text metascore;
        private Text plot;

        public MovieCell() { super(); }

        @Override
        public void updateItem(MovieContainer mc, boolean empty) {
            super.updateItem(mc, empty);
            setText(null);
            if (empty) {
                setGraphic(null);
            } else {
                gridPane = new GridPane();
                gridPane.setHgap(0);
                gridPane.setVgap(0);

                RowConstraints row0 = new RowConstraints(10, 50, 50);
                RowConstraints row1 = new RowConstraints(10, 50, 50);
                RowConstraints row2 = new RowConstraints(10, 50, 50);
                RowConstraints row3 = new RowConstraints(10, 50, 50);
                ColumnConstraints col0 = new ColumnConstraints(10, 210, 210);
                ColumnConstraints col1 = new ColumnConstraints(10, 510, 510);

                gridPane.getRowConstraints().addAll(row0, row1, row2, row3);
                gridPane.getColumnConstraints().addAll(col0, col1);

                poster = new ImageView(mc.image);
                GridPane.setMargin(poster, new Insets(0, 15, 0, 15));
                GridPane.setRowSpan(poster, 4);
                GridPane.setColumnSpan(poster, 1);
                gridPane.add(poster, 0, 0);

                titleYear = new Text(mc.title);
                titleYear.setFont(new Font("Ubuntu Bold", 18));
                gridPane.add(titleYear, 1, 0);

                this.runtime = new Text(mc.runtime);
                this.runtime.setFont(new Font("Ubuntu", 14));
                this.runtime.setFill(Color.GRAY);
                HBox.setMargin(this.runtime, new Insets(0, 0, 0, 0));

                IMDb = new Text(String.format("IMDb: %s/10 from %s", mc.imdbRating, mc.imdbVotes));
                IMDb.setFont(new Font("Ubuntu Bold", 14));
                HBox.setMargin(IMDb, new Insets(0, 0, 0, 15));

                ImageView rottenTom = new ImageView(new Image(Main.class.getResourceAsStream("view\\icons\\rotten_tomatoes.png"), 15, 15, false, false));
                HBox.setMargin(rottenTom, new Insets(0, 0, 0, 25));

                this.rotten = new Text(mc.rotten);
                this.rotten.setFont(new Font("Ubuntu", 14));
                HBox.setMargin(this.rotten, new Insets(0, 0, 0, 5));

                this.metascore = new Text(String.format("Metascore: %s/100", mc.metascore));
                this.metascore.setFont(new Font("Ubuntu", 14));
                HBox.setMargin(this.metascore, new Insets(0, 0, 0, 15));

                hBox = new HBox();
                hBox.setPrefWidth(495);
                hBox.setPrefHeight(35);
                hBox.getChildren().addAll(this.runtime, IMDb, rottenTom, this.rotten, this.metascore);
                gridPane.add(hBox, 1, 1);

                this.plot = new Text((!"N/A".equals(mc.plot)) ? mc.plot : "No plot description available.");
                this.plot.setFont(new Font("Ubuntu", 14));
                this.plot.setWrappingWidth(480);
                GridPane.setMargin(this.plot, new Insets(0, 0, 0, 0));
                GridPane.setRowSpan(this.plot, 2);
                gridPane.add(this.plot, 1, 2);
                gridPane.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2) {
                        try {
                            Stage stage = new Stage();
                            stage.initStyle(StageStyle.TRANSPARENT);
                            stage.getIcons().add(new Image(Main.getInstance().getClass().getResourceAsStream("view\\icons\\icon.png")));
                            stage.setTitle(mc.title);
                            stage.setResizable(false);

                            FXMLLoader fxmlLoader = new FXMLLoader(Main.getInstance().getClass().getResource("view\\movie_profile.fxml"));
                            Parent root = fxmlLoader.load();
                            Rectangle windowRect = new Rectangle(600, 600);
                            windowRect.setArcHeight(15.0);
                            windowRect.setArcWidth(15.0);
                            root.setClip(windowRect);

                            Scene scene = new Scene(root, 600, 600);
                            scene.setFill(Color.TRANSPARENT);

                            MovieProfileController mpc = fxmlLoader.getController();
                            mpc.sendStage(stage);
                            mpc.sendTitle(mc.title);

                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                setGraphic(gridPane);
            }
        }
    }
}
