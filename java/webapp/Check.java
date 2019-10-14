package webapp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Check {

    private int id;
    private LocalDateTime dateTime;
    private String currentUser;
    private String checkingUser;
    private int wardId;
    private boolean error;
    private StringBuilder errorMessage;

    public Check(int id, LocalDateTime dateTime, String currentUser, String checkingUser, int wardId) {
        this.id = id;
        this.dateTime = dateTime;
        this.currentUser = currentUser;
        this.checkingUser = checkingUser;
        this.wardId = wardId;
    }

    public Check(HttpServletRequest request) {
        this.error = false;
        this.errorMessage = new StringBuilder();
        if (Utils.sameUser(request)) {
            this.errorMessage.append("<li>Checking user cannot be the same as logged in user</li>");
            this.error = true;
        }
        if (!Utils.checkingUser(request)) {
            this.errorMessage.append("<li>Incorrect checking user details</li>");
            this.error = true;
        }
        String checkedAndCorrect = request.getParameter("checkedandcorrect");
        if (checkedAndCorrect == null) {
            this.errorMessage.append("<li>You need to check the box to confirm that all " +
                    "quantities are correct</li>");
            this.error = true;
        }
        HttpSession session = request.getSession();
        this.currentUser = session.getAttribute("user").toString();
        String loggedInUserLevel = session.getAttribute("userLevel").toString();
        this.checkingUser = request.getParameter("checkingUser");
        User checkingUser = User.getUser(this.checkingUser);
        if (!Authorisation.checkAuthority("performCheck", checkingUser.getLevel())) {
            this.error = true;
            this.errorMessage.append("<li>Checking user does not have authority to perform checks");
        }
        if (!Authorisation.checkAuthority("performCheck", loggedInUserLevel)) {
            this.error = true;
            this.errorMessage.append("<li>Logged in user does not have authority to perform checks");
        }
        this.dateTime = LocalDateTime.now();
        this.wardId = Integer.parseInt(request.getParameter("wardId"));
    }

    public void addCheck() {
        try {
            Connection connection = DbOp.connect();
            String newCheckSql = "INSERT INTO checks(date,month,year,hour,minute,seconds,user1,user2,wardId) " +
                    "VALUES(?,?,?,?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(newCheckSql);
            statement.setInt(1, this.dateTime.getDayOfMonth());
            statement.setInt(2, this.dateTime.getMonthValue());
            statement.setInt(3, this.dateTime.getYear());
            statement.setInt(4, this.dateTime.getHour());
            statement.setInt(5, this.dateTime.getMinute());
            statement.setInt(6, this.dateTime.getSecond());
            statement.setString(7, this.currentUser);
            statement.setString(8, this.checkingUser);
            statement.setInt(9, this.wardId);
            statement.executeUpdate();
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static ArrayList<Check> getChecks(int wardId) {
        ArrayList<Check> checkEntries = new ArrayList<>();
        try {
            Connection connection = DbOp.connect();
            String sql = "SELECT * from checks WHERE wardId = ? ORDER BY year DESC, month DESC, date DESC, hour DESC, " +
                    "minute DESC, seconds DESC";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, wardId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int date = resultSet.getInt("date");
                int month = resultSet.getInt("month");
                int year = resultSet.getInt("year");
                int hour = resultSet.getInt("hour");
                int minute = resultSet.getInt("minute");
                int seconds = resultSet.getInt("seconds");
                LocalDateTime dateTime = LocalDateTime.of(year,month,date,hour,minute,seconds);
                String currentUser = resultSet.getString("user1");
                String checkingUser = resultSet.getString("user2");
                Check check = new Check(id,dateTime,currentUser,checkingUser,wardId);
                checkEntries.add(check);
            }
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return checkEntries;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public String getCheckingUser() {
        return checkingUser;
    }

    public void setCheckingUser(String checkingUser) {
        this.checkingUser = checkingUser;
    }

    public int getWardId() {
        return wardId;
    }

    public void setWardId(int wardId) {
        this.wardId = wardId;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public StringBuilder getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(StringBuilder errorMessage) {
        this.errorMessage = errorMessage;
    }
}
