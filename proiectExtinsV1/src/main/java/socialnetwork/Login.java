package socialnetwork;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class Login {

    public Login(){

    }

    @FXML
    private Button LoginButton;
    @FXML
    private Button SignUpButton;
    @FXML
    private Label wrongLogin;
    @FXML
    private TextField username;
    @FXML
    private TextField password;

    public void userLogin(ActionEvent event) throws IOException{
        checkLogin();
    }

    private void checkLogin() throws IOException{
        HelloApplication m = new HelloApplication();
        if(username.getText().equals("mihai") && password.getText().equals("nan")){
            wrongLogin.setText("Success!");
            m.changeScene("Home.fxml");
        }
        else if (username.getText().isEmpty() && password.getText().isEmpty()){
            wrongLogin.setText("Please enter your credentials!");
        }
        else{
            wrongLogin.setText("Wrong username or password!");
        }
    }

    public void userSignUp() throws IOException{
        HelloApplication m = new HelloApplication();
        m.changeScene("SignUp.fxml");
    }
}
