package com.cosmeticsstore.sv.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.cosmeticsstore.sv.dao.InvoiceDAO;
import com.cosmeticsstore.sv.dao.InvoiceDetailsDAO;
import com.cosmeticsstore.sv.model.CartItem;
import com.cosmeticsstore.sv.model.Customers;
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

    // Inicialización de los DAOs
    private final InvoiceDAO invoiceDao = new InvoiceDAO();
    private final InvoiceDetailsDAO detailsDao = new InvoiceDetailsDAO();
    Integer generatedInvoiceId = null;
    // private final CustomerDAO customerDao = new CustomerDAO();
    // private final UserDAO userDao = new UserDAO();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        List<CartItem> shoppingCart = (List<CartItem>) session.getAttribute("shoppingCart");

        if (shoppingCart == null || shoppingCart.isEmpty()) {
            session.setAttribute("ErrorMessage", "No se puede confirmar la compra, el carrito está vacío.");
            response.sendRedirect("index.jsp");
            return;
        }

        try {
            Customers defaultCustomer = new Customers();
            defaultCustomer.setCustomerId(1);
            
            Users defaultUser = new Users();
            defaultUser.setUserId(1);
            
            if (defaultCustomer == null || defaultUser == null) {
                throw new IllegalStateException("Error de Configuración: Cliente o Usuario por defecto no encontrado.");
            }
            
            BigDecimal orderTotal = BigDecimal.ZERO;
            for (CartItem item : shoppingCart) {
                orderTotal = orderTotal.add(BigDecimal.valueOf(item.getSubtotal()));
            }

            Invoices newInvoice = new Invoices();
            newInvoice.setDate(new Date()); 
            newInvoice.setTotal(orderTotal);
            newInvoice.setCustomerId(defaultCustomer);
            newInvoice.setUserId(defaultUser);
            
            Invoices savedInvoice = invoiceDao.createInvoice(newInvoice);
            generatedInvoiceId = savedInvoice.getInvoiceId();
            session.setAttribute("lastInvoiceId", generatedInvoiceId);

            for (CartItem item : shoppingCart) {
                InvoiceDetails detail = new InvoiceDetails();
                
                detail.setInvoiceId(savedInvoice);                   
                detail.setProductId(item.getProducto());             
                detail.setQuantity(item.getCantidad());              
                detail.setUnitPrice(item.getProducto().getPrice());  
                detail.setSubtotal(BigDecimal.valueOf(item.getSubtotal()));

                detailsDao.createInvoiceDetail(detail); 
            }
            
            session.setAttribute("message", "¡Gracias Por Su Compra! Factura #" + generatedInvoiceId + " procesada.");
            
        } catch (Exception e) {
            // Si algo falla (DB, DAO, NullPointer, etc.), muestra un error
            System.err.println("Error procesando checkout: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("ErrorMessage", "Error al procesar la compra: " + e.getMessage());
        }
        
        response.sendRedirect("orderConfirmation?invoiceId=" + generatedInvoiceId); 
    }
    
    // Opcional: Proteger contra accesos GET
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Acceso solo permitido vía POST.");
    }
}