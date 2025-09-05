<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="model.User" %>
<%@ page import="dao.UserDAO" %>
<%@ page import="dao.PayrollDAO" %>
<%@ page import="model.Payroll" %>
<%@ page import="java.text.SimpleDateFormat" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Employee Dashboard</title>
	<link rel="stylesheet" href="assets/css/employeeDashboard.css">
</head>
<body>
<a href="index.html" class="back-home-btn">Logout</a>


<%
    HttpSession sess = request.getSession(false);
    User sessionUser = (sess != null) ? (User) sess.getAttribute("user") : null;

    if (sessionUser == null || !"Employee".equalsIgnoreCase(sessionUser.getRole())) {
        response.sendRedirect("login.jsp");
        return;
    }

    UserDAO userDAO = new UserDAO();
    PayrollDAO payrollDAO = new PayrollDAO();
    
    // Get employeeId using username from session
    int employeeId = userDAO.getEmployeeIdByUsername(sessionUser.getUsername());
    
    // Use employeeId to get full user details
    User fullUser = userDAO.getEmployeeById(employeeId);
    
    // Get payroll details by employeeId
    Payroll payroll = payrollDAO.getPayrollByEmployeeId(employeeId);
    
    // Date formatter for paymentDate
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
%>

<h2>Welcome, <%= fullUser.getFirstName() %> <%= fullUser.getLastName() %></h2>

<h3>Your Details</h3>
<table>
    <tr><th>Username</th><td><%= fullUser.getUsername() != null ? fullUser.getUsername() : "N/A" %></td></tr>
    <tr><th>Email</th><td><%= fullUser.getEmail() != null ? fullUser.getEmail() : "N/A" %></td></tr>
    <tr><th>Job Title</th><td><%= fullUser.getJobTitle() != null ? fullUser.getJobTitle() : "N/A" %></td></tr>
    <tr><th>Department</th><td><%= fullUser.getDepartment() != null ? fullUser.getDepartment() : "N/A" %></td></tr>
    <tr><th>Phone Number</th><td><%= fullUser.getPhoneNumber() != null ? fullUser.getPhoneNumber() : "N/A" %></td></tr>
    <tr><th>Date of Birth</th><td><%= fullUser.getDob() != null ? fullUser.getDob() : "N/A" %></td></tr>
    <tr><th>Gender</th><td><%= fullUser.getGender() != null ? fullUser.getGender() : "N/A" %></td></tr>
    <tr><th>Employee Type</th><td><%= fullUser.getEmployeeType() != null ? fullUser.getEmployeeType() : "N/A" %></td></tr>
    <tr><th>Date of Joining</th><td><%= fullUser.getDateOfJoining() != null ? fullUser.getDateOfJoining() : "N/A" %></td></tr>
</table>

<h3>Your Payroll Details</h3>
<% if (payroll == null) { %>
    <p style="color: red;">Payroll details not available</p>
<% } else { %>
    <table>
        <tr><th>Salary</th><td><%= String.format("$%.2f", payroll.getSalary()) %></td></tr>
        <tr><th>Bank Account</th><td><%= payroll.getBankAccount() != null ? payroll.getBankAccount() : "N/A" %></td></tr>
        <tr><th>Tax Deductions</th><td><%= String.format("$%.2f", payroll.getTaxDeductions()) %></td></tr>
        <tr><th>Net Salary</th><td><%= String.format("$%.2f", payroll.getNetSalary()) %></td></tr>
        <tr><th>Payment Date</th><td><%= payroll.getPaymentDate() != null ? dateFormat.format(payroll.getPaymentDate()) : "N/A" %></td></tr>
    </table>
<% } %>

<form action="DownloadPayrollServlet" method="get">
    <input type="hidden" name="employeeId" value="<%= employeeId %>">
    <input type="submit" value="Download Payroll as PDF" class="download-btn" <%= payroll == null ? "disabled" : "" %>>
</form>

<h3>Apply for Leave</h3>
<form action="ApplyLeaveServlet" method="post">
    <label>Leave Type:</label>
    <select name="leaveType" required>
        <option value="Sick Leave">Sick Leave</option>
        <option value="Casual Leave">Casual Leave</option>
        <option value="Annual Leave">Annual Leave</option>
        <option value="Maternity Leave">Maternity Leave</option>
        <option value="Unpaid Leave">Unpaid Leave</option>
    </select><br><br>

    <label>Start Date:</label>
    <input type="date" name="startDate" required><br><br>
    
    <label>End Date:</label>
    <input type="date" name="endDate" required><br><br>

    <input type="submit" value="Apply Leave">
</form>
<% if (request.getAttribute("message") != null) { %>
    <p style="color: green;"><%= request.getAttribute("message") %></p>
<% } %>
<% if (request.getAttribute("error") != null) { %>
    <p style="color: red;"><%= request.getAttribute("error") %></p>
<% } %>

<h3>Your Leave Balance</h3>
<%
    int leaveBalance = 0;
    try {
        leaveBalance = userDAO.getLeaveBalance(employeeId);
    } catch (Exception e) {
        out.println("<p style='color:red;'>Error retrieving leave balance: " + e.getMessage() + "</p>");
    }
%>
<p>Available Leaves: <%= leaveBalance %></p>

</body>
</html>