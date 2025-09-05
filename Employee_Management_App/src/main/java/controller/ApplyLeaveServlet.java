package controller;

import dao.UserDAO;
import dao.RequestDAO;
import model.LeaveRequest;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDateTime;

@WebServlet("/ApplyLeaveServlet")
public class ApplyLeaveServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            String action = request.getParameter("action");

            if ("approveLeave".equals(action) || "rejectLeave".equals(action)) {
                // Admin is trying to approve/reject a leave
                int leaveId = Integer.parseInt(request.getParameter("leaveId"));
                String status = "approveLeave".equals(action) ? "Approved" : "Rejected";

                UserDAO userDAO = new UserDAO();
                int approvedBy = userDAO.getEmployeeIdByUsername(user.getUsername()); // Admin's employee ID
                LocalDateTime approvalDate = LocalDateTime.now(); // Current date and time

                RequestDAO dao = new RequestDAO();
                boolean success = dao.updateLeaveStatus(leaveId, status, approvedBy, approvalDate);

                if (success) {
                    request.setAttribute("message", "Leave request " + status.toLowerCase() + " successfully.");
                } else {
                    request.setAttribute("error", "Failed to " + status.toLowerCase() + " leave request.");
                }

                // After approve/reject, forward to admin dashboard or leave list page
                request.getRequestDispatcher("hrDashboard.jsp").forward(request, response);

            } else {
                // Employee is applying for leave
                String startDateStr = request.getParameter("startDate");
                String endDateStr = request.getParameter("endDate");
                String leaveType = request.getParameter("leaveType");

                UserDAO userDAO = new UserDAO();
                LeaveRequest leave = new LeaveRequest();
                int employeeId = userDAO.getEmployeeIdByUsername(user.getUsername());
                leave.setEmployeeId(employeeId);
                leave.setLeaveType(leaveType);
                leave.setStartDate(Date.valueOf(startDateStr));
                leave.setEndDate(Date.valueOf(endDateStr));
                leave.setLeaveStatus("Pending");

                RequestDAO dao = new RequestDAO();
                boolean success = dao.applyLeave(leave);

                if (success) {
                    request.setAttribute("message", "Leave applied successfully.");
                } else {
                    request.setAttribute("error", "Failed to apply for leave.");
                }

                request.getRequestDispatcher("employeeDashboard.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
