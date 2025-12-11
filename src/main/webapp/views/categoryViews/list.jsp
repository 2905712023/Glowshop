<%@page contentType="text/html" pageEncoding="UTF-8"%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="container mt-5">
    
        
    <h3 class="mb-3">Administrar Categorías</h3>

    <a href="categories?action=new" class="btn btn-success btn-sm mb-3">
        <i class="fa fa-plus-circle"></i> Categoría Nueva
    </a>
    <div class="card">
        <div class="card-body">
            <table id="categoriesTable" class="table table-bordered table-striped mt-2 nowrap">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nombre de Categoría</th>
                        <th>Descripción</th>
                        <th>Acción</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${categories}" var="item">
                        <tr>
                            <td>${item.categoryId}</td>
                            <td>${item.name}</td>
                            <td>${item.description}</td>                         
                            <td>
                                <a href="categories?action=update&categoryId=${item.categoryId}"
                                    class="btn btn-info btn-sm" title="Editar">
                                    <i class="fa fa-edit"></i>
                                </a>
                                <a href="categories?action=delete&categoryId=${item.categoryId}"
                                    class="btn btn-danger btn-sm btn-delete"
                                    data-id="${item.categoryId}" title="Eliminar">
                                    <i class="fa fa-trash"></i>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${categories.size() == 0}">
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
                    title: "¿Eliminar categoría?",
                    text: `¿Seguro que deseas eliminar esta categoría?`,
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
        $('#categoriesTable').DataTable({
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