package webapp;

import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.DAYS;

public class Utils {

    public static String getDaysLeft(LocalDate date){
        LocalDate now = LocalDate.now();
        long daysLeft = now.until(date, DAYS);
        if (daysLeft < 0) {
            return " EXPIRED!";
        }
        return "(" + daysLeft + " days)";
    }

    public static int[] getNiceDateTime() {
        LocalDateTime dateTime = LocalDateTime.now();
        int date = dateTime.getDayOfMonth();
        int month = dateTime.getMonthValue();
        int year = dateTime.getYear();
        int hour = dateTime.getHour();
        int minute = dateTime.getMinute();
        return new int[] {date, month, year, hour, minute};
    }

    public static boolean createUserLevelCheck(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String userLevel = session.getAttribute("userLevel").toString();
        return
                userLevel.equals("nurse") || userLevel.equals("pharmacist") || userLevel.equals("admin") ||
                userLevel.equals("tech") || userLevel.equals("odp");
    }

    public static boolean dateInFuture(int month, int year) {
        int[] now = getNiceDateTime();
        boolean monthOkay = (now[1] < month);
        boolean yearOkay = (now[2] <= year);
        if (monthOkay && yearOkay) {
            return true;
        }
        return false;
    }

    public static boolean intChecker(String inputString) {
        try {
            Integer.parseInt(inputString);
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    public static boolean doubleChecker(String inputString) {
        try {
            Double.parseDouble(inputString);
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    public static boolean loginCheck(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object user = session.getAttribute("user");
        return user != null;
    }

    public static boolean nurseLevelCheck(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String userLevel = session.getAttribute("userLevel").toString();
        return userLevel.equals("nurse") ||
                userLevel.equals("pharmacist") ||
                userLevel.equals("admin") ||
                userLevel.equals("odp") ||
                userLevel.equals("tech");
    }

    public static boolean sameUser(HttpServletRequest request) {
        String checkingUserName = request.getParameter("checkingUser");
        HttpSession session = request.getSession();
        String currentUser = session.getAttribute("user").toString();
        return checkingUserName.equals(currentUser);
    }

    public static boolean checkingUser(HttpServletRequest request) {
        String checkingUserName = request.getParameter("checkingUser");
        String checkingUserPass = request.getParameter("checkPass");
        try {
            Connection connection = DbOp.connect();
            String sql = "SELECT * from users WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, checkingUserName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String hashed = resultSet.getString("password");
                connection.close();
                return Utils.checkPassword(hashed, checkingUserPass);
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean checkPassword(String hashed, String candidate){
        if (hashed.equals("test")){
            return candidate.equals("test");
        }
        return BCrypt.checkpw(candidate, hashed);
    }

}
