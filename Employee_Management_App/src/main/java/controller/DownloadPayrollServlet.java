package controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import dao.PayrollDAO;
import dao.UserDAO;
import model.Payroll;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/DownloadPayrollServlet")
public class DownloadPayrollServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int employeeId = Integer.parseInt(request.getParameter("employeeId"));
            UserDAO userDAO = new UserDAO();
            PayrollDAO payrollDAO = new PayrollDAO();
            
            User user = userDAO.getEmployeeById(employeeId);
            Payroll payroll = payrollDAO.getPayrollByEmployeeId(employeeId);

            if (user == null || payroll == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Employee or payroll details not found");
                return;
            }

            // Set response headers
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=payroll_" + employeeId + "_" + System.currentTimeMillis() + ".pdf");

            // Create PDF
            Document document = new Document();
            OutputStream out = response.getOutputStream();
            PdfWriter.getInstance(document, out);
            document.open();

            // Date formatter for paymentDate
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            // Add header content
            document.add(new Paragraph("Company Name: XYZ Corporation"));
            document.add(new Paragraph("Payroll Statement"));
            document.add(new Paragraph("Date: " + dateFormat.format(new Date())));
            document.add(new Paragraph("\n"));
            
            // Add employee details
            document.add(new Paragraph("Employee: " + user.getFirstName() + " " + user.getLastName()));
            document.add(new Paragraph("Employee ID: " + employeeId));
            document.add(new Paragraph("Department: " + (user.getDepartment() != null ? user.getDepartment() : "N/A")));
            document.add(new Paragraph("\n"));

            // Add payroll details
            document.add(new Paragraph("Payroll Details"));
            document.add(new Paragraph("Payroll ID: " + payroll.getPayrollId()));
            document.add(new Paragraph("Salary: $" + String.format("%.2f", payroll.getSalary())));
            document.add(new Paragraph("Bank Account: " + (payroll.getBankAccount() != null ? payroll.getBankAccount() : "N/A")));
            document.add(new Paragraph("Tax Deductions: $" + String.format("%.2f", payroll.getTaxDeductions())));
            document.add(new Paragraph("Net Salary: $" + String.format("%.2f", payroll.getNetSalary())));
            document.add(new Paragraph("Payment Date: " + (payroll.getPaymentDate() != null ? dateFormat.format(payroll.getPaymentDate()) : "N/A")));

            document.close();
            out.close();
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid employee ID");
        } catch (Exception e) {
            throw new ServletException("Error generating PDF: " + e.getMessage(), e);
        }
    }
}