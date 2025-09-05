package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.User;
import security.PasswordUtil;

import org.mindrot.jbcrypt.BCrypt;

public class UserDAO {

    private final String jdbcURL = "jdbc:mysql://localhost:3306/EmployeeLeaveManagement_db";
    private final String jdbcUsername = "root";
    private final String jdbcPassword = "Archer@123";

    public UserDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
    }

    // ✅ Validate user using hashed password
    public User validateUser(String username, String password, String role) {
        String sql = "SELECT username, password_hash, role FROM employee WHERE username = ? AND role = ?";
        User user = null;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, role);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password_hash");

                    // Verify the hashed password
                    if (BCrypt.checkpw(password, storedHash)) {
                        user = new User();
                        user.setUsername(rs.getString("username"));
                        user.setRole(rs.getString("role"));
                        // Add more fields as needed
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("User validation failed", e);
        }

        return user;
    }

    // Fetch all employees from the employee table
    public List<User> getAllEmployees() {
        List<User> employees = new ArrayList<>();
        String sql = "SELECT * FROM employee";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User user = new User();
                user.setEmployeeId(rs.getInt("employee_id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setJobTitle(rs.getString("job_title"));
                user.setDepartment(rs.getString("department"));
                user.setPhoneNumber(rs.getString("phone_number"));
                user.setDob(rs.getString("dob")); 
                user.setGender(rs.getString("gender"));
                user.setEmployeeType(rs.getString("employee_type"));
                user.setDateOfJoining(rs.getString("date_of_joining")); // Changed to String based on parameter
                user.setUsername(rs.getString("username"));
                user.setRole(rs.getString("role"));
                employees.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching employee list", e);
        }

        return employees;
    }

    // Fetch an employee's basic info using employee ID
    public User getEmployeeById(int employeeId) {
        String sql = "SELECT * FROM employee WHERE employee_id = ?";
        User user = null;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, employeeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setEmployeeId(rs.getInt("employee_id"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setEmail(rs.getString("email"));
                    user.setDepartment(rs.getString("department"));
                    user.setPhoneNumber(rs.getString("phone_number"));
                    user.setDob(rs.getString("dob"));
                    user.setGender(rs.getString("gender"));
                    user.setJobTitle(rs.getString("job_title"));
                    user.setEmployeeType(rs.getString("employee_type"));
                    user.setDateOfJoining(rs.getString("date_of_joining"));
                    user.setUsername(rs.getString("username"));
                    user.setRole(rs.getString("role"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching employee by ID", e);
        }

        return user;
    }

    // Add a new employee
    public boolean addEmployee(User user) throws SQLException {
        String sql = "INSERT INTO employee (first_name, last_name, email, password_hash, job_title, department, phone_number, dob, gender, employee_type, date_of_joining, username, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            
            // Use the utility class to hash the password
            String hashedPassword = PasswordUtil.hashPassword(user.getPassword());

            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setString(4, hashedPassword);
            ps.setString(5, user.getJobTitle());
            ps.setString(6, user.getDepartment());
            ps.setString(7, user.getPhoneNumber());
            ps.setString(8, user.getDob());
            ps.setString(9, user.getGender());
            ps.setString(10, user.getEmployeeType());
            ps.setString(11, user.getDateOfJoining());
            ps.setString(12, user.getUsername());
            ps.setString(13, user.getRole());

            return ps.executeUpdate() > 0;
        }
    }

    // Update employee details
    public boolean updateEmployee(User user) throws SQLException {
        String sql = "UPDATE employee SET first_name = ?, last_name = ?, email = ?, job_title = ?, department = ?, phone_number = ?, dob = ?, gender = ?, employee_type = ?, date_of_joining = ?, username = ?, role = ? WHERE employee_id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getJobTitle());
            ps.setString(5, user.getDepartment());
            ps.setString(6, user.getPhoneNumber());
            ps.setString(7, user.getDob());
            ps.setString(8, user.getGender());
            ps.setString(9, user.getEmployeeType());
            ps.setString(10, user.getDateOfJoining());
            ps.setString(11, user.getUsername());
            ps.setString(12, user.getRole());
            ps.setInt(13, user.getEmployeeId());   

            return ps.executeUpdate() > 0;
        }
    }

    // Delete employee by ID
    public boolean deleteEmployeeById(int employeeId) throws SQLException {
        String sql = "DELETE FROM employee WHERE employee_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            return ps.executeUpdate() > 0;
        }
    }
    
    public int getLeaveBalance(int employeeId) {
        int balance = 0;
        String query = "SELECT leave_balance FROM  employee WHERE employee_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

        	ps.setInt(1, employeeId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                balance = rs.getInt("leave_balance");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return balance;
    }
    
    public int getEmployeeIdByUsername(String username) {
        String sql = "SELECT employee_id FROM employee WHERE username = ?";
        int employeeId = -1; // Default value in case not found

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
            	if (rs.next()) {
            	    employeeId = rs.getInt("employee_id");
            	    System.out.println("Found employee ID: " + employeeId); // Debugging log
            	} else {
            	    System.out.println("No employee found for username: " + username); // Debugging log
            	}

            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving employee ID by username", e);
        }

        return employeeId;
    }    

  }




