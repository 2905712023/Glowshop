<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="container mt-5">
    
    <div class="row justify-content-center">
        <div class="col-md-9">
            
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2>📋 Detalles de Factura #${invoice.invoiceId}</h2>
                <span class="badge bg-secondary fs-6">
                    <i class="far fa-calendar-alt"></i> 
                    <fmt:formatDate value="${invoice.date}" pattern="dd/MM/yyyy HH:mm"/>
                </span>
            </div>
            
            <div class="card mb-4 shadow-sm">
                <div class="card-body bg-light">
                    <strong>Cliente:</strong> 
                    <span class="text-primary">
                        ${not empty invoice.customer ? invoice.customer : 'Clientes Varios'}
                    </span> 
                    <span class="mx-2">|</span>
                    <strong>Atendido por:</strong> ${invoice.userId.getName()}
                </div>
            </div>

            <hr>

            <table class="table table-bordered table-striped shadow-sm">
                <thead class="bg-dark text-white">
                    <tr>
                        <th>Producto</th>
                        <th class="text-center">Cant.</th>
                        <th class="text-end">Precio Unit.</th>
                        <th class="text-end">Subtotal</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="detail" items="${invoice.invoiceDetailsCollection}">
                        <tr>
                            <td>
                                <div class="d-flex align-items-center">
                                    <img src="${pageContext.request.contextPath}${detail.productId.path}" 
                                         alt="${detail.productId.name}" 
                                         style="width: 50px; height: 50px; object-fit: cover; margin-right: 10px;">

                                    <div>
                                        <strong>${detail.productId.getName()}</strong><br>
                                        <small class="text-muted">${detail.productId.category.getName()}</small>
                                    </div>
                                </div>
                            </td>
                            <td class="text-center align-middle">${detail.quantity}</td>
                            <td class="text-end align-middle">
                                $<fmt:formatNumber value="${detail.unitPrice}" pattern="#,##0.00"/>
                            </td>
                            <td class="text-end fw-bold text-success align-middle">
                                $<fmt:formatNumber value="${detail.subtotal}" pattern="#,##0.00"/>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
                <tfoot>
                    <tr>
                        <td colspan="3" class="text-end fw-bold">TOTAL PAGADO:</td>
                        <td class="text-end fw-bolder fs-5 text-primary">
                            $<fmt:formatNumber value="${invoice.total}" pattern="#,##0.00"/>
                        </td>
                    </tr>
                </tfoot>
            </table>

            <div class="d-flex justify-content-between align-items-center mt-4 mb-5">
                
                <a href="invoices?action=list" class="btn btn-outline-secondary">
                    <i class="fas fa-arrow-left"></i> Volver al Listado
                </a>
                
                <a href="orderSummary?action=view&invoiceId=${invoice.invoiceId}" 
                   target="_blank" class="btn btn-warning">
                    <i class="fas fa-file-pdf"></i> Descargar PDF
                </a>
                
            </div>
        </div>
    </div>
    
</div>