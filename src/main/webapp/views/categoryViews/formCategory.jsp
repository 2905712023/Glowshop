<%@page contentType="text/html" pageEncoding="UTF-8"%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="container-fluid min-vh-100">
    <h3 class="mb-3">${category.categoryId > 0 ? "Update": "Create"} category</h3>
    
    <div class="card">
        <div class="card-body">
            <form action="categories" method="post">
                <div class="mb-3">
                    <label>Category name</label>
                    <input value="${category.name}" name="name" type="text" maxlength="50" 
                            class="form-control" required>
                </div>
                <div class="mb-3">
                    <label>Description</label>
                    <input value="${category.description}" name="description" type="text" maxlength="50" 
                            class="form-control" required>
                </div>           
                <div class="mb-3">
                    <input type="hidden" name="categoryId" value="${category.categoryId}">
                    <input type="hidden" name="action" value="save">
                    <button class="btn btn-primary btn-sm">
                        <i class="fa fa-save"></i> Save
                    </button>
                    <a href="categories?action=list" 
                        class="btn btn-dark btn-sm">
                        <i class="fa fa-arrow-left"></i> category
                    </a>
                </div>
            </form>
        </div>
    </div>
</div>