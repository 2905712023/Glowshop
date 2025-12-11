package com.cosmeticsstore.sv.controller;

import java.io.IOException;

import com.cosmeticsstore.sv.dao.ProductDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "IndexController", urlPatterns = {"/index.jsp", ""}) 
public class IndexController extends HttpServlet {
    
    private ProductDAO productDao = new ProductDAO();
    private final String storePage = "/views/store.jsp";
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setAttribute("products", productDao.findAll());
        request.setAttribute("pageTitle", "Glowshop | Inicio y Productos");
                
        request.getRequestDispatcher(storePage).forward(request, response);
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