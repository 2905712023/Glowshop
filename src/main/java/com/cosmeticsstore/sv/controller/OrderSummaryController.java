package com.cosmeticsstore.sv.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cosmeticsstore.sv.dao.InvoiceDAO;
import com.cosmeticsstore.sv.model.CartItem;
import com.cosmeticsstore.sv.model.ErrorHandlerUtil;
import com.cosmeticsstore.sv.model.Invoices;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@WebServlet(name = "OrderSummaryController", urlPatterns = {"/orderSummary"})
public class OrderSummaryController extends HttpServlet {

    private final InvoiceDAO invoiceDao = new InvoiceDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        String invoiceIdParam = request.getParameter("invoiceId");
        
        if ("view".equals(action) && invoiceIdParam != null && !invoiceIdParam.isEmpty()) {
            generateOrderPdf(request, response);
            return;
        }

        HttpSession session = request.getSession();
        List<CartItem> shoppingCart = (List<CartItem>) session.getAttribute("shoppingCart");

        if (shoppingCart == null || shoppingCart.isEmpty()) {
            session.setAttribute("ErrorMessage", "El carrito está vacío, agrega productos primero.");
            response.sendRedirect("index.jsp");
            return;
        }

        double total = 0.0;
        for (CartItem item : shoppingCart) {
            total += item.getSubtotal();
        }

        request.setAttribute("summaryItems", shoppingCart);
        request.setAttribute("orderTotal", total);
        request.getRequestDispatcher("/views/orderViews/orderSummary.jsp").forward(request, response);
    }

    private void generateOrderPdf(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String invoiceIdParam = request.getParameter("invoiceId");
        Integer invoiceId = null;

        try {
            if (invoiceIdParam == null || invoiceIdParam.isEmpty()) throw new NumberFormatException();
            invoiceId = Integer.parseInt(invoiceIdParam);
            
            Invoices invoiceData = invoiceDao.findByInvoiceIdWithDetails(invoiceId);
            
            if (invoiceData == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Factura no encontrada");
                return;
            }

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(invoiceData.getInvoiceDetailsCollection());

            String reportPath = getServletContext().getRealPath("/WEB-INF/classes/order_summary.jrxml");
            
            JasperReport jasperReport = JasperCompileManager.compileReport(reportPath);
            
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("REPORT_TOTAL", invoiceData.getTotal());
            parameters.put("REPORT_INVOICE_ID", invoiceId);
            parameters.put("REPORT_INVOICE_CUSTOMER", invoiceData.getCustomer());

            SimpleDateFormat formater = new SimpleDateFormat("dd-MM-yyyy");
            parameters.put("REPORT_INVOICE_DATE", formater.format(invoiceData.getDate()));

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=\"Factura_Glowshop_" + invoiceId + ".pdf\"");
            
            ServletOutputStream out = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, out);
            out.flush();
            out.close();
            
        } catch (Exception e) {
            ErrorHandlerUtil.sendError(e, response);
        }
    }
}