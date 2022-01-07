package socialnetwork;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import socialnetwork.domain.User;
import socialnetwork.service.UserService;

import java.io.IOException;
import java.util.Scanner;

public class SignUp {
    @FXML
    private Button CreateAccountButton;
    @FXML
    private Label wrongSignUp;
    @FXML
    private Button backButton;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField confirmPassword;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;



    public void userSignUp() throws IOException{
        UserService service = HelloApplication.getService();
        if(!password.getText().equals(confirmPassword.getText())){
            wrongSignUp.setText("Password and Confirm Password must match");
        }
        else if(username.getText().isEmpty() ||
                password.getText().isEmpty() ||
                confirmPassword.getText().isEmpty()||
                firstName.getText().isEmpty() ||
                lastName.getText().isEmpty()){
                wrongSignUp.setText("All fields must be set");
        }
        else if(service.usernameTaken(username.getText())){
            wrongSignUp.setText("Username is taken");
        }
        else{
            Long id = service.getMaxId() + 1L;
            String AccountfirstName = firstName.getText();
            String AccountlastName = lastName.getText();
            User newAccount = new User(AccountfirstName, AccountlastName);
            newAccount.setId(id);
            service.addUser(newAccount);
            service.addAccount(username.getText(), password.getText(), id);
            HelloApplication.changeScene("Login.fxml");
        }
    }

    public void back() throws IOException{
        HelloApplication.changeScene("Login.fxml");
    }

}
