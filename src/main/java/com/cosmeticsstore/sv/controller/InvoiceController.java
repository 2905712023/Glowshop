package com.cosmeticsstore.sv.controller;

import java.io.IOException;

import com.cosmeticsstore.sv.dao.InvoiceDAO;
import com.cosmeticsstore.sv.model.ErrorHandlerUtil;
import com.cosmeticsstore.sv.model.Invoices;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "InvoiceController", urlPatterns = {"/invoices"})
public class InvoiceController extends HttpServlet {

    private final InvoiceDAO invoiceDao = new InvoiceDAO();
    private final String listInvoicePage = "/views/invoiceViews/list.jsp";
    private final String detailsInvoicePage = "/views/invoiceViews/details.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "list":
                List(request, response);
                break;
            case "details":
                viewDetails(request, response);
            break;
            case "delete": // <--- NUEVO CASO
                Delete(request, response);
            break;
            default:
                List(request, response);
                break;
        }
    }

    private void List(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("text/html;charset=UTF-8");
            request.setAttribute("invoices", invoiceDao.findAll());
            request.setAttribute("pageTitle", "Facturas");
            request.getRequestDispatcher("views/invoiceViews/list.jsp").forward(request, response);
        } catch (Exception e) {
            ErrorHandlerUtil.sendError(e, response);
        }
    }

    private void viewDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int invoiceId = Integer.parseInt(request.getParameter("id"));
            
            Invoices invoice = invoiceDao.findByInvoiceIdWithDetails(invoiceId);
            
            if (invoice != null) {
                request.setAttribute("invoice", invoice);
                request.getRequestDispatcher("views/invoiceViews/details.jsp").forward(request, response);
            } else {
                response.sendRedirect("invoices?action=list");
            }
        } catch (Exception e) {
            ErrorHandlerUtil.sendError(e, response);
        }
    }

    private void Delete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            invoiceDao.delete(id);
            
            request.getSession().setAttribute("message", "Factura #" + id + " eliminada correctamente.");
            
        } catch (Exception e) {
            ErrorHandlerUtil.sendError(e, response);
        }
        response.sendRedirect("invoices?action=list");
    }
}