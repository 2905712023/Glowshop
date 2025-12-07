<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="container-fluid d-flex flex-column justify-content-center align-items-center min-vh-100">
    <!-- <img src="${pageContext.request.contextPath}/img/products/aloe_vera_gel.jpg" class="mb-4" style="width: 150px; height: auto;"> -->
    <div class="card shadow-sm border-0">
        <div class="card-body py-5">
            <h1 class="fw-bold text-primary">✨ Welcome to Glowshop</h1>
            <p class="text-muted mt-3">
                Administra fácilmente tus productos y categorías desde un solo lugar.
            </p>
            <hr class="w-50 mx-auto my-4">
            <p>
                <a href="products?action=list" class="btn btn-primary btn-lg">
                    <i class="fa fa-box"></i> Products
                </a>
                <a href="categories?action=list" class="btn btn-outline-secondary btn-lg ms-2">
                     Categories
                </a>
            </p>
        </div>
    </div>
</div>
