package socialnetwork;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
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
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Home implements Initializable {
    private Long currentUser;
    private UserService service;

    @FXML
    private Button LogoutButton;

    @FXML
    private Button FriendsButton;

    @FXML
    private Button FriendRequestsButton;

    @FXML
    private ListView<User> FriendList;

    public void userLogout(ActionEvent event) throws IOException{
        HelloApplication m = new HelloApplication();
        m.changeScene("Login.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        service = new UserService(
                new DatabaseRepository("jdbc:postgresql://localhost:5432/DBRepository", "postgres", DBconfigs.password, new UtilizatorValidator()),
                new MessageDatabaseRepository("jdbc:postgresql://localhost:5432/DBRepository", "postgres", DBconfigs.password, new MessageValidator()),
                new LogInDatabaseRepository("jdbc:postgresql://localhost:5432/DBRepository", "postgres", DBconfigs.password, new AccountValidator())
        );
        List<User> currentUserFriends = service.showFriends(2L);
        FriendList.getItems().addAll(currentUserFriends);
    }
}
