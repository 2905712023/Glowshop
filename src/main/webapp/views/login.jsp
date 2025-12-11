<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar Sesión</title>
    
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">

    <style>
        body {
            background-color: #f8f9fa;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }
        .login-container {
            background-color: #ffffff;
            padding: 2rem;
            border-radius: 0.5rem;
            box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15);
            width: 100%;
            max-width: 400px;
        }
        .form-group {
            margin-bottom: 1rem; /* Bootstrap 5 eliminó la clase form-group, añadimos margen manual */
        }
    </style>
</head>
<body>

    <div class="login-container">
        <h3 class="text-center mb-4">Iniciar sesión</h3>

        <c:if test="${not empty sessionScope.errorMessage}">
            <div class="alert alert-danger" role="alert">
                ${sessionScope.errorMessage}
            </div>
        </c:if>

        <c:if test="${not empty sessionScope.lockUntil}">
            <div class="alert alert-warning">
                <strong>Cuenta regresiva:</strong> <span id="timerDisplay">Cargando...</span>
                <div class="progress mt-2" style="height: 20px;">
                    <div id="timerProgress" class="progress-bar progress-bar-striped progress-bar-animated bg-warning" role="progressbar" style="width: 100%;"></div>
                </div>
            </div>
            <script>
                (function() {
                    const lockUntilStr = '${sessionScope.lockUntil}';
                    if (!lockUntilStr) return;

                    const lockUntilMs = parseInt(lockUntilStr, 10);
                    const lockDurationStr = '${sessionScope.lockDuration}';
                    let totalDurationSec = parseInt(lockDurationStr, 10);
                    const now = Date.now();

                    if (isNaN(totalDurationSec) || totalDurationSec <= 0) {
                        totalDurationSec = Math.max(1, Math.ceil((lockUntilMs - now) / 1000));
                    }

                    const displayElement = document.getElementById('timerDisplay');
                    const progressElement = document.getElementById('timerProgress');
                    const form = document.querySelector('form');
                    const inputs = form ? form.querySelectorAll('input, button, select, textarea') : [];

                    // Deshabilitar todos los inputs mientras esté bloqueado
                    inputs.forEach(el => el.disabled = true);
                    if (form) form.style.opacity = '0.5';

                    function formatSecondsDisplay(seconds) {
                        if (seconds > 60) {
                            const minutes = Math.floor(seconds / 60);
                            const secs = seconds % 60;
                            return minutes + ':' + String(secs).padStart(2, '0') + ' m';
                        } else {
                            return seconds + ' s';
                        }
                    }

                    function updateTimer() {
                        const now2 = Date.now();
                        const remainingMs = Math.max(0, lockUntilMs - now2);
                        const remainingSec = Math.ceil(remainingMs / 1000);

                        if (displayElement) displayElement.textContent = formatSecondsDisplay(remainingSec);

                        if (progressElement) {
                            const percent = Math.min(100, Math.max(0, ((totalDurationSec - remainingSec) / totalDurationSec) * 100));
                            progressElement.style.width = percent + '%';
                        }

                        if (remainingSec > 0) {
                            setTimeout(updateTimer, 500);
                        } else {
                            // Expiró el bloqueo: habilitar inputs y recargar para limpiar estado del servidor
                            inputs.forEach(el => el.disabled = false);
                            if (form) form.style.opacity = '1';
                            if (displayElement) displayElement.textContent = formatSecondsDisplay(0);
                            setTimeout(function() { location.reload(); }, 800);
                        }
                    }

                    updateTimer();
                })();
            </script>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class="form-group mb-3">
                <label for="username" class="form-label">Usuario</label>
                <input id="username" name="username" class="form-control" required placeholder="Ingresa tu usuario" />
            </div>
            <div class="form-group mb-3">
                <label for="password" class="form-label">Password</label>
                <input id="password" name="password" type="password" class="form-control" required placeholder="Ingresa tu contraseña" />
            </div>
            <div class="d-grid gap-2">
                <button id="submitBtn" class="btn btn-primary mt-3" type="submit">Entrar</button>
            </div>
        </form>

    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>