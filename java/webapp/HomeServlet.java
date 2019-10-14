package webapp;

import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

@WebServlet(urlPatterns = "/home.do")
public class HomeServlet extends HttpServlet {

	private static void login(HttpServletRequest request,
							  String userName,
							  String userLevel,
							  String wardIdStr) {
		HttpSession session = request.getSession();
		session.setAttribute("user", userName);
		session.setAttribute("userLevel", userLevel);
		session.setAttribute("wardId", wardIdStr);
		int wardId = Integer.parseInt(wardIdStr);
		Ward ward = Ward.getWard(wardId);
		session.setAttribute("wardName", ward.getName());
		System.out.println("User \"" + session.getAttribute("user")
				+ "\" has logged in with level: " + session.getAttribute("userLevel"));
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		if (!Utils.loginCheck(request)) {
			response.sendRedirect("login.do");
		} else {
			request.getRequestDispatcher("/views/home.jsp").forward(request,response);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Retrieve entered username and password
		String enteredUsername = request.getParameter("username");
		String enteredPassword = request.getParameter("password");
		String wardId = request.getParameter("wardId");
		//Get connection
		Connection conn = null;
		try {
			conn = DbOp.connect();

			String sqlString = "SELECT * FROM users WHERE name = ?";
			PreparedStatement statement = conn.prepareStatement(sqlString);
			statement.setString(1, enteredUsername);
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				String hashed = resultSet.getString("password");
				String userLevel = resultSet.getString("level");
				if (Utils.checkPassword(hashed, enteredPassword)) {
					conn.close();
					login(request, enteredUsername, userLevel, wardId);
					response.sendRedirect("home.do");
				} else {
                    String errorMessage = "Sorry, password is incorrect";
                    request.setAttribute("errorMessage", errorMessage);
                    conn.close();
                    request.getRequestDispatcher("/views/login.jsp").forward(request,response);
                }
			} else {
			    String errorMessage = "Sorry, user \"" + enteredUsername + "\" does not exist";
			    request.setAttribute("errorMessage", errorMessage);
			    conn.close();
                request.getRequestDispatcher("/views/login.jsp").forward(request,response);
            }
		} catch (Exception e) {
			System.out.println("something is wrong");
			System.out.println(e.getMessage());
		}

	}
}