package webapp;

import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static java.time.temporal.ChronoUnit.DAYS;

public class DbOp {

    public static boolean checkPasswordForUser(String userName, String enteredPassword) {
        try {
            String sql = "SELECT * from users WHERE name = ?";
            Connection connection = connect();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, userName);
            ResultSet resultSet = statement.executeQuery();
            String hashed = null;
            while (resultSet.next()) {
                hashed = resultSet.getString("password");
            }
            connection.close();
            return Utils.checkPassword(hashed, enteredPassword);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static boolean updatePassword(String hashedPassword, String userName, String resetBy){
        boolean complete = true;
        try {
            String sql = "UPDATE users SET password = ? WHERE name = ?";
            Connection connection = DbOp.connect();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, hashedPassword);
            statement.setString(2, userName);
            statement.executeUpdate();
            String recordResetSql = "INSERT INTO passwordResets(date,month,year,hour,minute,seconds,resetBy) " +
                    "VALUES(?,?,?,?,?,?,?)";
            LocalDateTime dateTime = LocalDateTime.now();
            PreparedStatement recordResetStatement = connection.prepareStatement(recordResetSql);
            recordResetStatement.setInt(1, dateTime.getDayOfMonth());
            recordResetStatement.setInt(2, dateTime.getMonthValue());
            recordResetStatement.setInt(3, dateTime.getYear());
            recordResetStatement.setInt(4, dateTime.getHour());
            recordResetStatement.setInt(5, dateTime.getMinute());
            recordResetStatement.setInt(6, dateTime.getSecond());
            recordResetStatement.setString(7, resetBy);
            recordResetStatement.executeUpdate();
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            complete = false;
        }
        return complete;
    }

    public static ArrayList<String[]> getChecks(int wardId) {
        ArrayList<String[]> checkEntries = new ArrayList<>();
        try {
            Connection connection = connect();
            String sql = "SELECT * from checks WHERE wardId = ? ORDER BY year DESC, month DESC, date DESC, time DESC";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, wardId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String date = resultSet.getInt("date") +
                        "/" + resultSet.getInt("month") + "/" +
                        resultSet.getInt("year");
                int time = resultSet.getInt("time");
                String pattern = "0000.#";
                DecimalFormat decimalFormat = new DecimalFormat(pattern);
                String timeStr = decimalFormat.format(time);
                String user1 = resultSet.getString("user1");
                String user2 = resultSet.getString("user2");
                String[] entry = {date,timeStr,user1,user2};
                checkEntries.add(entry);
            }
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return checkEntries;
    }

    public static String createUsername(String first, String surname) {
        String candidate = first.substring(0,1) + surname;
        candidate = candidate.toLowerCase();
        Connection connection = null;
        String NewCandidate = candidate;
        try {
            connection = connect();
            String sql = "SELECT * FROM users WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, candidate);
            ResultSet resultSet = statement.executeQuery();
            int appendedNumber = 0;
            while (resultSet.next()) {
                appendedNumber++;
                NewCandidate = candidate + String.valueOf(appendedNumber);
                statement = connection.prepareStatement(sql);
                statement.setString(1, NewCandidate);
                resultSet = statement.executeQuery();
            }
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return NewCandidate;
    }

    public static Connection connect() throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        Connection conn = null;
        try {
            String dbLocation = "jdbc:sqlite:src/main/webapp/db/db.db";
            conn = DriverManager.getConnection(dbLocation);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
}
