package com.cosmeticsstore.sv.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.cosmeticsstore.sv.dao.InvoiceDAO;
import com.cosmeticsstore.sv.dao.InvoiceDetailsDAO;
import com.cosmeticsstore.sv.model.CartItem;
import com.cosmeticsstore.sv.model.InvoiceDetails;
import com.cosmeticsstore.sv.model.Invoices;
import com.cosmeticsstore.sv.model.Users;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "CheckoutProcessController", urlPatterns = {"/checkoutProcess"})
public class CheckoutProcessController extends HttpServlet {

    private final InvoiceDAO invoiceDao = new InvoiceDAO();
    private final InvoiceDetailsDAO detailsDao = new InvoiceDetailsDAO();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        List<CartItem> shoppingCart = (List<CartItem>) session.getAttribute("shoppingCart");
        String customerInput = request.getParameter("customerName");
        Integer generatedInvoiceId = null;

        if (shoppingCart == null || shoppingCart.isEmpty()) {
            session.setAttribute("ErrorMessage", "No se puede confirmar la compra, el carrito está vacío.");
            response.sendRedirect("index.jsp");
            return;
        }

        try {            
            Users currentUser = (Users) session.getAttribute("user");
            
            if (currentUser == null) {
                currentUser = new Users();
                currentUser.setUserId(1); 
            }

            BigDecimal orderTotal = BigDecimal.ZERO;
            for (CartItem item : shoppingCart) {
                orderTotal = orderTotal.add(BigDecimal.valueOf(item.getSubtotal()));
            }

            Invoices newInvoice = new Invoices();
            newInvoice.setDate(new Date()); 
            newInvoice.setTotal(orderTotal);
            newInvoice.setUserId(currentUser);
            
            if (customerInput != null && !customerInput.trim().isEmpty()) {
                newInvoice.setCustomer(customerInput.trim());
            }

            Invoices savedInvoice = invoiceDao.createInvoice(newInvoice);
            generatedInvoiceId = savedInvoice.getInvoiceId();
            session.setAttribute("lastInvoiceId", generatedInvoiceId);

            for (CartItem item : shoppingCart) {
                InvoiceDetails detail = new InvoiceDetails();
                detail.setInvoiceId(savedInvoice);                   
                detail.setProductId(item.getProducto());             
                detail.setQuantity(item.getCantidad());              
                detail.setUnitPrice(item.getProducto().getPrice());  

                detailsDao.createInvoiceDetail(detail); 
            }
            
            session.setAttribute("message", "¡Gracias Por Su Compra! Factura #" + generatedInvoiceId + " procesada.");
            
        } catch (Exception e) {
            System.err.println("Error procesando checkout: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("ErrorMessage", "Error al procesar la compra: " + e.getMessage());
        }
        
        if (generatedInvoiceId != null) {
            response.sendRedirect("orderConfirmation?invoiceId=" + generatedInvoiceId); 
        } else {
            response.sendRedirect("index.jsp");
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Acceso solo permitido vía POST.");
    }
}