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
import socialnetwork.config.DBconfigs;
import socialnetwork.domain.CurrentUserSingleton;
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

public class UserPage implements Initializable {
    private UserService service;
    @FXML
    private Button LogoutButton;

    @FXML
    private Button FriendsButton;

    @FXML
    private Button FriendRequestsButton;

    @FXML
    private Label currentUsername;

    @FXML
    private Label FirstNameLastName;

    @FXML
    private VBox FriendListVbox;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        service = HelloApplication.getService();
        Long userId = CurrentUserSingleton.getInstance().getUser();
        currentUsername.setText(service.getUsername(userId));
        String name = service.getUserById(userId).getFirstName() + " " + service.getUserById(userId).getLastName();
        FirstNameLastName.setText(name);
        List<Long> currentUserFriends = service.getFriendsAccepted(service.getUserById(userId));
        for (Long user: currentUserFriends){
            FriendListVbox.getChildren().addAll(getFriendHbox(service.getUserById(user)));
        }
    }
    public void userLogout(ActionEvent event) throws IOException {
        HelloApplication m = new HelloApplication();
        m.changeScene("Login.fxml");
    }

    private String userInfo(User user){
        return user.getFirstName() + " " +user.getLastName();
    }

    private HBox getFriendHbox(User user){
        HBox userHBox = new HBox();
        Text userName = new Text(userInfo(user));
        userName.setFill(Color.WHITE);
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

                }
            }
        });
        userHBox.getChildren().addAll(userName,seeFriends);
        return userHBox;
    }
}
