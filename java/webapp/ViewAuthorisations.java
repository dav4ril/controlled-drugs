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

@WebServlet(urlPatterns = "/viewAuthorisations.do")
public class ViewAuthorisations extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (!Utils.loginCheck(request)) {
            response.sendRedirect("login.do");
        } else {
            HttpSession session = request.getSession();
            String currentLevel = session.getAttribute("userLevel").toString();
            if (!currentLevel.equals("Admin")) {
                response.sendRedirect("home.do");
            } else {
                request.getRequestDispatcher("/views/viewAuthorisations.jsp").forward(request,response);
            }
        }
    }

}