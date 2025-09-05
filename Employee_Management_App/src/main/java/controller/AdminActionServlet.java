package controller;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dao.RequestDAO;
import dao.UserDAO;
import dao.PayrollDAO;
import model.User;
import model.Payroll;

@WebServlet("/AdminActionServlet")
public class AdminActionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;
    private RequestDAO requestDAO;
    private PayrollDAO payrollDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
        requestDAO = new RequestDAO();
        payrollDAO = new PayrollDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null || 
            !"HR".equalsIgnoreCase(((User) session.getAttribute("user")).getRole())) {
            response.sendRedirect("login.jsp");
            return;
        }
        

        String action = request.getParameter("action");
        System.out.println("Action received: " + action);

        try {
            switch (action) {
                case "addPayroll": {
                    String employeeIdStr = request.getParameter("employeeId");
                    String salaryStr = request.getParameter("salary");
                    String bankAccount = request.getParameter("bankAccount");
                    String taxDeductionsStr = request.getParameter("taxDeductions");
                    String paymentDate = request.getParameter("paymentDate");

                    if (employeeIdStr == null || employeeIdStr.trim().isEmpty() ||
                        salaryStr == null || salaryStr.trim().isEmpty() ||
                        bankAccount == null || bankAccount.trim().isEmpty() ||
                        taxDeductionsStr == null || taxDeductionsStr.trim().isEmpty() ||
                        paymentDate == null || paymentDate.trim().isEmpty()) {
                        response.sendRedirect("addPayroll.jsp?error=All+fields+are+required");
                        return;
                    }

                    int employeeId;
                    double salary, taxDeductions;
                    try {
                        employeeId = Integer.parseInt(employeeIdStr);
                        salary = Double.parseDouble(salaryStr);
                        taxDeductions = Double.parseDouble(taxDeductionsStr);
                    } catch (NumberFormatException e) {
                        response.sendRedirect("addPayroll.jsp?error=Invalid+numeric+values");
                        return;
                    }

                    if (salary < 0 || taxDeductions < 0) {
                        response.sendRedirect("addPayroll.jsp?error=Salary+and+tax+deductions+cannot+be+negative");
                        return;
                    }
                    if (salary < taxDeductions) {
                        response.sendRedirect("addPayroll.jsp?error=Tax+deductions+cannot+exceed+salary");
                        return;
                    }
                    if (!bankAccount.matches("^[0-9]{10,50}$")) {
                        response.sendRedirect("addPayroll.jsp?error=Bank+account+must+be+10-50+digits");
                        return;
                    }

                    User employee = userDAO.getEmployeeById(employeeId);
                    if (employee == null) {
                        response.sendRedirect("addPayroll.jsp?error=Employee+not+found");
                        return;
                    }

                    Payroll payroll = new Payroll();
                    payroll.setEmployeeId(employeeId);
                    payroll.setSalary(salary);
                    payroll.setBankAccount(bankAccount);
                    payroll.setTaxDeductions(taxDeductions);
                    payroll.setPaymentDate(java.sql.Date.valueOf(paymentDate));

                    System.out.println("AdminActionServlet.addPayroll: employeeId=" + employeeId + 
                                       ", salary=" + salary + ", bankAccount=" + bankAccount + 
                                       ", taxDeductions=" + taxDeductions + ", paymentDate=" + paymentDate);

                    boolean success = payrollDAO.addPayroll(payroll);
                    response.sendRedirect(success ? "managePayroll.jsp?msg=Payroll+added+successfully" : "addPayroll.jsp?error=Failed+to+add+payroll");
                    break;
                }
                case "viewPayroll": {
                    String payrollIdStr = request.getParameter("payrollId");
                    int payrollId;
                    try {
                        payrollId = Integer.parseInt(payrollIdStr);
                    } catch (NumberFormatException e) {
                        response.sendRedirect("managePayroll.jsp?error=Invalid+payroll+ID");
                        return;
                    }

                    Payroll payroll = payrollDAO.getPayrollById(payrollId);
                    if (payroll == null) {
                        response.sendRedirect("managePayroll.jsp?error=Payroll+not+found");
                        return;
                    }

                    request.setAttribute("payroll", payroll);
                    request.getRequestDispatcher("viewPayroll.jsp").forward(request, response);
                    break;
                }
                case "editPayroll": {
                    String payrollIdStr = request.getParameter("payrollId");
                    int payrollId;
                    try {
                        payrollId = Integer.parseInt(payrollIdStr);
                    } catch (NumberFormatException e) {
                        response.sendRedirect("managePayroll.jsp?error=Invalid+payroll+ID");
                        return;
                    }

                    Payroll payroll = payrollDAO.getPayrollById(payrollId);
                    if (payroll == null) {
                        response.sendRedirect("managePayroll.jsp?error=Payroll+not+found");
                        return;
                    }

                    request.setAttribute("payroll", payroll);
                    request.getRequestDispatcher("editPayroll.jsp").forward(request, response);
                    break;
                }
                case "updatePayroll": {
                    String payrollIdStr = request.getParameter("payrollId");
                    String employeeIdStr = request.getParameter("employeeId");
                    String salaryStr = request.getParameter("salary");
                    String bankAccount = request.getParameter("bankAccount");
                    String taxDeductionsStr = request.getParameter("taxDeductions");
                    String paymentDate = request.getParameter("paymentDate");

                    if (payrollIdStr == null || employeeIdStr == null || employeeIdStr.trim().isEmpty() ||
                        salaryStr == null || salaryStr.trim().isEmpty() ||
                        bankAccount == null || bankAccount.trim().isEmpty() ||
                        taxDeductionsStr == null || taxDeductionsStr.trim().isEmpty() ||
                        paymentDate == null || paymentDate.trim().isEmpty()) {
                        response.sendRedirect("editPayroll.jsp?error=All+fields+are+required");
                        return;
                    }

                    int payrollId, employeeId;
                    double salary, taxDeductions;
                    try {
                        payrollId = Integer.parseInt(payrollIdStr);
                        employeeId = Integer.parseInt(employeeIdStr);
                        salary = Double.parseDouble(salaryStr);
                        taxDeductions = Double.parseDouble(taxDeductionsStr);
                    } catch (NumberFormatException e) {
                        response.sendRedirect("editPayroll.jsp?error=Invalid+numeric+values");
                        return;
                    }

                    if (salary < 0 || taxDeductions < 0) {
                        response.sendRedirect("editPayroll.jsp?error=Salary+and+tax+deductions+cannot+be+negative");
                        return;
                    }
                    if (salary < taxDeductions) {
                        response.sendRedirect("editPayroll.jsp?error=Tax+deductions+cannot+exceed+salary");
                        return;
                    }
                    if (!bankAccount.matches("^[0-9]{10,50}$")) {
                        response.sendRedirect("editPayroll.jsp?error=Bank+account+must+be+10-50+digits");
                        return;
                    }

                    User employee = userDAO.getEmployeeById(employeeId);

                    Payroll payroll = new Payroll();
                    payroll.setPayrollId(payrollId);
                    payroll.setEmployeeId(employeeId);
                    payroll.setSalary(salary);
                    payroll.setBankAccount(bankAccount);
                    payroll.setTaxDeductions(taxDeductions);
                    payroll.setPaymentDate(java.sql.Date.valueOf(paymentDate));

                    boolean success = payrollDAO.updatePayroll(payroll);
                    response.sendRedirect(success ? "managePayroll.jsp?msg=Payroll+updated+successfully" : "editPayroll.jsp?error=Failed+to+update+payroll");
                    break;
                }
                case "deletePayroll": {
                    String payrollIdStr = request.getParameter("payrollId");
                    int payrollId;
                    try {
                        payrollId = Integer.parseInt(payrollIdStr);
                    } catch (NumberFormatException e) {
                        response.sendRedirect("managePayroll.jsp?error=Invalid+payroll+ID");
                        return;
                    }

                    boolean success = payrollDAO.deletePayroll(payrollId);
                    response.sendRedirect(success ? "managePayroll.jsp?msg=Payroll+deleted+successfully" : "managePayroll.jsp?error=Failed+to+delete+payroll");
                    break;
                }
                
                case "add": {
                    // Get and validate parameters
                    String firstName = request.getParameter("firstName");
                    String lastName = request.getParameter("lastName");
                    String email = request.getParameter("email");
                    String password = request.getParameter("password");
                    String username = request.getParameter("username");
                    String jobTitle = request.getParameter("jobTitle");
                    String department = request.getParameter("department");
                    String role = request.getParameter("role");
                    String phoneNumber = request.getParameter("phoneNumber");
                    String dob = request.getParameter("dob");
                    String gender = request.getParameter("gender");
                    String employeeType = request.getParameter("employee_Type");
                    String dateOfJoining = request.getParameter("date_Of_Joining");

                    if (firstName == null || lastName == null || email == null || username == null || password == null) {
                        request.setAttribute("error", "Please fill all mandatory fields.");
                        request.getRequestDispatcher("addEmployee.jsp").forward(request, response);
                        return;
                    }

                    // Create and save employee
                    User employee = new User();
                    employee.setFirstName(firstName);
                    employee.setLastName(lastName);
                    employee.setEmail(email);
                    employee.setPassword(password);
                    employee.setUsername(username);
                    employee.setJobTitle(jobTitle);
                    employee.setDepartment(department);
                    employee.setRole(role);
                    employee.setPhoneNumber(phoneNumber);
                    employee.setDob(dob);
                    employee.setGender(gender);
                    employee.setEmployeeType(employeeType);
                    employee.setDateOfJoining(dateOfJoining);

                    boolean success = userDAO.addEmployee(employee);
                    if (success) {
                        response.sendRedirect("hrDashboard.jsp?msg=Employee+added+successfully");
                        return; // ✅ very important
                    } else {
                        request.setAttribute("error", "Failed to add employee. Please try again.");
                        request.getRequestDispatcher("addEmployee.jsp").forward(request, response);
                        return; // ✅ also important
                    }
                }


                
                case "viewEmployee": {
                    String employeeIdStr = request.getParameter("employeeId");
                    int employeeId;
                    try {
                        employeeId = Integer.parseInt(employeeIdStr);
                    } catch (NumberFormatException e) {
                        response.sendRedirect("hrDashboard.jsp?error=Invalid+Employee+ID");
                        return;
                    }

                    User employee = userDAO.getEmployeeById(employeeId);
                    if (employee == null) {
                        response.sendRedirect("hrDashboard.jsp?error=Employee+not+found");
                        return;
                    }

                    request.setAttribute("employee", employee);
                    request.getRequestDispatcher("viewEmployee.jsp").forward(request, response);
                    break;
                }
                
                case "editEmployee": {
                    String employeeIdStr = request.getParameter("employeeId");
                    int employeeId;
                    try {
                        employeeId = Integer.parseInt(employeeIdStr);
                    } catch (NumberFormatException e) {
                        response.sendRedirect("hrDashboard.jsp?error=Invalid+Employee+ID");
                        return;
                    }

                    // Fetch employee details from the database
                    User employee = userDAO.getEmployeeById(employeeId);
                    if (employee == null) {
                        response.sendRedirect("hrDashboard.jsp?error=Employee+not+found");
                        return;
                    }

                    // Set employee as a request attribute to forward to the JSP
                    request.setAttribute("employee", employee);

                    // Set editMode to true to allow the editing of employee details
                    request.setAttribute("editMode", true);

                    // Forward to the viewEmployee.jsp page to show the employee details for editing
                    request.getRequestDispatcher("viewEmployee.jsp").forward(request, response);
                    break;
                }

                case "updateEmployee": {
                    String employeeIdStr = request.getParameter("id");
                    int employeeId = Integer.parseInt(employeeIdStr);

                    String firstName = request.getParameter("firstName");
                    String lastName = request.getParameter("lastName");
                    String email = request.getParameter("email");
                    String jobTitle = request.getParameter("jobTitle");
                    String department = request.getParameter("department");
                    String role = request.getParameter("role");
                    String phoneNumber = request.getParameter("phoneNumber");
                    String dob = request.getParameter("dob");
                    String gender = request.getParameter("gender");
                    String employeeType = request.getParameter("employeeType");
                    String dateOfJoining = request.getParameter("dateOfJoining");

                    // Fetch the existing employee data (to keep the current username)
                    UserDAO userDAO = new UserDAO();
                    User currentEmployee = userDAO.getEmployeeById(employeeId);  // Assuming this method exists to fetch the employee by ID

                    if (currentEmployee == null) {
                        response.sendRedirect("hrDashboard.jsp?error=Employee+not+found");
                        return;
                    }

                    String username = currentEmployee.getUsername();  // Get the existing username (if not updated)

                    // Create the updated User object
                    User updatedEmployee = new User();
                    updatedEmployee.setEmployeeId(employeeId);
                    updatedEmployee.setFirstName(firstName);
                    updatedEmployee.setLastName(lastName);
                    updatedEmployee.setEmail(email);
                    updatedEmployee.setJobTitle(jobTitle);
                    updatedEmployee.setDepartment(department);
                    updatedEmployee.setRole(role);
                    updatedEmployee.setPhoneNumber(phoneNumber);
                    updatedEmployee.setDob(dob);
                    updatedEmployee.setGender(gender);
                    updatedEmployee.setEmployeeType(employeeType);
                    updatedEmployee.setDateOfJoining(dateOfJoining);
                    updatedEmployee.setUsername(username);  // Set the username (if not changing)

                    // Update the employee details in the database
                    boolean isUpdated = userDAO.updateEmployee(updatedEmployee);

                    if (isUpdated) {
                        // Fetch the updated employee data after the update
                        User updatedEmployeeDetails = userDAO.getEmployeeById(employeeId);

                        if (updatedEmployeeDetails != null) {
                            // Set the updated employee as a request attribute to forward to the JSP
                            request.setAttribute("employee", updatedEmployeeDetails);
                            request.setAttribute("message", "Employee details updated successfully!");
                            // Forward to the viewEmployee.jsp page to display updated employee details
                            request.getRequestDispatcher("viewEmployee.jsp").forward(request, response);
                        } else {
                            response.sendRedirect("viewEmployee.jsp?employeeId=" + employeeId + "&error=Employee+not+found+after+update");
                        }
                    } else {
                        response.sendRedirect("viewEmployee.jsp?employeeId=" + employeeId + "&error=Update+failed");
                    }
                    break;
                }
    
                case "deleteEmployee": {
                    String employeeIdStr = request.getParameter("employeeId");
                    int employeeId;
                    try {
                        employeeId = Integer.parseInt(employeeIdStr);
                    } catch (NumberFormatException e) {
                        response.sendRedirect("hrDashboard.jsp?error=Invalid+Employee+ID");
                        return;
                    }

                    boolean success = userDAO.deleteEmployeeById(employeeId);
                    response.sendRedirect(success ? "hrDashboard.jsp?msg=Employee+deleted+successfully" : "hrDashboard.jsp?error=Failed+to+delete+employee");
                    break;
                }

                default:
                    response.sendRedirect("hrDashboard.jsp?error=Invalid+action");
                    break;

            }
        } catch (Exception e) {
            System.err.println("Unexpected Error in AdminActionServlet: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("hrDashboard.jsp?error=Unexpected+error");
        }
    }
}