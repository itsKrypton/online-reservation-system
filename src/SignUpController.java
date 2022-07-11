import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class SignUpController 
{
    @FXML
    private TextField fNameField;

    @FXML
    private TextField lNameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button signUpButton;

    @FXML
    private Button toLogInButton;

    public void initialize()
    {
        signUpButton.setDisable(true);
        signUpButton.setDefaultButton(true);

        signUpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event)
            {
                if(emailField.getText().trim().matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$"))
                {
                    try {
                        DBUtils.signUpUser(event, usernameField.getText().trim(), passwordField.getText().trim(), emailField.getText().trim(), fNameField.getText().trim(), lNameField.getText().trim());
                        usernameField.clear();
                        passwordField.clear();
                    } catch (ClassNotFoundException | SQLException | IOException e) {
                        e.printStackTrace();
                    }
                }

                else
                {
                    System.out.println("Wrong Email ID");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Invalid Email");
                    alert.setContentText("Please try again with a valid Email");
                    alert.show();
                }
            }
        });

        toLogInButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event)
            {
                try {
                    DBUtils.changeScene(event, "LogIn.fxml", "Log In!", null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void handleKeyReleased()
    {
        String fName = fNameField.getText();
        String lName = lNameField.getText();
        String email = emailField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        boolean disableButton = (username.isEmpty() || username.trim().isEmpty()) || (password.isEmpty() || password.trim().isEmpty()) || (fName.isEmpty() || fName.trim().isEmpty()) || (lName.isEmpty() || lName.trim().isEmpty()) || (email.isEmpty() || email.trim().isEmpty());
        signUpButton.setDisable(disableButton); 
    }
}
