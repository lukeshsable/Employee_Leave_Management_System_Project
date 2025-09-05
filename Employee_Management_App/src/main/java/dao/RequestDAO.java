package dao;

import model.LeaveRequest;
import model.Payroll;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RequestDAO {

    private final String jdbcURL = "jdbc:mysql://localhost:3306/EmployeeLeaveManagement_db";
    private final String jdbcUsername = "root";
    private final String jdbcPassword = "Archer@123";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
    }

    public boolean applyLeave(LeaveRequest leave) {
        String sql = "INSERT INTO leaverequests (employee_id, leave_type, start_date, end_date, leave_days, leave_status, applied_on) VALUES (?, ?, ?, ?, ?, ?, NOW())";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Calculate leave days using java.sql.Date
            long diff = leave.getEndDate().getTime() - leave.getStartDate().getTime();
            int days = (int) (diff / (1000 * 60 * 60 * 24)) + 1;

            ps.setInt(1, leave.getEmployeeId());
            ps.setString(2, leave.getLeaveType());
            ps.setDate(3, leave.getStartDate());
            ps.setDate(4, leave.getEndDate());
            ps.setInt(5, days);
            ps.setString(6, leave.getLeaveStatus());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateLeaveStatus(int leaveId, String status, int approvedBy, LocalDateTime approvalDate) {
        String sql = "UPDATE leaverequests SET leave_status = ?, approved_by = ?, approval_date = ? WHERE leave_id = ? AND leave_status = 'Pending'";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status); // Maps to leaveStatus (String)
            ps.setInt(2, approvedBy); // Maps to approvedBy (int)
            ps.setTimestamp(3, java.sql.Timestamp.valueOf(approvalDate)); // Maps to approvalDate (Timestamp)
            ps.setInt(4, leaveId); // Maps to leaveId (int)
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<LeaveRequest> getAllLeaveRequests() {
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        String sql = "SELECT leave_id, employee_id, leave_type, start_date, end_date, leave_days, leave_status, applied_on FROM leaverequests ORDER BY applied_on DESC";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                LeaveRequest leave = new LeaveRequest();
                leave.setLeaveId(rs.getInt("leave_id"));
                leave.setEmployeeId(rs.getInt("employee_id"));
                leave.setLeaveType(rs.getString("leave_type"));
                leave.setStartDate(rs.getDate("start_date"));
                leave.setEndDate(rs.getDate("end_date"));
                leave.setLeaveDays(rs.getInt("leave_days"));
                leave.setLeaveStatus(rs.getString("leave_status"));
                leave.setAppliedOn(rs.getTimestamp("applied_on"));
                leaveRequests.add(leave);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return leaveRequests;
    }
    
}