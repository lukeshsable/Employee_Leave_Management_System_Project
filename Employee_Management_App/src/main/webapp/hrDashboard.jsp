<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="dao.UserDAO" %>
<%@ page import="dao.RequestDAO" %>
<%@ page import="model.User" %>
<%@ page import="model.LeaveRequest" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>HR Dashboard</title>
    <link rel="stylesheet" href="assets/css/hrDashboard.css">
    
</head>
<body>

    <% 
        // Retrieve session and check for logged-in HR user
        HttpSession sess = request.getSession(false);
        User user = (sess != null) ? (User) sess.getAttribute("user") : null;

        if (user == null || !"HR".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Retrieve the username from the session
        String loggedInUsername = user.getUsername();
        // Date formatters for display
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    %>
		<div class="top-bar">
		    <span>Welcome, <%= loggedInUsername %></span>
		    <form action="index.html" method="get">
		        <button type="submit" class="logout-btn">Logout</button>
		    </form>
		</div>
		

    

    <main>
        <!-- Success or error messages -->
        <% 
            String msg = request.getParameter("msg");
            String error = request.getParameter("error");
            if (msg != null) {
        %>
            <p><%= msg %></p>
        <% 
            }
            if (error != null) {
        %>
            <p><%= error %></p>
        <% 
            }
        %>

        <nav>
            <form action="addEmployee.jsp" method="get">
                <button type="submit">Add New Employee</button>
            </form>
            <form action="managePayroll.jsp" method="get">
                <button type="submit">Manage Payroll</button>
            </form>
        </nav>

        <section>
            <h2>Employee Records</h2>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Job Title</th>
                        <th>Department</th>
                        <th>Role</th>
                        <th>Phone Number</th>
                        <th>Username</th>
                        <th>Date of Birth</th>
                        <th>Gender</th>
                        <th>Employee Type</th>
                        <th>Date of Joining</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                        try {
                            UserDAO userDAO = new UserDAO();
                            List<User> employees = userDAO.getAllEmployees();
                            if (employees != null && !employees.isEmpty()) {
                                for (User emp : employees) {
                    %>
                    <tr>
                        <td><%= emp.getEmployeeId() != 0 ? emp.getEmployeeId() : "N/A" %></td>
                        <td><%= emp.getFirstName() != null ? emp.getFirstName() : "" %> <%= emp.getLastName() != null ? emp.getLastName() : "" %></td>
                        <td><%= emp.getEmail() != null ? emp.getEmail() : "N/A" %></td>
                        <td><%= emp.getJobTitle() != null ? emp.getJobTitle() : "N/A" %></td>
                        <td><%= emp.getDepartment() != null ? emp.getDepartment() : "N/A" %></td>
                        <td><%= emp.getRole() != null ? emp.getRole() : "N/A" %></td>
                        <td><%= emp.getPhoneNumber() != null ? emp.getPhoneNumber() : "N/A" %></td>
                        <td><%= emp.getUsername() != null ? emp.getUsername() : "N/A" %></td>
                        <td><%= emp.getDob() != null ? emp.getDob() : "N/A" %></td>
                        <td><%= emp.getGender() != null ? emp.getGender() : "N/A" %></td>
                        <td><%= emp.getEmployeeType() != null ? emp.getEmployeeType() : "N/A" %></td>
                        <td><%= emp.getDateOfJoining() != null ? emp.getDateOfJoining() : "N/A" %></td>
                        <td>
                            <form action="AdminActionServlet" method="post">
                                <input type="hidden" name="employeeId" value="<%= emp.getEmployeeId() %>">
                                <input type="hidden" name="username" value="<%= emp.getUsername() %>">
                                <button type="submit" name="action" value="viewEmployee">View</button>
                                <button type="submit" name="action" value="editEmployee">Edit</button>
                                <button type="submit" name="action" value="deleteEmployee" onclick="return confirm('Are you sure you want to delete this employee?');">Delete</button>
                            </form>
                        </td>
                    </tr>
                    <% 
                                }
                            } else { 
                    %>
                    <tr><td colspan="13">No employee records found.</td></tr>
                    <% 
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                    %>
                    <tr><td colspan="13">Error loading employee records: <%= e.getMessage() %></td></tr>
                    <% } %>
                </tbody>
            </table>
        </section>

        <section>
            <h2>Leave Requests</h2>
            <table>
                <thead>
                    <tr>
                        <th>Leave ID</th>
                        <th>Employee ID</th>
                        <th>Employee Name</th>
                        <th>Leave Type</th>
                        <th>Start Date</th>
                        <th>End Date</th>
                        <th>Leave Days</th>
                        <th>Status</th>
                        <th>Applied On</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                        try {
                            RequestDAO requestDAO = new RequestDAO();
                            UserDAO userDAO = new UserDAO();
                            List<LeaveRequest> leaveRequests = requestDAO.getAllLeaveRequests();
                            if (leaveRequests != null && !leaveRequests.isEmpty()) {
                                for (LeaveRequest leave : leaveRequests) {
                                    User employee = userDAO.getEmployeeById(leave.getEmployeeId());
                                    String employeeName = (employee != null) ? 
                                        (employee.getFirstName() != null ? employee.getFirstName() : "") + " " + 
                                        (employee.getLastName() != null ? employee.getLastName() : "") : "Unknown";
                    %>
				<tr> 
				    <td><%= leave.getLeaveId() %></td>
				    <td><%= leave.getEmployeeId() %></td>
				    <td><%= employeeName %></td>
				    <td><%= leave.getLeaveType() %></td>
				    <td><%= leave.getStartDate() != null ? dateFormat.format(leave.getStartDate()) : "N/A" %></td>
				    <td><%= leave.getEndDate() != null ? dateFormat.format(leave.getEndDate()) : "N/A" %></td>
				    <td><%= leave.getLeaveDays() %></td>
				    <td><%= leave.getLeaveStatus() %></td>
				    <td><%= leave.getAppliedOn() != null ? timestampFormat.format(leave.getAppliedOn()) : "N/A" %></td>
				    <td>
				        <% if ("Pending".equals(leave.getLeaveStatus())) { %>
				            <form action="ApplyLeaveServlet" method="post" style="display:inline;">
				                <input type="hidden" name="leaveId" value="<%= leave.getLeaveId() %>">
				                <button type="submit" name="action" value="approveLeave">Approve</button>
				                <button type="submit" name="action" value="rejectLeave" onclick="return confirm('Are you sure you want to reject this leave request?');">Reject</button>
				            </form>
				        <% } else { %>
				            <%= leave.getLeaveStatus() %>
				        <% } %>
				    </td>
				</tr>

                    <% 
                                }
                            } else { 
                    %>
                    <tr><td colspan="10">No leave requests found.</td></tr>
                    <% 
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                    %>
                    <tr><td colspan="10">Error loading leave requests: <%= e.getMessage() %></td></tr>
                    <% } %>
                </tbody>
            </table>
        </section>
    </main>

    <footer>
        <p>HR Dashboard - Employee Management System</p>
    </footer>
</body>
</html>