<%@page contentType="text/html" pageEncoding="UTF-8"%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="container mt-5">
    <h3 class="mb-3">${category.categoryId > 0 ? "Editar": "Crear"} Categoria</h3>
    
    <div class="card">
        <div class="card-body">
            <form action="categories" method="post">
                <div class="mb-3">
                    <label>Nombre Categoria</label>
                    <input value="${category.name}" name="name" type="text" maxlength="50" 
                            class="form-control" required>
                </div>
                <div class="mb-3">
                    <label>Descripción</label>
                    <input value="${category.description}" name="description" type="text" maxlength="50" 
                            class="form-control" required>
                </div>           
                <div class="mb-3">
                    <input type="hidden" name="categoryId" value="${category.categoryId}">
                    <input type="hidden" name="action" value="save">
                    <button class="btn btn-primary btn-sm">
                        <i class="fa fa-save"></i> Guardar
                    </button>
                    <a href="categories?action=list" 
                        class="btn btn-dark btn-sm">
                        <i class="fa fa-arrow-left"></i> Categorias
                    </a>
                </div>
            </form>
        </div>
    </div>
</div>