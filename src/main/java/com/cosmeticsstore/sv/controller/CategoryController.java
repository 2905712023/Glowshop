package com.cosmeticsstore.sv.controller;
import java.io.IOException;
import java.math.BigDecimal;
import com.cosmeticsstore.sv.dao.CategoryDao;
import com.cosmeticsstore.sv.model.Categories;
import com.cosmeticsstore.sv.model.Products;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "CategoryController", urlPatterns = {"/categories"})
public class CategoryController extends HttpServlet {
    private CategoryDao categoryDao = new CategoryDao();
    private final String mainLayout = "/layout/mainLayout.jsp";
    private final String listCategoryPage = "/views/categoryViews/list.jsp";
    private final String formCategoryPage = "/views/categoryViews/formCategory.jsp";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
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
                case "update":
                    Update(request, response);
                    break;
                case "save":
                    Save(request, response);
                    break;
                case "delete":
                    Delete(request, response);
                    break;
                default:
                    throw new IllegalArgumentException("unrecognized action: " + action);
            } 
        } catch(Exception ex) {
            // enviamos de vuelta a la vista principal
            request.setAttribute("content", listCategoryPage);
            request.getRequestDispatcher(mainLayout).forward(request, response);
        }
    }

    protected void List(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        request.setAttribute("categories", categoryDao.findAll());
        // Definir las variables del layout
        request.setAttribute("pageTitle", "Product Dashboard");
        request.setAttribute("pageContent", listCategoryPage);
        request.getRequestDispatcher(mainLayout).forward(request, response);
    }

    private void Delete(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        String idParam = request.getParameter("categoryId");
        int result = 0;
        String message = null;
        String errorMessage = null;

        if (idParam != null && !idParam.isEmpty()) {
            int categoryId = Integer.parseInt(idParam);
            result = categoryDao.delete(categoryId);

            if (result == 1) {
                message = "Categoría eliminada correctamente.";
                request.getSession().setAttribute("message", message);
            } else if (result == -1) {
                errorMessage = "No se puede eliminar la categoría porque tiene productos asociados.";
                request.getSession().setAttribute("ErrorMessage", errorMessage);
            } else {
                errorMessage = "No se encontró la categoría o ocurrió un error.";
            }
        } else {
            errorMessage = "ID de categoría inválido.";
        }

        response.sendRedirect("categories?action=list");
    }

    private void Create(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Categories category = new Categories();

        request.setAttribute("category", category);
        request.setAttribute("pageTitle", "Create Category");
        request.setAttribute("pageContent", formCategoryPage);
        request.getRequestDispatcher(mainLayout).forward(request, response);
    }

    private void Save(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        Categories category = new Categories();
        String errorMessage = null;
        int result = 0;
        CategoryDao categoryDao = new CategoryDao();

        try {
            String idParam = request.getParameter("categoryId");
            String name = request.getParameter("name");
            String description = request.getParameter("description");

            // Validaciones básicas
            if (name == null || name.trim().isEmpty())
                throw new IllegalArgumentException("El nombre de la Categoría es obligatorio.");
            if (description == null || description.trim().isEmpty())
                throw new IllegalArgumentException("La description de la Categoría es obligatoria.");

            category.setName(name);
            category.setDescription(description);
            
            System.out.println("Id de categoría" + idParam);
            System.out.println("Id de categoría n" + name);
            System.out.println("Id de categoría d" + description);
            if (idParam == null || idParam.trim().isEmpty()) {
                result = categoryDao.create(category);
                request.setAttribute("message", "Categoría creada correctamente.");
            } else {
                System.out.println("actualizando Categoría");
                result = categoryDao.update(category);
                request.setAttribute("message", "Categoría actualizada correctamente.");
            }

        } catch (Exception ex) {
            errorMessage = "Error al guardar Categoría: " + ex.getMessage();
            System.out.println("Error al guardar Categoría: " + ex.getMessage());
            ex.printStackTrace();
        }
        if (errorMessage != null) {
            request.setAttribute("errorMessage", errorMessage);
            System.out.println("Error al guardar. Message: " + errorMessage);
        }
        if (result > 0) {
            System.out.println("Categoría guardada correctamente.");
            request.getSession().setAttribute("message", "Categoría guardada correctamente.");
            response.sendRedirect("categories?action=list");
        } else {
            System.out.println("ERROR AL GUARDAR : "+errorMessage);
            request.getSession().setAttribute("ErrorMessage", "ERROR AL GUARDAR : "+errorMessage);
            request.setAttribute("category", category);
            request.setAttribute("pageContent", formCategoryPage);
            request.getRequestDispatcher(mainLayout).forward(request, response);
        }
    }

    private void Update(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        
        Categories category = categoryDao.findById(categoryId);
        System.out.println("category "+ category);
        if (category != null) {
            request.setAttribute("category", category);
            request.setAttribute("pageTitle", "Update category");
            request.setAttribute("pageContent", formCategoryPage);
            request.getRequestDispatcher(mainLayout).forward(request, response);
        } else {
            request.setAttribute("ErrorMessage", "Category Not found");
            response.sendRedirect("categories?action=list");
        }
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

}
