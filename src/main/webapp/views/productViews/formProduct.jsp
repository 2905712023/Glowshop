<%@page contentType="text/html" pageEncoding="UTF-8"%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="container-fluid min-vh-100">
    <h3 class="mb-3">${product.productId > 0 ? "Update": "Create"} Product</h3>
    
    <div class="card">
        <div class="card-body">
            <form action="products" method="post" enctype="multipart/form-data">
                <div class="mb-3">
                    <label>Product name</label>
                    <input value="${product.name}" name="name" type="text" maxlength="50" 
                            class="form-control" required>
                </div>
                <div class="mb-3">
                    <label>Description</label>
                    <input value="${product.description}" name="description" type="text" maxlength="50" 
                            class="form-control" required>
                </div>
                <div class="mb-3">
                    <label>Product price</label>
                    <input value="${product.price}" name="price" type="number" 
                        step="0.01"    class="form-control" required>
                </div>
                <div class="mb-3">
                    <label>Items in stock</label>
                    <input value="${product.stock}" name="stock" type="number" 
                            class="form-control" required>
                </div>
                    <div class="mb-3">
                    <label>Category</label>
                    <select class="form-control" name="categoryId" id="categoryId">
                        <option value="">Seleccione una categoría</option>
                        <c:forEach items="${categories}" var="category">
                            <option value="${category.categoryId}">${category.name}</option>
                        </c:forEach>
                    </select>                   
                </div>
                <div class="mb-3">
                    <label>Product Image</label>
                    <input name="image" type="file" accept="image/*" class="form-control" ${product.productId > 0 ? "" : "required"}> 
                    
                    <c:if test="${not empty imageUrl}">
                        <div class="mt-2">
                            <p class="mb-1 text-muted">Imagen actual:</p>
                            <img src="${imageUrl}" alt="Imagen del producto ${product.name}" 
                                style="max-width: 200px; height: auto; border: 1px solid #ddd; padding: 5px;">
                        </div>
                    </c:if>
                </div>
                <div class="mb-3">
                    <input type="hidden" name="productId" value="${product.productId}">
                    <input type="hidden" id="category-hidden" name="categoryId" value="${categoryId}">
                    <input type="hidden" name="action" value="save">
                    <button class="btn btn-primary btn-sm">
                        <i class="fa fa-save"></i> Save
                    </button>
                    <a href="products?action=list" 
                        class="btn btn-dark btn-sm">
                        <i class="fa fa-arrow-left"></i> Products
                    </a>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
    const categoryId = document.getElementById('category-hidden').value;
    if (categoryId > 0) {
        document.getElementById('categoryId').value = categoryId;
    }
</script>