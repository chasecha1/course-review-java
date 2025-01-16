package edu.virginia.sde.reviews;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.List;

import java.io.IOException;


public class CourseSearchController {

    @FXML
    public Button userReviews;
    @FXML
    public ListView<String> courseList;

    @FXML
    public VBox courseBox;
    public GridPane topBar;
    public HBox searchBar;
    private CourseReviewDriver driver;
    private SceneManager sceneManager;

    @FXML
    public TextField subjectSearch;
    @FXML
    public TextField numberSearch;
    @FXML
    public TextField titleSearch;



    private String cur_subject;
    private String cur_title;
    private String cur_number;

    private List<Course> courses;


    public void initData(CourseReviewDriver driver, SceneManager sceneManager) {
        this.driver = driver;
        this.sceneManager = sceneManager;
        do_init();
    }

    public void do_init() {

        topBar.getStylesheets().clear();
        topBar.getStylesheets().add("topbar.css");
        String username = driver.getCurrentUser().getUsername();
        String textToShow;
        if (username.length() > 10) {
            textToShow = username.substring(0, 10) + "...";
        } else {
            textToShow = username;
        }
        userReviews.setText(textToShow +"'s reviews");

        numberSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            cur_number = newValue;
            updateCourses();
            showCourses();
        });
        titleSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            cur_title = newValue;
            updateCourses();
            showCourses();
        });
        subjectSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            cur_subject = newValue;
            updateCourses();
            showCourses();
        });


        showCourses();

    }

    private void showCourses() {
        courseBox.getChildren().removeIf(c -> {
            boolean isSearch;
            if (c == null) {
                return true;
            } else if (c.getId() == null) {
                isSearch = false;
            } else if (c.getId().equals("region")){
                return true;
            }else {
                isSearch = c.getId().equals("searchBar");
            }
            boolean isHBox = c.getClass() == HBox.class;
            return isHBox && !isSearch;
        });
        updateCourses();

        int counter = 0;
        HBox hbox = getHBox();

        for(Course course: courses) {
            if (counter % 4 == 0 && counter != 0) {
                courseBox.getChildren().add(hbox);
                hbox = getHBox();
            }
            Pane box = courseBox(course);
            hbox.getChildren().add(box);
            counter++;
        }
        Region region = new Region();
        region.setMinHeight(50);
        region.setId("region");
        courseBox.getChildren().add(hbox);
        courseBox.getChildren().add(region);
    }

    public HBox getHBox() {
        HBox hbox = new HBox();
        hbox.getStyleClass().clear();
        hbox.getStyleClass().add("card-row");
        return hbox;
    }

    private Pane courseBox(Course course) {
        VBox box = new VBox();
        box.getStyleClass().clear();
        box.getStyleClass().add("course-card");
        Text title = getTitle(course.getTitle());
        double width = title.getLayoutBounds().getWidth();
        if (width > 70) {
            double newSize = 30 * (70.0 / width);
            title.setStyle(String.format("-fx-font-size: %d", (int)newSize));
        }
        Text subject = getSubject(course.getSubject() + " " + course.getCourseNumber());
        String avg = String.format("Average Rating: %.2f", course.getAverageRating());
        if (course.getAverageRating() == -1) {
            avg = "Average Rating: N/A";
        }
        Text averageRating = new Text(avg);
        box.getChildren().addAll(List.of(title, subject, averageRating));
        box.setOnMouseClicked((EventHandler<Event>) event -> {
            try {
                courseClicked(course);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        /* code for potential delete button functionality, from the writeup we didn't
        think this should be implemented
        Button button = new Button();
        button.setText("[ X ]");
        button.getStyleClass().clear();
        button.getStyleClass().add("delete-button");
        button.setOnAction(actionEvent -> deleteClicked(course));
        box.getChildren().add(button);
         */

        return box;
    }


    private void deleteClicked(Course course) {
        try {
            driver.deleteCourse(course);
        } catch (Exception ignored) {}
        showCourses();
    }

    private Text getSubject(String message) {
        Text subject = new Text(message);
        subject.getStyleClass().clear();
        subject.getStyleClass().add("course-subject");
        return subject;
    }

    private Text getTitle(String message) {
        Text title = new Text(message);
        title.getStyleClass().clear();
        title.getStyleClass().add("course-title");
        return title;
    }

    private void courseClicked(Course course) {
        Stage stage = (Stage) userReviews.getScene().getWindow();
        sceneManager.toCourseReviews(stage, course);
    }


    public void toLogin() throws Exception{
        Stage stage = (Stage) userReviews.getScene().getWindow();
        sceneManager.toLogin(stage);
    }

    public void toCourseCreation() {
        Stage stage = (Stage) userReviews.getScene().getWindow();
        sceneManager.toCourseCreation(stage);
    }

    public void toUserReviews() {
        Stage stage = (Stage) userReviews.getScene().getWindow();
        sceneManager.toUserReviews(stage);
    }

    public void close() {
        Stage stage = (Stage) userReviews.getScene().getWindow();
        stage.close();
    }

    private void updateCourses() {
        boolean noNumber = true;
        boolean noSubject = true;
        boolean noTitle = true;
        if (cur_number != null) {
            if (!cur_number.isEmpty()) {
                noNumber = false;
            }
        }
        if (cur_subject != null) {
            if (!cur_subject.isEmpty()) {
                noSubject = false;
            }
        }
        if (cur_title != null) {
            if (!cur_title.isEmpty()) {
                noTitle = false;
            }
        }
        if (noNumber && noSubject && noTitle) {
            this.courses = driver.getAllCourses();
            this.courses.forEach(c -> driver.updateRating(c));
            return;
        }
        String searchNumber = noNumber ? "" : cur_number;
        String searchTitle = noTitle ? "" : cur_title;
        String searchSubject = noSubject ? "" : cur_subject;

        this.courses = driver.courseSearch(searchSubject, searchNumber, searchTitle);
        this.courses.forEach(c -> driver.updateRating(c));
    }

}
