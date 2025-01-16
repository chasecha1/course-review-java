package edu.virginia.sde.reviews;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.*;

import java.io.IOException;


public class CourseReviewsController {

    public GridPane topBar;
    public Button showText;
    public VBox reviewHolder;
    public Text courseDescription;
    @FXML
    public TextField rating;

    @FXML
    public TextArea comment;
    CourseReviewDriver driver;
    SceneManager sceneManager;

    @FXML
    public VBox reviewer = new VBox();

    private List<Review> reviews;

    private Course course;
    
    

    public void initData(CourseReviewDriver driver, SceneManager sceneManager, Course course) {
        this.driver = driver;
        this.sceneManager = sceneManager;
        this.course = course;
        driver.updateRating(this.course);
        do_init();
    }

    public void do_init() {
        topBar.getStylesheets().clear();
        topBar.getStylesheets().add("topbar.css");
        showText.setText(driver.getCurrentUser().getUsername() +"'s reviews");
        courseDescription.setText(courseText());
        updateReviews();
    }

    private String courseText() {
        this.driver.updateRating(course);
        String subject = course.getSubject();
        int number = course.getCourseNumber();
        String title = course.getTitle();
        double averageRating = course.getAverageRating();
        String courseInfo = String.format("%s %d: %s", subject, number, title);
        if (averageRating != -1) {
            courseInfo += String.format(", average rating: %.2f", averageRating);
        } else {
            courseInfo += ", average rating: N/A";
        }
        return courseInfo;
    }

    private void addReviewCheck() {
        if (driver.hasReviewed(reviews)) {
            return;
        }
        Button addButton = new Button();
        addButton.setText("add review");
        addButton.getStyleClass().clear();
        addButton.getStyleClass().add("add-button");
        addButton.setOnAction(actionEvent -> showAddReview());
        addButton.setId("add-button");
        reviewHolder.getChildren().add(addButton);
    }


    private void showAddReview() {
        reviewHolder.getChildren().removeIf(c -> {
            if (c.getId() != null) {
                return c.getId().equals("add-button");
            }
            return false;
        });
        this.reviewer = new VBox();
        reviewer.setId("reviewer");
        Text title = new Text(String.format("Add Review for %s", course.getTitle()));
        title.getStyleClass().clear();
        title.setStyle("-fx-font-family: sans-serif");
        title.setStyle("-fx-font-size: 15");
        reviewer.getChildren().add(title);
        rating = new TextField();
        rating.getStyleClass().clear();
        rating.getStyleClass().add("input");
        rating.setPromptText("rating");
        reviewer.getChildren().add(rating);
        comment = new TextArea();
        comment.setPromptText("comment");
        comment.getStyleClass().clear();
        comment.getStyleClass().add("area");
        reviewer.getChildren().add(comment);
        Button addButton = new Button();
        addButton.getStyleClass().clear();
        addButton.getStyleClass().add("add-button");
        addButton.setText("submit");
        addButton.setOnAction(ActionEvent -> createReview());
        Button abort = new Button();
        abort.getStyleClass().clear();
        abort.getStyleClass().add("add-button");
        abort.setText("close");
        abort.setOnAction(ActionEvent -> closeReview());
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.getChildren().addAll(List.of(addButton, abort));
        reviewer.getChildren().add(hbox);
        reviewer.setSpacing(8);
        reviewHolder.getChildren().add(reviewer);
    }

    private void closeReview() {
        reviewHolder.getChildren().removeIf(c -> {
            if (c.getId() != null) {
                return c.getId().equals("reviewer");
            }
            return false;
        });
        updateReviews();
    }

    private void createReview() {
        String ratingScore = rating.getText();
        String givenComment = comment.getText();
        ReviewCreationCode code = driver.createReview(givenComment, ratingScore, this.course);
        switch(code) {
            case SUCCESS -> {
                updateReviews();
                closeReview();
            }
            case FAILED -> {
                reviewError("failed");
            }
            case NOT_AN_INT -> {
                reviewError("Must be an integer");
            }
            case NOT_A_NUMBER -> {
                reviewError("not a number");
            }
            case RATING_NOT_IN_RANGE -> {
                reviewError("not in range");
            }
        }
    }

    private void reviewError(String message) {
        reviewer.getChildren().removeIf(c -> {
            if (c.getId() == null) {
                return false;
            } else if (c.getId().equals("error")) {
                return true;
            } else {
                return false;
            }
        });
        Label errorMessage = new Label(message);
        errorMessage.getStyleClass().clear();
        errorMessage.getStyleClass().add("error");
        errorMessage.setId("error");
        reviewer.getChildren().add(errorMessage);
    }

