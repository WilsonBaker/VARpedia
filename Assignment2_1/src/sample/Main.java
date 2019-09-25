package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    Stage window;
    Scene scene1, scene2;

    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;

        Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
        window.setTitle("VARpedia");
        window.setScene(new Scene(root, 500, 500));
        window.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
