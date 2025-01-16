package edu.virginia.sde.reviews;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;


public class CourseCreationController {

    @FXML
    public Button showText;

    @FXML
    public TextField courseInput;

    @FXML
    public TextField subjectInput;

    @FXML
    public HBox responseBox;

    @FXML
    public Label response;

    @FXML
    public TextField numberInput;

    @FXML
    public VBox courseBox;
    public GridPane topBar;
    private CourseReviewDriver driver;
    private SceneManager sceneManager;





    public void initData(CourseReviewDriver driver, SceneManager sceneManager) {
        this.driver = driver;
        this.sceneManager = sceneManager;
        do_init();
    }

    public void do_init() {
        showText.setText(driver.getCurrentUser().getUsername() + "'s reviews");
        topBar.getStylesheets().clear();
        topBar.getStylesheets().add("topbar.css");
    }


    public void toLogin() {
        try {
            Stage stage = (Stage) showText.getScene().getWindow();
            sceneManager.toLogin(stage);
        } catch (Exception ignored) {

        }
    }

    @FXML
    private void toCourseSearch() {
        Stage stage = (Stage)numberInput.getScene().getWindow();
        sceneManager.toCourseSearch(stage);
    }

    public void createCourse() {
        String courseName = courseInput.getText();
        CourseCreationCode code;
        try {
            int courseNumber = Integer.parseInt(numberInput.getText());
            String subject = subjectInput.getText();
            Course course = new Course(subject, courseNumber, courseName);
            code = driver.createCourse(course);
        } catch (NumberFormatException e) {
            code = CourseCreationCode.IMPROPER_COURSE_NUMBER;
        }

        response.getStyleClass().clear();
        switch (code) {
            case SUCCESS -> {
            response.setText("course created successfully");
            response.getStyleClass().add("success");
            }
            case IMPROPER_COURSE_DEPARTMENT -> showFailure("Invalid Subject Name");
            case IMPROPER_COURSE_NUMBER -> showFailure("Invalid Course Number");
            case IMPROPER_COURSE_TITLE -> showFailure("Improper Course Title");
            case FAILURE_COURSE_ALREADY_EXISTS -> showFailure("Course already exists");
        }
    }

    private void showFailure(String message) {
        response.setText(message);
        response.getStyleClass().add("failure");
    }

    public void close() {
        Stage stage = (Stage)courseInput.getScene().getWindow();
        stage.close();
    }

    public void toUserReviews() {
        Stage stage = (Stage) courseInput.getScene().getWindow();
        sceneManager.toUserReviews(stage);
    }
}
