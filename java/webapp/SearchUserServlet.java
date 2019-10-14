package webapp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(urlPatterns = "/searchUser.do")
public class SearchUserServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!Utils.loginCheck(request)) {
            response.sendRedirect("login.do");
        } else if(!Authorisation.checkAuthority("resetPassword", request.getSession().getAttribute("userLevel").toString())) {
            response.sendRedirect("home.do");
        } else {
            String method = request.getParameter("method");
            StringBuilder errorMessage = new StringBuilder();
            boolean error = false;
            if (method.equals("username")) {
                String userNameEntered = request.getParameter("username");
                if (userNameEntered.equals("")) {
                    errorMessage.append("<li>Please enter a Username</li>");
                    error = true;
                } else {
                    if (!User.userExists(userNameEntered)) {
                        error = true;
                        errorMessage.append("<li>User not found</li");
                    }
                }
                if (!error) {
                    String redirect = "viewUser.do?username=" + userNameEntered;
                    response.sendRedirect(redirect);
                } else {
                    request.setAttribute("searchErrorMessage", errorMessage.toString());
                    request.getRequestDispatcher("/views/createUser.jsp").forward(request,response);
                }
            } else {
                String firstNameEntered = request.getParameter("first");
                if (firstNameEntered.equals("")) {
                    errorMessage.append("<li>Please enter a first name</li>");
                    error = true;
                }
                String surnameEntered = request.getParameter("surname");
                if (surnameEntered.equals("")) {
                    errorMessage.append("<li>Please enter a surname</li>");
                    error = true;
                }
                if (!error) {
                    request.getRequestDispatcher("/views/searchUserResults.jsp").forward(request,response);
                } else {
                    request.setAttribute("searchErrorMessage", errorMessage.toString());
                    request.getRequestDispatcher("/views/createUser.jsp").forward(request,response);
                }
            }
        }
    }
}