package model;
import java.util.Date;
public class Payroll {
    private int payrollId;
    private int employeeId;
    private double salary;
    private String bankAccount;
    private double taxDeductions;
    private double netSalary;
    private Date paymentDate;
    // Getters and setters
    public int getPayrollId() { return payrollId; }
    public void setPayrollId(int payrollId) { this.payrollId = payrollId; }
    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
    public String getBankAccount() { return bankAccount; }
    public void setBankAccount(String bankAccount) { this.bankAccount = bankAccount; }
    public double getTaxDeductions() { return taxDeductions; }
    public void setTaxDeductions(double taxDeductions) { this.taxDeductions = taxDeductions; }
    public double getNetSalary() { return netSalary; }
    public void setNetSalary(double netSalary) { this.netSalary = netSalary; }
    public Date getPaymentDate() { return paymentDate; }
    public void setPaymentDate(Date paymentDate) { this.paymentDate = paymentDate; }

}
