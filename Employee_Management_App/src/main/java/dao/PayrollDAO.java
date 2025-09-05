package dao;

import model.Payroll;
import java.sql.*;
import java.util.*;

public class PayrollDAO {
    private final String jdbcURL = "jdbc:mysql://localhost:3306/EmployeeLeaveManagement_db";
    private final String jdbcUsername = "root";
    private final String jdbcPassword = "Archer@123";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
    }

    public boolean addPayroll(Payroll payroll) {
        String sql = "INSERT INTO payroll (employee_id, salary, bank_account, tax_deductions, payment_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, payroll.getEmployeeId());
            ps.setDouble(2, payroll.getSalary());
            ps.setString(3, payroll.getBankAccount());
            ps.setDouble(4, payroll.getTaxDeductions());
            ps.setDate(5, new java.sql.Date(payroll.getPaymentDate().getTime()));
            System.out.println("PayrollDAO.addPayroll: Executing SQL: " + sql + 
                              ", Parameters: employeeId=" + payroll.getEmployeeId() + 
                              ", salary=" + payroll.getSalary() + 
                              ", bankAccount=" + payroll.getBankAccount() + 
                              ", taxDeductions=" + payroll.getTaxDeductions() + 
                              ", paymentDate=" + payroll.getPaymentDate());
            int rowsAffected = ps.executeUpdate();
            System.out.println("PayrollDAO.addPayroll: Inserted " + rowsAffected + " row(s)");
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("SQL Error in addPayroll: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Payroll getPayrollById(int payrollId) {
        String sql = "SELECT payroll_id, employee_id, salary, bank_account, tax_deductions, net_salary, payment_date FROM payroll WHERE payroll_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, payrollId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Payroll payroll = new Payroll();
                    payroll.setPayrollId(rs.getInt("payroll_id"));
                    payroll.setEmployeeId(rs.getInt("employee_id"));
                    payroll.setSalary(rs.getDouble("salary"));
                    payroll.setBankAccount(rs.getString("bank_account"));
                    payroll.setTaxDeductions(rs.getDouble("tax_deductions"));
                    payroll.setNetSalary(rs.getDouble("net_salary"));
                    payroll.setPaymentDate(rs.getDate("payment_date"));
                    return payroll;
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in getPayrollById: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    public Payroll getPayrollByEmployeeId(int employeeId) {
        String sql = "SELECT payroll_id, employee_id, salary, bank_account, tax_deductions, net_salary, payment_date FROM payroll WHERE employee_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Payroll payroll = new Payroll();
                    payroll.setPayrollId(rs.getInt("payroll_id"));
                    payroll.setEmployeeId(rs.getInt("employee_id"));
                    payroll.setSalary(rs.getDouble("salary"));
                    payroll.setBankAccount(rs.getString("bank_account"));
                    payroll.setTaxDeductions(rs.getDouble("tax_deductions"));
                    payroll.setNetSalary(rs.getDouble("net_salary"));
                    payroll.setPaymentDate(rs.getDate("payment_date"));
                    return payroll;
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in getPayrollByEmployeeId: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    

    public boolean updatePayroll(Payroll payroll) {
        String sql = "UPDATE payroll SET employee_id = ?, salary = ?, bank_account = ?, tax_deductions = ?, payment_date = ? WHERE payroll_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, payroll.getEmployeeId());
            ps.setDouble(2, payroll.getSalary());
            ps.setString(3, payroll.getBankAccount());
            ps.setDouble(4, payroll.getTaxDeductions());
            ps.setDate(5, new java.sql.Date(payroll.getPaymentDate().getTime()));
            ps.setInt(6, payroll.getPayrollId());
            System.out.println("PayrollDAO.updatePayroll: Executing SQL: " + sql + 
                              ", Parameters: employeeId=" + payroll.getEmployeeId() + 
                              ", salary=" + payroll.getSalary() + 
                              ", bankAccount=" + payroll.getBankAccount() + 
                              ", taxDeductions=" + payroll.getTaxDeductions() + 
                              ", paymentDate=" + payroll.getPaymentDate() + 
                              ", payrollId=" + payroll.getPayrollId());
            int rowsAffected = ps.executeUpdate();
            System.out.println("PayrollDAO.updatePayroll: Updated " + rowsAffected + " row(s)");
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("SQL Error in updatePayroll: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePayroll(int payrollId) {
        String sql = "DELETE FROM payroll WHERE payroll_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, payrollId);
            System.out.println("PayrollDAO.deletePayroll: Executing SQL: " + sql + 
                              ", Parameter: payrollId=" + payrollId);
            int rowsAffected = ps.executeUpdate();
            System.out.println("PayrollDAO.deletePayroll: Deleted " + rowsAffected + " row(s)");
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("SQL Error in deletePayroll: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    

    public List<Payroll> getAllPayrollRecords() {
        List<Payroll> payrolls = new ArrayList<>();
        String sql = "SELECT payroll_id, employee_id, salary, bank_account, tax_deductions, net_salary, payment_date FROM payroll";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Payroll payroll = new Payroll();
                payroll.setPayrollId(rs.getInt("payroll_id"));
                payroll.setEmployeeId(rs.getInt("employee_id"));
                payroll.setSalary(rs.getDouble("salary"));
                payroll.setBankAccount(rs.getString("bank_account"));
                payroll.setTaxDeductions(rs.getDouble("tax_deductions"));
                payroll.setNetSalary(rs.getDouble("net_salary"));
                payroll.setPaymentDate(rs.getDate("payment_date"));
                payrolls.add(payroll);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in getAllPayrollRecords: " + e.getMessage());
            e.printStackTrace();
        }
        return payrolls;
    }
}