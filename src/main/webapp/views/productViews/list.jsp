<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<div class="container mt-5">
    <h3 class="mb-3">Administrar Productos</h3>
    <a href="products?action=new" class="btn btn-success btn-sm mb-3">
        <i class="fa fa-plus-circle"></i> Producto Nuevo
    </a>
    <div class="card">
        <div class="card-body">
            <table id="productsTable" class="table table-bordered table-striped mt-2 nowrap">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nombre Producto</th>
                        <th>Descripción</th>
                        <th>Precio</th>
                        <th>Stock</th>
                        <th>Categoría</th>
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
                                    class="btn btn-info btn-sm" title="Editar">
                                    <i class="fa fa-edit"></i>
                                </a>
                                <a href="products?action=delete&productId=${item.productId}"
                                    class="btn btn-danger btn-sm btn-delete"
                                    data-id="${item.productId}" title="Eliminar">
                                    <i class="fa fa-trash"></i>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
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
    window.addEventListener("load", function(){
        $('#productsTable').DataTable({
            "pagingType": "simple_numbers",
            "language": {
                "lengthMenu": "Mostrar _MENU_ registros por página",
                "zeroRecords": "No se encontraron productos",
                "info": "Mostrando página _PAGE_ de _PAGES_",
                "infoEmpty": "No hay registros disponibles",
                "infoFiltered": "(filtrado de _MAX_ registros)",
                "search": "Buscar:",
                "paginate": {
                    "first": "Primero",
                    "last": "Último",
                    "next": "Siguiente",
                    "previous": "Anterior"
                }
            },
            "responsive" : true
        });
    });
</script>