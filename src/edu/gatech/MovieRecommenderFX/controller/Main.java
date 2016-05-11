package edu.gatech.MovieRecommenderFX.controller;

import com.firebase.client.Firebase;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import edu.gatech.MovieRecommenderFX.controller.model.Movie;
import edu.gatech.MovieRecommenderFX.controller.model.User;

public class Main extends Application {

    // Shared window for the application
    private static Stage sharedStage;

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

        primaryStage.setTitle("Movie Recommender FX");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 640, 480));
        primaryStage.show();
    }

    @Override
    public void stop() { System.exit(0); }

    public static Main getInstance() { return main; }

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
                sharedStage.setScene(new Scene(root, 640, 480));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
