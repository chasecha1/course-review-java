package edu.virginia.sde.reviews;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

public class HelloWorldApplication extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        CourseReviewDriver driver = new CourseReviewDriver();
        SceneManager sceneManager = new SceneManager(driver);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-world.fxml"));
        Parent root = fxmlLoader.load();
        LoginController controller = fxmlLoader.getController();
        controller.initData(driver, sceneManager);
        Scene scene = new Scene(root);
        stage.setTitle("Hello World");
        stage.setScene(scene);
        stage.show();
    }
}
