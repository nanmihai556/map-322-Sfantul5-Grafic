package socialnetwork;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import socialnetwork.UI.Menu;
import socialnetwork.config.DBconfigs;
import socialnetwork.domain.validators.AccountValidator;
import socialnetwork.domain.validators.MessageValidator;
import socialnetwork.repository.database.LogInDatabaseRepository;
import socialnetwork.repository.database.MessageDatabaseRepository;

import java.io.IOException;

public class HelloApplication extends Application {

    private static Stage stg;
    @Override
    public void start(Stage primaryStage) throws IOException {
        stg = primaryStage;
        primaryStage.setResizable(false);
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 777, 395);
        primaryStage.setTitle("Sfantul 5");
        primaryStage.setScene(scene);
        primaryStage.show();

//        Menu menu = new Menu();
//        menu.menu();
    }

    public void changeScene(String fxml) throws IOException{
        Parent pane = FXMLLoader.load(getClass().getResource(fxml));
        stg.getScene().setRoot(pane);
    }
    public static void main(String[] args) {
        launch();
    }
}