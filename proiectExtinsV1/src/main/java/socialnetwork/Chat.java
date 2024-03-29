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
import socialnetwork.domain.Message;
import socialnetwork.domain.User;
import socialnetwork.service.UserService;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
    private TextField chatField;

    @FXML
    private VBox chatVBox;

    @FXML
    private Button sendButton;

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
        for (Message message: messages){
            chatVBox.getChildren().addAll(getMessageHbox(message));
        }
    }

    private HBox getMessageHbox(Message message){
        UserService service = HelloApplication.getService();
        HBox messageHBox = new HBox();
        Text userName = new Text(userInfo(service.getUserById(message.getFrom()))+":");
        Text message1 = new Text(message.getMessage());
        Text messageDate = new Text(message.getDate().toString());
        messageHBox.setSpacing(10);
        if(message.getFrom().equals(CurrentUserSingleton.getInstance().getUser())) {
            Color c = Color.web("#FFEEDB", 1.0);
            userName.setFill(c);
            userName.setFont(Font.font ("Verdana", 20));
            message1.setFill(c);
            message1.setFont(Font.font("Verdana", 20));
        }
        else {
            Color c = Color.web("#fc6603", 1.0);
            message1.setFill(c);
            message1.setFont(Font.font ("Verdana", 20));
            userName.setFill(c);
            userName.setFont(Font.font ("Verdana", 20));
        }
        messageDate.setFill(Color.BLACK);
        messageDate.setFont(Font.font ("Verdana", 10));
        messageHBox.getChildren().addAll(userName,message1,messageDate);
        return messageHBox;
    }

    private String userInfo(User user){
        return user.getFirstName() + " " +user.getLastName();
    }

    public void sendMessage() throws IOException{
        ArrayList<Long> user = new ArrayList<>();
        user.add(CurrentUserSingleton.getInstance().getUser());
        HelloApplication.getService().addMessage(MainUserSingleton.getInstance().getUser(),
                                                 user,chatField.getText(),0);
        HelloApplication.changeScene("Chat.fxml");
    }

    public void refresh() throws IOException{
        HelloApplication.changeScene("Chat.fxml");
    }

    public void toUserPage() throws IOException {
        HelloApplication.changeScene("UserPage.fxml");
    }
}
