package webapp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

@WebServlet(urlPatterns = "/editAuthorisation.do")
public class EditAuthorisations extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (!Utils.loginCheck(request)) {
            response.sendRedirect("login.do");
        } else if(!request.getSession().getAttribute("userLevel").toString().equals("Admin")) {
            response.sendRedirect("home.do");
        } else {
            HttpSession session = request.getSession();
            String currentLevel = session.getAttribute("userLevel").toString();
            if (!currentLevel.equals("Admin")) {
                response.sendRedirect("home.do");
            } else {
                request.getRequestDispatcher("/views/editAuthorisation.jsp").forward(request,response);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!Utils.loginCheck(request)) {
            response.sendRedirect("login.do");
        } else if(!request.getSession().getAttribute("userLevel").toString().equals("Admin")) {
            response.sendRedirect("home.do");
        } else {
            try {
                String sql = "UPDATE authorisations SET createNurse = ?, createOdp = ?, createPharmacist = ?, " +
                        "createStudent = ?, createTech = ?, createDoctor = ?, createSister = ?, createEntry = ?, " +
                        "checkEntry = ?, createWard = ?, createDrug = ?, allocateDrug = ?, performCheck = ?, " +
                        "setAlert = ?, giveDrug = ? WHERE level = ?";
                Connection connection = DbOp.connect();
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, Authorisation.convertToInt(request.getParameter("createNurse")));
                statement.setInt(2, Authorisation.convertToInt(request.getParameter("createOdp")));
                statement.setInt(3, Authorisation.convertToInt(request.getParameter("createPharmacist")));
                statement.setInt(4, Authorisation.convertToInt(request.getParameter("createStudent")));
                statement.setInt(5, Authorisation.convertToInt(request.getParameter("createTech")));
                statement.setInt(6, Authorisation.convertToInt(request.getParameter("createDoctor")));
                statement.setInt(7, Authorisation.convertToInt(request.getParameter("createSister")));
                statement.setInt(8, Authorisation.convertToInt(request.getParameter("createEntry")));
                statement.setInt(9, Authorisation.convertToInt(request.getParameter("checkEntry")));
                statement.setInt(10, Authorisation.convertToInt(request.getParameter("createWard")));
                statement.setInt(11, Authorisation.convertToInt(request.getParameter("createDrug")));
                statement.setInt(12, Authorisation.convertToInt(request.getParameter("allocateDrug")));
                statement.setInt(13, Authorisation.convertToInt(request.getParameter("performCheck")));
                statement.setInt(14, Authorisation.convertToInt(request.getParameter("setAlert")));
                statement.setInt(15, Authorisation.convertToInt(request.getParameter("giveDrug")));
                statement.setString(16, request.getParameter("level"));
                statement.executeUpdate();
                connection.close();
                String successMessage = "User Level modified successfully";
                request.setAttribute("successMessage", successMessage);
            } catch (Exception e) {
                String errorMessage = "There was an unknown error";
                request.setAttribute("errorMessage", errorMessage);
                System.out.println(e.getMessage());
            }
            request.getRequestDispatcher("/views/viewAuthorisations.jsp").forward(request,response);
        }
    }
}