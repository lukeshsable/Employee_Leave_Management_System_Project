package model;

import java.sql.Timestamp;
import java.sql.Date;

public class LeaveRequest {

    private int leaveId;
    private int employeeId;
    private String leaveType;
    private Date startDate; // java.sql.Date for database compatibility
    private Date endDate;   // java.sql.Date for database compatibility
    private int leaveDays;
    private String leaveStatus; // 'Pending', 'Approved', 'Rejected', etc.
    private Timestamp appliedOn;
    private int approvedBy;
    private Timestamp approvalDate;

    // Getter and Setter for leaveId
    public int getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(int leaveId) {
        this.leaveId = leaveId;
    }

    // Getter and Setter for employeeId
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    // Getter and Setter for leaveType
    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    // Getter and Setter for startDate
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    // Getter and Setter for endDate
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    // Getter and Setter for leaveDays
    public int getLeaveDays() {
        return leaveDays;
    }

    public void setLeaveDays(int leaveDays) {
        this.leaveDays = leaveDays;
    }

    // Getter and Setter for leaveStatus
    public String getLeaveStatus() {
        return leaveStatus;
    }

    public void setLeaveStatus(String leaveStatus) {
        this.leaveStatus = leaveStatus;
    }

    // Getter and Setter for appliedOn
    public Timestamp getAppliedOn() {
        return appliedOn;
    }

    public void setAppliedOn(Timestamp appliedOn) {
        this.appliedOn = appliedOn;
    }

    // Getter and Setter for approvedBy
    public int getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(int approvedBy) {
        this.approvedBy = approvedBy;
    }

    // Getter and Setter for approvalDate
    public Timestamp getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Timestamp approvalDate) {
        this.approvalDate = approvalDate;
    }

    // Optional: toString for debugging
    @Override
    public String toString() {
        return "LeaveRequest{" +
               "leaveId=" + leaveId +
               ", employeeId=" + employeeId +
               ", leaveType='" + leaveType + '\'' +
               ", startDate=" + startDate +
               ", endDate=" + endDate +
               ", leaveDays=" + leaveDays +
               ", leaveStatus='" + leaveStatus + '\'' +
               ", appliedOn=" + appliedOn +
               ", approvedBy=" + approvedBy +
               ", approvalDate=" + approvalDate +
               '}';
    }

}