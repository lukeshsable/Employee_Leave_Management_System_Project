<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Payroll" %>
<%@ page import="dao.UserDAO" %>
<%@ page import="model.User" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>View Payroll</title>
    <link rel="stylesheet" href="assets/css/viewPayroll.css">
</head>
<body>
    <div style="margin: 20px;">
        <h2>Payroll Details</h2>

        <% 
            HttpSession sess = request.getSession(false);
            User user = (sess != null) ? (User) sess.getAttribute("user") : null;
            if (user == null || !"HR".equalsIgnoreCase(user.getRole())) {
                response.sendRedirect("login.jsp");
                return;
            }

            Payroll payroll = (Payroll) request.getAttribute("payroll");
            if (payroll == null) {
        %>
            <p style="color: red;">Error: Payroll record not found.</p>
        <% 
                return;
            }

            UserDAO userDAO = new UserDAO();
            User employee = userDAO.getEmployeeById(payroll.getEmployeeId());
            String employeeName = (employee != null) ? 
                (employee.getFirstName() != null ? employee.getFirstName() : "") + " " + 
                (employee.getLastName() != null ? employee.getLastName() : "") : "Unknown";
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        %>

        <div style="margin-bottom: 10px;">
            <a href="managePayroll.jsp" style="margin-right: 10px;">Back to Payroll</a>
        </div>

        <table border="1" cellpadding="10">
            <tr><th>Payroll ID</th><td><%= payroll.getPayrollId() %></td></tr>
            <tr><th>Employee ID</th><td><%= payroll.getEmployeeId() %></td></tr>
            <tr><th>Employee Name</th><td><%= employeeName %></td></tr>
            <tr><th>Salary</th><td><%= String.format("%.2f", payroll.getSalary()) %></td></tr>
            <tr><th>Bank Account</th><td><%= payroll.getBankAccount() != null ? payroll.getBankAccount() : "N/A" %></td></tr>
            <tr><th>Tax Deductions</th><td><%= String.format("%.2f", payroll.getTaxDeductions()) %></td></tr>
            <tr><th>Net Salary</th><td><%= String.format("%.2f", payroll.getNetSalary()) %></td></tr>
            <tr><th>Payment Date</th><td><%= payroll.getPaymentDate() != null ? dateFormat.format(payroll.getPaymentDate()) : "N/A" %></td></tr>
        </table>
    </div>
</body>
</html>