    private void showEditReview(Review r) {

        ArrayList<Node> children = new ArrayList<>();
        
        reviewHolder.getChildren().forEach(c -> {
            if (c.getId().contains(r.getUser().getUsername())) {
                this.reviewer = getEditScreen(r, r.getCourse());
                children.add(reviewer);
            } else {
                children.add(c);
            }
        });

        reviewHolder.getChildren().clear();
        reviewHolder.getChildren().addAll(children);

    }

    private VBox getEditScreen(Review r, Course c) {
        VBox reviewer = new VBox();
        reviewer.setId("reviewer");
        Text title = new Text(String.format("Edit Review for %s", course.getTitle()));
        title.getStyleClass().clear();
        title.setStyle("-fx-font-family: sans-serif");
        title.setStyle("-fx-font-size: 15");
        reviewer.getChildren().add(title);
        rating = new TextField();
        rating.getStyleClass().clear();
        rating.getStyleClass().add("input");
        rating.setText(String.valueOf(r.getRating()));
        reviewer.getChildren().add(rating);
        comment = new TextArea();
        comment.getStyleClass().clear();
        comment.getStyleClass().add("area");
        Optional<String> getComment = r.getComment();
        getComment.ifPresent(s -> this.comment.setText(s));
        this.rating.setText(String.valueOf(r.getRating()));
        reviewer.getChildren().add(comment);
        Button addButton = new Button();
        addButton.getStyleClass().clear();
        addButton.getStyleClass().add("add-button");
        addButton.setText("edit");
        addButton.setOnAction(ActionEvent -> editReview(c));
        Button abort = new Button();
        abort.getStyleClass().clear();
        abort.getStyleClass().add("add-button");
        abort.setText("close");
        abort.setOnAction(ActionEvent -> closeReview());
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.getChildren().addAll(List.of(addButton, abort));
        reviewer.getChildren().add(hbox);
        reviewer.setSpacing(8);
        return reviewer;
    }

    private void updateReviews() {
        courseDescription.setText(courseText());
        reviewHolder.getChildren().removeIf(c -> {
            if (c.getId() != null) {
                return c.getId().startsWith("review");
            }
            return false;
        });
        this.reviews = driver.getReviewsForCourse(course);
        this.addReviewCheck();
        this.reviews.forEach(r -> {
            VBox review = getReviewBox(r);
            reviewHolder.getChildren().add(review);
        });
        this.driver.updateRating(this.course);
    }

    private VBox getReviewBox(Review r) {
        VBox review = new VBox();
        review.getStyleClass().clear();
        String user = r.getUser().getUsername();
        review.setId(String.format("review//%s", user));
        review.getStyleClass().add("review");
        HBox header = new HBox();
        Label score = new Label("Score:");
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
        review.getStyleClass().clear();
        review.getStyleClass().add("review");
        return review;
    }


    private void editReview(Course c) {
        String ratingScore = rating.getText();
        String newComment = comment.getText();
        ReviewCreationCode code = driver.updateReview(newComment, ratingScore, c);
        switch(code) {
            case SUCCESS -> {
                this.closeReview();
            }
            case NOT_AN_INT -> {
                reviewError("Must be an integer");
            }
            case FAILED -> {
                reviewError("sql exception");
            }
            case NOT_A_NUMBER -> {
                reviewError("not a number");
            }
            case RATING_NOT_IN_RANGE -> {
                reviewError("Must be in the range 1-5");
            }
        }

    }

    private void deleteReview(Review review) {
        driver.deleteReview(review);
        this.updateReviews();
    }

    public void toLogin() throws Exception {
        Stage stage = (Stage) showText.getScene().getWindow();
        sceneManager.toLogin(stage);
    }

    public void toCourseCreation() {
        Stage stage = (Stage) showText.getScene().getWindow();
        sceneManager.toCourseCreation(stage);
    }

    public void toCourseSearch()  {
        Stage stage = (Stage) showText.getScene().getWindow();
        sceneManager.toCourseSearch(stage);
    }

    public void close() {
        Stage stage = (Stage) showText.getScene().getWindow();
        stage.close();
    }

    public void toUserReviews() {
        Stage stage = (Stage) showText.getScene().getWindow();
        sceneManager.toUserReviews(stage);
    }
}
