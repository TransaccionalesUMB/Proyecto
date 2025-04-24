<!doctype html>
<html lang="es">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <link rel="stylesheet" href="css/styles.css">
    <script src="https://kit.fontawesome.com/a6bc1015cf.js" crossorigin="anonymous"></script>
    <title>Inicio de Sesión - Sistema Transaccional</title>
</head>

<body>
    <div class="container h-100">
        <div class="d-flex justify-content-center align-items-center h-100">
            <div class="login">
                <div class="text-center">
                    <h2 class="login-title">Inicio de Sesión</h2>
                    <p class="text-muted mb-4">Ingresa tus credenciales para continuar</p>
                </div>
                <form action="login" method="post" id="loginForm">
                    <div class="input-group">
                        <div class="input-group-prepend">
                            <span class="input-group-text">
                                <i class="fas fa-envelope"></i>
                            </span>
                        </div>
                        <input type="email" name="email" class="form-control" placeholder="Correo Electrónico" required>
                    </div>

                    <div class="input-group">
                        <div class="input-group-prepend">
                            <span class="input-group-text">
                                <i class="fas fa-lock"></i>
                            </span>
                        </div>
                        <input type="password" name="password" class="form-control" placeholder="Contraseña" required>
                    </div>

                    <div class="form-group mb-4">
                        <div class="custom-control">
                            <input type="checkbox" class="custom-control-input" id="customRecuerda" name="remember">
                            <label class="custom-control-label" for="customRecuerda">Recordar mi sesión</label>
                        </div>
                    </div>

                    <div id="errorMessage" class="alert alert-danger mb-4" role="alert" style="display: none;">
                        <i class="fas fa-exclamation-circle me-2"></i>
                        <span id="errorText"></span>
                    </div>

                    <div class="row g-3">
                        <div class="col-6">
                            <a href="register.jsp" class="btn btn-formulario w-100">
                                <i class="fas fa-user-plus me-2"></i>Registrar
                            </a>
                        </div>
                        <div class="col-6">
                            <button type="submit" name="ingresar" class="btn btn-formulario w-100">
                                <i class="fas fa-sign-in-alt me-2"></i>Ingresar
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
    <script>
        // Manejo de errores mediante JavaScript
        document.addEventListener('DOMContentLoaded', function() {
            const urlParams = new URLSearchParams(window.location.search);
            const error = urlParams.get('error');
            
            if (error) {
                const errorDiv = document.getElementById('errorMessage');
                const errorText = document.getElementById('errorText');
                errorDiv.style.display = 'block';
                errorText.textContent = decodeURIComponent(error);
            }

            // Manejo del formulario
            const form = document.getElementById('loginForm');
            form.addEventListener('submit', function(e) {
                const email = form.querySelector('input[name="email"]').value;
                const password = form.querySelector('input[name="password"]').value;

                if (!email || !password) {
                    e.preventDefault();
                    const errorDiv = document.getElementById('errorMessage');
                    const errorText = document.getElementById('errorText');
                    errorDiv.style.display = 'block';
                    errorText.textContent = 'Por favor, completa todos los campos';
                }
            });
        });
    </script>
</body>

</html>