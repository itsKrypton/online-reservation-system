import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.io.IOException;
import java.sql.Date;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;

public class NewTicketController {

    @FXML
    private ComboBox<String> genderField;

    @FXML
    private ComboBox<String> classField;

    @FXML
    private Label PNRInfoLabel;

    @FXML
    private TextField trainNumberField;

    @FXML
    private TextField trainNameField;

    @FXML
    private TextField originField;

    @FXML
    private TextField destinationField;

    @FXML
    private DatePicker dateField;

    @FXML
    private TextField userIdField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;    

    @FXML
    private TextField ageField;

    @FXML
    private Button goBackButton;

    @FXML
    private Button submitButton;

    @FXML
    private CheckBox saveDetailsBox;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private ImageView passengerIcon;

    private String username;
    private int PNR;
    private String seatNumber;

    private Stage primaryStage;
    private Scene scene;
    private Parent root;

    private List<List<String>> autoCompleteDetails = new ArrayList<>();

    public void initialize(String username) throws SQLException
    {
        PNRInfoLabel.setVisible(false);
        this.username = username;
        userIdField.setText(username);
        submitButton.setDisable(true);

        trainNumberField.textProperty().addListener(new ChangeListener<String>() 
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
            {
                if (!newValue.matches("\\d*")) 
                {
                    trainNumberField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        originField.textProperty().addListener(new ChangeListener<String>() 
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
            {
                if (!newValue.matches("\\sa-zA-Z*")) 
                {
                    originField.setText(newValue.replaceAll("[^\\sa-zA-Z]", ""));
                }
            }
        });

        destinationField.textProperty().addListener(new ChangeListener<String>() 
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
            {
                if (!newValue.matches("\\sa-zA-Z*")) 
                {
                    destinationField.setText(newValue.replaceAll("[^\\sa-zA-Z]", ""));
                }
            }
        });

        firstNameField.textProperty().addListener(new ChangeListener<String>() 
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
            {
                if (!newValue.matches("\\sa-zA-Z*")) 
                {
                    firstNameField.setText(newValue.replaceAll("[^\\sa-zA-Z]", ""));
                }
            }
        });

        lastNameField.textProperty().addListener(new ChangeListener<String>() 
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
            {
                if (!newValue.matches("\\sa-zA-Z*")) 
                {
                    lastNameField.setText(newValue.replaceAll("[^\\sa-zA-Z]", ""));
                }
            }
        });

        ageField.textProperty().addListener(new ChangeListener<String>() 
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
            {
                if (!newValue.matches("\\d*")) 
                {
                    ageField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        dateField.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
        
                setDisable(empty || date.compareTo(today) < 0 || date.compareTo(today.plusDays(120)) > 0);
            }
        });
        
        autoCompleteDetails = DBUtils.getAutoCompletetionDetails(username);
        Button[] autoCompleteButtons = new Button[autoCompleteDetails.size()];

        if(autoCompleteDetails.size() == 0)
        {
            scrollPane.setVisible(false);
            passengerIcon.setVisible(false);
        }

