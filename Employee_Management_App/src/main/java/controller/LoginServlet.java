package controller;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.UserDAO;
import model.User;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        // Initialize DAO to interact with database
        UserDAO userDAO = new UserDAO();

        try {
            // Validate user credentials
            User user = userDAO.validateUser(username, password, role);

            // Check if user is valid
            if (user != null) {
                // Create session and store user details
                HttpSession session = request.getSession();
                session.setAttribute("user", user);

                // Redirect to role-based dashboard
                switch (role.toLowerCase()) {
                    case "hr":
                        response.sendRedirect("hrDashboard.jsp");
                        break;
                    case "manager":
                    	System.out.println("i am in login servlet");
                    	response.sendRedirect("managerDashboard.jsp");
                        break;
                    case "employee":
                        response.sendRedirect("employeeDashboard.jsp");
                        break;
                    default:
                        request.setAttribute("error", "Invalid role selected!");
                        RequestDispatcher rd = request.getRequestDispatcher("login.html");
                        rd.forward(request, response);
                        break;
                }
            } else {
                // Invalid username, password, or role
                request.setAttribute("error", "Invalid username, password, or role!");
                RequestDispatcher rd = request.getRequestDispatcher("login.html");
                rd.forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred during login.");
            RequestDispatcher rd = request.getRequestDispatcher("login.html");
            rd.forward(request, response);
        }
    }
}
