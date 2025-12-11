package com.cosmeticsstore.sv.auth;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Helper estático que define qué roles tienen acceso a cada módulo.
 * Ajusta el mapeo según tus necesidades o carga desde BD/config.
 */
public class ModulePermissions {

    private static final Map<String, Set<String>> MODULE_ROLES = new HashMap<>();

    static {
        // Módulos conocidos y roles permitidos
        // ADMIN: solo productos
        // ADMINTOTAL: todos los módulos
        MODULE_ROLES.put("PRODUCTS", new HashSet<>(Set.of("ADMIN", "ADMINTOTAL")));
        MODULE_ROLES.put("CATEGORIES", new HashSet<>(Set.of("ADMINTOTAL")));
        MODULE_ROLES.put("INVOICES", new HashSet<>(Set.of("ADMINTOTAL")));
        MODULE_ROLES.put("USERS", new HashSet<>(Set.of("ADMINTOTAL")));
        // puedes añadir más módulos aquí
    }

    public static Set<String> getRolesForModule(String module) {
        if (module == null) return Collections.emptySet();
        return MODULE_ROLES.getOrDefault(module, Collections.emptySet());
    }

    public static Set<String> getAllModules() {
        return MODULE_ROLES.keySet();
    }

    public static boolean isRoleAllowed(String role, String module) {
        if (role == null || module == null) return false;
        Set<String> roles = getRolesForModule(module);
        return roles.contains(role.toUpperCase());
    }

    /**
     * Determina el módulo a partir del path de la petición.
     * Devuelve null si el path no corresponde a un módulo protegido.
     */
    public static String getModuleForPath(String path) {
        if (path == null) return null;
        path = path.toLowerCase();
        if (path.startsWith("/products")) return "PRODUCTS";
        if (path.startsWith("/categories")) return "CATEGORIES";
        if (path.startsWith("/invoices")) return "INVOICES";
        if (path.startsWith("/users")) return "USERS";
        // soporte para la pantalla de administración de permisos
        if (path.startsWith("/user-permissions")) return "USERS";
        // agregar más heurísticas según URLs del proyecto
        return null;
    }
}
