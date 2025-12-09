package com.cosmeticsstore.sv.controller;

import com.cosmeticsstore.sv.auth.ModulePermissions;
import com.cosmeticsstore.sv.dao.UserPermissionDAO;
import com.cosmeticsstore.sv.dao.UserDAO;
import com.cosmeticsstore.sv.model.UserModulePermission;
import com.cosmeticsstore.sv.model.Users;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "UserPermissionController", urlPatterns = {"/user-permissions"})
public class UserPermissionController extends HttpServlet {

    private final String mainLayout = "/layout/mainLayout.jsp";
    private final String permissionsPage = "/views/users/permissions.jsp";
    private UserDAO userDao = new UserDAO();
    private UserPermissionDAO permDao = new UserPermissionDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // listar usuarios y permisos
        List<Users> users = userDao.findAll();
        Set<String> modules = ModulePermissions.getAllModules();

        request.setAttribute("users", users);
        request.setAttribute("modules", modules);

        // Si se pasó userId, cargar sus permisos
        String uid = request.getParameter("userId");
        if (uid != null) {
            try {
                int userId = Integer.parseInt(uid);
                List<UserModulePermission> perms = permDao.findByUserId(userId);
                Map<String, Boolean> map = new HashMap<>();
                for (String m : modules) map.put(m, false);
                for (UserModulePermission p : perms) map.put(p.getModuleName(), true);
                request.setAttribute("selectedUserId", userId);
                request.setAttribute("userPerms", map);
            } catch (NumberFormatException ex) {
                // ignore
            }
        }

        request.setAttribute("pageTitle", "Administrar permisos por usuario");
        request.setAttribute("pageContent", permissionsPage);
        request.getRequestDispatcher(mainLayout).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // recibir userId y lista de módulos seleccionados
        String uid = request.getParameter("userId");
        if (uid == null || uid.isEmpty()) {
            request.getSession().setAttribute("errorMessage", "Seleccione un usuario.");
            response.sendRedirect(request.getContextPath() + "/user-permissions");
            return;
        }
        int userId = Integer.parseInt(uid);
        Set<String> modules = ModulePermissions.getAllModules();

        // los parámetros con nombre module_<MODULE> vienen cuando están marcados
        for (String module : modules) {
            String paramName = "module_" + module;
            boolean checked = request.getParameter(paramName) != null;
            boolean exists = permDao.exists(userId, module);
            if (checked && !exists) {
                permDao.grant(userId, module);
            } else if (!checked && exists) {
                permDao.revoke(userId, module);
            }
        }

        // Si el usuario que estamos modificando es el que está en sesión, refrescar su mapa de permisos
        jakarta.servlet.http.HttpSession session = request.getSession(false);
        if (session != null) {
            com.cosmeticsstore.sv.model.Users logged = (com.cosmeticsstore.sv.model.Users) session.getAttribute("user");
            if (logged != null && logged.getUserId() != null && logged.getUserId().intValue() == userId) {
                java.util.List<com.cosmeticsstore.sv.model.UserModulePermission> newPerms = permDao.findByUserId(userId);
                java.util.Map<String, Boolean> permMap = new java.util.HashMap<>();
                for (String m : modules) permMap.put(m, false);
                for (com.cosmeticsstore.sv.model.UserModulePermission p : newPerms) permMap.put(p.getModuleName(), true);
                session.setAttribute("userPerms", permMap);
            }
        }

        request.getSession().setAttribute("message", "Permisos actualizados.");
        response.sendRedirect(request.getContextPath() + "/user-permissions?userId=" + userId);
    }
}
