package webapp;

import java.sql.*;
import java.util.ArrayList;

public class Authorisation {

    private String level;
    private boolean createAdmin;
    private boolean createNurse;
    private boolean createOdp;
    private boolean createPharmacist;
    private boolean createStudent;
    private boolean createTech;
    private boolean createDoctor;
    private boolean createSister;
    private boolean createEntry;
    private boolean checkEntry;
    private boolean createWard;
    private boolean createDrug;
    private boolean allocateDrug;
    private boolean performCheck;
    private boolean setAlert;
    private boolean giveDrug;
    private boolean resetPassword;

    public Authorisation(String level, boolean createAdmin, boolean createNurse, boolean createOdp,
                         boolean createPharmacist, boolean createStudent, boolean createTech, boolean createDoctor,
                         boolean createSister, boolean createEntry, boolean checkEntry, boolean createWard,
                         boolean createDrug, boolean allocateDrug, boolean performCheck, boolean setAlert,
                         boolean giveDrug, boolean resetPassword) {
        this.level = level;
        this.createAdmin = createAdmin;
        this.createNurse = createNurse;
        this.createOdp = createOdp;
        this.createPharmacist = createPharmacist;
        this.createStudent = createStudent;
        this.createTech = createTech;
        this.createDoctor = createDoctor;
        this.createSister = createSister;
        this.createEntry = createEntry;
        this.checkEntry = checkEntry;
        this.createWard = createWard;
        this.createDrug = createDrug;
        this.allocateDrug = allocateDrug;
        this.performCheck = performCheck;
        this.setAlert = setAlert;
        this.giveDrug = giveDrug;
        this.resetPassword = resetPassword;
    }

    public boolean canResetPassword() {
        return resetPassword;
    }

