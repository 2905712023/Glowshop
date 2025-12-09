<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="login-container">
    <h3>Iniciar sesión</h3>

    <c:if test="${not empty sessionScope.errorMessage}">
        <div class="alert alert-danger">${sessionScope.errorMessage}</div>
    </c:if>

    <!-- Timer solo visible si está bloqueado -->
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
        <div class="form-group">
            <label for="username">Usuario</label>
            <input id="username" name="username" class="form-control" required />
        </div>
        <div class="form-group">
            <label for="password">Password</label>
            <input id="password" name="password" type="password" class="form-control" required />
        </div>
        <button id="submitBtn" class="btn btn-primary mt-3" type="submit">Entrar</button>
    </form>

</div>

<!-- El control de habilitar/deshabilitar se maneja en el script del bloque de bloqueo -->
