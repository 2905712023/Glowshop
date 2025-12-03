<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Resumen de Orden - Glow Shop</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body class="d-flex flex-column min-vh-100">

    <main class="container mt-5 flex-grow-1">
        
        <div class="row justify-content-center">
            <div class="col-md-8">
                
                <h2 class="mb-4">📋 Resumen de tu Orden</h2>
                <hr>

                <table class="table table-bordered table-striped">
                    <thead class="bg-dark text-white">
                        <tr>
                            <th>Producto</th>
                            <th class="text-center">Cant.</th>
                            <th class="text-end">Precio Unit.</th>
                            <th class="text-end">Subtotal</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="item" items="${summaryItems}">
                            <tr>
                                <td>
                                    <div class="d-flex align-items-center">
                                        <img src="${pageContext.request.contextPath}${item.producto.path}" 
                                             alt="${item.producto.name}" 
                                             style="width: 50px; height: 50px; object-fit: cover; margin-right: 10px;">
                                        <strong>${item.producto.name}</strong>
                                    </div>
                                </td>
                                <td class="text-center">${item.cantidad}</td>
                                <td class="text-end">
                                    $<fmt:formatNumber value="${item.producto.price}" pattern="#,##0.00"/>
                                </td>
                                <td class="text-end fw-bold text-success">
                                    $<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                    <tfoot>
                        <tr>
                            <td colspan="3" class="text-end fw-bold">TOTAL A PAGAR:</td>
                            <td class="text-end fw-bolder fs-5 text-primary">
                                $<fmt:formatNumber value="${orderTotal}" pattern="#,##0.00"/>
                            </td>
                        </tr>
                    </tfoot>
                </table>

                <div class="d-flex justify-content-between align-items-center mt-4">
    
                    <a href="index.jsp" class="btn btn-outline-secondary">
                        <i class="fas fa-chevron-left"></i> Seguir Comprando
                    </a>
                    
                    <form action="checkoutProcess" method="POST" class="d-flex gap-2"> 
                        
                        <button type="button" class="btn btn-danger" disabled 
                                title="Debe Confirmar la Compra primero">
                            <i class="fas fa-file-pdf"></i> Generar Reporte PDF
                        </button>
                        
                        <button type="submit" class="btn btn-success">
                            <i class="fas fa-check-circle"></i> Confirmar Compra
                        </button>
                        
                    </form>
                </div>
            </div>
        </div>
        
    </main>

    </body>
</html>