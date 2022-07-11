import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import javafx.scene.Node;

public class LoggedInController {

    @FXML
    private Label topWelcomeLabel;

    @FXML
    private Button myTicketsButton;

    @FXML
    private Button newTicketButton;

    @FXML
    private Button PNREnquiryButton;

    @FXML
    private Button logOutButton;

    private String username;
    private Stage primaryStage;
    private Scene scene;
    private Parent root;

    public void initialize(String username)
    {
        topWelcomeLabel.setText("Hi " + username + "!");
        this.username = username;
    }

    public void switchToNewTicket(ActionEvent event) throws IOException, SQLException
    {
        String name = username;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("NewTicket.fxml"));
        root = loader.load();

        NewTicketController newTicketController = loader.getController();
        newTicketController.initialize(name);
        
        primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1280, 720);
        JMetro jMetro = new JMetro();
        jMetro.setScene(scene);
        scene.getStylesheets().add("newTicketStyle.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle("New Ticket!");
        primaryStage.show();
    }

    public void switchToPNREnquiry(ActionEvent event) throws IOException
    {
        String name = username;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PNREnquiry.fxml"));
        root = loader.load();

        PNREnquiryController PNREnquiryController = loader.getController();
        PNREnquiryController.initialize(name);
        
        primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1280, 720);
        JMetro jMetro = new JMetro();
        jMetro.setScene(scene);
        scene.getStylesheets().add("styling.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle("PNR Enquiry!");
        primaryStage.show();
    }

    public void switchToMyTickets(ActionEvent event) throws IOException, SQLException
    {
        String name = username;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MyTickets.fxml"));
        root = loader.load();

        MyTicketsController myTicketsController = loader.getController();
        myTicketsController.initialize(name, false);
        
        primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1280, 720);
        JMetro jMetro = new JMetro();
        jMetro.setScene(scene);
        scene.getStylesheets().add("styling.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle("My Tickets!");
        primaryStage.show();
    }

    public void onLogOut(ActionEvent event) throws IOException
    {
        DBUtils.changeScene(event, "LogIn.fxml", "Log In!", null);
    }
}
