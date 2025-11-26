
package com.cosmeticsstore.sv.controller;

import java.io.IOException;
import java.math.BigDecimal;

import com.cosmeticsstore.sv.dao.CategoryDao;
import com.cosmeticsstore.sv.dao.ProductDAO;
import com.cosmeticsstore.sv.model.Categories;
import com.cosmeticsstore.sv.model.Product;
import com.cosmeticsstore.sv.model.Products;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ProductsServlet", urlPatterns = {"/products"})
public class ProductController extends HttpServlet {
    private ProductDAO productDao = new ProductDAO();
    private final String mainLayout = "/layout/mainLayout.jsp";
    private final String listProductPage = "/views/productViews/list.jsp";
    private final String formProductPage = "/views/productViews/formProduct.jsp";
    
  
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
    
        String action = request.getParameter("action");
        if (action == null) {
            action = "list"; // default value
        }

        try {
            switch (action) {
                case "list":
                    List(request, response);
                    break;
                case "new":
                    Create(request, response);
                    break;
                case "save":
                    Save(request, response);
                    break;
                case "update":
                    Update(request, response);
                    break;
                case "delete":
                    Delete(request, response);
                    break;
                default:
                    throw new IllegalArgumentException("unrecognized action: " + action);
            } 
        } catch(Exception ex) {
            // capturamos cualquier excepción y la ponemos en la request
            request.setAttribute("ErrorMessage", ex.getMessage());
            // enviamos de vuelta a la vista principal
            request.setAttribute("content", listProductPage);
            request.getRequestDispatcher(mainLayout).forward(request, response);
        }
    }
    
    private void Delete(HttpServletRequest request, HttpServletResponse response) 
                throws ServletException, IOException {
        int productId = Integer.parseInt(request.getParameter("productId"));
        
        boolean result = productDao.Delete(productId);
        
        if (result == true) {
            request.getSession().setAttribute("message", "Producto con id " + productId + " eliminado!");
        } else {
            request.getSession().setAttribute("errorMessage", "No se pudo eliminar Producto");
        }
        response.sendRedirect("products?action=list");
    }
   
    private void Update(HttpServletRequest request, HttpServletResponse response) 
              throws ServletException, IOException {
        int productId = Integer.parseInt(request.getParameter("productId"));
        
        Products product = productDao.FindById(productId);
        CategoryDao categoryDao = new CategoryDao();
        
        if (product != null) {
            request.setAttribute("product", product);
            request.setAttribute("categories", categoryDao.findAllToShowSelect());
            request.setAttribute("pageTitle", "Update product");
            request.setAttribute("pageContent", formProductPage);
            request.getRequestDispatcher(mainLayout).forward(request, response);
        } else {
            request.getSession().setAttribute("error", "Product Not found");
            response.sendRedirect("products?action=list");
        }
       
    }
    
    private void Save(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        Products product = new Products();
        String errorMessage = null;
        int result = 0;

        try {
            String idParam = request.getParameter("productId");
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String priceString = request.getParameter("price");
            String stockString = request.getParameter("stock");
            String categoryIdString = request.getParameter("categoryId");

            // 🔍 Validaciones básicas
            if (name == null || name.trim().isEmpty())
                throw new IllegalArgumentException("El nombre del producto es obligatorio.");
            if (priceString == null || priceString.trim().isEmpty())
                throw new IllegalArgumentException("El precio es obligatorio.");
            if (stockString == null || stockString.trim().isEmpty())
                throw new IllegalArgumentException("El stock es obligatorio.");
            if (categoryIdString == null || categoryIdString.trim().isEmpty())
                throw new IllegalArgumentException("La categoría es obligatoria.");

            BigDecimal price = new BigDecimal(priceString);
            int stock = Integer.parseInt(stockString);
            int categoryId = Integer.parseInt(categoryIdString);

            CategoryDao categoryDao = new CategoryDao();
            Categories category = categoryDao.findById(categoryId);
            if (category == null) {
                throw new IllegalArgumentException("La categoría seleccionada no existe.");
            }

            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setStock(stock);
            product.setCategory(category);
            
            System.out.println("Id del product " + idParam);
            int productId = Integer.parseInt(idParam);
            if (productId == 0) {
                // Crear nuevo producto
                result = productDao.Create(product);
                request.setAttribute("message", "Producto creado correctamente.");
            } else {
                productId = Integer.parseInt(idParam);
                product.setProductId(productId);
                result = productDao.Update(product);
                request.setAttribute("message", "Producto actualizado correctamente.");
            }

        } catch (Exception ex) {
            errorMessage = "Error al guardar producto: " + ex.getMessage();
            System.out.println("Error al guardar producto: " + ex.getMessage());
            ex.printStackTrace();
        }

        if (errorMessage != null) {
            request.setAttribute("errorMessage", errorMessage);
            System.out.println("Error al guardar. Message: " + errorMessage);
        }

        if (result > 0) {
            System.out.println("Producto creado correctamente.");
            request.getSession().setAttribute("message", "Producto creado correctamente.");
            response.sendRedirect("products?action=list");
        } else {
            System.out.println("ERROR AL GUARDAR : "+errorMessage);
            request.getSession().setAttribute("message", "ERROR AL GUARDAR : "+errorMessage);
            request.setAttribute("product", product);
            request.setAttribute("pageContent", formProductPage);
            request.getRequestDispatcher(mainLayout).forward(request, response);
        }
    }

        
    protected void List(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        request.setAttribute("products", productDao.findAll());
        // Definir las variables del layout
        request.setAttribute("pageTitle", "Product Dashboard");
        request.setAttribute("pageContent", listProductPage);
        request.getRequestDispatcher(mainLayout).forward(request, response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void Create(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CategoryDao categoryDao = new CategoryDao();

        request.setAttribute("product", new Product());
        request.setAttribute("categories", categoryDao.findAllToShowSelect());
        request.setAttribute("pageTitle", "Create Product");
        request.setAttribute("pageContent", formProductPage);
        request.getRequestDispatcher(mainLayout).forward(request, response);
    }

  
   

    
}
