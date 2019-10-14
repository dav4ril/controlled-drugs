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


@WebServlet(urlPatterns = "/updateAlertLevel.do")
public class UpdateAlertLevelServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		if (!Utils.loginCheck(request)) {
			response.sendRedirect("login.do");
		} else {
			request.getRequestDispatcher("/views/viewDrug.jsp").forward(request,response);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (!Utils.loginCheck(request)) {
			response.sendRedirect("login.do");
		} else {
			int drugId = Integer.parseInt(request.getParameter("drugId"));
			int wardId = Integer.parseInt(request.getParameter("wardId"));
			request.setAttribute("drugId", drugId);
			boolean error = false;
			StringBuilder errorMessage = new StringBuilder();
			String newAlertLevelStr = request.getParameter("alert");
			double newAlertLevel = 0;
			HttpSession session = request.getSession();
			if (!Authorisation.checkAuthority("setAlert", session.getAttribute("userLevel").toString())) {
				error = true;
				errorMessage.append("<li>Checking user does not have authority to set alert level</li>");
			}
			if (!Utils.doubleChecker(newAlertLevelStr)) {
				errorMessage.append("<li>New alert level must be a number</li>");
				error = true;
			} else {
				newAlertLevel = Double.parseDouble(newAlertLevelStr);
				if (newAlertLevel < 0) {
					errorMessage.append("<li>New alert level must be 0 or greater</li>");
					error = true;
				}
			}
			if (!error) {
				Ward.setWardDrugAlert(wardId, drugId, newAlertLevel);
			} else {
				request.setAttribute("alertErrorMessage", errorMessage.toString());
			}
//			doGet(request, response);
			String redirect = "viewDrug.do?drugId=" + drugId + "&wardId=" + wardId;
			response.sendRedirect(redirect);
		}
	}

}

