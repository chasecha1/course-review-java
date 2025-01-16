package edu.virginia.sde.reviews;

import com.sun.javafx.css.CssUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;


public class LoginController {

    @FXML
    public Label errorText;
    @FXML
    public HBox errorBox;
    private CourseReviewDriver driver;
    private SceneManager sceneManager;

    @FXML
    private TextField usernameInput;
    @FXML
    private TextField passwordInput;


    public void initData(CourseReviewDriver driver, SceneManager sceneManager) {
        this.driver = driver;
        this.sceneManager = sceneManager;
    }

    public void login() throws Exception {
        User user = getUser();
        if (driver.userExists(user)) {
            if (driver.correctPassword(user)) {
                driver.setCurrentUser(user);
                try {
                    toCourseSearch();
                } catch (Exception e) {
                    troubleLoggingIn();
                }
            } else {
                loginError("Incorrect password");
            }
        } else {
            loginError("Username not found");
        }
    }


    public void register() throws Exception {
        User user = getUser();
        UserCreationCode responseCode = driver.createUser(user);
        if (responseCode == UserCreationCode.USER_ADDED) {
            loginSuccess();
        } else if (responseCode == UserCreationCode.PASSWORD_NOT_8_CHARACTERS) {
            loginError("Password not long enough");
        } else if (responseCode == UserCreationCode.USERNAME_TAKEN) {
            loginError(String.format("Username: %s is already taken", user.getUsername()));
        } else if (responseCode == UserCreationCode.SQL_EXCEPTION) {
            troubleLoggingIn();
        }
    }

    private void loginError(String message) {
        errorText.setText(message);
        errorBox.setStyle("-fx-background-color: #ff8c8c");
    }

    private void loginSuccess() {
        errorText.setText("User Created!");
        errorBox.setStyle("-fx-background-color: #79d26c");
    }

    private User getUser() {
        String username = usernameInput.getText();
        String password = passwordInput.getText();
        return new User(username, password);
    }

    private void toCourseSearch() throws Exception {
        Stage stage = (Stage)usernameInput.getScene().getWindow();
        sceneManager.toCourseSearch(stage);
    }

    private void troubleLoggingIn() throws Exception {
        Stage stage = (Stage)usernameInput.getScene().getWindow();
        sceneManager.toBadDatabaseConnection(stage);
    }

    public void close() {
        Stage stage = (Stage)usernameInput.getScene().getWindow();
        stage.close();
    }






}
