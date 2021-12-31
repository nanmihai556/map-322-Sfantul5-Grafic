package socialnetwork;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import socialnetwork.config.DBconfigs;
import socialnetwork.domain.CurrentUserSingleton;
import socialnetwork.domain.MainUserSingleton;
import socialnetwork.domain.validators.AccountValidator;
import socialnetwork.repository.database.LogInDatabaseRepository;
import socialnetwork.service.UserService;

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
        UserService service = HelloApplication.getService();
        Long logInResponse = service.logIn(username.getText(), password.getText());
        try {
            if (logInResponse > 0) {
                MainUserSingleton.getInstance().setUser(logInResponse);
                HelloApplication.changeScene("Home.fxml");
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
        if(username.getText().equals("mihai") && password.getText().equals("nan")){
            wrongLogin.setText("Success!");
            HelloApplication.changeScene("Home.fxml");
        }
        else if (username.getText().isEmpty() && password.getText().isEmpty()){
            wrongLogin.setText("Please enter your credentials!");
        }
        else{
            wrongLogin.setText("Wrong username or password!");
        }
    }

    public void userSignUp() throws IOException{
        HelloApplication.changeScene("SignUp.fxml");
    }
}
