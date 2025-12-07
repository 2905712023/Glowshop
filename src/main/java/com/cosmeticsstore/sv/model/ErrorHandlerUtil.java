package com.cosmeticsstore.sv.model;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.http.HttpServletResponse;

public class ErrorHandlerUtil {
    public static void sendError (Exception e, HttpServletResponse response) throws IOException {
        if (response.isCommitted()) {
            System.err.println("ERROR CRÍTICO (Respuesta ya enviada): " + e.getMessage());
            e.printStackTrace();
            return;
        }

        response.reset();
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.setContentType("text/plain;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            System.err.println("Error en producción:");
            e.printStackTrace();
            
            e.printStackTrace(out);
        }
    }
}
