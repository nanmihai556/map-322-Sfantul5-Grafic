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
import socialnetwork.domain.CurrentUserSingleton;
import socialnetwork.domain.MainUserSingleton;
import socialnetwork.domain.User;
import socialnetwork.service.UserService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FriendRequestPage implements Initializable {
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

    public void toHomePage(ActionEvent event) throws IOException {
        HelloApplication.changeScene("Home.fxml");
    }

    public void userLogout(ActionEvent event) throws IOException{
        HelloApplication.changeScene("Login.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        service = HelloApplication.getService();
        Long userId = MainUserSingleton.getInstance().getUser();
        currentUsername.setText(service.getUsername(userId));
        String name = service.getUserById(userId).getFirstName() + " " + service.getUserById(userId).getLastName();
        FirstNameLastName.setText(name);
        List<Long> currentUserFriends = service.getFriendsPending(service.getUserById(userId));
        for (Long user: currentUserFriends){
            FriendListVbox.getChildren().addAll(getFriendHbox(service.getUserById(user)));
        }
        List<Long> acceptedFriends = service.getFriendsAccepted(service.getUserById(userId));
        for (Long user: acceptedFriends){
            FriendListVbox.getChildren().addAll(getNonPendingFriends(service.getUserById(user),":Accepted"));
        }
        List<Long> rejectedFriends = service.getFriendsRejected(service.getUserById(userId));
        for (Long user: rejectedFriends){
            FriendListVbox.getChildren().addAll(getNonPendingFriends(service.getUserById(user),":Rejected"));
        }
    }

    private String userInfo(User user){
        return user.getFirstName() + " " +user.getLastName();
    }

    private String dateOfFriendShip(User user){
        User MainUser = HelloApplication.getService().getUserById(MainUserSingleton.getInstance().getUser());
        return HelloApplication.getService().getDateOfFriendship(MainUser, user).toString();
    }

    private HBox getFriendHbox(User user){
        HBox userHBox = new HBox();
        Text userName = new Text(userInfo(user));
        Text userDate = new Text(dateOfFriendShip(user));
        userHBox.setSpacing(10);
        userName.setFill(Color.BLACK);
        userName.setFont(Font.font ("Verdana", 20));
        userDate.setFill(Color.BLACK);
        userDate.setFont(Font.font ("Verdana", 10));
        Button acceptFriends = new Button("Accept");
        acceptFriends.setStyle("-fx-background-color: #f06103; ");
        acceptFriends.setId(user.getId().toString());
        acceptFriends.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                Button btn = (Button) event.getSource();
                HelloApplication.getService().acceptFriendRequest(MainUserSingleton.getInstance().getUser(), Long.valueOf(btn.getId()));
                try{
                    HelloApplication.changeScene("FriendRequests.fxml");
                }
                catch(Exception e){
                    System.out.println(e);
                }
            }
        });
        Button rejectFriends = new Button("Reject");
        rejectFriends.setStyle("-fx-background-color: #f06103; ");
        rejectFriends.setId(user.getId().toString());
        rejectFriends.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                Button btn = (Button) event.getSource();
                HelloApplication.getService().rejectFriendRequest(MainUserSingleton.getInstance().getUser(), Long.valueOf(btn.getId()));
                try{
                    HelloApplication.changeScene("FriendRequests.fxml");
                }
                catch(Exception e){

                }
            }
        });
        userHBox.getChildren().addAll(userName,acceptFriends,rejectFriends, userDate);
        return userHBox;
    }

    private HBox getNonPendingFriends(User user, String status){
        HBox userHBox = new HBox();
        Text userName = new Text(userInfo(user));
        Text userDate = new Text(dateOfFriendShip(user));
        Text userStatus = new Text(status);
        userHBox.setSpacing(10);
        userName.setFill(Color.BLACK);
        userName.setFont(Font.font ("Verdana", 20));
        userDate.setFill(Color.BLACK);
        userDate.setFont(Font.font ("Verdana", 10));
        userStatus.setFill(Color.BLACK);
        userStatus.setFont(Font.font ("Verdana", 20));
        userHBox.getChildren().addAll(userName,userStatus,userDate);
        return userHBox;
    }

    public void seeChat(ActionEvent event) throws IOException {
        HelloApplication.changeScene("Chat.fxml");
    }}
