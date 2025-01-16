package edu.virginia.sde.reviews;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.util.List;
import java.util.Optional;

public class UserReviewsController {

    public GridPane topBar;
    public Label userReviews;
    public VBox reviewHolder;
    public Text userDescription;
    private SceneManager sceneManager;
    private CourseReviewDriver driver;

    private List<Review> reviews;


    public void toCourseCreation(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) topBar.getScene().getWindow();
            sceneManager.toCourseCreation(stage);
        } catch (Exception ignored) {
        }
    }

    public void toCourseSearch(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) topBar.getScene().getWindow();
            sceneManager.toCourseSearch(stage);
        } catch (Exception ignored) {
        }
    }

    public void toLogin(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) topBar.getScene().getWindow();
            sceneManager.toLogin(stage);
        } catch (Exception ignored) {
        }
    }

    public void close(ActionEvent actionEvent) {
        Stage stage = (Stage) userReviews.getScene().getWindow();
        stage.close();
    }

    public void initData(CourseReviewDriver driver, SceneManager sceneManager) {
        this.driver = driver;
        this.sceneManager = sceneManager;
        topBar.getStylesheets().clear();
        topBar.getStylesheets().add("topbar.css");
        userReviews.setText(driver.getCurrentUser().getUsername() +"'s reviews");
        userDescription.setText(String.format("%s's reviews", driver.getCurrentUser().getUsername()));
        this.updateReviews();
    }

    private void updateReviews() {
        this.reviews = driver.getReviewsForUser();
        this.reviews.forEach(r -> {
            VBox reviewBox = getReviewBox(r);
            reviewHolder.getChildren().add(reviewBox);
        });
    }

    private VBox getReviewBox(Review r) {
        VBox review = new VBox();
        review.getStyleClass().clear();
        String user = r.getUser().getUsername();
        review.setId(String.format("review//%s", user));
        review.getStyleClass().add("review");
        HBox header = new HBox();
        String courseString = r.getCourse().getSubject() + ":" + r.getCourse().getCourseNumber();
        String scoreString = String.format("Score for %s -- ", courseString);
        Label score = new Label(scoreString);
        Label scoreLabel = new Label(String.valueOf(r.getRating()));
        scoreLabel.setStyle("-fx-font-size: 20");
        scoreLabel.setStyle("-fx-font-weight: normal");
        header.getChildren().addAll(List.of(score, scoreLabel));
        String timeString = r.getTimeStamp().toString();
        Label time = new Label(timeString);
        time.getStyleClass().clear();

        time.setStyle("-fx-text-fill: grey; " +
                "-fx-font-weight: normal;" +
                " -fx-font-size: 15");

        header.setStyle("-fx-spacing: 5");
        review.getChildren().addAll(List.of(header, time));


        Optional<String> comment = r.getComment();
        if(comment.isPresent()) {
            Text l = new Text(comment.get());
            l.setStyle("-fx-font-size: 15");
            l.setStyle("-fx-font-weight: normal");
            l.setWrappingWidth(800);
            review.getChildren().add(l);
        }

        /*
        if (r.getDeletable()) {
            Button delete = new Button();
            delete.setText("[ delete ]");
            delete.getStyleClass().clear();
            delete.getStyleClass().add("delete-button");
            delete.setOnAction(ActionEvent -> deleteReview(r));
            Button edit = new Button();
            edit.setText("[ edit ]");
            edit.getStyleClass().clear();
            edit.getStyleClass().add("delete-button");
            edit.setStyle("-fx-text-fill: orange");
            edit.setOnAction(ActionEvent -> showEditReview(r));
            HBox buttons = new HBox();
            buttons.setStyle("-fx-spacing: 6; -fx-padding: 0");
            buttons.getChildren().addAll(List.of(delete, edit));
            review.getChildren().add(buttons);
        }
        */
        review.getStyleClass().clear();
        review.getStyleClass().add("review");
        review.setOnMouseClicked(ActionEvent -> {
            reviewClicked(r);
        });
        return review;
    }

    private void reviewClicked(Review review) {
        try {
            Stage stage = (Stage) reviewHolder.getScene().getWindow();
            sceneManager.toCourseReviews(stage, review.getCourse());
        } catch (Exception ignored) {

        }
    }
}
