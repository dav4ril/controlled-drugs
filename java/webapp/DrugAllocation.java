package webapp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class DrugAllocation {

    private int wardId;
    private int id;
    private int drugId;
    private StringBuilder errorMessage;
    private double quantity;
    private boolean error;
    private double alert;

    public DrugAllocation(int wardId, int drugId, double quantity, double alert) {
        this.wardId = wardId;
        this.drugId = drugId;
        this.quantity = quantity;
        this.alert = alert;
    }

    public DrugAllocation(HttpServletRequest request) {
        this.errorMessage = new StringBuilder();
        this.error = false;
        this.wardId = Integer.parseInt(request.getParameter("wardId"));
        this.drugId = Integer.parseInt(request.getParameter("drug"));
        if (allocationExists(this.drugId, this.wardId)) {
            this.error = true;
            this.errorMessage.append("<li>Drug is already allocated to your area</li>");
        }
        this.quantity = 0;
        this.alert = 0;
        HttpSession session = request.getSession();
        if (!Authorisation.checkAuthority("allocateDrug", session.getAttribute("userLevel").toString())) {
            this.error = true;
            this.errorMessage.append("<li>You do not have authority to allocate drugs to areas");
        }
        if (!this.error) {
            try {
                String sql = "INSERT INTO drugQuantities(wardId,drugId,quantity,alert) VALUES(?,?,?,?)";
                Connection connection = DbOp.connect();
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, this.wardId);
                statement.setInt(2, this.drugId);
                statement.setDouble(3, this.quantity);
                statement.setDouble(4, this.alert);
                statement.executeUpdate();
                connection.close();
            } catch (Exception e) {
                this.error = true;
                this.errorMessage.append("<li>An unknown error occured</li>");
                System.out.println(e.getMessage());
            }
        }
    }

    public int getWardId() {
        return wardId;
    }

    public void setWardId(int wardId) {
        this.wardId = wardId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDrugId() {
        return drugId;
    }

    public void setDrugId(int drugId) {
        this.drugId = drugId;
    }

    public StringBuilder getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(StringBuilder errorMessage) {
        this.errorMessage = errorMessage;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public double getAlert() {
        return alert;
    }

    public void setAlert(double alert) {
        this.alert = alert;
    }

    public static boolean allocationExists(int drugId, int wardId) {
        double quantity = Ward.getWardDrugQuantity(wardId, drugId);
        if (quantity == -1) {
            return false;
        }
        return true;
    }
}
