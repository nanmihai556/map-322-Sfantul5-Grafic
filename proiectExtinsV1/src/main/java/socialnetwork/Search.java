package socialnetwork;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import socialnetwork.domain.MainUserSingleton;
import socialnetwork.service.UserService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Search implements Initializable {

    @FXML
    private Label currentUsername;

    @FXML
    private Label firstNameLastName;


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
}
