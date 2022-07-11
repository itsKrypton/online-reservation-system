import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;

public class DBUtils {
    
    public static void changeScene(ActionEvent event, String fxmlFile, String title, String username) throws IOException
    {
        Parent root = null;

        if(username != null)
        {
            FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
            root = loader.load();
            LoggedInController loggedInController = loader.getController();
            loggedInController.initialize(username);
        }

        else
        root = FXMLLoader.load(DBUtils.class.getResource(fxmlFile));

        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        Scene scene = new Scene(root, 1280, 720);
        JMetro jMetro = new JMetro();
        jMetro.setScene(scene);
        scene.getStylesheets().add("styling.css");
        stage.setScene(scene);
        stage.show();
    }

    public static void signUpUser(ActionEvent event, String username, String password, String email, String fName, String lName) throws SQLException, IOException, ClassNotFoundException
    {
        Connection conn = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet rs = null;

        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ors", "root", "password");
        psCheckUserExists = conn.prepareStatement("SELECT * FROM userdetails WHERE username = ?");
        psCheckUserExists.setString(1, username);
        rs = psCheckUserExists.executeQuery();

        if(rs.isBeforeFirst())
        {
            System.out.println("User already exists!");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Username is already used.");
            alert.show();
        }

        else
        {
            psInsert = conn.prepareStatement("INSERT INTO userdetails (username, password, first_name, last_name, email_id) VALUES(?, ?, ?, ?, ?)");
            psInsert.setString(1, username);
            psInsert.setString(2, password);
            psInsert.setString(3, fName);
            psInsert.setString(4, lName);
            psInsert.setString(5, email);
            psInsert.executeUpdate();

            changeScene(event, "LoggedIn.fxml", "Welcome!", username);
        }

        rs.close();
        psCheckUserExists.close();
        psInsert.close();
        conn.close();
    }

    public static void logInUser(ActionEvent event, String username, String password) throws SQLException, IOException, ClassNotFoundException
    {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ors", "root", "password");
        ps = conn.prepareStatement("SELECT password FROM userdetails WHERE username = ?");
        ps.setString(1, username);
        rs = ps.executeQuery();

        if(!rs.isBeforeFirst())
        {
            System.out.println("User not found!");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Wrong credentials!");
            alert.show();
        }

        else
        {
            while(rs.next())
            {
                String retrievedPassword = rs.getString("password");
                if(retrievedPassword.equals(password))
                {
                    changeScene(event, "LoggedIn.fxml", "Welcome!", username);
                }

                else
                {
                    System.out.println("Password does not match");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Wrong credentials!");
                    alert.show();
                }
            }
        }

        rs.close();
        ps.close();
        conn.close();
    }

    public static String handleTrainName(String trainNumber) throws SQLException
    {
        int trainNo = Integer.parseInt(trainNumber);
        Connection conn = null;
        PreparedStatement psTrainNumberExists = null;
        ResultSet rs = null;

        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ors", "root", "password");
        psTrainNumberExists = conn.prepareStatement("SELECT train_name FROM train_info WHERE train_number = ?");
        psTrainNumberExists.setInt(1, trainNo);
        rs = psTrainNumberExists.executeQuery();

        if(!rs.isBeforeFirst())
        {
            System.out.println("Wrong Train Number");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please enter a Valid Train Number!");
            alert.show();
        }

        else
        {
            while(rs.next())
            {
                String receivedTrainName = rs.getString("train_name");
                return receivedTrainName;
            }
        }

        psTrainNumberExists.close();
        rs.close();
        conn.close();
        return "";
    }

