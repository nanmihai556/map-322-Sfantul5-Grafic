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

public class UserPage implements Initializable {
    private UserService service;
    @FXML
    private Button LogoutButton;

    @FXML
    private Button removeFriendRequestButton;

    @FXML
    private Button addFriendButton;
    @FXML
    private Button seeChatButton;
    @FXML
    private Button removeFriendButton;
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
            if(!user.equals(MainUserSingleton.getInstance().getUser()))
                FriendListVbox.getChildren().addAll(getFriendHbox(service.getUserById(user)));
        }
        if (!service.areFriends(MainUserSingleton.getInstance().getUser(),CurrentUserSingleton.getInstance().getUser())){
            addFriendButton.setOpacity(1.0D);
            addFriendButton.setDisable(false);
        }
        if (
                service.areFriends(MainUserSingleton.getInstance().getUser(),CurrentUserSingleton.getInstance().getUser())&&
                !service.didF1RequestF2(MainUserSingleton.getInstance().getUser(),CurrentUserSingleton.getInstance().getUser())
        ){
            removeFriendButton.setOpacity(1.0D);
            removeFriendButton.setDisable(false);
            seeChatButton.setOpacity(1.0D);
            seeChatButton.setDisable(false);
        }
        if(service.didF1RequestF2(MainUserSingleton.getInstance().getUser(),CurrentUserSingleton.getInstance().getUser())){
            removeFriendRequestButton.setOpacity(1.0D);
            removeFriendRequestButton.setDisable(false);
        }

    }

    public void userLogout(ActionEvent event) throws IOException {
        HelloApplication.changeScene("Home.fxml");
    }

    public void seeChat(ActionEvent event) throws IOException {
        HelloApplication.changeScene("Chat.fxml");
    }

    public void addFriend(ActionEvent event) throws IOException {
        HelloApplication.getService().addFriend(MainUserSingleton.getInstance().getUser(),CurrentUserSingleton.getInstance().getUser(),0);
        try{
            HelloApplication.changeScene("UserPage.fxml");
        }
        catch(Exception e){
            System.err.println(e);
        }
    }

    public void removeFriend(ActionEvent event) throws IOException {
        HelloApplication.getService().removeFriend(MainUserSingleton.getInstance().getUser(),CurrentUserSingleton.getInstance().getUser());
        try{
            HelloApplication.changeScene("UserPage.fxml");
        }
        catch(Exception e){

        }
    }

    private String userInfo(User user){
        return user.getFirstName() + " " +user.getLastName();
    }

    private HBox getFriendHbox(User user){
        HBox userHBox = new HBox();
        Text userName = new Text(userInfo(user));
        Color c = Color.web("#FFEEDB", 1.0);
        userName.setFill(c);
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
