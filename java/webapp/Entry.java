package webapp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Entry {
    public int getBatchId() {
        return batchId;
    }

    public boolean isBatchExists() {
        return batchExists;
    }

    public void setBatchExists(boolean batchExists) {
        this.batchExists = batchExists;
    }

    private int id;
    private int wardId;
    private LocalDateTime dateTime;
    private String patient;
    private String givenBy;
    private String checkedBy;
    private double quantityChange;
    private String type;
    private StringBuilder comments;
    private double newQuantity;
    private boolean error;
    private StringBuilder errorMessage;
    private int batchId;
    private int drugId;
    private boolean batchExists;
    private StringBuilder successMessage;
    private HttpServletRequest request;
    private double newBatchQuantity;

    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }

    public StringBuilder getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(StringBuilder successMessage) {
        this.successMessage = successMessage;
    }

    private void validatePatientName() {
        this.patient = request.getParameter("patient");
        if (this.patient.equals("")) {
            this.errorMessage.append("<li>Please enter the patient's name</li>");
            this.error = true;
        }
        if (!Pattern.matches("[a-zA-Z ]+", this.patient)) {
            errorMessage.append("<li>Patient name must be letters only</li>");
            error = true;
        }
    }

    private void validateQuantityTaken() {
        String takenStr = request.getParameter("taken");
        if (Utils.doubleChecker(takenStr)) {
            double initQuantity = Double.parseDouble(takenStr);
            if (initQuantity < 0) {
                this.errorMessage.append("<li>Quantity taken must not be negative</li>");
                this.error = true;
            }
            this.quantityChange = initQuantity * -1;
            double currentQuantity = Ward.getWardDrugQuantity(this.wardId, this.drugId);
            this.newQuantity = currentQuantity + this.quantityChange;
            if (this.newQuantity < 0) {
                this.errorMessage.append("<li>Insufficient quantity in stock. If sufficient quantity exists, " +
                        "please record an error to correct quantity and try again.</li>");
                this.error = true;
            }
        } else {
            this.errorMessage.append("<li>Quantity taken must be a number</li>");
            this.error = true;
        }
        if (takenStr.equals("")) {
            this.errorMessage.append("<li>Quantity taken cannot be empty</li>");
            this.error = true;
        }
    }

    private void validateTakeBatch() {
        String selectedBatchStr = request.getParameter("takeBatchSelect");
        if (selectedBatchStr.equals("notlisted")) {
            this.batchExists = false;
            if (this.type.equals("return")) {
                this.batchId = -1;
            } else {
                this.comments.append("Batch did not exist on record, this is probably because " +
                        "it was not entered correctly when added to stock");
                addBatch();
                this.newBatchQuantity = 0;
                this.successMessage.append(" with batch error, see entry comment for details");
            }
        } else {
            this.batchId = Integer.parseInt(selectedBatchStr);
            double batchQuantity = Ward.getWardBatchQuantity(this.wardId, this.batchId);
            this.newBatchQuantity = batchQuantity + this.quantityChange;
            System.out.println("new batch quant: " + this.newBatchQuantity);
            if (this.newBatchQuantity < 0) {
                this.error = true;
                this.errorMessage.append("<li>Insufficient quantity in selected batch. Check batch " +
                        "quantities below.</li>");
            }
        }
    }

    private void validateAddBatch() {
        String selectedBatchStr = request.getParameter("addBatchSelect");
        if (selectedBatchStr.equals("newbatch")) {
            this.batchExists = false;
            this.comments.append("New batch added");
            addBatch();
            this.newBatchQuantity = this.quantityChange;
        } else {
            this.batchId = Integer.parseInt(selectedBatchStr);
        }
    }

    private void validateQuantityAdded() {
        String addedStr = request.getParameter("added");
        if (Utils.doubleChecker(addedStr)) {
            this.quantityChange = Double.parseDouble(addedStr);
            if (this.quantityChange < 0) {
                this.errorMessage.append("<li>Quantity added cannot be negative</li>");
                this.error = true;
            } else {
                double currentQuantity = Ward.getWardDrugQuantity(wardId, drugId);
                this.newQuantity = currentQuantity + this.quantityChange;
            }
        } else {
            this.errorMessage.append("<li>Quantity added must be a number</li>");
            this.error = true;
        }
        if (addedStr.equals("")) {
            this.errorMessage.append("<li>Quantity added cannot be empty</li>");
            this.error = true;
        }
    }

    private void addBatch() {
        int expMonth = Integer.parseInt(request.getParameter("expMonth"));
        int expYear = 0;
        String yearStr = request.getParameter("expYear");
        if (yearStr.equals("")) {
            this.errorMessage.append("<li>Please enter an expiry year</li>");
            this.error = true;
        }
        String batchStr = request.getParameter("batch");
        if (batchStr.equals("")) {
            this.errorMessage.append("<li>Please enter a batch number</li>");
            this.error = true;
        }
        if (Utils.intChecker(yearStr)) {
            expYear = Integer.parseInt(yearStr);
            if (Utils.dateInFuture(expMonth, expYear)) {
                LocalDate date = LocalDate.of(expYear, expMonth, 1);
                Batch newBatch = new Batch(this.drugId, batchStr, date);
                if (!this.error) {
                    newBatch.addBatch();
                    this.batchId = newBatch.getId();
                }
            } else {
                this.errorMessage.append("<li>Expiry date must be in the future</li>");
                this.error = true;
            }
        } else {
            this.errorMessage.append("<li>Expiry year must be a number</li>");
            this.error = true;
        }
    }

    private void validateComments() {
        String initComments = request.getParameter("comments");
        if (initComments.equals("")) {
            this.error = true;
            this.errorMessage.append("<li>Please enter " + this.type + " details in comments box</li>");
        } else {
            this.comments.append(initComments);
        }
    }

    private void validateNewQuantity() {
        String newQuantityStr = request.getParameter("newQuantity");
        if (newQuantityStr.equals("")) {
            this.errorMessage.append("<li>New quantity cannot be empty</li>");
            this.error = true;
        }
        if (Utils.doubleChecker(newQuantityStr)) {
            this.newQuantity = Double.parseDouble(newQuantityStr);
            double currentQuantity = Ward.getWardDrugQuantity(this.wardId, this.drugId);
            this.quantityChange = this.newQuantity - currentQuantity;
            if (this.newQuantity < 0) {
                this.errorMessage.append("<li>New quantity must not be negative</li>");
                this.error = true;
            }
        } else {
            this.errorMessage.append("<li>New quantity must be a number</li>");
            this.error = true;
        }
    }

    private void setPatientForNonPatientEntry() {
        this.patient = "(" + this.type.substring(0,1).toUpperCase() + this.type.substring(1) + ")";
    }

    private void patientEntry() {
        validatePatientName();
        validateQuantityTaken();
        validateTakeBatch();
    }

    private void restockEntry() {
        validateQuantityAdded();
        validateAddBatch();
        setPatientForNonPatientEntry();
    }

    private void returnEntry() {
        validateQuantityTaken();
        validateComments();
        validateTakeBatch();
        setPatientForNonPatientEntry();
    }

    private void errorEntry() {
        validateNewQuantity();
        validateComments();
        setPatientForNonPatientEntry();
        this.batchId = -1;
    }

    public Entry(HttpServletRequest request) {
        this.request = request;
        this.wardId = Integer.parseInt(request.getParameter("wardId"));
        this.drugId = Integer.parseInt(request.getParameter("drugId"));
        this.successMessage = new StringBuilder();
        this.batchExists = true;
        this.comments = new StringBuilder();
        this.errorMessage = new StringBuilder();
        this.type = request.getParameter("type");
        this.successMessage.append(this.type.substring(0, 1).toUpperCase() +
                this.type.substring(1) + " entry successfully added");
        this.dateTime = LocalDateTime.now();
        HttpSession session = request.getSession();
        this.givenBy = session.getAttribute("user").toString();
        this.checkedBy = request.getParameter("checkingUser");
        if (this.givenBy.equals(this.checkedBy)) {
            this.error = true;
            this.errorMessage.append("<li>Checking user has to be different to current user</li>");
        }
        if (!Utils.checkingUser(request)) {
            this.error = true;
            this.errorMessage.append("<li>Incorrect checking user or password</li>");
        } else {
            User checkingUser = User.getUser(this.checkedBy);
            if (!Authorisation.checkAuthority("checkEntry", checkingUser.getLevel())) {
                this.error = true;
                this.errorMessage.append("<li>Checking user does not have authority to check entries");
            }
        }
        switch (this.type) {
            case "patient":
                patientEntry();
                break;
            case "return":
                returnEntry();
                break;
            case "restock":
                restockEntry();
                break;
            case "error":
                errorEntry();
                break;
            default:
                this.error = true;
                this.errorMessage.append("<li>Invalid Type</li>");
        }
        if (this.type.equals("patient")) {
            if (!Authorisation.checkAuthority("giveDrug", session.getAttribute("userLevel").toString())) {
                this.error = true;
                this.errorMessage.append("<li>Logged in user does not have authority to give Drugs");
            }
        }
        if (!Authorisation.checkAuthority("createEntry", session.getAttribute("userLevel").toString())) {
            this.error = true;
            this.errorMessage.append("<li>You do not have authority to create Entries");
        }
        if (!this.error) {
            addEntry();
            if (Batch.wardDrugBatchExists(this.wardId, this.batchId)) {
                this.newBatchQuantity = Ward.getWardBatchQuantity(this.wardId, this.batchId) + this.quantityChange;
                Ward.setWardBatchQuantity(this.wardId, this.batchId, this.newBatchQuantity);
            } else {
                if (this.type.equals("restock")) {
                    this.newBatchQuantity = this.quantityChange;
                }
                Batch.addNewWardBatchQuantity(this.wardId, this.batchId, this.newBatchQuantity);
            }
            Ward.setWardDrugQuantity(this.wardId, this.drugId, this.newQuantity);
        }
    }

    public Entry(int id, LocalDateTime dateTime, int drugId, String patient, String givenBy, String checkedBy,
                 double quantity, String type, String comments, double newQuantity, int batchId) {
        this.id = id;
        this.dateTime = dateTime;
        this.patient = patient;
        this.givenBy = givenBy;
        this.checkedBy = checkedBy;
        this.quantityChange = quantity;
        this.type = type;
        this.comments = new StringBuilder();
        this.comments.append(comments);
        this.newQuantity = newQuantity;
        this.drugId = drugId;
        this.batchId = batchId;
    }

    public void addEntry() {
        if (!this.error) {
            try {
                String updateEntriesSql = "INSERT INTO entries(day,month,year,hour,minute,seconds," +
                        "drug,patient,givenby, checkedby,quantity,type,comments,newquantity,batchid," +
                        "wardId) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                Connection connection = DbOp.connect();
                PreparedStatement entriesUpdateStmt = connection.prepareStatement(updateEntriesSql);
                entriesUpdateStmt.setInt(1, this.dateTime.getDayOfMonth());
                entriesUpdateStmt.setInt(2, this.dateTime.getMonthValue());
                entriesUpdateStmt.setInt(3, this.dateTime.getYear());
                entriesUpdateStmt.setInt(4, this.dateTime.getHour());
                entriesUpdateStmt.setInt(5, this.dateTime.getMinute());
                entriesUpdateStmt.setInt(6, this.dateTime.getSecond());
                entriesUpdateStmt.setInt(7, this.drugId);
                entriesUpdateStmt.setString(8, this.patient);
                entriesUpdateStmt.setString(9, this.givenBy);
                entriesUpdateStmt.setString(10, this.checkedBy);
                entriesUpdateStmt.setDouble(11, this.quantityChange);
                entriesUpdateStmt.setString(12, this.type);
                entriesUpdateStmt.setString(13, this.comments.toString());
                entriesUpdateStmt.setDouble(14, this.newQuantity);
                entriesUpdateStmt.setInt(15, this.batchId);
                entriesUpdateStmt.setInt(16, this.wardId);
                entriesUpdateStmt.executeUpdate();
                connection.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static ArrayList<Entry> getEntries(int drugId, int wardId) {
        ArrayList<Entry> entriesList = new ArrayList<>();
        try {
            String sql = "SELECT * from entries WHERE drug = ? AND wardId = ? ORDER BY year DESC, month DESC, " +
                    "day DESC, hour DESC, minute DESC, seconds DESC";
            Connection connection = DbOp.connect();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, drugId);
            statement.setInt(2, wardId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int day = resultSet.getInt("day");
                int month = resultSet.getInt("month");
                int year = resultSet.getInt("year");
                int hour = resultSet.getInt("hour");
                int minute = resultSet.getInt("minute");
                int seconds = resultSet.getInt("seconds");
                LocalDateTime dateTime = LocalDateTime.of(year,month,day,hour,minute,seconds);
                String patient = resultSet.getString("patient");
                String givenBy = resultSet.getString("givenby");
                String checkedBy = resultSet.getString("checkedby");
                double quantity = resultSet.getDouble("quantity");
                String type = resultSet.getString("type");
                String comments = resultSet.getString("comments");
                double newQuantity = resultSet.getDouble("newquantity");
                int batchId = resultSet.getInt("batchid");
                Entry entry = new Entry(
                        id, dateTime, drugId, patient, givenBy, checkedBy, quantity,
                        type, comments, newQuantity, batchId);
                entriesList.add(entry);
            }
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return entriesList;
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

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getGivenBy() {
        return givenBy;
    }

    public void setGivenBy(String givenBy) {
        this.givenBy = givenBy;
    }

    public String getCheckedBy() {
        return checkedBy;
    }

    public void setCheckedBy(String checkedBy) {
        this.checkedBy = checkedBy;
    }

    public int getWardId() {
        return wardId;
    }

    public void setWardId(int wardId) {
        this.wardId = wardId;
    }

    public double getQuantityChange() {
        return quantityChange;
    }

    public void setQuantityChange(double quantityChange) {
        this.quantityChange = quantityChange;
    }

    public void setComments(StringBuilder comments) {
        this.comments = comments;
    }

    public int getDrugId() {
        return drugId;
    }

    public void setDrugId(int drugId) {
        this.drugId = drugId;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public double getNewBatchQuantity() {
        return newBatchQuantity;
    }

    public void setNewBatchQuantity(double newBatchQuantity) {
        this.newBatchQuantity = newBatchQuantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComments() {
        return comments.toString();
    }

    public double getNewQuantity() {
        return newQuantity;
    }

    public void setNewQuantity(double newQuantity) {
        this.newQuantity = newQuantity;
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
