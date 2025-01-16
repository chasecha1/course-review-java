package edu.virginia.sde.reviews;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Front-end driver
 * Will be given a CourseReviewDriver object, on button clicks will simply pass on
 * information to CRD w/o any logic, will wait for response from CRD
 */
public class CourseReviewsApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        SceneManager sceneManager = new SceneManager(null);
        sceneManager.toLogin(stage);

    }

    public static void main(String[] args) {
        launch(args);
    }

}

