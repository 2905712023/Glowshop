<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head lang="es">
    <meta charset="UTF-8">
    <title>${pageTitle}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" xintegrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/2.3.5/css/dataTables.dataTables.min.css">
    <script type="text/javascript" src="https://cdn.datatables.net/2.3.5/js/dataTables.min.js"></script>
    <link rel="stylesheet" href="https://cdn.datatables.net/responsive/3.0.2/css/responsive.bootstrap5.css">
    <script src="https://cdn.datatables.net/responsive/3.0.2/js/dataTables.responsive.js"></script>
    <script src="https://cdn.datatables.net/responsive/3.0.2/js/responsive.bootstrap5.js"></script>
</head>
    <body class="d-flex flex-column min-vh-100">
        <!-- NAVBAR -->
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <div class="container-fluid">
                <a class="navbar-brand" href="index.jsp">Glow Shop</a>
                <button class="navbar-toggler" 
                        type="button" 
                        data-bs-toggle="collapse" 
                        data-bs-target="#navbarNavContent" 
                        aria-controls="navbarNavContent" 
                        aria-expanded="false" 
                        aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarNavContent">
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                        <li class="nav-item"><a class="nav-link" href="products?action=list">Products</a></li>
                        <li class="nav-item"><a class="nav-link" href="categories?action=list">Categories</a></li>
                    </ul>
                </div>
            </div>
        </nav>

        <!-- CONTENIDO VARIABLE -->
        <main class="container mt-4">
            <jsp:include page="${pageContent}" flush="true" />
        </main>

        <!-- FOOTER -->
        <footer class="bg-dark text-white text-center p-3 mt-4">
            Glow Shop © 2025 - Todos los derechos reservados
        </footer>

                <!-- Spinner global -->
        <div id="loadingSpinner" class="spinner-overlay" style="display: none;">
            <div class="spinner"></div>
        </div>

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

        <script>
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
        </script>
        <script>
            document.addEventListener("DOMContentLoaded", function() {
                document.getElementById("loadingSpinner").style.display = "none";
            });

            window.addEventListener("beforeunload", function() {
                setTimeout(()=>{
                    document.getElementById("loadingSpinner").style.display = "flex";
                },500);
            });
        </script>
        <script>
            document.addEventListener('DOMContentLoaded', function() {
                const toggler = document.querySelector('.navbar-toggler');
                const collapseTarget = document.getElementById('navbarNavContent');

                if (toggler && collapseTarget) {
                    toggler.addEventListener('click', function() {
                        const isExpanded = toggler.getAttribute('aria-expanded') === 'true';                       
                        toggler.setAttribute('aria-expanded', !isExpanded);
                        collapseTarget.classList.toggle('show');
                    });
                }
            });
        </script>
        <c:remove var="message" scope="session"/>
        <c:remove var="ErrorMessage" scope="session"/>
    </body>
</html>