    public static void storeNewTicket(int PNR, String trainNumber, String seatNumber, String classType, String origin, String destination, Date date, String username, String firstName, String lastName, int age, String gender) throws SQLException
    {
        Connection conn = null;
        PreparedStatement ps = null;

        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ors", "root", "password");
        ps = conn.prepareStatement("INSERT INTO reservation_details (pnr_number, train_number, seat_number, class, source, destination, date, username, first_name, last_name, Age, Gender) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        ps.setInt(1, PNR);
        ps.setString(2, trainNumber);
        ps.setString(3, seatNumber);
        ps.setString(4, classType);
        ps.setString(5, origin);
        ps.setString(6, destination);
        ps.setDate(7, date);
        ps.setString(8, username);
        ps.setString(9, firstName);
        ps.setString(10, lastName);
        ps.setInt(11, age);
        ps.setString(12, gender);
        ps.executeUpdate();

        ps.close();
        conn.close();
    }

    public static Hashtable<String, String> getPNRDetails(String PNR) throws SQLException
    {
        Hashtable<String, String> pnrInfo = new Hashtable<>();
        int pnrNumber = Integer.parseInt(PNR);

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ors", "root", "password");
        ps = conn.prepareStatement("SELECT * FROM reservation_details WHERE pnr_number = ?");
        ps.setInt(1, pnrNumber);
        rs = ps.executeQuery();

        if(!rs.isBeforeFirst())
        System.out.println("Wrong PNR Number");

        else
        {
            while(rs.next())
            {
                String trainNumber = rs.getString("train_number");
                pnrInfo.put("trainNumber", trainNumber);
                pnrInfo.put("trainName", handleTrainName(trainNumber));
                String classSeat = rs.getString("class") + "/" + rs.getString("seat_number");
                pnrInfo.put("classSeat", classSeat);
                pnrInfo.put("source", rs.getString("source"));
                pnrInfo.put("destination", rs.getString("destination"));
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
                pnrInfo.put("date", formatter.format(rs.getDate("date").toLocalDate()));
                pnrInfo.put("firstName", rs.getString("first_name"));
                pnrInfo.put("lastName", rs.getString("last_name"));
                String ageGender = Integer.toString(rs.getInt("age")) + "/" + rs.getString("gender");
                pnrInfo.put("ageGender", ageGender);
                return pnrInfo;
            }
        }
        ps.close();
        rs.close();
        conn.close();;
        return pnrInfo;
    }

    public static void saveAutoCompletionDetails(String username, String firstName, String lastName, int age, String gender) throws SQLException
    {
        Connection conn = null;
        PreparedStatement psDetailsAlreadyExists = null;
        ResultSet rs = null;

        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ors", "root", "password");
        psDetailsAlreadyExists = conn.prepareStatement("SELECT * FROM auto_complete_details WHERE user_id = ?");
        psDetailsAlreadyExists.setString(1, username);
        rs = psDetailsAlreadyExists.executeQuery();

        if(!rs.isBeforeFirst())
        {
            storeAutoCompletionDetails(username, firstName, lastName, age, gender);
        }

        else if(rs.isBeforeFirst())
        {
            while(rs.next())
            {
                if((!firstName.equalsIgnoreCase(rs.getString("first_name"))) || (!lastName.equalsIgnoreCase(rs.getString("last_name"))) || (age != rs.getInt("age")) || (!gender.equalsIgnoreCase(rs.getString("gender"))))
                storeAutoCompletionDetails(username, firstName, lastName, age, gender);
                break;
            }
        }

        psDetailsAlreadyExists.close();
        rs.close();
        conn.close();
    }

    public static void storeAutoCompletionDetails(String username, String firstName, String lastName, int age, String gender) throws SQLException
    {
        Connection conn = null;
        PreparedStatement ps = null;

        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ors", "root", "password");
        ps = conn.prepareStatement("INSERT INTO auto_complete_details (user_id, first_name, last_name, age, gender) VALUES (?, ?, ?, ?, ?)");
        ps.setString(1, username);
        ps.setString(2, firstName);
        ps.setString(3, lastName);
        ps.setInt(4, age);
        ps.setString(5, gender);
        ps.executeUpdate();

        ps.close();
        conn.close();
    }

    public static List<List<String>> getAutoCompletetionDetails(String username) throws SQLException
    {
        List<List<String>> autoCompletionDetails = new ArrayList<>();
        List<String> details;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ors", "root", "password");
        ps = conn.prepareStatement("SELECT * FROM auto_complete_details WHERE user_id = ?");
        ps.setString(1, username);
        rs = ps.executeQuery();

        if(!rs.isBeforeFirst())
        {
            System.out.println("No autocomplete details");
        }

        else
        {
            while(rs.next())
            {   
                details = new ArrayList<>();
                details.add(rs.getString("first_name"));
                details.add(rs.getString("last_name"));
                details.add(Integer.toString(rs.getInt("age")));
                details.add(rs.getString("gender"));
                autoCompletionDetails.add(details);
            }
            System.out.println("Autocomplete details loaded");
        }
        ps.close();
        rs.close();
        conn.close();
        return autoCompletionDetails;
    }

    public static ObservableList<Integer> getPNRList(String username) throws SQLException
    {
        ObservableList<Integer> PNRlist = FXCollections.observableArrayList();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ors", "root", "password");
        ps = conn.prepareStatement("SELECT pnr_number FROM reservation_details WHERE username = ?");
        ps.setString(1, username);
        rs = ps.executeQuery();

        if(!rs.isBeforeFirst())
        {
            System.out.println("No bookings");
        }

        else
        {
            while(rs.next())
            {
                PNRlist.add(rs.getInt("pnr_number"));
            }
        }
        ps.close();
        rs.close();
        conn.close();
        return PNRlist;
    }

    public static void cancelTicket(int PNRnumber) throws SQLException
    {
        Connection conn = null;
        PreparedStatement ps = null;

        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ors", "root", "password");
        ps = conn.prepareStatement("DELETE FROM reservation_details WHERE pnr_number = ?");
        ps.setInt(1, PNRnumber);
        ps.executeUpdate();

        ps.close();
        conn.close();
    }
}
