<%@page contentType="text/html" pageEncoding="UTF-8"%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="container-fluid min-vh-100">
    
        
    <h3 class="mb-3">Manage Products</h3>

    <a href="products?action=new" class="btn btn-success btn-sm mb-3">
        <i class="fa fa-plus-circle"></i> New Product
    </a>
    <div class="card">
        <div class="card-body">

            <table class="table table-bordered table-striped mt-2">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nombre de producto</th>
                        <th>Descripción</th>
                        <th>Precio</th>
                        <th>Stock</th>
                            <th>Categoria</th>
                        <th>Acción</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${products}" var="item">
                        <tr>
                            <td>${item.productId}</td>
                            <td>${item.name}</td>
                            <td>${item.description}</td>
                            <td>${item.price}</td>
                            <td>${item.stock}</td>
                            <td>${item.category.name}</td>
                            <td>
                                <a href="products?action=update&productId=${item.productId}"
                                    class="btn btn-info btn-sm">
                                    <i class="fa fa-edit"></i>
                                </a>
                                <a href="products?action=delete&productId=${item.productId}"
                                    class="btn btn-danger btn-sm btn-delete"
                                    data-id="${item.productId}">
                                    <i class="fa fa-trash"></i>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${productos.size() == 0}">
                        <tr class="text-center">
                            <td colspan="6">No hay registros</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>
<script>
document.addEventListener("DOMContentLoaded", function() {
    const deleteButtons = document.querySelectorAll(".btn-delete");
    
    deleteButtons.forEach(btn => {
        btn.addEventListener("click", function(event) {
            event.preventDefault(); // Evita ir directamente al enlace
            
            const productId = this.getAttribute("data-id");
            const url = this.getAttribute("href");
            
            Swal.fire({
                title: "¿Eliminar producto?",
                text: `¿Seguro que deseas eliminar este producto?`,
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: "#3085d6",
                cancelButtonColor: "#d33",
                confirmButtonText: "Sí, eliminar",
                cancelButtonText: "Cancelar"
            }).then((result) => {
                if (result.isConfirmed) {
                    // Redirigimos al servlet si el usuario confirma
                    window.location.href = url;
                }
            });
        });
    });
});
</script>