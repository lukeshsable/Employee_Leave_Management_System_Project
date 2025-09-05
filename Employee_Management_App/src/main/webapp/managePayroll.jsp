<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, dao.UserDAO, dao.PayrollDAO,model.User,model.Payroll, java.text.SimpleDateFormat" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Manage Payroll</title>
  <link rel="stylesheet" href="assets/css/managePayroll.css">
</head>
<body>
  <div class="page-wrapper">
    <h2>Manage Payroll</h2>

    <%
      HttpSession sess = request.getSession(false);
      User user = (sess != null) ? (User) sess.getAttribute("user") : null;
      if (user == null || !"HR".equalsIgnoreCase(user.getRole())) {
        response.sendRedirect("login.jsp");
        return;
      }
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    %>

    <c:if test="${not empty param.msg}">
      <p class="message success">${param.msg}</p>
    </c:if>
    <c:if test="${not empty param.error}">
      <p class="message error">${param.error}</p>
    </c:if>

    <div class="page-actions">
      <a href="hrDashboard.jsp" class="btn-back">Back to HR Dashboard</a>
      <form action="addPayroll.jsp" method="get" class="inline-form">
        <button type="submit" class="btn-primary">Add New Payroll</button>
      </form>
    </div>

    <h3>Payroll Records</h3>
    <table class="data-table">
      <thead>
        <tr>
          <th>Payroll ID</th>
          <th>Employee ID</th>
          <th>Employee Name</th>
          <th>Salary</th>
          <th>Bank Account</th>
          <th>Tax Deductions</th>
          <th>Net Salary</th>
          <th>Payment Date</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <%
          try {
            PayrollDAO payrollDAO = new PayrollDAO();
            UserDAO userDAO = new UserDAO();
            List<Payroll> records = payrollDAO.getAllPayrollRecords();
            if (records != null && !records.isEmpty()) {
              for (Payroll p : records) {
                User emp = userDAO.getEmployeeById(p.getEmployeeId());
                String name = emp != null
                  ? (emp.getFirstName()!=null?emp.getFirstName():"") + " " + (emp.getLastName()!=null?emp.getLastName():"")
                  : "Unknown";
        %>
        <tr>
          <td><%= p.getPayrollId() %></td>
          <td><%= p.getEmployeeId() %></td>
          <td><%= name %></td>
          <td><%= p.getSalary()!=0 ? String.format("%.2f",p.getSalary()) : "N/A" %></td>
          <td><%= p.getBankAccount()!=null ? p.getBankAccount() : "N/A" %></td>
          <td><%= p.getTaxDeductions()!=0 ? String.format("%.2f",p.getTaxDeductions()) : "N/A" %></td>
          <td><%= p.getNetSalary()!=0 ? String.format("%.2f",p.getNetSalary()) : "N/A" %></td>
          <td><%= p.getPaymentDate()!=null ? dateFormat.format(p.getPaymentDate()) : "N/A" %></td>
          <td class="actions-cell">
            <form action="AdminActionServlet" method="post" class="inline-form">
              <input type="hidden" name="payrollId" value="<%= p.getPayrollId() %>"/>
              <button type="submit" name="action" value="viewPayroll" class="btn-action">View</button>
              <button type="submit" name="action" value="editPayroll" class="btn-action">Edit</button>
              <button type="submit" name="action" value="deletePayroll" class="btn-delete"
                      onclick="return confirm('Are you sure you want to delete this payroll record?');">
                Delete
              </button>
            </form>
          </td>
        </tr>
        <%
              }
            } else {
        %>
        <tr><td colspan="9">No payroll records found.</td></tr>
        <%
            }
          } catch (Exception e) {
        %>
        <tr><td colspan="9">Error loading payroll records: <%= e.getMessage() %></td></tr>
        <%
          }
        %>
      </tbody>
    </table>
  </div>
</body>
</html>
