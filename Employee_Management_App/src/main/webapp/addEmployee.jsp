<%@ page contentType="text/html; charset=UTF-8" language="java" session="true" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    HttpSession currentSession = request.getSession(false);
    if (currentSession == null || currentSession.getAttribute("user") == null || 
        !"HR".equalsIgnoreCase(((model.User) currentSession.getAttribute("user")).getRole())) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Add Employee</title>
    <link rel="stylesheet" href="assets/css/addEmployee.css"> 
</head>
<body>
<div class="container">
    <h1>Add New Employee</h1>

    <!-- Success or error messages -->
    <c:if test="${not empty message}">
        <p class="success">${message}</p>
    </c:if>
    <c:if test="${not empty error}">
        <p class="error">${error}</p>
    </c:if>

    <form action="AdminActionServlet" method="post">
        <input type="hidden" name="action" value="add" />

		<label>First Name:</label>
        <input type="text" name="firstName" required />

        <label>Last Name:</label>
        <input type="text" name="lastName" required />

        <label>Email:</label>
        <input type="email" name="email" required />
        
        <label>Password:</label>
        <input type="password" name="password" required />

        <label>Username:</label>
        <input type="text" name="username" required />

        <label>Job Title:</label>
        <input type="text" name="jobTitle" required />

        <label>Department:</label>
        <input type="text" name="department" required/> 
        
 		<label>Role:</label>
        <input type="text" name="role" required/> 

        <label>Phone Number:</label>
        <input type="text" name="phoneNumber" required />

        <label>Date of Birth:</label>
        <input type="date" name="dob" required />

        <label>Gender:</label>
        <select name="gender" required>
            <option value="">Select Gender</option>
            <option value="Male">Male</option>
            <option value="Female">Female</option>
            <option value="Other">Other</option>
        </select>

        <label>Employee Type:</label>
        <select name="employee_Type" required>
            <option value="">Select Type</option>
            <option value="Full-Time">Full-Time</option>
            <option value="Part-Time">Part-Time</option>
            <option value="Contract">Contract</option>
            <option value="Intern">Intern</option>
        </select>

        <label>Date of Joining:</label>
        <input type="date" name="date_Of_Joining" required />

        <div class="actions">
            <button type="submit">Add Employee</button>
            <a href="hrDashboard.jsp" class="btn cancel-btn">Cancel</a>
        </div>
    </form>
</div>
</body>
</html>
