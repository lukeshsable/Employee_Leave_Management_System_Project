<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Employee Details</title>
    <link rel="stylesheet" href="assets/css/viewEmployee.css">
</head>
<body>
    <div class="container">
        <h1>Employee Details</h1>

        <c:if test="${not empty employee}">
            <!-- Main form for update -->
            <form action="AdminActionServlet" method="post">
                <input type="hidden" name="id" value="${employee.employeeId}" />
                <input type="hidden" name="action" value="updateEmployee" />  <!-- The action to trigger the update -->

                <table class="employee-details">
                    <tr>
                        <th>Employee ID</th>
                        <td>${employee.employeeId}</td>
                    </tr>
                    <tr>
                        <th>First Name</th>
                        <td>
                            <c:choose>
                                <c:when test="${editMode}">
                                    <input type="text" name="firstName" value="${employee.firstName}" required />
                                </c:when>
                                <c:otherwise>
                                    ${employee.firstName}
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    <tr>
                        <th>Last Name</th>
                        <td>
                            <c:choose>
                                <c:when test="${editMode}">
                                    <input type="text" name="lastName" value="${employee.lastName}" required />
                                </c:when>
                                <c:otherwise>
                                    ${employee.lastName}
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    <tr>
                        <th>Email</th>
                        <td>
                            <c:choose>
                                <c:when test="${editMode}">
                                    <input type="email" name="email" value="${employee.email}" required />
                                </c:when>
                                <c:otherwise>
                                    ${employee.email}
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    <tr>
                        <th>Job Title</th>
                        <td>
                            <c:choose>
                                <c:when test="${editMode}">
                                    <input type="text" name="jobTitle" value="${employee.jobTitle}" required />
                                </c:when>
                                <c:otherwise>
                                    ${employee.jobTitle}
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    <tr>
                        <th>Department</th>
                        <td>
                            <c:choose>
                                <c:when test="${editMode}">
                                    <input type="text" name="department" value="${employee.department}" required />
                                </c:when>
                                <c:otherwise>
                                    ${employee.department}
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    <tr>
                        <th>Role</th>
                        <td>
                            <c:choose>
                                <c:when test="${editMode}">
                                    <input type="text" name="role" value="${employee.role}" required />
                                </c:when>
                                <c:otherwise>
                                    ${employee.role}
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    <tr>
                        <th>Phone Number</th>
                        <td>
                            <c:choose>
                                <c:when test="${editMode}">
                                    <input type="text" name="phoneNumber" value="${employee.phoneNumber}" required />
                                </c:when>
                                <c:otherwise>
                                    ${employee.phoneNumber}
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    <!-- Newly added fields -->
                    <tr>
                        <th>Date of Birth</th>
                        <td>
                            <c:choose>
                                <c:when test="${editMode}">
                                    <input type="text" name="dob" value="${employee.dob}" required />
                                </c:when>
                                <c:otherwise>
                                    ${employee.dob}
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    <tr>
                        <th>Gender</th>
                        <td>
                            <c:choose>
                                <c:when test="${editMode}">
                                    <input type="text" name="gender" value="${employee.gender}" required />
                                </c:when>
                                <c:otherwise>
                                    ${employee.gender}
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    <tr>
                        <th>Employee Type</th>
                        <td>
                            <c:choose>
                                <c:when test="${editMode}">
                                    <input type="text" name="employeeType" value="${employee.employeeType}" required />
                                </c:when>
                                <c:otherwise>
                                    ${employee.employeeType}
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    <tr>
                        <th>Date of Joining</th>
                        <td>
                            <c:choose>
                                <c:when test="${editMode}">
                                    <input type="text" name="dateOfJoining" value="${employee.dateOfJoining}" required />
                                </c:when>
                                <c:otherwise>
                                    ${employee.dateOfJoining}
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </table>

                <div class="actions">
                    <c:if test="${editMode}">
                        <button type="submit">Update</button>
                    </c:if>
                </div>
            </form>

            <!-- Back to Dashboard Button -->
            <div class="actions">
                <form action="hrDashboard.jsp" method="get">
                    <button type="submit">Back to Dashboard</button>
                </form>
            </div>
        </c:if>

        <c:if test="${empty employee}">
            <p class="error">Employee not found.</p>
            <div class="actions">
                <form action="hrDashboard.jsp" method="get">
                    <button type="submit">Back to Dashboard</button>
                </form>
            </div>
        </c:if>

        <c:if test="${not empty message}">
            <p class="success">${message}</p>
        </c:if>
    </div>
</body>
</html>
