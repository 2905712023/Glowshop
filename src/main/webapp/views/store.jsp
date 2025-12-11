<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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

<div class="container mt-4 flex-grow-1">
    
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

    <div class="offcanvas offcanvas-end" tabindex="-1" id="cartOffcanvas">
        <div class="offcanvas-header bg-dark text-white">
            <h5 class="offcanvas-title" id="cartOffcanvasLabel"><i class="fas fa-shopping-cart"></i> Tu Carrito</h5>
            <button type="button" class="btn-close text-reset bg-white" data-bs-dismiss="offcanvas" aria-label="Close"></button>
        </div>
        <div class="offcanvas-body">
            
            <c:choose>
                <c:when test="${empty shoppingCart}">
                    <div class="alert alert-info text-center mt-3" role="alert">
                        El carrito está vacío.
                    </div>
                </c:when>
                <c:otherwise>
                    <ul class="list-group mb-3">
                        <c:set var="totalPagar" value="${0}"/>
                        <c:forEach var="item" items="${shoppingCart}">
                            <li class="list-group-item d-flex justify-content-between align-items-center py-2">
                                <div class="flex-grow-1 me-2">
                                    <h6 class="my-0">${item.producto.name}</h6>
                                    <small class="text-muted">${item.cantidad} x $<fmt:formatNumber value="${item.producto.price}" pattern="#,##0.00"/></small>
                                </div>
                                <div class="d-flex align-items-center">
                                    <span class="text-success fw-bold me-2">
                                        $<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/>
                                    </span>
                                    <a href="cart?action=remove&productId=${item.producto.productId}" 
                                       class="btn btn-sm btn-outline-danger" title="Eliminar">
                                        <i class="fas fa-trash"></i>
                                    </a>
                                </div>
                            </li>
                            <c:set var="totalPagar" value="${totalPagar + item.subtotal}"/>
                        </c:forEach>
                    </ul>
                    
                    <div class="d-flex justify-content-between align-items-center border-top pt-3 mt-3">
                        <h4 class="mb-0">Total:</h4>
                        <h4 class="text-primary mb-0 fw-bold">
                            $<fmt:formatNumber value="${totalPagar}" pattern="#,##0.00"/>
                        </h4>
                    </div>
                    
                    <div class="d-grid gap-2 mt-3">
                        <a href="orderSummary?action=view" class="btn btn-success btn-lg">
                            <i class="fas fa-file-invoice"></i> Finalizar Compra
                        </a>
                    </div>

                    <div class="d-grid gap-2 mt-3">
                        <a href="cart?action=clear" class="btn btn-outline-danger btn-sm" title="Vacíar Carrito">
                            <i class="fas fa-trash"></i> Vacíar Carrito
                        </a>
                    </div>
                    
                </c:otherwise>
            </c:choose>
            
        </div>
    </div>
    
</div>

<div id="loadingSpinner" class="spinner-overlay" style="display: none;">
    <div class="spinner"></div>
</div>

<script>
    var errorMessage = '${errorMessage}';
    var message = '${message}';

    if (errorMessage && errorMessage !== '') {
        Swal.fire({
            title: 'Error',
            text: errorMessage,
            icon: 'error',
            confirmButtonColor: '#d33',
            confirmButtonText: 'Entendido'
        });
    }

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

<c:remove var="message" scope="session"/>
<c:remove var="ErrorMessage" scope="session"/>