package socialnetwork;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import socialnetwork.UI.Menu;
import socialnetwork.config.DBconfigs;
import socialnetwork.domain.CurrentUserSingleton;
import socialnetwork.domain.MainUserSingleton;
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

    @FXML
    private Label currentUsername;

    @FXML
    private Label FirstNameLastName;

    @FXML
    private VBox FriendListVbox;

    public void userLogout(ActionEvent event) throws IOException{
        HelloApplication.changeScene("Login.fxml");
    }

    public void userFriendRequestPage(ActionEvent event) throws  IOException{
        HelloApplication.changeScene("FriendRequests.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        service = HelloApplication.getService();
        Long userId = MainUserSingleton.getInstance().getUser();
        currentUsername.setText(service.getUsername(userId));
        String name = service.getUserById(userId).getFirstName() + " " + service.getUserById(userId).getLastName();
        FirstNameLastName.setText(name);
        List<Long> currentUserFriends = service.getFriendsAccepted(service.getUserById(userId));
        for (Long user : currentUserFriends) {
                FriendListVbox.getChildren().addAll(getFriendHbox(service.getUserById(user)));
        }
    }

    private String userInfo(User user){
        return user.getFirstName() + " " +user.getLastName();
    }

    private HBox getFriendHbox(User user){
        HBox userHBox = new HBox();
        Text userName = new Text(userInfo(user));
        userName.setFill(Color.BLACK);
        userName.setFont(Font.font ("Verdana", 20));
        Button seeFriends = new Button("Click to see Profile");
        seeFriends.setStyle("-fx-background-color: #f06103; ");
        seeFriends.setId(user.getId().toString());
        seeFriends.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                Button btn = (Button) event.getSource();
                CurrentUserSingleton.getInstance().setUser(Long.valueOf(btn.getId()));
                try{
                    HelloApplication.changeScene("UserPage.fxml");
                }
                catch(Exception e){
                    System.out.println(e);
                }
            }
        });
        userHBox.getChildren().addAll(userName,seeFriends);
        return userHBox;
    }
}
