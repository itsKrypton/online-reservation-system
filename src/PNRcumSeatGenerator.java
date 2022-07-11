import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PNRcumSeatGenerator {

    public static int generatePNR() throws SQLException
    {
        Connection conn;
        PreparedStatement psPNRExists = null;
        ResultSet rs = null;

        int generatedPNR = (int)Math.floor(Math.random()*(99999999-11111111+1)+11111111);

        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ors", "root", "password");
        psPNRExists = conn.prepareStatement("SELECT * FROM reservation_details WHERE pnr_number = ?");
        psPNRExists.setInt(1, generatedPNR);
        rs = psPNRExists.executeQuery();

        if(rs.isBeforeFirst())
        {
            psPNRExists.close();
            rs.close();
            conn.close();
            return generatePNR();
        }

        else
        {
            psPNRExists.close();
            rs.close();
            conn.close();
            return generatedPNR;
        }
    }

    public static String generateSeat(String classType, int trainNumber, Date date) throws SQLException
    {
        Connection conn;
        PreparedStatement psSeatExists = null;
        ResultSet rs = null;

        int generatedSeat = (int)Math.floor(Math.random()*(99-1+1)+1);
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ors", "root", "password");
        psSeatExists = conn.prepareStatement("SELECT * FROM reservation_details WHERE seat_number = ? AND train_number = ? AND date = ?");
        String seat = getCoach(classType) + generatedSeat;
        psSeatExists.setString(1, seat);
        psSeatExists.setInt(2, trainNumber);
        psSeatExists.setDate(3, date);
        rs = psSeatExists.executeQuery();

        if(rs.isBeforeFirst())
        {
            psSeatExists.close();
            rs.close();
            conn.close();
            return generateSeat(classType, trainNumber, date);
        }

        else
        {
            psSeatExists.close();
            rs.close();
            conn.close();
            return seat;
        }
    }

    public static String getCoach(String classType)
    {
        if(classType.equals("1A"))
        return "A";

        else if(classType.equals("2A"))
        return "B";

        else if(classType.equals("CC"))
        return "C";

        else if(classType.equals("2S"))
        return "D";

        else 
        return "S";
    }
}
