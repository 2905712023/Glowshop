<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="container mt-5">
    
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