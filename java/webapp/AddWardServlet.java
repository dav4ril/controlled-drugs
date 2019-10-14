package webapp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(urlPatterns = "/addWard.do")
public class AddWardServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		if (!Utils.loginCheck(request)) {
			response.sendRedirect("login.do");
		} else if(!Authorisation.checkAuthority("createWard", request.getSession().getAttribute("userLevel").toString())) {
			response.sendRedirect("home.do");
		} else {
			request.getRequestDispatcher("/views/addWard.jsp").forward(request,response);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (!Utils.loginCheck(request)) {
			response.sendRedirect("login.do");
		} else if(!Authorisation.checkAuthority("createWard", request.getSession().getAttribute("userLevel").toString())) {
			response.sendRedirect("home.do");
		} else {
			Ward ward = new Ward(request);
			if (!ward.isError()) {
				ward.addWard();
				String successMessage = "New area: \"" + ward.getName() + "\" has been successfully added";
				request.setAttribute("successMessage", successMessage);
			} else {
				request.setAttribute("errorMessage", ward.getErrorMessage().toString());
			}
			request.getRequestDispatcher("/views/addWard.jsp").forward(request,response);
		}
	}
}

