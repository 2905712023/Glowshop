package com.cosmeticsstore.sv.controller;

import java.io.IOException;

import com.cosmeticsstore.sv.dao.ProductDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Mantenemos el mapeo para que controle la raíz y el index
@WebServlet(name = "IndexController", urlPatterns = {"/index.jsp", ""}) 
public class IndexController extends HttpServlet {
    
    private ProductDAO productDao = new ProductDAO();
    private final String mainLayout = "/layout/mainLayout.jsp"; 
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setAttribute("products", productDao.findAll());
        request.setAttribute("pageTitle", "Inicio - Glowshop");
        request.setAttribute("pageContent", "/views/store.jsp");

        if ("1".equals(request.getParameter("debug"))) {
            System.out.println("[DEBUG Controller] Redirigiendo a Layout: " + mainLayout);
            System.out.println("[DEBUG Controller] Contenido inyectado: /views/store.jsp");
        }

        request.getRequestDispatcher(mainLayout).forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}