import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LogInController
{
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button logInButton;

    @FXML
    private Button toSignUpButton;

    public void initialize() 
    {
        logInButton.setDisable(true);
        logInButton.setDefaultButton(true);

        logInButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) 
            {
                try {
                    DBUtils.logInUser(event, usernameField.getText(), passwordField.getText());
                    usernameField.clear();
                    passwordField.clear();
                    usernameField.requestFocus();
                } catch (SQLException | IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        toSignUpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) 
            {
                try {
                    DBUtils.changeScene(event, "SignUp.fxml", "Sign Up!", null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void handleKeyReleased()
    {
        String username = usernameField.getText();
        String password = passwordField.getText();
        boolean disableButton = (username.isEmpty() || username.trim().isEmpty()) || (password.isEmpty() || password.trim().isEmpty());
        logInButton.setDisable(disableButton); 
    }
}