package com.cosmeticsstore.sv.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Scanner;

import com.cosmeticsstore.sv.dao.CategoryDao;
import com.cosmeticsstore.sv.dao.ProductDAO;
import com.cosmeticsstore.sv.model.Categories;
import com.cosmeticsstore.sv.model.Products;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet(name = "ProductsServlet", urlPatterns = {"/products"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
    maxFileSize = 1024 * 1024 * 1,      // 1 MB
    maxRequestSize = 1024 * 1024 * 5    // 5 MB
)
public class ProductController extends HttpServlet {
    private ProductDAO productDao = new ProductDAO();
    private final String mainLayout = "/layout/mainLayout.jsp";
    private final String listProductPage = "/views/productViews/list.jsp";
    private final String formProductPage = "/views/productViews/formProduct.jsp";
    private static final String UPLOAD_DIR_RELATIVE = "img" + File.separator + "products";// Carpeta donde se guardarán las imágenes
    private static final String EXTERNAL_ROOT = "C:\\Glowshop_Archivos\\productos";
    private static final String DEFAULT_IMAGE = "default.jpg"; // Imagen por defecto si es necesario
  
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
        
        Products productToDelete = productDao.FindById(productId);
        String imagePath = (productToDelete != null) ? productToDelete.getPath() : null;

        boolean result = productDao.Delete(productId);
        
        if (result == true) {
            if (imagePath != null) {
                deleteFile(imagePath); 
            }
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
        String urlBase = "http://localhost:8080/Glowshop";
        String imageUrl = null;
        if(product != null){
            imageUrl = urlBase + product.getPath();
        }
        CategoryDao categoryDao = new CategoryDao();
        
        if (product != null) {
            request.setAttribute("product", product);
            request.setAttribute("imageUrl", imageUrl);
            request.setAttribute("categoryId", product.getCategoryId());
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
            Part filePart = request.getPart("image");
            String idParam = getParameterValue(request, "productId");
            String name = getParameterValue(request, "name");
            String description = getParameterValue(request, "description");
            String priceString = getParameterValue(request, "price");
            String stockString = getParameterValue(request, "stock");
            String categoryIdString = getParameterValue(request, "categoryId");

            // Validaciones básicas
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
            String currentPath = null; // Para la edición

            if (idParam != null && !idParam.trim().isEmpty()) {
                // Es una ACTUALIZACIÓN
            
            System.out.println("Id del product " + idParam);
            
            if (idParam == null || idParam.trim().isEmpty()) {
                // Crear nuevo producto
                System.out.println("creando");
                result = productDao.Create(product);
                request.setAttribute("message", "Producto creado correctamente.");
            } else {
                System.out.println("actualizando producto");
                int productId = Integer.parseInt(idParam);
                product.setProductId(productId);
                
                // 1. Obtener el producto existente para no perder la ruta de la imagen actual
                Products existingProduct = productDao.FindById(productId);
                if(existingProduct != null) {
                    currentPath = existingProduct.getPath();
                    product.setPath(currentPath); // Mantener la ruta actual por defecto
                }
            }
        
        if (filePart != null && filePart.getSize() > 0) {
            String newPath = uploadFile(request, filePart);
            
            if (newPath != null) {
                // 3. Eliminar la imagen antigua si existe y es una actualización
                if (currentPath != null && !currentPath.equals(DEFAULT_IMAGE)) {
                    deleteFile(currentPath); 
                }
                product.setPath(newPath); // Asignar la nueva ruta
            }
        } else if (product.getProductId() == null || product.getProductId() == 0) {
            product.setPath(null);
        }
        
        if (idParam == null || idParam.trim().isEmpty()) {
            // Crear nuevo producto
            result = productDao.Create(product);
            request.setAttribute("message", "Producto creado correctamente.");
        } else {
            // Actualizar producto
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
            System.out.println("Producto guardado correctamente.");
            request.getSession().setAttribute("message", "Producto guardado correctamente.");
            response.sendRedirect("products?action=list");
        } else {
            System.out.println("ERROR AL GUARDAR : "+errorMessage);
            request.getSession().setAttribute("ErrorMessage", "ERROR AL GUARDAR : "+errorMessage);
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

    private void Create(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CategoryDao categoryDao = new CategoryDao();

        request.setAttribute("product", new Products());
        request.setAttribute("categoryId", 0);
        request.setAttribute("categories", categoryDao.findAllToShowSelect());
        request.setAttribute("pageTitle", "Create Product");
        request.setAttribute("pageContent", formProductPage);
        request.getRequestDispatcher(mainLayout).forward(request, response);
    }

    /**
     * Guarda el archivo en el directorio de subidas y retorna la ruta relativa.
     * @param applicationPath La ruta absoluta del servidor a la carpeta 'webapps'.
     * @param part El objeto Part que contiene el archivo.
     * @return La ruta relativa del archivo guardado (ej: /images/nombre_archivo.jpg)
     */
    private String uploadFile(HttpServletRequest request, Part part) {
        String fileName = part.getSubmittedFileName();
        String savePath = EXTERNAL_ROOT;

        if (fileName == null || fileName.isEmpty()) {
            return null; 
        }

        String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
        
        // Crear el directorio si no existe
        File fileSaveDir = new File(savePath);
        if (!fileSaveDir.exists()) {
            System.out.println("Creando directorio de subida: " + savePath);
            if (fileSaveDir.mkdirs()) {
                System.out.println("Directorios creados exitosamente.");
            } else {
                // Esto solo se ejecuta si falla la creación (problema de permisos o ruta inválida)
                System.err.println("¡ERROR! Fallo al crear los directorios.");
            }
        }
        
        try {
            String absoluteFilePath = savePath + File.separator + uniqueFileName;
            System.out.println("Ruta ABSOLUTA de guardado: " + absoluteFilePath);
            part.write(absoluteFilePath);
            System.out.println("archivo guardado exitosamente.");
        } catch (IOException e) {
            System.out.println("Error al guardar archivo: " + e.getMessage());
            return null; 
        } catch (Exception ex){
            System.out.println("Error al guardar archivo: " + ex.getMessage());
            return null;
        }
        
        return "/" + UPLOAD_DIR_RELATIVE.replace(File.separator, "/") + "/" + uniqueFileName;
    }

    private String getParameterValue(HttpServletRequest request, String name) throws IOException, ServletException {
        Part part = request.getPart(name);
        if (part == null) {
            return null;
        }
        try (InputStream inputStream = part.getInputStream();
            Scanner scanner = new Scanner(inputStream, "UTF-8")) {
            return scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
        }
    }

    private boolean deleteFile(String relativePath) {
        if (relativePath == null || relativePath.isEmpty()) return false;
        
        String fileName = relativePath.substring(UPLOAD_DIR_RELATIVE.length());
        String name = fileName.substring(1);
        String filePath = EXTERNAL_ROOT + name.replace("/", File.separator);
        
        File file = new File(filePath);
        System.out.println("Ruta física verificada (existencia/eliminación): " + filePath);
        
        if (file.exists() && file.isFile()) {
            System.out.println("Eliminando archivo: " + filePath);
            boolean deleted = file.delete();
            if (deleted) {
                System.out.println("Archivo eliminado exitosamente.");
            } else {
                // Esto sucede si el archivo está en uso por otro proceso (por ejemplo, Tomcat no lo liberó).
                System.err.println("¡ERROR! Fallo en file.delete(). El archivo puede estar en uso.");
            }
            return deleted;
        } else {
            System.err.println("ADVERTENCIA: Archivo no encontrado en la ruta física o no es un archivo: " + filePath);
            return false;
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
