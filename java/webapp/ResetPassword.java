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

@WebServlet(urlPatterns = "/resetPassword.do")
public class ResetPassword extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!Utils.loginCheck(request)) {
            response.sendRedirect("login.do");
        } else if(!Authorisation.checkAuthority("resetPassword", request.getSession().getAttribute("userLevel").toString())) {
            response.sendRedirect("home.do");
        } else {
            String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";
            StringBuilder password = new StringBuilder();
            for (int i = 0; i < 8; i++) {
                int index = (int)(AlphaNumericString.length() * Math.random());
                password.append(AlphaNumericString.charAt(index));
            }
            String userName = request.getParameter("userName");
            String hashedPassword = BCrypt.hashpw(password.toString(), BCrypt.gensalt(12));
            boolean error = false;
            String errorMessage = null;
            HttpSession session = request.getSession();
            error = !DbOp.updatePassword(hashedPassword, userName, session.getAttribute("user").toString());
            if (!error) {
                String successMessage = "Password for " + userName + " has been changed to: " + password + "<br>" +
                        "Please inform the user of their new password and remind them to change it.";
                request.setAttribute("successMessage", successMessage);
            } else {
                errorMessage = "An unknown error occured";
                request.setAttribute("errorMessage", errorMessage);
            }
            request.getRequestDispatcher("/views/passwordChanged.jsp").forward(request,response);
        }
    }
}