        HBox hBox = new HBox();
        hBox.setId("scrollHBox");
        for(int i=0; i<autoCompleteDetails.size(); i++)
        {
            autoCompleteButtons[i] = new Button((i+1) + ". " + autoCompleteDetails.get(i).get(0) + " " + autoCompleteDetails.get(i).get(1));
            hBox.getChildren().add(autoCompleteButtons[i]);
            autoCompleteButtons[i].setOnAction(event -> {
                System.out.println(((Button) event.getSource()).getText());
                setAutoCompleteText(((Button) event.getSource()).getText());
            });
        }
        scrollPane.setContent(hBox);
        scrollPane.setPannable(true);
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);

        genderField.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
                handleKeyReleased();
            }
        });

        classField.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
                handleKeyReleased();
            }
        });

        dateField.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> arg0, LocalDate arg1, LocalDate arg2) {
                handleKeyReleased();
            }
        });
    }

    public void setAutoCompleteText(String buttonText)
    {
        int index = Integer.parseInt(Character.toString(buttonText.charAt(0))) - 1;
        firstNameField.setText(autoCompleteDetails.get(index).get(0));
        lastNameField.setText(autoCompleteDetails.get(index).get(1));
        ageField.setText(autoCompleteDetails.get(index).get(2));
        genderField.getSelectionModel().select(autoCompleteDetails.get(index).get(3));
        handleKeyReleased();
    }

    public void setTrainName() throws SQLException
    {
        if(trainNameField.getText() != null)
        {
            trainNameField.setText(DBUtils.handleTrainName(trainNumberField.getText()));
        }
    }

    public void onSubmit(ActionEvent event) throws SQLException, IOException
    {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setHeaderText("Are you sure?");
        confirmAlert.setContentText("Do you want to confirm the booking?");
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK)
        {
            if(saveDetailsBox.isSelected())
            DBUtils.saveAutoCompletionDetails(username, firstNameField.getText().trim(), lastNameField.getText().trim(), Integer.parseInt(ageField.getText().trim()), genderField.getValue().trim());

            PNR = PNRcumSeatGenerator.generatePNR();
            seatNumber = PNRcumSeatGenerator.generateSeat(classField.getValue().trim(), Integer.parseInt(trainNumberField.getText().trim()), Date.valueOf(dateField.getValue()));
            DBUtils.storeNewTicket(PNR, trainNumberField.getText().trim(), seatNumber, classField.getValue(), originField.getText().trim(), destinationField.getText().trim(), Date.valueOf(dateField.getValue()), username, firstNameField.getText().trim(), lastNameField.getText().trim(), Integer.parseInt(ageField.getText().trim()), genderField.getValue());
            trainNumberField.clear();
            trainNameField.clear();
            originField.clear();
            destinationField.clear();
            dateField.setValue(null);
            classField.setValue(null);
            firstNameField.clear();
            lastNameField.clear();
            ageField.clear();
            genderField.setValue(null);
            submitButton.setDisable(true);
            saveDetailsBox.setSelected(false);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Thanks for booking with us!");
            alert.setContentText("Your PNR is: " + PNR + ". You may continue booking more tickets now.");
            ButtonType ticketDetails = new ButtonType("See Ticket Details");
            alert.getButtonTypes().add(ticketDetails);
            Optional<ButtonType> option = alert.showAndWait();

            if(option.get() == ticketDetails)
            {
                String name = username;
                FXMLLoader loader = new FXMLLoader(getClass().getResource("MyTickets.fxml"));
                root = loader.load();

                MyTicketsController myTicketsController = loader.getController();
                myTicketsController.initialize(name, true);
                
                primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
                scene = new Scene(root, 1280, 720);
                JMetro jMetro = new JMetro();
                jMetro.setScene(scene);
                scene.getStylesheets().add("styling.css");
                primaryStage.setScene(scene);
                primaryStage.setTitle("My Tickets!");
                primaryStage.show();
            }
        }

        else
        {
            System.out.println("Ticket booking Cancelled.");
        }
    }

    public void onGoBack(ActionEvent event) throws IOException
    {
        DBUtils.changeScene(event, "LoggedIn.fxml", "Welcome!", username);
    }

    public void handleKeyReleased()
    {
        boolean disableButton = trainNumberField.getText().trim().isEmpty() || originField.getText().trim().isEmpty() || destinationField.getText().trim().isEmpty() || firstNameField.getText().trim().isEmpty() || lastNameField.getText().trim().isEmpty() || ageField.getText().trim().isEmpty() || genderField.getSelectionModel().isEmpty() || classField.getSelectionModel().isEmpty() || (dateField.getValue() == null); 
        submitButton.setDisable(disableButton); 
    }
}