package com.cosmeticsstore.sv.controller;

import java.io.IOException;

import com.cosmeticsstore.sv.dao.InvoiceDAO;
import com.cosmeticsstore.sv.model.Invoices;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "OrderConfirmationController", urlPatterns = {"/orderConfirmation"})
public class OrderConfirmationController extends HttpServlet {
    
    private InvoiceDAO invoiceDao = new InvoiceDAO(); // Asume que existe
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            int invoiceId = Integer.parseInt(request.getParameter("invoiceId"));
            
            Invoices invoice = invoiceDao.findByInvoiceIdWithDetails(invoiceId);
            
            if (invoice == null) {
                request.getSession().setAttribute("ErrorMessage", "Factura no encontrada.");
                response.sendRedirect("index.jsp");
                return;
            }
            
            request.setAttribute("invoiceData", invoice);
            
            request.getRequestDispatcher("/views/orderViews/confirmation.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("ErrorMessage", "ID de factura inválido.");
            response.sendRedirect("index.jsp");
        } catch (Exception e) {
            request.getSession().setAttribute("ErrorMessage", "Error al cargar la factura: " + e.getMessage());
            response.sendRedirect("index.jsp");
        }
    }
}