package webapp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

public class Drug {
    private int id;

    private String name;
    private String strength;
    private String units;
    private String createdBy;
    private StringBuilder errorMessage;
    private boolean error;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Drug(String name, String strength, String units, String createdBy, int id) {
        this.name = name;
        this.strength = strength;
        this.units = units;
        this.createdBy = createdBy;
        this.id = id;
    }

    public Drug(HttpServletRequest request) {
        this.errorMessage = new StringBuilder();
        this.error = false;
        String tempName = request.getParameter("name");
        if (tempName.length() < 3) {
            this.errorMessage.append("<li>Drug name must be 3 letters or more</li>");
            this.error = true;
        } else {
            this.name = tempName.substring(0,1).toUpperCase() + tempName.substring(1);
        }
        this.strength = request.getParameter("strength");
        if (this.strength.equals("")) {
            this.errorMessage.append("<li>Drug strength cannot be empty</li>");
            this.error = true;
        }
        this.units = request.getParameter("units");
        HttpSession session  = request.getSession();
        this.createdBy = session.getAttribute("user").toString();
        boolean drugExists = drugExists(this.name, this.strength, this.units);
        if (drugExists) {
            this.errorMessage.append("<li>Drug already exists</li>");
            this.error = true;
        }
        if (!Authorisation.checkAuthority("createDrug", session.getAttribute("userLevel").toString())) {
            this.error = true;
            this.errorMessage.append("<li>You do not have authority to create new drug profiles");
        }
    }

    public void addDrug() {
        try {
            Connection connection = DbOp.connect();
            String sql = "INSERT INTO drugs(name,strength,units,createdby) VALUES(?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, this.name);
            statement.setString(2, this.strength);
            statement.setString(3, this.units);
            statement.setString(4, this.createdBy);
            statement.executeUpdate();
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String toString() {
        StringBuilder drugStr = new StringBuilder();
        drugStr.append(this.getName());
        drugStr.append(" | ");
        drugStr.append(this.getStrength());
        drugStr.append(" | ");
        drugStr.append(this.getUnits());
        return drugStr.toString();
    }

    public static ArrayList<Drug> getDrugs() throws ClassNotFoundException, SQLException {
        ArrayList<Drug> drugList = new ArrayList<>();
        Connection connection = DbOp.connect();
        String sql = "SELECT * from drugs ORDER BY name";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            Drug drug = new Drug(resultSet.getString("name"),
                    resultSet.getString("strength"),
                    resultSet.getString("units"),
                    resultSet.getString("createdby"), id);
            drugList.add(drug);
        }
        connection.close();
        return drugList;
    }

    public static ArrayList<Drug> getWardDrugs(int wardId) {
        ArrayList<Drug> wardDrugs = new ArrayList<>();
        try {
            Connection connection = DbOp.connect();
            String sql = "SELECT * from drugQuantities WHERE wardId = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, wardId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int foundDrugId = resultSet.getInt("drugId");
                Drug drug = getDrug(foundDrugId);
                wardDrugs.add(drug);
            }
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        wardDrugs.sort(new Comparator<Drug>() {
            @Override
            public int compare(Drug o1, Drug o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return wardDrugs;
    }

    public static ArrayList<Drug> getLowStockDrugs(int wardId) {
        ArrayList<Drug> wardDrugs = new ArrayList<>();
        try {
            Connection connection = DbOp.connect();
            String sql = "SELECT * from drugQuantities WHERE wardId = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, wardId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int foundDrugId = resultSet.getInt("drugId");
                Drug drug = getDrug(foundDrugId);
                double quantity = resultSet.getDouble("quantity");
                double alert = resultSet.getDouble("alert");
                if (quantity <= alert) {
                    wardDrugs.add(drug);
                }
            }
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return wardDrugs;
    }

    public static Drug getDrug(int drugId)  {
        Drug drug = null;
        try {
        Connection connection = DbOp.connect();
        String sql = "SELECT * from drugs WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, drugId);
        ResultSet resultSet = statement.executeQuery();
        drug = new Drug(resultSet.getString("name"),
                resultSet.getString("strength"),
                resultSet.getString("units"),
                resultSet.getString("createdby"), drugId);
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return drug;
    }

    public static boolean drugExists(String name, String strength, String units) {
        Drug drug = getDrug(name, strength, units);
        if (drug == null) {
            return false;
        }
        return true;
    }

    public static Drug getDrug(String name, String strength, String units) {
        Drug drug = null;
        try {
            String sql = "SELECT * from drugs WHERE name = ? AND strength = ? AND units = ?";
            Connection connection = DbOp.connect();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, strength);
            statement.setString(3, units);
            ResultSet resultSet = statement.executeQuery();
            int id = resultSet.getInt("id");
            String createdBy = resultSet.getString("createdby");
            connection.close();
            drug = new Drug(name,strength,units,createdBy,id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return drug;
    }
}
