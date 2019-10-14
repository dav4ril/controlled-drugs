package webapp;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import static java.time.temporal.ChronoUnit.DAYS;

public class Batch {
    private int id;
    private int drugId;
    private String batchStr;
    private LocalDate expiryDate;
    private double quantity;

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
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

    public String getBatchStr() {
        return batchStr;
    }

    public void setBatchStr(String batchStr) {
        this.batchStr = batchStr;
    }

    public Batch(int id, int drugId, String batchStr, LocalDate expiryDate) {
        this.id = id;
        this.drugId = drugId;
        this.batchStr = batchStr.toLowerCase().replace(" ", "");
        this.expiryDate = expiryDate;
    }

    public Batch(int drugId, String batchStr, LocalDate expiryDate) {
        this.drugId = drugId;
        this.batchStr = batchStr.toLowerCase().replace(" ", "");
        this.expiryDate = expiryDate;
    }

    public static boolean batchExists(int drugId, String batchStr) {
        batchStr = batchStr.toLowerCase().replace(" ", "");
        Batch batch = getBatch(drugId, batchStr);
        if (batch != null) {
            return true;
        }
        return false;
    }

    public boolean batchExists() {
        Batch batch = getBatch(this.getDrugId(), this.batchStr);
        if (batch != null) {
            this.id = batch.getId();
            return true;
        }
        return false;
    }

    public void addBatch() {
        addBatch(this.drugId,
                this.batchStr,
                this.expiryDate.getMonthValue(),
                this.expiryDate.getYear());
        Batch batchJustAdded = getBatch(this.drugId, this.batchStr);
        this.id = batchJustAdded.getId();
    }

    public static void addBatch(int drugId, String batchStr, int expMonth, int expYear) {
        String sql = "INSERT INTO batches(drugid,batch,expmonth,expyear) VALUES(?,?,?,?)";
        try {
            batchStr = batchStr.toLowerCase().replace(" ", "");
            Connection connection = DbOp.connect();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1,drugId);
            statement.setString(2,batchStr);
            statement.setInt(3,expMonth);
            statement.setInt(4,expYear);
            statement.executeUpdate();
            connection.close();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static Batch getBatch(int batchId) {
        Batch batch = null;
        try {
            Connection connection = DbOp.connect();
            String sql = "SELECT * from batches WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, batchId);
            ResultSet resultSet = statement.executeQuery();
            LocalDate date = LocalDate.of(
                    resultSet.getInt("expyear"),
                    resultSet.getInt("expmonth"), 1);
            int drugId = resultSet.getInt("drugid");
            String batchStr = resultSet.getString("batch");
            connection.close();
            return new Batch(batchId, drugId, batchStr, date);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return batch;
    }

    public static Batch getBatch(int drugId, String batchStr) {
        Batch batch = null;
        try {
            Connection connection = DbOp.connect();
            String sql = "SELECT * from batches WHERE drugid = ? AND batch = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, drugId);
            statement.setString(2, batchStr);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                int expMonth = resultSet.getInt("expmonth");
                int expYear = resultSet.getInt("expmonth");
                LocalDate date = LocalDate.of(expYear, expMonth, 1);
                batch = new Batch(id, drugId, batchStr, date);
            }
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return batch;
    }

    public static ArrayList<Batch> getWardBatchList(int wardId) {
        ArrayList<Batch> batchList = new ArrayList<>();
        try {
            Connection connection = DbOp.connect();
            String sql = "SELECT * from batchQuantities WHERE wardId = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, wardId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int batchId = resultSet.getInt("batchId");
                if (batchId == -1) {
                    continue;
                }
                Batch batch = getBatch(batchId);
                batchList.add(batch);
            }
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return batchList;
    }

    public static ArrayList<Batch> getWardExpiringBatches(int wardId) {
        ArrayList<Batch> wardBatchList = getWardBatchList(wardId);
        ArrayList<Batch> expiringBatches = new ArrayList<>();
        LocalDate now = LocalDate.now();
        for (Batch batch : wardBatchList) {
            long daysUntilExpiry = now.until(batch.getExpiryDate(), DAYS);
            if (daysUntilExpiry < 20) {
                if (Ward.getWardBatchQuantity(wardId, batch.getId()) > 0) {
                    expiringBatches.add(batch);
                }
            }
        }
        return  expiringBatches;
    }

    public static ArrayList<Batch> getWardDrugBatches(int wardId, int drugId) {
        ArrayList<Batch> wardBatchList = getWardBatchList(wardId);
        ArrayList<Batch> batchesForDrug = new ArrayList<>();
        for (Batch batch : wardBatchList) {
            if (batch.getDrugId() == drugId) {
                batchesForDrug.add(batch);
            }
        }
        return batchesForDrug;
    }


    public static ArrayList<Batch> getBatchList() {
        ArrayList<Batch> batchList = new ArrayList<>();
        try {
            Connection connection = DbOp.connect();
            String sql = "SELECT * from batches";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int drugId = resultSet.getInt("drugid");
                String batchStr = resultSet.getString("batch");
                int expMonth = resultSet.getInt("expmonth");
                int expYear = resultSet.getInt("expyear");
                LocalDate date = LocalDate.of(expYear, expMonth, 1);
                Batch batch = new Batch(id, drugId, batchStr, date);
                batchList.add(batch);
            }
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return batchList;
    }

    public static ArrayList<Batch> getDrugBatches(int drugId) {
        ArrayList<Batch> batchList = new ArrayList<>();
        try {
            Connection connection = DbOp.connect();
            String sql = "SELECT * from batches WHERE drugid = ? ORDER BY batch";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, drugId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                if (id == -1) {
                    continue;
                }
                String batchStr = resultSet.getString("batch");
                int expMonth = resultSet.getInt("expmonth");
                int expYear = resultSet.getInt("expyear");
                LocalDate date = LocalDate.of(expYear, expMonth, 1);
                Batch batch = new Batch(id, drugId, batchStr, date);
                batchList.add(batch);
            }
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return batchList;
    }

    public static boolean wardDrugBatchExists(int wardId, int batchId) {
        try {
            String sql = "SELECT * from batchQuantities WHERE batchId = ? AND wardId = ?";
            Connection connection = DbOp.connect();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, batchId);
            statement.setInt(2, wardId);
            ResultSet resultSet = statement.executeQuery();
            boolean exists = resultSet.next();
            connection.close();
            return exists;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static void addNewWardBatchQuantity(int wardId, int batchId, double newBatchQuantity) {
        try {
            String sql = "INSERT INTO batchQuantities(wardId,batchId,quantity) VALUES(?,?,?)";
            Connection connection = DbOp.connect();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, wardId);
            statement.setInt(2, batchId);
            statement.setDouble(3, newBatchQuantity);
            statement.executeUpdate();
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
