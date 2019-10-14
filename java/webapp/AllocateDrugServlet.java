package webapp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(urlPatterns = "/allocateDrug.do")
public class AllocateDrugServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		if (!Utils.loginCheck(request)) {
			response.sendRedirect("login.do");
		} else {
			request.getRequestDispatcher("/views/allocateDrugs.jsp").forward(request,response);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (!Utils.loginCheck(request)) {
			response.sendRedirect("login.do");
		} else {
			DrugAllocation allocation = new DrugAllocation(request);
			if (!allocation.isError()) {
				Drug drug = Drug.getDrug(allocation.getDrugId());
				String successMessage = drug.toString() + " has been successfully allocated to your area";
				request.setAttribute("successMessage", successMessage);
			} else {
				request.setAttribute("errorMessage", allocation.getErrorMessage().toString());
			}
			request.getRequestDispatcher("/views/allocateDrugs.jsp").forward(request,response);
		}
	}
}

