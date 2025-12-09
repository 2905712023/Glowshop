<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="container mt-4">
    <h3>Administrar permisos por usuario</h3>

    <c:if test="${not empty sessionScope.errorMessage}">
        <div class="alert alert-danger">${sessionScope.errorMessage}</div>
    </c:if>
    <c:if test="${not empty sessionScope.message}">
        <div class="alert alert-success">${sessionScope.message}</div>
    </c:if>

    <form method="get" action="${pageContext.request.contextPath}/user-permissions" class="mb-3">
        <label for="userId">Seleccionar usuario</label>
        <select id="userId" name="userId" class="form-control" onchange="this.form.submit()">
            <option value="">-- elegir --</option>
            <c:forEach var="u" items="${users}">
                <option value="${u.userId}" ${u.userId == selectedUserId ? 'selected' : ''}>${u.name} (${u.role})</option>
            </c:forEach>
        </select>
    </form>

    <c:if test="${not empty selectedUserId}">
        <form method="post" action="${pageContext.request.contextPath}/user-permissions">
            <input type="hidden" name="userId" value="${selectedUserId}" />
            <div class="form-group">
                <label>Permisos</label>
                <div class="row">
                    <c:forEach var="m" items="${modules}">
                        <div class="col-3">
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" name="module_${m}" id="module_${m}" ${userPerms[m] ? 'checked' : ''} />
                                <label class="form-check-label" for="module_${m}">${m}</label>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
            <button class="btn btn-primary mt-3" type="submit">Guardar</button>
        </form>
    </c:if>

</div>
