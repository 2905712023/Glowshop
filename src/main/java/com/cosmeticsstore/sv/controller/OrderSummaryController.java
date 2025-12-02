package com.cosmeticsstore.sv.controller;

import java.io.IOException;
import java.util.List;

import com.cosmeticsstore.sv.model.CartItem;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "OrderSummaryController", urlPatterns = {"/orderSummary"})
public class OrderSummaryController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        List<CartItem> cart = (List<CartItem>) session.getAttribute("shoppingCart");

        if (cart == null || cart.isEmpty()) {
            session.setAttribute("ErrorMessage", "No se puede ver el resumen, el carrito está vacío.");
            response.sendRedirect("index.jsp");
            return;
        }

        double total = 0.0;
        for (CartItem item : cart) {
            total += item.getSubtotal();
        }

        request.setAttribute("summaryItems", cart);
        request.setAttribute("orderTotal", total);

        request.getRequestDispatcher("/views/orderViews/orderSummary.jsp").forward(request, response);
    }
}
