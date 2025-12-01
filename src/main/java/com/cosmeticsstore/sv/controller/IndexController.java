package com.cosmeticsstore.sv.controller;

import java.io.IOException;

import com.cosmeticsstore.sv.dao.ProductDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Mapea a la raíz del proyecto para cargar la página de inicio/tienda
@WebServlet(name = "IndexController", urlPatterns = {"/index.jsp", ""}) 
public class IndexController extends HttpServlet {
    
    private ProductDAO productDao = new ProductDAO();
    private final String mainLayout = "/layout/mainLayout.jsp";
    
    // Define la ruta de la vista de cuadrícula de Cards (Tu nueva vista de tienda)
    private final String storeGridPage = "/views/productViews/products-list.jsp"; 

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. CARGA DE DATOS: Obtener la lista de productos
        request.setAttribute("products", productDao.findAll());
        
        // 2. CONFIGURACIÓN DEL LAYOUT: 
        // Define el título de la página
        request.setAttribute("pageTitle", "Glow Shop | Inicio y Productos");
        
        // Define qué contenido debe ser incluido en <main> (La cuadrícula de Cards)
        request.setAttribute("pageContent", storeGridPage); 
        
        // 3. DESPACHAR LA VISTA
        // Redirige al layout principal para que construya la página completa.
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