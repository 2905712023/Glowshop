<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Historial de Facturas - Glowshop</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body class="d-flex flex-column min-vh-100">

    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="index.jsp">Glowshop</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarContent">
                <span class="navbar-toggler-icon"></span>
            </button>
            
            <div class="collapse navbar-collapse" id="navbarContent">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item"><a class="nav-link" href="products?action=list">Productos</a></li>
                    <li class="nav-item"><a class="nav-link" href="categories?action=list">Categorías</a></li>
                    <li class="nav-item"><a class="nav-link active fw-bold" href="invoices?action=list">Facturas</a></li>
                </ul>
            </div>
            
            <a href="index.jsp" class="btn btn-outline-warning ms-2">
                <i class="fas fa-store"></i> Ir a la Tienda
            </a>
        </div>
    </nav>

    <main class="flex-grow-1 mt-5">
        <div class="container">
            
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2 class="mb-0">📂 Listado de Facturas</h2>
                <span class="badge bg-secondary fs-6">
                    ${invoices.size()} Registros
                </span>
            </div>
            
            <hr class="mb-4">

            <div class="table-responsive shadow-sm">
                <table class="table table-bordered table-striped table-hover mb-0 align-middle">
                    <thead class="bg-dark text-white">
                        <tr>
                            <th class="py-3">ID Factura</th>
                            <th class="py-3">Fecha</th>
                            <th class="py-3">Cliente</th>
                            <th class="py-3">Usuario (Vendedor)</th>
                            <th class="py-3 text-end">Total</th>
                            <th class="py-3 text-center" style="width: 320px;">Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${invoices}" var="inv">
                            <tr>
                                <td class="fw-bold">#${inv.invoiceId}</td>
                                <td>
                                    <fmt:formatDate value="${inv.date}" pattern="dd/MM/yyyy HH:mm"/>
                                </td>
                                
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty inv.customer}">
                                            <i class="fas fa-user-tag text-muted small"></i> ${inv.customer}
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-muted fst-italic">Clientes Varios</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>

                                <td>
                                    <c:if test="${not empty inv.userId}">
                                         ${inv.userId.getName()}
                                    </c:if>
                                </td>
                                
                                <td class="text-end fw-bold text-success fs-5">
                                    $<fmt:formatNumber value="${inv.total}" pattern="#,##0.00"/>
                                </td>
                                <td class="text-center">
                                    <div class="d-flex justify-content-center gap-2">
                                        
                                        <a href="invoices?action=details&id=${inv.invoiceId}" 
                                            class="btn btn-dark btn-sm d-flex align-items-center gap-1">
                                            <i class="fas fa-eye"></i> Detalles
                                        </a>

                                        <a href="orderSummary?action=view&invoiceId=${inv.invoiceId}" 
                                           target="_blank"
                                           class="btn btn-warning btn-sm d-flex align-items-center gap-1">
                                            <i class="fas fa-file-pdf"></i> PDF
                                        </a>

                                        <a href="invoices?action=delete&id=${inv.invoiceId}" 
                                           class="btn btn-danger btn-sm btn-delete d-flex align-items-center gap-1"
                                           data-id="${inv.invoiceId}">
                                            <i class="fas fa-trash"></i> Borrar
                                        </a>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        
                        <c:if test="${empty invoices}">
                            <tr class="text-center">
                                <td colspan="6" class="p-5 text-muted">
                                    <i class="fas fa-inbox fa-3x mb-3 text-secondary"></i><br>
                                    <h4>No hay ventas registradas</h4>
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>

        </div>
    </main>

    <footer class="bg-dark text-white text-center p-3 mt-5">
        Glowshop © 2025 - Todos los derechos reservados
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>

    <script>
    document.addEventListener("DOMContentLoaded", function() {
        const deleteButtons = document.querySelectorAll(".btn-delete");
        
        deleteButtons.forEach(btn => {
            btn.addEventListener("click", function(event) {
                event.preventDefault(); // Detiene el clic
                
                const invoiceId = this.getAttribute("data-id");
                const url = this.getAttribute("href");
                
                Swal.fire({
                    title: "¿Eliminar Factura #" + invoiceId + "?",
                    text: "Esto eliminará también todos los detalles de productos asociados. No se puede deshacer.",
                    icon: "warning",
                    showCancelButton: true,
                    confirmButtonColor: "#dc3545",
                    cancelButtonColor: "#6c757d",
                    confirmButtonText: "Sí, borrar factura",
                    cancelButtonText: "Cancelar"
                }).then((result) => {
                    if (result.isConfirmed) {
                        window.location.href = url;
                    }
                });
            });
        });

        var message = "${sessionScope.message}";

        if (message && message.trim() !== "") {
            Swal.fire({
                icon: 'success',
                title: '¡Operación Exitosa!',
                text: message,
                timer: 3000,
                showConfirmButton: false
            });
        }
    });
    </script>

    <c:remove var="message" scope="session"/>
</body>
</html>