package com.cosmeticsstore.sv.filter;

import java.io.IOException;

import com.cosmeticsstore.sv.auth.ModulePermissions;
import com.cosmeticsstore.sv.dao.UserPermissionDAO;
import com.cosmeticsstore.sv.model.Users;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Filtro de autorización por módulos. Comprueba el rol del usuario en sesión
 * y verifica si el rol tiene permiso para acceder al módulo mapeado por la URL.
 */
@WebFilter("/*")
public class AuthorizationFilter extends HttpFilter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // no-op
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getServletPath();

        // Caso especial: la pantalla de administración de permisos
        // Permitimos a ADMINTOTAL o a cualquier usuario que tenga el permiso 'USERS' en BD
        if (path != null && path.toLowerCase().startsWith("/user-permissions")) {
            HttpSession sess = req.getSession(false);
            if (sess == null) {
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }
            Users suser = (Users) sess.getAttribute("user");
            if (suser == null) {
                sess = req.getSession(true);
                sess.setAttribute("errorMessage", "Por favor, inicie sesión para acceder a este recurso.");
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }

            boolean allowed = false;
            // role ADMIN siempre permite
            if (suser.getRole() != null && suser.getRole().equals("admin")) {
                allowed = true;
            } else {
                // comprobar permiso en BD (user_module_permissions)
                try {
                    com.cosmeticsstore.sv.dao.UserPermissionDAO updao = new com.cosmeticsstore.sv.dao.UserPermissionDAO();
                    allowed = updao.exists(suser.getUserId(), "USERS");
                } catch (Exception ex) {
                    // en caso de error de BD, negar por seguridad
                    allowed = false;
                }
            }

            if (allowed) {
                chain.doFilter(request, response);
            } else {
                sess.setAttribute("errorMessage", "No tienes permisos para administrar usuarios.");
                resp.sendRedirect(req.getContextPath() + "/index.jsp");
            }
            return;
        }

        // Rutas públicas que deben permanecer accesibles sin autenticación
        if (isPublicPath(path)) {
            chain.doFilter(request, response);
            return;
        }

        String module = ModulePermissions.getModuleForPath(path);
        // Si no corresponde a un módulo protegido, permitir (controversial: ajustar si desea negar por defecto)
        if (module == null) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        if (session == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            // no autenticado
            session = req.getSession(true);
            session.setAttribute("errorMessage", "Por favor, inicie sesión para acceder a este recurso.");
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Primero: si existe permiso asignado en BD para este usuario y módulo, permitir
        UserPermissionDAO permDao = new UserPermissionDAO();
        boolean hasUserPermission = permDao.exists(user.getUserId(), module);
        if (hasUserPermission) {
            chain.doFilter(request, response);
            return;
        }

        // Si no hay permiso por usuario, ver si el role está autorizado por la configuración estática
        String role = user.getRole();
        boolean allowedByRole = ModulePermissions.isRoleAllowed(role, module);
        if (allowedByRole) {
            chain.doFilter(request, response);
        } else {
            // No autorizado
            session.setAttribute("errorMessage", "No tienes permisos para acceder al módulo: " + module);
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
        }
    }

    private boolean isPublicPath(String path) {
        if (path == null) return true;
        path = path.toLowerCase();
        // permitir recursos estáticos y páginas públicas
        if (path.startsWith("/assets/") || path.startsWith("/layout/") || path.startsWith("/views/") ) return true;
        if (path.equals("/login") || path.equals("/logout") || path.equals("/index.jsp") || path.equals("/")) return true;
        if (path.startsWith("/pages/") || path.startsWith("/assets")) return true;
        // permitir servlet de recursos de JS/CSS si necesario
        return false;
    }

    @Override
    public void destroy() {
        // no-op
    }
}
