package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class SceneManager {

    CourseReviewDriver driver;

    public SceneManager(CourseReviewDriver driver) {
        this.driver = driver;
    }

    public void toCourseSearch(Stage stage) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("course-search.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            Group failed_root = new Group();
            VBox box = new VBox();
            box.setStyle("-fx-min-width: 1280; -fx-min-height: 800; -fx-padding: 100");
            Text label = new Text(
                    "Something has gone wrong, this error occurred in toCourseSearch in Scene manager " +
                            "and is not an error that ever happened during development. It was an " +
                            "IOException that occured with fxmlLoader.load(), and we are not sure" +
                            "what could have caused this. This screen has been placed in lieu of " +
                            "a fatal error");
            label.setWrappingWidth(400);
            box.getChildren().add(label);
            failed_root.getChildren().add(box);
            Scene scene_ = new Scene(failed_root);
            stage.setScene(scene_);
            return;
        }
        CourseSearchController controller = fxmlLoader.getController();
        controller.initData(this.driver, this);
        Scene scene = new Scene(root);
        scene.getStylesheets().add("courseSearch.css");
        stage.setScene(scene);
    }

    public void toCourseCreation(Stage stage) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("course-creation.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            Group failed_root = new Group();
            VBox box = new VBox();
            box.setStyle("-fx-min-width: 1280; -fx-min-height: 800; -fx-padding: 100");
            Text label = new Text(
                    "Something has gone wrong, this error occurred in toCourseCreation in Scene manager " +
                            "and is not an error that ever happened during development. It was an " +
                            "IOException that occured with fxmlLoader.load(), and we are not sure" +
                            "what could have caused this. This screen has been placed in lieu of " +
                            "a fatal error");
            label.setWrappingWidth(400);
            box.getChildren().add(label);
            failed_root.getChildren().add(box);
            Scene scene_ = new Scene(failed_root);
            stage.setScene(scene_);
            return;
        }
        CourseCreationController controller = fxmlLoader.getController();
        controller.initData(this.driver, this);
        Scene scene = new Scene(root);
        scene.getStylesheets().add("courseCreation.css");
        stage.setScene(scene);
    }

    public void toLogin(Stage stage) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            Group failed_root = new Group();
            VBox box = new VBox();
            box.setStyle("-fx-min-width: 1280; -fx-min-height: 800; -fx-padding: 100");
            Text label = new Text(
                    "Something has gone wrong, this error occurred in toLogin in Scene manager " +
                            "and is not an error that ever happened during development. It was an " +
                            "IOException that occured with fxmlLoader.load(), and we are not sure" +
                            "what could have caused this. This screen has been placed in lieu of " +
                            "a fatal error");
            label.setWrappingWidth(400);
            box.getChildren().add(label);
            failed_root.getChildren().add(box);
            Scene scene_ = new Scene(failed_root);
            stage.setScene(scene_);
            return;
        }
        LoginController controller = fxmlLoader.getController();
        if (driver == null) {
            driver = new CourseReviewDriver();
        }
        driver.setCurrentUser(null);
        if (driver.didFail()) { toBadDatabaseConnection(stage); }
        SceneManager sceneManager = new SceneManager(driver);
        controller.initData(driver, sceneManager);
        Scene scene = new Scene(root);
        scene.getStylesheets().add("login.css");
        stage.setTitle("Hello World");
        stage.setScene(scene);
        stage.show();
    }

    public void toBadDatabaseConnection(Stage stage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("failed.xml"));
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            Group failed_root = new Group();
            VBox box = new VBox();
            box.setStyle("-fx-min-width: 1280; -fx-min-height: 800; -fx-padding: 100");
            Text label = new Text(
                    "Something has gone wrong, this error occurred in toBadDatabaseConnection in Scene manager " +
                            "and is not an error that ever happened during development. It was an " +
                            "IOException that occured with fxmlLoader.load(), and we are not sure" +
                            "what could have caused this. This screen has been placed in lieu of " +
                            "a fatal error");
            label.setWrappingWidth(400);
            box.getChildren().add(label);
            failed_root.getChildren().add(box);
            Scene scene_ = new Scene(failed_root);
            stage.setScene(scene_);
            return;
        }
        scene.getStylesheets().add("failed.css");
        stage.setTitle("Oops");
        stage.setScene(scene);
        stage.show();
    }

    public void toUserReviews(Stage stage) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("user-reviews.fxml"));
        Parent root;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            Group failed_root = new Group();
            VBox box = new VBox();
            box.setStyle("-fx-min-width: 1280; -fx-min-height: 800; -fx-padding: 100");
            Text label = new Text(
                    "Something has gone wrong, this error occurred in toUserReviews in Scene manager " +
                            "and is not an error that ever happened during development. It was an " +
                            "IOException that occured with fxmlLoader.load(), and we are not sure" +
                            "what could have caused this. This screen has been placed in lieu of " +
                            "a fatal error");
            label.setWrappingWidth(400);
            box.getChildren().add(label);
            failed_root.getChildren().add(box);
            Scene scene_ = new Scene(failed_root);
            stage.setScene(scene_);

            return;
        }
        UserReviewsController controller = fxmlLoader.getController();
        controller.initData(this.driver, this);
        Scene scene = new Scene(root);
        scene.getStylesheets().addAll(List.of("userReviews.css", "courseReviews.css"));
        stage.setScene(scene);
    }

    public void toCourseReviews(Stage stage, Course course) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("course-reviews.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            Group failed_root = new Group();
            VBox box = new VBox();
            box.setStyle("-fx-min-width: 1280; -fx-min-height: 800; -fx-padding: 100");
            Text label = new Text(
                    "Something has gone wrong, this error occurred in toCourseReviews in Scene manager " +
                            "and is not an error that ever happened during development. It was an " +
                            "IOException that occured with fxmlLoader.load(), and we are not sure" +
                            "what could have caused this. This screen has been placed in lieu of " +
                            "a fatal error");
            label.setWrappingWidth(400);
            box.getChildren().add(label);
            failed_root.getChildren().add(box);
            Scene scene_ = new Scene(failed_root);
            stage.setScene(scene_);
            return;
        }
        CourseReviewsController controller = fxmlLoader.getController();
        controller.initData(this.driver, this, course);
        Scene scene = new Scene(root);
        scene.getStylesheets().add("courseReviews.css");
        stage.setScene(scene);
    }
}
