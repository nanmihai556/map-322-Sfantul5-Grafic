package socialnetwork;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import socialnetwork.domain.CurrentUserSingleton;
import socialnetwork.domain.MainUserSingleton;
import socialnetwork.domain.Message;
import socialnetwork.service.UserService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Chat implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private Label chatUserUsername;

    @FXML
    private Label chatUserFirstNameLastName;

    @FXML
    private VBox FriendListVbox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UserService service = HelloApplication.getService();
        Long userId = MainUserSingleton.getInstance().getUser();
        Long chatUserId = CurrentUserSingleton.getInstance().getUser();
        chatUserUsername.setText(service.getUsername(chatUserId));
        String name = service.getUserById(chatUserId).getFirstName() + " " +
                      service.getUserById(chatUserId).getLastName();
        chatUserFirstNameLastName.setText(name);
        List<Message> messages = service.showMessagesBetweenTwo(userId, chatUserId);
        for (Long user: currentUserFriends){
            FriendListVbox.getChildren().addAll(getFriendHbox(service.getUserById(user)));
        }
    }

    public void toUserPage() throws IOException {
        HelloApplication.changeScene("UserPage.fxml");
    }
}
