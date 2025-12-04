package com.cosmeticsstore.sv.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cosmeticsstore.sv.dao.InvoiceDAO;
import com.cosmeticsstore.sv.model.CartItem;
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
import net.sf.jasperreports.engine.JasperReport; // Necesario para cargar el .jasper
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@WebServlet(name = "OrderSummaryController", urlPatterns = {"/orderSummary"})
public class OrderSummaryController extends HttpServlet {

    private final InvoiceDAO invoiceDao = new InvoiceDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        
        List<CartItem> shoppingCart = (List<CartItem>) session.getAttribute("shoppingCart");

        if (shoppingCart == null || shoppingCart.isEmpty()) {
            session.setAttribute("ErrorMessage", "No se puede ver el resumen, el carrito está vacío.");
            response.sendRedirect("index.jsp");
            return;
        }

        double total = 0.0;
        for (CartItem item : shoppingCart) {
            total += item.getSubtotal();
        }

        if ("generatePdf".equals(action)) {
            System.out.println("Generando PDF del resumen de la orden");
            generateOrderPdf(request, response);
            session.removeAttribute("shoppingCart");
        } else {
            request.setAttribute("summaryItems", shoppingCart);
            request.setAttribute("orderTotal", total);
            request.getRequestDispatcher("/views/orderViews/orderSummary.jsp").forward(request, response);
        }
    }

    private void generateOrderPdf(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
        String invoiceIdParam = request.getParameter("invoiceId");
        Integer invoiceId = null;

        try {
            if (invoiceIdParam == null || invoiceIdParam.isEmpty()) throw new NumberFormatException();
            invoiceId = Integer.parseInt(invoiceIdParam);
            
            Invoices invoiceData = invoiceDao.findByInvoiceIdWithDetails(invoiceId);
            if (invoiceData == null) {
                response.sendRedirect("index.jsp");
                return;
            }

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(invoiceData.getInvoiceDetailsCollection());

            String reportPath = getServletContext().getRealPath("/WEB-INF/classes/order_summary.jrxml");
            
            JasperReport jasperReport = JasperCompileManager.compileReport(reportPath);
            
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("REPORT_TOTAL", invoiceData.getTotal());
            parameters.put("REPORT_INVOICE_ID", invoiceId);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=\"reporte_orden_" + invoiceId + ".pdf\"");
            
            ServletOutputStream out = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, out);
            out.flush();
            out.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }
}
