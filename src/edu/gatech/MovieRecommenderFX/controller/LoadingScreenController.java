package edu.gatech.MovieRecommenderFX.controller;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import edu.gatech.MovieRecommenderFX.Main;
import edu.gatech.MovieRecommenderFX.model.Movie;
import edu.gatech.MovieRecommenderFX.model.Profile;
import edu.gatech.MovieRecommenderFX.model.Rating;
import edu.gatech.MovieRecommenderFX.model.User;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.text.Text;

import java.net.URL;

import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class LoadingScreenController implements Initializable {

    @FXML private Text loadingMessage;
    @FXML private ProgressIndicator loadingWheel;

    private static AtomicBoolean listenersOpened = new AtomicBoolean();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        assert loadingMessage != null : "id=\"loadingMessage\" was not injected: check your FXML file 'loading_screen.fxml'.";
        assert loadingWheel != null : "id=\"loadingWheel\" was not injected: check your FXML file 'loading_screen.fxml'.";

        // logic initialization
        loadingWheel.setMinSize(50, 50);

        // initialize databases
        Main.USER_TABLE.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String name = postSnapshot.child("name").getValue(String.class);
                    String email = postSnapshot.child("email").getValue(String.class);
                    String username = postSnapshot.child("username").getValue(String.class);
                    String passwordHash = postSnapshot.child("passwordHash").getValue(String.class);

                    User tempU = new User(name, email, username, passwordHash);

                    String major = postSnapshot.child("major").getValue(String.class);
                    String description = postSnapshot.child("description").getValue(String.class);
                    String status = postSnapshot.child("status").getValue(String.class);

                    tempU.setStatus(status);

                    if (!"".equals(major) && !"".equals(description)) {
                        tempU.setProfile(new Profile(major, description));
                    }

                    Main.allUsers.put(username, tempU);
                }

                if (!listenersOpened.get()) {
                    Main.MOVIE_TABLE.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                String title = postSnapshot.child("title").getValue(String.class);
                                float avg = postSnapshot.child("averageRating").getValue(Float.class);
                                String imgURL = postSnapshot.child("imgURL").getValue(String.class);

                                Movie tempM = new Movie(title);

                                tempM.setAverageRating(avg);
                                tempM.setURL(imgURL);

                                if (postSnapshot.hasChild("ratings")) {
                                    for (DataSnapshot post2Snapshot : postSnapshot.child("ratings").getChildren()) {
                                        float rating = post2Snapshot.child("rating").getValue(Float.class);
                                        String comment = post2Snapshot.child("comment").getValue(String.class);
                                        String poster = post2Snapshot.child("user").getValue(String.class);

                                        Rating r = new Rating(rating, comment, Main.allUsers.get(poster));

                                        if (!tempM.getRatings().contains(r)) {
                                            tempM.addRating(r);
                                        }
                                    }
                                }

                                Main.allMovies.put(title, tempM);
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            firebaseError.toException().printStackTrace();
                        }
                    });

                    Main.loadingCallback("login.fxml");
                    listenersOpened.set(true);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                firebaseError.toException().printStackTrace();
            }
        });
    }
}
