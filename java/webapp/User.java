package webapp;

import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class User {


    private String firstName;
    private String surname;
    private String userName;
    private String password1;
    private String password2;
    private String hashedPassword;
    private String level;
    private StringBuilder errorMessage;
    private boolean error;
    private LocalDate createdOn;
    private String createdBy;

    public User(String firstName, String surname, String userName, String level, LocalDate createdOn, String createdBy) {
        this.firstName = firstName;
        this.surname = surname;
        this.userName = userName;
        this.level = level;
        this.createdOn = createdOn;
        this.createdBy = createdBy;
    }

    public User(HttpServletRequest request) {
        this.errorMessage = new StringBuilder();
        this.createdOn = LocalDate.now();
        this.firstName = request.getParameter("first");
        if (this.firstName.equals("")) {
            this.errorMessage.append("<li>First name cannot be empty</li>");
            this.error = true;
        }
        this.surname = request.getParameter("surname");
        if (surname.equals("")) {
            this.errorMessage.append("<li>Surname cannot be empty</li>");
            this.error = true;
        }
        this.password1 = request.getParameter("pass1");
        this.password2 = request.getParameter("pass2");
        if (!this.password1.equals(this.password2)) {
            this.errorMessage.append("<li>Passwords don't match</li>");
            this.error = true;
        }
        if (this.password1.length() < 8) {
            this.errorMessage.append("<li>Passwords is too short, must be 8 characters or more</li>");
            this.error = true;
        }
        if (!this.error) {
            this.userName = DbOp.createUsername(this.firstName, this.surname);
            this.hashedPassword = BCrypt.hashpw(this.password1, BCrypt.gensalt(12));
        }
        this.level = request.getParameter("level");
        HttpSession session = request.getSession();
        String currentUserLevel = session.getAttribute("userLevel").toString();
        if (!Authorisation.checkAuthority("create" + this.level, currentUserLevel)) {
            this.errorMessage.append("<li>You are not authorised to create users at the selected level</li>");
            this.error = true;
        }
        this.createdBy = session.getAttribute("user").toString();
    }

    public void addUser() {
        if (!this.error) {
            try {
                Connection conn = DbOp.connect();
                String sql = "INSERT INTO users(name,password,level,first,surname,date,month,year,createdBy) " +
                        "VALUES(?,?,?,?,?,?,?,?,?)";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, this.userName);
                statement.setString(2, this.hashedPassword);
                statement.setString(3, this.level);
                statement.setString(4, this.firstName.toUpperCase());
                statement.setString(5, this.surname.toUpperCase());
                statement.setInt(6, this.createdOn.getDayOfMonth());
                statement.setInt(7, this.createdOn.getMonthValue());
                statement.setInt(8, this.createdOn.getYear());
                statement.setString(9, this.createdBy);
                statement.executeUpdate();
                conn.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public StringBuilder getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(StringBuilder errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public static User getUser(String userName) {
        User user = null;
        try {
            String sql = "SELECT * from users WHERE name = ?";
            Connection connection = DbOp.connect();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, userName);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String firstName = correctNameCase(resultSet.getString("first"));
                String surname = correctNameCase(resultSet.getString("surname"));
                String level = resultSet.getString("level");
                int date = resultSet.getInt("date");
                int month = resultSet.getInt("month");
                int year = resultSet.getInt("year");
                LocalDate createdOn = LocalDate.of(year, month, date);
                String createdBy = resultSet.getString("createdBy");
                user = new User(firstName, surname, userName, level, createdOn, createdBy);
            }
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return user;
    }

    public static boolean userExists(String userName) {
        User user = getUser(userName);
        if (user == null) {
            return false;
        }
        return true;
    }

    public static ArrayList<User> getUserList() {
        ArrayList<User> userList = new ArrayList<>();
        try {
            String sql = "SELECT * from users";
            Connection connection = DbOp.connect();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String username = resultSet.getString("name");
                String firstName = correctNameCase(resultSet.getString("first"));
                String surname = correctNameCase(resultSet.getString("surname"));
                String level = resultSet.getString("level");
                int date = resultSet.getInt("date");
                int month = resultSet.getInt("month");
                int year = resultSet.getInt("year");
                LocalDate createdOn = LocalDate.of(year, month, date);
                String createdBy = resultSet.getString("createdBy");
                User user = new User(firstName, surname, username, level, createdOn, createdBy);
                userList.add(user);
            }
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return userList;
    }

    public static ArrayList<User> getUsersCreatedBy(String createdBy) {
        ArrayList<User> userList = new ArrayList<>();
        try {
            String sql = "SELECT * from users WHERE createdBy = ?";
            Connection connection = DbOp.connect();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, createdBy);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String firstName = correctNameCase(resultSet.getString("first"));
                String surname = correctNameCase(resultSet.getString("surname"));
                String level = resultSet.getString("level");
                int date = resultSet.getInt("date");
                int month = resultSet.getInt("month");
                int year = resultSet.getInt("year");
                LocalDate createdOn = LocalDate.of(year, month, date);
                String userName = resultSet.getString("name");
                User user = new User(firstName, surname, userName, level, createdOn, createdBy);
                userList.add(user);
            }
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return userList;
    }

    public static ArrayList<User> getUsersCalled(String first, String surname) {
        ArrayList<User> userList = new ArrayList<>();
        try {
            String sql = "SELECT * from users WHERE first = ? AND surname = ? ORDER BY surname ASC, first ASC";
            Connection connection = DbOp.connect();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, first.toUpperCase());
            statement.setString(2, surname.toUpperCase());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String foundFirstName = correctNameCase(resultSet.getString("first"));
                String foundSurname = correctNameCase(resultSet.getString("surname"));
                String level = resultSet.getString("level");
                String createdBy = resultSet.getString("createdBy");
                int date = resultSet.getInt("date");
                int month = resultSet.getInt("month");
                int year = resultSet.getInt("year");
                LocalDate createdOn = LocalDate.of(year, month, date);
                String userName = resultSet.getString("name");
                User user = new User(foundFirstName, foundSurname, userName, level, createdOn, createdBy);
                userList.add(user);
            }
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return userList;
    }

    public static String correctNameCase(String name) {
        String corrected = name.substring(0,1) + name.substring(1).toLowerCase();
        return corrected;
    }
}
