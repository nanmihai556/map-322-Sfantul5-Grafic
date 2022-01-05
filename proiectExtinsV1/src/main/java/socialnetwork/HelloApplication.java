package socialnetwork;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import socialnetwork.UI.Menu;
import socialnetwork.config.DBconfigs;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.AccountValidator;
import socialnetwork.domain.validators.MessageValidator;
import socialnetwork.domain.validators.UtilizatorValidator;
import socialnetwork.repository.database.DatabaseRepository;
import socialnetwork.repository.database.LogInDatabaseRepository;
import socialnetwork.repository.database.MessageDatabaseRepository;
import socialnetwork.service.UserService;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {
    private static UserService service;
    private static Stage stg;

    public static UserService getService() {
        return service;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        stg = primaryStage;
        service = new UserService(
                new DatabaseRepository("jdbc:postgresql://localhost:5432/DBRepository", "postgres", DBconfigs.password, new UtilizatorValidator()),
                new MessageDatabaseRepository("jdbc:postgresql://localhost:5432/DBRepository", "postgres", DBconfigs.password, new MessageValidator()),
                new LogInDatabaseRepository("jdbc:postgresql://localhost:5432/DBRepository", "postgres", DBconfigs.password, new AccountValidator())
        );
        //primaryStage.setResizable(false);
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 777, 395);
        primaryStage.setTitle("Sfantul 5");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void changeScene(String fxml) throws IOException{
        Parent pane = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource(fxml)));
        stg.getScene().setRoot(pane);
    }

    public static Stage getStg() {
        return stg;
    }

    public static void main(String[] args) {
        launch();
    }
}