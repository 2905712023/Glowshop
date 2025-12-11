<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>${pageTitle}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" 
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" 
        crossorigin="anonymous"></script>

    <style>
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
</head>
<body class="d-flex flex-column min-vh-100">
    
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="index.jsp">Glowshop</a>
            
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent">
                <span class="navbar-toggler-icon"></span>
            </button>

            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">

                    <c:if test="${not empty sessionScope.user and sessionScope.user.role == 'admintotal' or sessionScope.user.role == 'admin' or sessionScope.user.role == 'employee'}">
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/products?action=list">Productos</a></li>
                    </c:if>

                    <c:if test="${not empty sessionScope.user and sessionScope.user.role == 'admintotal'}">
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/categories?action=list">Categorías</a></li>
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/invoices?action=list">Facturas</a></li>
                    </c:if>
                    
                </ul>
                
                <div class="d-flex align-items-center me-3">
                    <c:choose>
                        <c:when test="${empty sessionScope.user}">
                            <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-light btn-sm">
                                <i class="fas fa-sign-in-alt"></i> Iniciar Sesión
                            </a>
                        </c:when>

                        <c:otherwise>
                            <span class="text-white me-2 small">
                                <i class="fas fa-user-circle"></i>
                                <strong>${sessionScope.user.getName()} | ${sessionScope.user.getRole()}</strong>
                            </span>
                            <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger btn-sm" title="Cerrar Sesión">
                                <i class="fas fa-sign-out-alt"></i>
                            </a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </nav>

    <main class="container mt-4 flex-grow-1">
        <jsp:include page="${pageContent}" />
    </main>

    <footer class="bg-dark text-white text-center p-3 mt-4">
        Glowshop © 2025 - Todos los derechos reservados
    </footer>

    <div id="loadingSpinner" class="spinner-overlay" style="display: none;">
        <div class="spinner"></div>
    </div>

    <script>
        var errorMessage = '${errorMessage}';
        var message = '${message}';

        // 1. Manejo de Errores (Modal bloqueante)
        if (errorMessage && errorMessage !== '') {
            Swal.fire({
                title: 'Error',
                text: errorMessage,
                icon: 'error',
                confirmButtonColor: '#d33',
                confirmButtonText: 'Entendido'
            });
        }

        // 2. Manejo de Éxito (Toast no intrusivo)
        if (message && message !== '') {
            const Toast = Swal.mixin({
                toast: true,
                position: 'bottom-end',
                showConfirmButton: false,
                timer: 3000,
                timerProgressBar: true,
                didOpen: (toast) => {
                    toast.addEventListener('mouseenter', Swal.stopTimer)
                    toast.addEventListener('mouseleave', Swal.resumeTimer)
                }
            })

            Toast.fire({
                icon: 'success',
                title: message
            })
        }

        // Lógica del Spinner
        document.addEventListener("DOMContentLoaded", function() {
            var spinner = document.getElementById("loadingSpinner");
            if(spinner) spinner.style.display = "none";
        });

        window.addEventListener("beforeunload", function() {
            setTimeout(()=>{
                var spinner = document.getElementById("loadingSpinner");
                if(spinner) spinner.style.display = "flex";
            }, 500);
        });
    </script>

</body>
<c:remove var="message" scope="session"/>
<c:remove var="ErrorMessage" scope="session"/>
</html>