    public void setResetPassword(boolean resetPassword) {
        this.resetPassword = resetPassword;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public boolean canCreateAdmin() {
        return createAdmin;
    }

    public void setCreateAdmin(boolean createAdmin) {
        this.createAdmin = createAdmin;
    }

    public boolean canCreateNurse() {
        return createNurse;
    }

    public void setCreateNurse(boolean createNurse) {
        this.createNurse = createNurse;
    }

    public boolean canCreateOdp() {
        return createOdp;
    }

    public void setCreateOdp(boolean createOdp) {
        this.createOdp = createOdp;
    }

    public boolean canCreatePharmacist() {
        return createPharmacist;
    }

    public void setCreatePharmacist(boolean createPharmacist) {
        this.createPharmacist = createPharmacist;
    }

    public boolean canCreateStudent() {
        return createStudent;
    }

    public void setCreateStudent(boolean createStudent) {
        this.createStudent = createStudent;
    }

    public boolean canCreateTech() {
        return createTech;
    }

    public void setCreateTech(boolean createTech) {
        this.createTech = createTech;
    }

    public boolean canCreateDoctor() {
        return createDoctor;
    }

    public void setCreateDoctor(boolean createDoctor) {
        this.createDoctor = createDoctor;
    }

    public boolean canCreateSister() {
        return createSister;
    }

    public void setCreateSister(boolean createSister) {
        this.createSister = createSister;
    }

    public boolean canCreateEntry() {
        return createEntry;
    }

    public void setCreateEntry(boolean createEntry) {
        this.createEntry = createEntry;
    }

    public boolean canCheckEntry() {
        return checkEntry;
    }

    public void setCheckEntry(boolean checkEntry) {
        this.checkEntry = checkEntry;
    }

    public boolean canCreateWard() {
        return createWard;
    }

    public void setCreateWard(boolean createWard) {
        this.createWard = createWard;
    }

    public boolean canCreateDrug() {
        return createDrug;
    }

    public void setCreateDrug(boolean createDrug) {
        this.createDrug = createDrug;
    }

    public boolean canAllocateDrug() {
        return allocateDrug;
    }

    public void setAllocateDrug(boolean allocateDrug) {
        this.allocateDrug = allocateDrug;
    }

    public boolean canPerformCheck() {
        return performCheck;
    }

    public void setPerformCheck(boolean performCheck) {
        this.performCheck = performCheck;
    }

    public boolean canSetAlert() {
        return setAlert;
    }

    public void setSetAlert(boolean setAlert) {
        this.setAlert = setAlert;
    }

    public boolean canGiveDrug() {
        return giveDrug;
    }

    public void setGiveDrug(boolean giveDrug) {
        this.giveDrug = giveDrug;
    }

    public static boolean convertToBoolean(int value) {
        if (value == 1) {
            return true;
        }
        return false;
    }

    public static int convertToInt(String value) {
        if (value.equals("yes")) {
            return 1;
        }
        return 0;
    }

    public static ArrayList<Authorisation> getAuthorisations() {
        ArrayList<Authorisation> authorisationsList = new ArrayList<>();
        try {
            String sql = "SELECT * from authorisations";
            Connection connection = DbOp.connect();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String level = resultSet.getString("level");
                boolean createAdmin = convertToBoolean(resultSet.getInt("createAdmin"));
                boolean createNurse = convertToBoolean(resultSet.getInt("createNurse"));
                boolean createOdp = convertToBoolean(resultSet.getInt("createOdp"));
                boolean createPharmacist = convertToBoolean(resultSet.getInt("createPharmacist"));
                boolean createStudent = convertToBoolean(resultSet.getInt("createStudent"));
                boolean createTech = convertToBoolean(resultSet.getInt("createTech"));
                boolean createDoctor = convertToBoolean(resultSet.getInt("createDoctor"));
                boolean createSister = convertToBoolean(resultSet.getInt("createSister"));
                boolean createEntry = convertToBoolean(resultSet.getInt("createEntry"));
                boolean checkEntry = convertToBoolean(resultSet.getInt("checkEntry"));
                boolean createWard = convertToBoolean(resultSet.getInt("createWard"));
                boolean createDrug = convertToBoolean(resultSet.getInt("createDrug"));
                boolean allocateDrug = convertToBoolean(resultSet.getInt("allocateDrug"));
                boolean performCheck = convertToBoolean(resultSet.getInt("performCheck"));
                boolean setAlert = convertToBoolean(resultSet.getInt("setAlert"));
                boolean giveDrug = convertToBoolean(resultSet.getInt("giveDrug"));
                boolean resetPassword = convertToBoolean(resultSet.getInt("resetPassword"));
                Authorisation authorisation = new Authorisation(level,createAdmin,createNurse,createOdp,
                        createPharmacist,createStudent,createTech,createDoctor,createSister,createEntry,
                        checkEntry,createWard,createDrug,allocateDrug,performCheck,setAlert,giveDrug, resetPassword);
                authorisationsList.add(authorisation);
            }
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return authorisationsList;
    }

    public static String cellCreation(boolean authorised) {
        if (authorised) {
            return "<td class=\"table-success\">Yes</td>";
        }
        return "<td class=\"table-danger\">No</td>";
    }

    public static boolean checkAuthority(String action, String currentUserLevel) {
        try {
            String sql = "SELECT * from authorisations WHERE level = ?";
            Connection connection = DbOp.connect();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, currentUserLevel);
            ResultSet resultSet = statement.executeQuery();
            int authorityBin = resultSet.getInt(action);
            connection.close();
            if (authorityBin == 1) {
                return true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static ArrayList<String> getColumnLabels() {
        ArrayList<String> columnLabels = new ArrayList<>();
        try {
            Connection connection = DbOp.connect();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * from authorisations");
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();
            for (int i = 3; i <= columnCount ; i++) {
                columnLabels.add(resultSetMetaData.getColumnName(i));
            }
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return columnLabels;
    }

    public static Authorisation getAuthorisation(String level) {
        Authorisation authorisation = null;
        try {
            String sql = "SELECT * from authorisations WHERE level = ?";
            Connection connection = DbOp.connect();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, level);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                boolean createAdmin = convertToBoolean(resultSet.getInt("createAdmin"));
                boolean createNurse = convertToBoolean(resultSet.getInt("createNurse"));
                boolean createOdp = convertToBoolean(resultSet.getInt("createOdp"));
                boolean createPharmacist = convertToBoolean(resultSet.getInt("createPharmacist"));
                boolean createStudent = convertToBoolean(resultSet.getInt("createStudent"));
                boolean createTech = convertToBoolean(resultSet.getInt("createTech"));
                boolean createDoctor = convertToBoolean(resultSet.getInt("createDoctor"));
                boolean createSister = convertToBoolean(resultSet.getInt("createSister"));
                boolean createEntry = convertToBoolean(resultSet.getInt("createEntry"));
                boolean checkEntry = convertToBoolean(resultSet.getInt("checkEntry"));
                boolean createWard = convertToBoolean(resultSet.getInt("createWard"));
                boolean createDrug = convertToBoolean(resultSet.getInt("createDrug"));
                boolean allocateDrug = convertToBoolean(resultSet.getInt("allocateDrug"));
                boolean performCheck = convertToBoolean(resultSet.getInt("performCheck"));
                boolean setAlert = convertToBoolean(resultSet.getInt("setAlert"));
                boolean giveDrug = convertToBoolean(resultSet.getInt("giveDrug"));
                boolean resetPassword = convertToBoolean(resultSet.getInt("resetPassword"));
                authorisation = new Authorisation(level,createAdmin,createNurse,createOdp,
                        createPharmacist,createStudent,createTech,createDoctor,createSister,createEntry,
                        checkEntry,createWard,createDrug,allocateDrug,performCheck,setAlert,giveDrug,resetPassword);
            }
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("problem getting authorisation");
        }
        return authorisation;
    }

    public static String getActionDesc(String action) {
        switch (action) {
            case "createAdmin":
                return "Create Admin";
            case "createNurse":
                return "Create Nurse";
            case "createOdp":
                return "Create ODP";
            case "createPharmacist":
                return "Create Pharmacist";
            case "createStudent":
                return "Create Student";
            case "createTech":
                return "Create Pharmacy Technician";
            case "createDoctor":
                return "Create Doctor";
            case "createSister":
                return "Create Sister/Charge Nurse";
            case "createEntry":
                return "Create Entry";
            case "createWard":
                return "Create Area";
            case "createDrug":
                return "Create Drug Profile";
            case "allocateDrug":
                return "Allocate Drug to Area";
            case "performCheck":
                return "Perform Full Check";
            case "setAlert":
                return "Set Low Stock Alert Level";
            case "giveDrug":
                return "Give Drug to Patient";
            case "checkEntry":
                return "Check Entry";
            case "resetPassword":
                return "Reset Passwords";
            default:
                return "Undefined Action";
        }
    }

    public boolean getAction(String action) {
        switch (action) {
            case "createAdmin":
                return this.canCreateAdmin();
            case "createNurse":
                return this.canCreateNurse();
            case "createOdp":
                return this.canCreateOdp();
            case "createPharmacist":
                return this.canCreatePharmacist();
            case "createStudent":
                return this.canCreateStudent();
            case "createTech":
                return this.canCreateTech();
            case "createDoctor":
                return this.canCreateDoctor();
            case "createSister":
                return this.canCreateSister();
            case "createEntry":
                return this.canCreateEntry();
            case "createWard":
                return this.canCreateWard();
            case "createDrug":
                return this.canCreateDrug();
            case "allocateDrug":
                return this.canAllocateDrug();
            case "performCheck":
                return this.canPerformCheck();
            case "setAlert":
                return this.canSetAlert();
            case "giveDrug":
                return this.canGiveDrug();
            case "checkEntry":
                return this.canCheckEntry();
            case "resetPassword":
                return this.canResetPassword();
                default:
                    return false;
        }
    }
}
