import java.io.IOException;
import java.sql.SQLException;
import java.util.Hashtable;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PNREnquiryController
{
    @FXML
    private TextField PNRNumberField;

    @FXML
    private Button goBackButton;

    @FXML
    private Label trainNumberLabel;

    @FXML
    private Label trainNameLabel;

    @FXML
    private Label classSeatLabel;

    @FXML
    private Label sourceLabel;

    @FXML
    private Label destinationLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label firstNameLabel;

    @FXML
    private Label lastNameLabel;

    @FXML
    private Label ageGenderLabel;

    @FXML
    private Button searchButton;

    private String username;

    public void initialize(String username)
    {
        this.username = username;

        trainNameLabel.setVisible(false);
        trainNumberLabel.setVisible(false);
        classSeatLabel.setVisible(false);
        sourceLabel.setVisible(false);
        destinationLabel.setVisible(false);
        dateLabel.setVisible(false);
        firstNameLabel.setVisible(false);
        lastNameLabel.setVisible(false);
        ageGenderLabel.setVisible(false);

        PNRNumberField.textProperty().addListener(new ChangeListener<String>() 
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
            {
                if (!newValue.matches("\\d*")) 
                {
                    PNRNumberField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    public void onSearch() throws SQLException
    {
        Hashtable<String, String> pnrDetails = new Hashtable<>();
        pnrDetails = DBUtils.getPNRDetails(PNRNumberField.getText());
        if(pnrDetails.size() == 0)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please enter a valid PNR Number");
            alert.setHeaderText("Invalid PNR");
            alert.show();
        }

        else
        {
            trainNumberLabel.setText(pnrDetails.get("trainNumber"));
            trainNumberLabel.setVisible(true);
            trainNameLabel.setText(pnrDetails.get("trainName"));
            trainNameLabel.setVisible(true);
            classSeatLabel.setText(pnrDetails.get("classSeat"));
            classSeatLabel.setVisible(true);
            sourceLabel.setText(pnrDetails.get("source"));
            sourceLabel.setVisible(true);
            destinationLabel.setText(pnrDetails.get("destination"));
            destinationLabel.setVisible(true);
            dateLabel.setText(pnrDetails.get("date"));
            dateLabel.setVisible(true);
            firstNameLabel.setText(pnrDetails.get("firstName"));
            firstNameLabel.setVisible(true);
            lastNameLabel.setText(pnrDetails.get("lastName"));
            lastNameLabel.setVisible(true);
            ageGenderLabel.setText(pnrDetails.get("ageGender"));
            ageGenderLabel.setVisible(true);
        }
    }

    public void onGoBack(ActionEvent event) throws IOException
    {
        DBUtils.changeScene(event, "LoggedIn.fxml", "Welcome!", username);
    }
}