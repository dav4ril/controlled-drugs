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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

/*
 * Browser sends Http Request to Web Server
 *
 * Code in Web Server => Input:HttpRequest, Output: HttpResponse
 * JEE with Servlets
 *
 * Web Server responds with Http Response
 */

//Java Platform, Enterprise Edition (Java EE) JEE6

//Servlet is a Java programming language class 
//used to extend the capabilities of servers 
//that host applications accessed by means of 
//a request-response programming model.

//1. extends javax.servlet.http.HttpServlet
//2. @WebServlet(urlPatterns = "/login.do")
//3. doGet(HttpServletRequest request, HttpServletResponse response)
//4. How is the response created?

@WebServlet(urlPatterns = "/viewUser.do")
public class ViewUserServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (!Utils.loginCheck(request)) {
            response.sendRedirect("login.do");
        } else {
            String username = request.getParameter("username");
            String sql = "SELECT * from users WHERE name = ?";
            try {
                Connection connection = DbOp.connect();
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    request.setAttribute("first", resultSet.getString("first"));
                    request.setAttribute("surname", resultSet.getString("surname"));
                    request.setAttribute("level", resultSet.getString("level"));
                    request.setAttribute("username", username);
                }
                connection.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            request.getRequestDispatcher("/views/viewUser.jsp").forward(request,response);
        }
    }

}