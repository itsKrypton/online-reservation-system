import java.io.IOException;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;


public class MyTicketsController {

    @FXML
    private ListView<Integer> pnrListView;

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
    private Button cancelTicketButton;

    private String username;

    Hashtable<String, String> pnrDetails;

    public void initialize(String username, boolean isNewTicket) throws SQLException
    {
        this.username = username;

        pnrListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>()
        {
            @Override
            public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {
                if(arg2 != null)
                {
                    try {
                        int selectedPNR = pnrListView.getSelectionModel().getSelectedItem();
                        pnrDetails = DBUtils.getPNRDetails(Integer.toString(selectedPNR));
                        trainNumberLabel.setText(pnrDetails.get("trainNumber"));
                        trainNameLabel.setText(pnrDetails.get("trainName"));
                        classSeatLabel.setText(pnrDetails.get("classSeat"));
                        sourceLabel.setText(pnrDetails.get("source"));
                        destinationLabel.setText(pnrDetails.get("destination"));
                        dateLabel.setText(pnrDetails.get("date"));
                        firstNameLabel.setText(pnrDetails.get("firstName"));
                        lastNameLabel.setText(pnrDetails.get("lastName"));
                        ageGenderLabel.setText(pnrDetails.get("ageGender"));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        ObservableList<Integer> pnrList = DBUtils.getPNRList(username);
        if(pnrList.size() == 0)
        {
            trainNameLabel.setVisible(false);
            trainNumberLabel.setVisible(false);
            classSeatLabel.setVisible(false);
            sourceLabel.setVisible(false);
            destinationLabel.setVisible(false);
            dateLabel.setVisible(false);
            firstNameLabel.setVisible(false);
            lastNameLabel.setVisible(false);
            ageGenderLabel.setVisible(false);
            cancelTicketButton.setVisible(false);
            
        }
        pnrListView.setItems(pnrList);
        if(!isNewTicket)
        pnrListView.getSelectionModel().selectFirst();

        else
        pnrListView.getSelectionModel().selectLast();
    }

    public void onCancelTicketButton() throws SQLException
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Are you sure?");
        alert.setContentText("Do you want to CANCEL your ticket? You CANNOT undo this action later.");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK)
        {
            int PNRnumber = pnrListView.getSelectionModel().getSelectedItem();
            DBUtils.cancelTicket(PNRnumber);
            initialize(username, false);
        }
    }

    public void onGoBack(ActionEvent event) throws IOException
    {
        DBUtils.changeScene(event, "LoggedIn.fxml", "Welcome!", username);
    }
}
