package webapp;

import javax.servlet.http.HttpServletRequest;
import java.sql.*;
import java.util.ArrayList;

public class Ward {

    private String name;
    private ArrayList<Drug> drugList;
    private int id;
    private ArrayList<Batch> batchList;
    private StringBuilder errorMessage;
    private boolean error;

    public Ward(String name) {
        this.name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
    }

    public Ward(String name, int id) {
        this.name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
        this.id = id;
    }

    public static Ward getWard(int wardId) {
        Ward ward = null;
        try {
            String sql = "SELECT * from wards WHERE id = ?";
            Connection connection = DbOp.connect();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, wardId);
            ResultSet resultSet = statement.executeQuery();
            String wardName = resultSet.getString("name");
            connection.close();
            ward = new Ward(wardName, wardId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ward;
    }

    public Ward(HttpServletRequest request) {
        this.errorMessage = new StringBuilder();
        this.error = false;
        this.name = request.getParameter("name");
        if (this.name.equals("")) {
            this.error = true;
            this.errorMessage.append("<li>Ward name cannot be empty</li>");
        }
        if (wardExists(this.name)) {
            this.error = true;
            this.errorMessage.append("<li>Ward already exists</li>");
        }
    }

    public static boolean wardExists(String name) {
        try {
            Connection connection = DbOp.connect();
            String getWardSql = "SELECT * from wards WHERE name = ?";
            PreparedStatement getWardStatement = connection.prepareStatement(getWardSql);
            getWardStatement.setString(1, name);
            ResultSet resultSet = getWardStatement.executeQuery();
            boolean wardExists = resultSet.next();
            connection.close();
            return  wardExists;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Drug> getDrugList() {
        return drugList;
    }

    public void setDrugList(ArrayList<Drug> drugList) {
        this.drugList = drugList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Batch> getBatchList() {
        return batchList;
    }

    public void setBatchList(ArrayList<Batch> batchList) {
        this.batchList = batchList;
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

    public void addWard() {
        if (!this.error) {
            try {
                Connection connection = DbOp.connect();
                String sql = "INSERT INTO wards(name) VALUES(?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, this.name);
                statement.executeUpdate();
                String getWardSql = "SELECT * from wards WHERE name = ?";
                PreparedStatement getWardStatement = connection.prepareStatement(getWardSql);
                getWardStatement.setString(1, this.name);
                ResultSet resultSet = getWardStatement.executeQuery();
                this.id = resultSet.getInt("id");
                connection.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static double getWardDrugQuantity(int wardId, int drugId) {
        String sql = "SELECT * from drugQuantities WHERE wardID = ? AND drugID = ?";
        double quantity = -1;
        try {
            Connection connection = DbOp.connect();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, wardId);
            statement.setInt(2, drugId);
            ResultSet resultSet = statement.executeQuery();
            quantity = resultSet.getDouble("quantity");
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return quantity;
    }

    public static void setWardDrugQuantity(int wardId, int drugId, double newQuantity) {
        String sql = "UPDATE drugQuantities SET quantity = ? WHERE wardID = ? and drugID = ?";
        try {
            Connection connection = DbOp.connect();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setDouble(1, newQuantity);
            statement.setInt(2, wardId);
            statement.setInt(3, drugId);
            statement.executeUpdate();
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void setWardDrugAlert(int wardId, int drugId, double alert) {
        String sql = "UPDATE drugQuantities SET alert = ? WHERE wardID = ? and drugID = ?";
        try {
            Connection connection = DbOp.connect();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setDouble(1, alert);
            statement.setInt(2, wardId);
            statement.setInt(3, drugId);
            statement.executeUpdate();
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static double getWardDrugAlert(int wardId, int drugId) {
        String sql = "SELECT * from drugQuantities WHERE wardID = ? AND drugID = ?";
        double alert = -1;
        try {
            Connection connection = DbOp.connect();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, wardId);
            statement.setInt(2, drugId);
            ResultSet resultSet = statement.executeQuery();
            alert = resultSet.getDouble("alert");
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return alert;
    }

    public static double getWardBatchQuantity(int wardId, int batchId) {
        String sql = "SELECT * from batchQuantities WHERE wardID = ? AND batchID = ?";
        double quantity = -1;
        try {
            Connection connection = DbOp.connect();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, wardId);
            statement.setInt(2, batchId);
            ResultSet resultSet = statement.executeQuery();
            quantity = resultSet.getDouble("quantity");
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return quantity;
    }

    public static void setWardBatchQuantity(int wardId, int batchId, double newQuantity) {
        String sql = "UPDATE batchQuantities SET quantity = ? WHERE wardID = ? and batchID = ?";
        try {
            Connection connection = DbOp.connect();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setDouble(1, newQuantity);
            statement.setInt(2, wardId);
            statement.setInt(3, batchId);
            statement.executeUpdate();
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static ArrayList<Ward> getWardList() {
        ArrayList<Ward> wardList = new ArrayList<>();
        String sql = "SELECT * from wards";
        try {
            Connection connection = DbOp.connect();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String wardName = resultSet.getString("name");
                int wardId = resultSet.getInt("id");
                Ward ward = new Ward(wardName, wardId);
                wardList.add(ward);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return wardList;
    }
}

