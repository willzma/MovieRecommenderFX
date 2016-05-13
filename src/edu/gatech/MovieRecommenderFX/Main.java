package edu.gatech.MovieRecommenderFX;

import com.firebase.client.Firebase;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import edu.gatech.MovieRecommenderFX.model.Movie;
import edu.gatech.MovieRecommenderFX.model.User;

public class Main extends Application {

    // Shared window for the application
    private static Stage sharedStage;
    private static Rectangle windowRect;

    // One MessageDigest for all password hashing
    private static MessageDigest sharedDigest;

    // Shared Firebase database table references
    public static Firebase DATABASE;
    public static Firebase USER_TABLE;
    public static Firebase MOVIE_TABLE;

    // General global resources
    public static User currentUser;
    public static Map<String, User> allUsers;
    public static Map<String, Movie> allMovies;

    private static Main main;

    // Window position tracking
    private static double xOffset = 0;
    private static double yOffset = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        main = this;

        // Initial global resource loading
        sharedStage = primaryStage;

        // Load Firebase references
        DATABASE = new Firebase("https://movierecommender.firebaseio.com/");
        USER_TABLE = DATABASE.child("users");
        MOVIE_TABLE = DATABASE.child("movies");

        currentUser = null;
        allUsers = new ConcurrentHashMap<>();
        allMovies = new ConcurrentHashMap<>();

        // Try loading SHA-256 hashing algorithm
        try {
            sharedDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        Parent root = FXMLLoader.load(getClass().getResource("view\\loading_screen.fxml"));
        windowRect = new Rectangle(720, 480);
        windowRect.setArcHeight(15.0);
        windowRect.setArcWidth(15.0);
        root.setClip(windowRect);

        Scene scene = new Scene(root, 720, 480);
        scene.setFill(Color.TRANSPARENT);

        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("icon.png")));
        primaryStage.setTitle("Movie Recommender FX");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() { System.exit(0); }

    public static Main getInstance() { return main; }

    public static Stage getSharedStage() { return sharedStage; }

    public static void main(String[] args) { launch(args); }

    public static String getDigest(String password) {
        byte[] hash = sharedDigest.digest(password.getBytes());

        StringBuilder sb = new StringBuilder(hash.length * 2);
        for (Byte b : hash) {
            sb.append(String.format("%02X", b & 0xFF));
        }

        return sb.toString();
    }

    public static void loadingCallback(String filename) {
        Platform.runLater(() -> {
            try {
                Parent root = FXMLLoader.load(Main.getInstance().getClass().getResource("view\\" + filename));
                windowRect = new Rectangle(720, 480);
                windowRect.setArcHeight(15.0);
                windowRect.setArcWidth(15.0);
                root.setClip(windowRect);

                Scene scene = new Scene(root, 720, 480);
                scene.setFill(Color.TRANSPARENT);

                sharedStage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    // From here on are reused logic in method references
    public static void panePressed(MouseEvent event) {
        xOffset = sharedStage.getX() - event.getScreenX();
        yOffset = sharedStage.getY() - event.getScreenY();
    }

    public static void paneDragged(MouseEvent event) {
        sharedStage.setX(event.getScreenX() + xOffset);
        sharedStage.setY(event.getScreenY() + yOffset);
    }
}
