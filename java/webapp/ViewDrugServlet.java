package webapp;

import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.regex.Pattern;

@WebServlet(urlPatterns = "/viewDrug.do")
public class ViewDrugServlet extends HttpServlet {


    public static void updateDrugQuantity(double newQuantity, int drugId) {
        try {
            String updateDrugQuantitySql = "UPDATE drugs SET quantity = ? WHERE id = ?";
            Connection connection = DbOp.connect();
            PreparedStatement updateQuantityStmt = connection.prepareStatement(updateDrugQuantitySql);
            updateQuantityStmt.setDouble(1, newQuantity);
            updateQuantityStmt.setInt(2, drugId);
            updateQuantityStmt.executeUpdate();
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (!Utils.loginCheck(request)) {
            response.sendRedirect("login.do");
        } else {
            HttpSession session = request.getSession();
            String currentUserLevel = session.getAttribute("userLevel").toString();
            String currentUserName = session.getAttribute("user").toString();
            request.setAttribute("currentUserLevel", currentUserLevel);
            request.setAttribute("currentUserName", currentUserName);
            String drugIdStr = request.getParameter("drugId");
            int selectedDrugId = 0;
            if (drugIdStr == null) {
                selectedDrugId = Integer.parseInt(request.getAttribute("drugId").toString());
            } else {
                selectedDrugId = Integer.parseInt(drugIdStr);
            }
            request.setAttribute("drugId", selectedDrugId);
            request.getRequestDispatcher("/views/viewDrug.jsp").forward(request,response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!Utils.loginCheck(request)) {
            System.out.println("user not logged in");
            response.sendRedirect("login.do");
        } else {
            Entry entry = new Entry(request);
            if (!entry.isError()) {
                String successMessage = entry.getSuccessMessage().toString();
                request.setAttribute("successMessage", successMessage);
            } else {
                String errorMessage = entry.getErrorMessage().toString();
                request.setAttribute("errorMessage", errorMessage);
            }
            doGet(request, response);
        }
    }

}