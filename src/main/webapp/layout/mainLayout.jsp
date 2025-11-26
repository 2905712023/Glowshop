<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${pageTitle}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
    <body class="d-flex flex-column min-vh-100">
        <!-- NAVBAR -->
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <div class="container-fluid">
                <a class="navbar-brand" href="index.jsp">Glow Shop</a>
                <div class="collapse navbar-collapse">
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                        <li class="nav-item"><a class="nav-link" href="products?action=list">Products</a></li>
                        <li class="nav-item"><a class="nav-link" href="categories?action=list">Categories</a></li>
                    </ul>
                </div>
            </div>
        </nav>

        <!-- CONTENIDO VARIABLE -->
        <main class="container mt-4">
            <jsp:include page="${pageContent}" />
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

    </body>
    <c:remove var="message" scope="session"/>
    <c:remove var="ErrorMessage" scope="session"/>
</html>