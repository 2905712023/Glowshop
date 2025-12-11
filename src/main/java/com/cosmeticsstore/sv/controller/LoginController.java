package com.cosmeticsstore.sv.controller;

import java.io.IOException;
import java.util.Date;

import com.cosmeticsstore.sv.dao.BitacoraDAO;
import com.cosmeticsstore.sv.dao.UserDAO;
import com.cosmeticsstore.sv.dao.UserPermissionDAO;
import com.cosmeticsstore.sv.model.Bitacora;
import com.cosmeticsstore.sv.model.Users;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginController extends HttpServlet {

	private final String mainLayout = "/layout/mainLayout.jsp";
	private final String loginPage = "/views/login.jsp";
	private Integer attempts = 0;

	private UserDAO userDao = new UserDAO();
	private BitacoraDAO bitacoraDao = new BitacoraDAO();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Mostrar login; si la sesión está bloqueada, informar del tiempo restante
		HttpSession session = request.getSession();
		Long lockUntil = (Long) session.getAttribute("lockUntil");
		if (lockUntil != null) {
			long now = System.currentTimeMillis();
			if (lockUntil > now) {
				long secondsLeft = (lockUntil - now) / 1000;
				session.setAttribute("errorMessage", "Usuario bloqueado. Intenta nuevamente en " + secondsLeft + " segundos.");
				// Pasar lockUntil a la JSP para que el timer JS lo use
				request.setAttribute("lockUntil", lockUntil);
			}
		}

		request.setAttribute("pageTitle", "Login");
		request.setAttribute("pageContent", loginPage);
		request.getRequestDispatcher(loginPage).forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String username = request.getParameter("username");
		String password = request.getParameter("password");

		HttpSession session = request.getSession();

		// Chequear bloqueo actual
		Long lockUntilPost = (Long) session.getAttribute("lockUntil");
		long now = System.currentTimeMillis();
		if (lockUntilPost != null && lockUntilPost > now) {
			long secondsLeft = (lockUntilPost - now) / 1000;
			session.setAttribute("errorMessage", "Usuario bloqueado. Intenta nuevamente en " + secondsLeft + " segundos.");
			request.setAttribute("pageTitle", "Login");
			request.setAttribute("pageContent", loginPage);
			request.getRequestDispatcher(loginPage).forward(request, response);
			return;
		}
		
		// Si el bloqueo expiró, limpiar estado de bloqueo
		if (lockUntilPost != null && lockUntilPost <= now) {
			session.removeAttribute("lockUntil");
			session.removeAttribute("lockLevel");
			session.removeAttribute("loginAttempts");
		}

		//autenticación
		Users user = userDao.findByNameAndPassword(username, password);
		if (user != null) {
			session.setAttribute("user", user);
			// Cargar permisos por usuario y guardarlos en sesión para uso en JSPs
			try {
				UserPermissionDAO updao = new UserPermissionDAO();
				java.util.List<com.cosmeticsstore.sv.model.UserModulePermission> perms = updao.findByUserId(user.getUserId());
				java.util.Map<String, Boolean> permMap = new java.util.HashMap<>();
				for (com.cosmeticsstore.sv.model.UserModulePermission p : perms) {
					permMap.put(p.getModuleName(), true);
				}
				session.setAttribute("userPerms", permMap);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			session.removeAttribute("loginAttempts");
			session.removeAttribute("lockUntil");
			session.removeAttribute("lockDuration");
			session.removeAttribute("errorMessage");
			response.sendRedirect("products?action=list");
			return;
		} else {
			attempts = (Integer) session.getAttribute("loginAttempts");
			if (attempts == null) attempts = 0;
			attempts = attempts + 1;
			session.setAttribute("loginAttempts", attempts);

			// Determinar duración del bloqueo según el número total de intentos
			int lockSeconds = 0;
			if (attempts == 3) {
				lockSeconds = 60; // 1 minuto
			} else if (attempts == 4) {
				lockSeconds = 180; // 3 minutos
			} else if (attempts >= 5) {
				lockSeconds = 300; // 5 minutos
			}

			if (lockSeconds > 0) {
				long newLockUntil = System.currentTimeMillis() + (lockSeconds * 1000L);
				session.setAttribute("lockUntil", newLockUntil);
				session.setAttribute("lockDuration", lockSeconds);

				// registrar en bitacora
				Bitacora b = new Bitacora();
				b.setUsernameAttempt(username);
				b.setMessage("Bloqueo automático por superar intentos: intentos=" + attempts + ", duracion(s)=" + lockSeconds);
				b.setAttemptedAt(new Date());
				try {
					bitacoraDao.create(b);
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				session.setAttribute("errorMessage", "Usuario bloqueado por " + lockSeconds + " segundos debido a múltiples intentos fallidos.");
				request.setAttribute("pageTitle", "Login");
				request.setAttribute("pageContent", loginPage);
				request.getRequestDispatcher(loginPage).forward(request, response);
				return;
			} else {
				// aún dentro del límite: mostrar intentos
				session.setAttribute("errorMessage", "Credenciales inválidas. Intentos: " + attempts);
				request.setAttribute("pageTitle", "Login");
				request.setAttribute("pageContent", loginPage);
				request.getRequestDispatcher(loginPage).forward(request, response);
				return;
			}

		}
	}

}
