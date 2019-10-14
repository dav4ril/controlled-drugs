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
import java.sql.SQLException;
import java.util.regex.Pattern;

@WebServlet(urlPatterns = "/createUser.do")
public class CreateUserServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		if (!Utils.loginCheck(request)) {
			response.sendRedirect("login.do");
		} else {
			HttpSession session = request.getSession();
			String currentUserLevel = session.getAttribute("userLevel").toString();
			request.setAttribute("userLevel", currentUserLevel);
			request.getRequestDispatcher("/views/createUser.jsp").forward(request,response);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (!Utils.loginCheck(request)) {
			response.sendRedirect("login.do");
		} else {
			User user = new User(request);
			if (!user.isError()) {
				user.addUser();
				String successMessage = "New user with username: \"" + user.getUserName() + "\" created." +
						"<br>Please make a note of the username.";
				request.setAttribute("successMessage", successMessage);
			} else {
				request.setAttribute("errorMessage", user.getErrorMessage().toString());
			}
			request.getRequestDispatcher("/views/createUser.jsp").forward(request,response);
		}
	}
}