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
    
    private ProductDAO productDao;
    private final String mainLayout = "/layout/mainLayout.jsp";
    private final String storeGridPage = "/views/productViews/products-list.jsp"; 

    @Override
    public void init() throws ServletException {
        try {
            productDao = new ProductDAO(); 
        } catch (Throwable e) {
            e.printStackTrace(); 
            throw new ServletException(e);
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setAttribute("products", productDao.findAll());
        request.setAttribute("pageTitle", "Glowshop | Inicio y Productos");
        request.setAttribute("pageContent", storeGridPage); 
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