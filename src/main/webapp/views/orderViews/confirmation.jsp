<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Confirmación de Compra - Glowshop</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css"> 
</head>
<body class="d-flex flex-column min-vh-100">
    
    <header class="bg-dark p-3 text-white text-center">
        <h4>Glowshop - Confirmación de Compra</h4>
    </header>

    <main class="container mt-5 flex-grow-1">
        
        <div class="row justify-content-center">
            <div class="col-md-9">

                <div class="alert alert-success text-center border border-success">
                    <h2>🎉 ¡Compra Exitosa!</h2>
                    <p class="lead mb-0">Tu orden ha sido procesada con éxito. <strong>Tu brillo, nuestra misión.</strong></p>
                </div>
                
                <div class="card shadow-lg mb-4">
                    <div class="card-body">
                        
                        <h4 class="card-title mb-3 text-secondary">Información de la Factura</h4>
                        <hr>

                        <div class="d-flex justify-content-between mb-4 flex-wrap">
                            <div>
                                <p class="fs-4 mb-0">Factura No: 
                                    <strong class="text-primary">#<c:out value="${invoiceData.invoiceId}"/></strong>
                                </p>
                                
                                <p class="mb-0 fs-5">Cliente: 
                                    <strong class="text-dark">
                                        <c:out value="${not empty invoiceData.customer ? invoiceData.customer : 'Clientes Varios'}"/>
                                    </strong>
                                </p>
                            </div>

                            <p class="mb-0 text-muted align-self-center">Fecha: 
                                <fmt:formatDate value="${invoiceData.date}" pattern="dd/MM/yyyy HH:mm"/>
                            </p>
                        </div>

                        
                        <h5 class="mt-4 mb-3">Productos Comprados</h5>
                        <div class="table-responsive">
                            <table class="table table-striped table-hover align-middle">
                                <thead class="bg-dark text-white">
                                    <tr>
                                        <th>Producto</th>
                                        <th class="text-center">Cant.</th>
                                        <th class="text-end">Precio Unit.</th>
                                        <th class="text-end">Subtotal</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="detail" items="${invoiceData.invoiceDetailsCollection}">
                                        <tr>
                                            <td>
                                                <div class="d-flex align-items-center">
                                                    <img src="${pageContext.request.contextPath}${detail.productId.path}" 
                                                         alt="${detail.productId.name}" 
                                                         style="width: 40px; height: 40px; object-fit: cover; margin-right: 10px;"
                                                         class="rounded shadow-sm">
                                                    <c:out value="${detail.productId.name}"/>
                                                </div>
                                            </td>
                                            <td class="text-center"><c:out value="${detail.quantity}"/></td>
                                            <td class="text-end">
                                                $<fmt:formatNumber value="${detail.unitPrice}" pattern="#,##0.00"/>
                                            </td>
                                            <td class="text-end fw-bold text-success">
                                                $<fmt:formatNumber value="${detail.subtotal}" pattern="#,##0.00"/>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                        
                        <div class="d-flex justify-content-end align-items-center border-top pt-3 mt-3">
                            <h3 class="mb-0 me-3">TOTAL FINAL:</h3>
                            <h3 class="text-success mb-0 fw-bolder fs-3">
                                $<fmt:formatNumber value="${invoiceData.total}" pattern="#,##0.00"/>
                            </h3>
                        </div>
                        
                        <div class="d-flex justify-content-end gap-3 mt-4 pt-3 border-top">
                            <a href="index.jsp" class="btn btn-outline-secondary">
                                <i class="fas fa-home"></i> Volver a la Tienda
                            </a>
                            
                            <a href="orderSummary?action=view&invoiceId=${invoiceData.invoiceId}" 
                                class="btn btn-danger" 
                                target="_blank"> <i class="fas fa-file-pdf"></i> Descargar Factura PDF
                            </a>
                        </div>
                    </div>
                </div>
                
            </div>
        </div>
    </main>
    
    <footer class="bg-dark text-white text-center p-3 mt-4">
        Glowshop © 2025 - Todos los derechos reservados
    </footer>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>

    <c:remove var="shoppingCart" scope="session"/>
</body>
</html>