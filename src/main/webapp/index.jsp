<%-- index.jsp --%>
<%
    // Redirige al Servlet que maneja la lógica de la tienda
    response.sendRedirect(request.getContextPath() + "/");
%>