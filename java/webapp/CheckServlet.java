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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

@WebServlet(urlPatterns = "/check.do")
public class CheckServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		if (!Utils.loginCheck(request)) {
			response.sendRedirect("login.do");
		} else if(!Authorisation.checkAuthority("performCheck", request.getSession().getAttribute("userLevel").toString())) {
			response.sendRedirect("home.do");
		} else {
			request.getRequestDispatcher("/views/check.jsp").forward(request,response);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (!Utils.loginCheck(request)) {
			response.sendRedirect("login.do");
		} else if(!Authorisation.checkAuthority("performCheck", request.getSession().getAttribute("userLevel").toString())) {
			response.sendRedirect("home.do");
		} else {
			Check check = new Check(request);
			if (!check.isError()) {
				check.addCheck();
				String successMessage = "Check completed successfully";
				request.setAttribute("successMessage", successMessage);
			} else {
				request.setAttribute("errorMessage", check.getErrorMessage().toString());
			}
		}
		request.getRequestDispatcher("/views/check.jsp").forward(request,response);
	}
}