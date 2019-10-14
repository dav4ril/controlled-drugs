package webapp;

import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = "/changePassword.do")
public class ChangePasswordServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!Utils.loginCheck(request)) {
            response.sendRedirect("login.do");
        } else {
            request.getRequestDispatcher("/views/changePassword.jsp").forward(request,response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!Utils.loginCheck(request)) {
            response.sendRedirect("login.do");
        } else if (!request.getSession().getAttribute("user").toString().equals(request.getParameter("userName"))) {
            response.sendRedirect("login.do");
        } else {
            StringBuilder errorMessage = new StringBuilder();
            String pass1 = request.getParameter("pass1");
            String pass2 = request.getParameter("pass2");
            String old = request.getParameter("old");
            String userName = request.getParameter("userName");
            boolean error = false;
            if (old.equals("")) {
                error = true;
                errorMessage.append("<li>Please enter your old password</li>");
            } else {
                if (!DbOp.checkPasswordForUser(userName, old)) {
                    error = true;
                    errorMessage.append("<li>Old password is incorrect</li>");
                }
            }
            if (!pass1.equals(pass2)) {
                errorMessage.append("<li>Passwords don't match</li>");
                error = true;
            }
            if (pass1.length() < 8) {
                errorMessage.append("<li>Passwords is too short, must be 8 characters or more</li>");
                error = true;
            }
            HttpSession session = request.getSession();
            if (!error) {
                String hashedPassword = BCrypt.hashpw(pass1, BCrypt.gensalt(12));
                error = !DbOp.updatePassword(hashedPassword, userName, session.getAttribute("user").toString());
            }
            if (!error) {
                String successMessage = "Password successfully changed";
                request.setAttribute("successMessage", successMessage);
            } else {
                request.setAttribute("errorMessage", errorMessage);
            }
            request.getRequestDispatcher("/views/changePassword.jsp").forward(request,response);
        }
    }
}