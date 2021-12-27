package socialnetwork;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class SignUp {
    @FXML
    private Button CreateAccountButton;
    @FXML
    private Label wrongSignUp;

    @FXML
    private Button backButton;

    public void userSignUp() throws IOException{
    }

    public void back() throws IOException{
        HelloApplication m = new HelloApplication();
        m.changeScene("Login.fxml");
    }

}
