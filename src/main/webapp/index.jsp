<%-- index.jsp --%>
<%
    // Decide qué vista mostrar según sesión
    Object user = session.getAttribute("user");
    if (user != null) {
        request.setAttribute("pageTitle", "Home");
        request.setAttribute("pageContent", "/views/home.jsp");
    } else {
        request.setAttribute("pageTitle", "Login");
        request.setAttribute("pageContent", "/views/login.jsp");
    }

    // debug: activa con ?debug=1 para mostrar información útil en página y logs
    boolean debugMode = "1".equals(request.getParameter("debug"));
    if (debugMode) {
        Object attempts = session.getAttribute("loginAttempts");
        try {
            System.out.println("[DEBUG] index.jsp contextPath=" + request.getContextPath());
            System.out.println("[DEBUG] index.jsp pageContent=" + request.getAttribute("pageContent"));
            System.out.println("[DEBUG] index.jsp session.user=" + (user != null ? user.toString() : "null"));
            System.out.println("[DEBUG] index.jsp session.loginAttempts=" + (attempts != null ? attempts.toString() : "null"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.setAttribute("__debug_mode__", Boolean.TRUE);
    }
%>

<jsp:include page="/layout/mainLayout.jsp" />

<%
    if (request.getAttribute("__debug_mode__") != null) {
        out.println("<div style='position:fixed; right:10px; bottom:10px; z-index:99999; background:#fff; border:1px solid #ccc; padding:10px; font-size:12px;'>");
        out.println("<strong>DEBUG</strong><br/>");
        out.println("pageContent: " + request.getAttribute("pageContent") + "<br/>");
        out.println("session.user: " + (session.getAttribute("user") != null ? session.getAttribute("user").toString() : "null") + "<br/>");
        out.println("loginAttempts: " + (session.getAttribute("loginAttempts") != null ? session.getAttribute("loginAttempts").toString() : "null") + "<br/>");
        out.println("</div>");
    }
%>