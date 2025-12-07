package com.cosmeticsstore.sv.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.cosmeticsstore.sv.dao.ProductDAO;
import com.cosmeticsstore.sv.model.CartItem;
import com.cosmeticsstore.sv.model.Products;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "CartController", urlPatterns = {"/cart"})
public class CartController extends HttpServlet {
    
    private final ProductDAO productDao = new ProductDAO(); 
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        if (action == null || action.isEmpty()) {
            response.sendRedirect("index.jsp"); 
            return;
        }
        
        HttpSession session = request.getSession();
        
        List<CartItem> shoppingCart = (List<CartItem>) session.getAttribute("shoppingCart");
        
        if (shoppingCart == null) {
            // Si la lista no existe, la inicializa y la guarda
            shoppingCart = new ArrayList<>();
            session.setAttribute("shoppingCart", shoppingCart);
        }

        switch (action) {
            case "add":
                addItem(request, shoppingCart);
                break;
            case "remove":
                removeItem(request, shoppingCart);
                break;
            case "clear":
                removeAllItems(request, shoppingCart);
                break;
            default:
                break;
        }
        
        // Redirige siempre de vuelta a la página principal para actualizar la vista
        response.sendRedirect("index.jsp"); 
    }
    
    private void addItem(HttpServletRequest request, List<CartItem> cart) {
        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            
            Products producto = productDao.FindById(productId);

            if (producto != null) {
                boolean found = false;
                
                for (CartItem item : cart) {
                    if (item.getProducto().getProductId().equals(producto.getProductId())) {
                        item.setCantidad(item.getCantidad() + 1);
                        found = true;
                        break;
                    }
                }
                
                if (!found) {
                    cart.add(new CartItem(producto, 1));
                }
                
                request.getSession().setAttribute("message", producto.getName() + " agregado.");

            } else {
                 request.getSession().setAttribute("ErrorMessage", "Producto no encontrado.");
            }
            
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("ErrorMessage", "Error al procesar el ID del producto.");
        }
    }
    
    private void removeItem(HttpServletRequest request, List<CartItem> cart) {
        try {
            int product = Integer.parseInt(request.getParameter("productId"));
            
            boolean removed = cart.removeIf(item -> 
                item.getProducto().getProductId().equals(product)
            );
            
            if (removed) {
                request.getSession().setAttribute("message", "Producto eliminado del carrito.");
            } else {
                request.getSession().setAttribute("ErrorMessage", "El producto no se encontró en el carrito.");
            }

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("ErrorMessage", "Error al procesar el ID del producto.");
        }
    }

    private void removeAllItems(HttpServletRequest request, List<CartItem> cart) {
        try {
            cart.clear();
            request.getSession().setAttribute("message", "Carrito vaciado correctamente.");

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("ErrorMessage", "Error al vaciar el carrito.");
        }
    }
}
