<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Tienda de Productos - Glow Shop</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body class="d-flex flex-column min-vh-100">
    
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="index.jsp">Glow Shop</a>
            <div class="collapse navbar-collapse">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item"><a class="nav-link active" href="products?action=list">Products</a></li>
                    <li class="nav-item"><a class="nav-link" href="categories?action=list">Categories</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <main class="container mt-4 flex-grow-1">
        
        <h3 class="mb-4">🛍️ Nuestros Productos</h3>

        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-4">
            
            <c:forEach var="item" items="${products}">
                <div class="col">
                    <div class="card h-100 shadow-sm border-0">
                        
                        <img src="${pageContext.request.contextPath}${item.path}" 
                             class="card-img-top" alt="${item.name}" 
                             style="height: 250px; object-fit: cover;">
                        
                        <div class="card-body d-flex flex-column">
    
                            <h5 class="card-title text-truncate mb-1">
                                <strong>${item.name}</strong>
                            </h5>
                            
                            <p class="text-muted small mb-2">
                                <i class="fas fa-tag"></i> ${item.category.name}
                            </p>
                            
                            <p class="card-text text-truncate-3" style="max-height: 4.5em; overflow: hidden;">
                                ${item.description}
                            </p>
                            
                            <div class="d-flex justify-content-between align-items-center mt-auto pt-2 border-top">
                                <h4 class="text-success mb-0 fw-bold">
                                    $<fmt:formatNumber value="${item.price}" pattern="#,##0.00"/>
                                </h4>
                                <span class="badge ${item.stock > 0 ? 'bg-primary' : 'bg-danger'}">
                                    ${item.stock > 0 ? 'En Stock: ' : 'Agotado'} ${item.stock}
                                </span>
                            </div>
                            
                        </div>
                        
                        <div class="card-footer bg-white border-top-0 d-grid gap-2">
                            <a href="cart?action=add&productId=${item.productId}" 
                               class="btn btn-warning ${item.stock > 0 ? '' : 'disabled'}"
                               title="${item.stock > 0 ? 'Agregar al carrito' : 'Producto agotado'}">
                                <i class="fas fa-shopping-cart"></i> Comprar
                            </a>
                            
                            <a href="products?action=view&productId=${item.productId}" 
                               class="btn btn-outline-secondary btn-sm">
                               Ver Detalles
                            </a>
                        </div>
                    </div>
                </div>
            </c:forEach>
            
            <c:if test="${empty products}">
                <div class="col-12">
                    <div class="alert alert-info text-center" role="alert">
                        <i class="fas fa-info-circle"></i> No hay productos listados para mostrar.
                    </div>
                </div>
            </c:if>
        </div>
        
    </main>

    <footer class="bg-dark text-white text-center p-3 mt-4">
        Glow Shop © 2025 - Todos los derechos reservados
    </footer>

    <div id="loadingSpinner" class="spinner-overlay" style="display: none;">
        <div class="spinner"></div>
    </div>
    
    <style>
        /* ... Estilos del spinner ... */
        .spinner-overlay {
            position: fixed;
            top: 0; left: 0;
            width: 100%; height: 100%;
            background-color: rgba(255, 255, 255, 0.7);
            display: flex;
            justify-content: center;
            align-items: center;
            z-index: 9999;
        }
        .spinner {
            border: 6px solid #f3f3f3;
            border-top: 6px solid #3498db;
            border-radius: 50%;
            width: 60px;
            height: 60px;
            animation: spin 1s linear infinite;
        }
        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }
    </style>

    <script>
        // ... Lógica de SweetAlert y Spinner ...
        var errorMessage = '${ErrorMessage}';
        var message = '${message}';

        if (errorMessage && errorMessage !== '') {
            Swal.fire({
                title: 'Error',
                text: errorMessage,
                icon: 'error'
            });
        }

        if (message && message !== '') {
            Swal.fire({
                title: 'Éxito',
                text: message,
                icon: 'success'
            });
        }
        
        document.addEventListener("DOMContentLoaded", function() {
            document.getElementById("loadingSpinner").style.display = "none";
        });

        window.addEventListener("beforeunload", function() {
            setTimeout(()=>{
                document.getElementById("loadingSpinner").style.display = "flex";
            },500);
        });
    </script>

</body>
<c:remove var="message" scope="session"/>
<c:remove var="ErrorMessage" scope="session"/>
</html>