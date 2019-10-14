package webapp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;


@WebServlet(urlPatterns = "/addDrug.do")
public class AddDrugServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		if (!Utils.loginCheck(request)) {
			response.sendRedirect("login.do");
		} else if(!Authorisation.checkAuthority("createDrug", request.getSession().getAttribute("userLevel").toString())) {
			response.sendRedirect("home.do");
		} else {
			request.getRequestDispatcher("/views/addDrug.jsp").forward(request,response);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (!Utils.loginCheck(request)) {
			response.sendRedirect("login.do");
		} else if(!Authorisation.checkAuthority("createDrug", request.getSession().getAttribute("userLevel").toString())) {
			response.sendRedirect("home.do");
		} else {
			Drug drug = new Drug(request);
			if (!drug.isError()) {
				drug.addDrug();
				Drug drugJustAdded = Drug.getDrug(drug.getName(),drug.getStrength(),drug.getUnits());
				int drugJustAddedId = drugJustAdded.getId();
				String successMessage = "New drug " + drug.getName() + " has been successfully added";
				request.setAttribute("successMessage", successMessage);
			} else {
				request.setAttribute("errorMessage", drug.getErrorMessage().toString());
			}
			request.getRequestDispatcher("/views/addDrug.jsp").forward(request,response);
		}
	}
}

