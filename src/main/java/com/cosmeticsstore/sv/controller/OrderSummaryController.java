package com.cosmeticsstore.sv.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cosmeticsstore.sv.model.CartItem;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet; // Necesario para cargar el .jasper
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@WebServlet(name = "OrderSummaryController", urlPatterns = {"/orderSummary"})
public class OrderSummaryController extends HttpServlet {
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
            BigDecimal totalBd = BigDecimal.valueOf(total);
            generateOrderPdf(request, response, shoppingCart, totalBd);
            session.removeAttribute("shoppingCart");
        } else {
            request.setAttribute("summaryItems", shoppingCart);
            request.setAttribute("orderTotal", total);
            request.getRequestDispatcher("/views/orderViews/orderSummary.jsp").forward(request, response);
        }
    }

    private void generateOrderPdf(HttpServletRequest request, HttpServletResponse response, List<CartItem> cartItems, BigDecimal total) throws IOException {
        
        String invoiceIdParam = request.getParameter("invoiceId");
        Integer invoiceId = null;

        try {
            if (invoiceIdParam == null || invoiceIdParam.isEmpty()) {
                throw new NumberFormatException("El parámetro invoiceId es obligatorio.");
            }
            invoiceId = Integer.parseInt(invoiceIdParam);
            
            String compiledReportPath = getServletContext().getRealPath("/WEB-INF/classes/reports/order_summary.jasper");
            System.out.println("Ruta del reporte compilado: " + compiledReportPath);

            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(compiledReportPath);
            
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(cartItems);
            
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("REPORT_TOTAL", total);
            parameters.put("REPORT_INVOICE_ID", invoiceId);
            parameters.put("REPORT_TITLE", "Glow Shop - Reporte de Orden");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"reporte_orden_" + invoiceId + ".pdf\"");
            
            ServletOutputStream out = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, out);
            out.flush();
            out.close();
            
            request.getSession().removeAttribute("lastInvoiceId");

        } catch (NumberFormatException e) {
            System.err.println("Error de formato de ID: " + e.getMessage());
            request.getSession().setAttribute("ErrorMessage", "ID de factura inválido o faltante.");
            response.sendRedirect("index.jsp"); // Redirige en caso de error de URL
            
        } catch (JRException e) {
            System.err.println("Error de JasperReports: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al generar el PDF del reporte.");
        }
    }
}
