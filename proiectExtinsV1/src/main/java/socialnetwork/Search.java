package socialnetwork;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
import java.util.Objects;
import java.util.ResourceBundle;

public class Search implements Initializable {

    @FXML
    private Label currentUsername;

    @FXML
    private Label firstNameLastName;

    @FXML
    private TextField searchBarText;

    @FXML
    private VBox searchVbox;


    public void toHomePage(ActionEvent event) throws IOException {
        HelloApplication.changeScene("Home.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UserService service = HelloApplication.getService();
        Long userId = MainUserSingleton.getInstance().getUser();
        currentUsername.setText(service.getUsername(userId));
        String name = service.getUserById(userId).getFirstName() + " " + service.getUserById(userId).getLastName();
        firstNameLastName.setText(name);
    }

    public void searchForUsers() {
        int gasit = 0;
        searchVbox.getChildren().removeAll(searchVbox.getChildren());
        String searchText = searchBarText.getText();
        searchBarText.setText("");
        UserService service = HelloApplication.getService();
        Long currentUserId = MainUserSingleton.getInstance().getUser();
        for(User user: service.getAll())
            if((user.getFirstName().contains(searchText) || user.getLastName().contains(searchText) ||
               service.getUsername(user.getId()).contains(searchText)) && !Objects.equals(user.getId(), currentUserId)) {
                gasit = 1;
                searchVbox.getChildren().addAll(getUserHbox(service.getUserById(user.getId())));
            }
        if(gasit == 0){
            HBox nothingFound = new HBox();
            Text none = new Text("No users found");
            none.setFill(Color.BLACK);
            none.setFont(Font.font ("Verdana", 20));
            nothingFound.getChildren().addAll(none);
            searchVbox.getChildren().addAll(nothingFound);
        }
    }

    private HBox getUserHbox(User user){
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
                    System.out.println(e);
                }
            }
        });
        userHBox.getChildren().addAll(userName,seeFriends);
        return userHBox;
    }

    private String userInfo(User user){
        return user.getFirstName() + " " +user.getLastName();
    }
}
