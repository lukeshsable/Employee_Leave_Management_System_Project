<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="dao.UserDAO" %>
<%@ page import="model.User,model.Payroll" %>

<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Edit Payroll</title>
    <link rel="stylesheet" href="assets/css/editPayroll.css">
</head>
<body>
    <div style="margin: 20px;">
        <h2>Edit Payroll</h2>

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

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String error = request.getParameter("error");
            if (error != null) {
        %>
            <p style="color: red;"><%= error %></p>
        <% 
            }
        %>

        <div style="margin-bottom: 10px;">
            <a href="managePayroll.jsp" style="margin-right: 10px;">Back to Payroll</a>
        </div>

        <form action="AdminActionServlet" method="post" style="margin-top: 20px;" onsubmit="return validateForm()">
            <input type="hidden" name="action" value="updatePayroll">
            <input type="hidden" name="payrollId" value="<%= payroll.getPayrollId() %>">
            <div style="margin-bottom: 10px;">
                <label for="employeeId">Employee: </label>
                <select name="employeeId" id="employeeId" required>
                    <option value="">Select Employee</option>
                    <% 
                        try {
                            UserDAO userDAO = new UserDAO();
                            List<User> employees = userDAO.getAllEmployees();
                            if (employees != null && !employees.isEmpty()) {
                                for (User emp : employees) {
                                    String employeeName = (emp.getFirstName() != null ? emp.getFirstName() : "") + " " +
                                                         (emp.getLastName() != null ? emp.getLastName() : "");
                                    String selected = (emp.getEmployeeId() == payroll.getEmployeeId()) ? "selected" : "";
                    %>
                    <option value="<%= emp.getEmployeeId() %>" <%= selected %>><%= emp.getEmployeeId() %> - <%= employeeName %></option>
                    <% 
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                    %>
                    <option value="">Error loading employees</option>
                    <% } %>
                </select>
            </div>
            <div style="margin-bottom: 10px;">
                <label for="salary">Salary: </label>
                <input type="number" step="0.01" min="0" name="salary" id="salary" value="<%= String.format("%.2f", payroll.getSalary()) %>" required>
            </div>
            <div style="margin-bottom: 10px;">
                <label for="bankAccount">Bank Account: </label>
                <input type="text" name="bankAccount" id="bankAccount" maxlength="50" value="<%= payroll.getBankAccount() != null ? payroll.getBankAccount() : "" %>" required pattern="[0-9]{10,50}">
            </div>
            <div style="margin-bottom: 10px;">
                <label for="taxDeductions">Tax Deductions: </label>
                <input type="number" step="0.01" min="0" name="taxDeductions" id="taxDeductions" value="<%= String.format("%.2f", payroll.getTaxDeductions()) %>" required>
            </div>
            <div style="margin-bottom: 10px;">
                <label for="netSalary">Net Salary: </label>
                <input type="number" step="0.01" min="0" name="netSalary" id="netSalary" value="<%= String.format("%.2f", payroll.getNetSalary()) %>" required readonly>
            </div>
            <div style="margin-bottom: 10px;">
                <label for="paymentDate">Payment Date: </label>
                <input type="date" name="paymentDate" id="paymentDate" value="<%= payroll.getPaymentDate() != null ? dateFormat.format(payroll.getPaymentDate()) : "" %>" required>
            </div>
            <button type="submit">Update Payroll</button>
        </form>

        <script>
            document.getElementById('salary').addEventListener('input', updateNetSalary);
            document.getElementById('taxDeductions').addEventListener('input', updateNetSalary);
            document.getElementById('bankAccount').addEventListener('input', validateBankAccount);

            function updateNetSalary() {
                let salary = parseFloat(document.getElementById('salary').value) || 0;
                let taxDeductions = parseFloat(document.getElementById('taxDeductions').value) || 0;
                let netSalary = salary - taxDeductions;
                document.getElementById('netSalary').value = netSalary >= 0 ? netSalary.toFixed(2) : '';
                document.querySelector('button[type="submit"]').disabled = netSalary < 0;
            }

            function validateBankAccount() {
                let bankAccount = document.getElementById('bankAccount').value;
                let valid = /^[0-9]{10,50}$/.test(bankAccount);
                document.getElementById('bankAccount').setCustomValidity(valid ? '' : 'Bank account must be 10-50 digits');
            }

            function validateForm() {
                let salary = parseFloat(document.getElementById('salary').value) || 0;
                let taxDeductions = parseFloat(document.getElementById('taxDeductions').value) || 0;
                if (taxDeductions > salary) {
                    alert('Tax deductions cannot exceed salary.');
                    return false;
                }
                return true;
            }
        </script>
    </div>
</body>
</html>