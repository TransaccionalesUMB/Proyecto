<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sistema Transaccional</title>
    <link href="node_modules/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="node_modules/@fortawesome/fontawesome-free/css/all.min.css" rel="stylesheet">
    <link href="css/styles.css" rel="stylesheet">
    <style>
        .hero-section {
            background: rgba(255, 255, 255, 0.95);
            border-radius: 15px;
            padding: 2rem;
            box-shadow: 0 8px 32px rgba(0,0,0,0.1);
            max-width: 800px;
            width: 100%;
            text-align: center;
        }

        .hero-title {
            color: #1a237e;
            font-size: 2.5rem;
            margin-bottom: 1.5rem;
            font-weight: 600;
        }

        .feature-card {
            background: white;
            border-radius: 10px;
            padding: 1.5rem;
            margin: 1rem 0;
            transition: transform 0.3s ease;
            box-shadow: 0 4px 6px rgba(0,0,0,0.05);
        }

        .feature-card:hover {
            transform: translateY(-5px);
        }

        .feature-icon {
            font-size: 2rem;
            color: #1a237e;
            margin-bottom: 1rem;
        }

        .nav-button {
            background-color: #1a237e;
            color: white;
            border-radius: 8px;
            padding: 0.8rem 1.5rem;
            margin: 0.5rem;
            text-decoration: none;
            transition: all 0.3s ease;
            display: inline-flex;
            align-items: center;
            gap: 0.5rem;
        }

        .nav-button:hover {
            background-color: #303f9f;
            color: white;
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }

        footer {
            color: rgba(255, 255, 255, 0.8);
            margin-top: 2rem;
            font-size: 0.9rem;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="hero-section">
            <h1 class="hero-title">Sistema Transaccional</h1>
            
            <div class="row mb-4">
                <div class="col-md-6">
                    <div class="feature-card">
                        <i class="fas fa-user-lock feature-icon"></i>
                        <h3>Gestión de Acceso</h3>
                        <p>Accede de forma segura a tu cuenta para gestionar tus requerimientos.</p>
                        <a href="login.jsp" class="nav-button">
                            <i class="fas fa-sign-in-alt"></i>
                            Iniciar Sesión
                        </a>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="feature-card">
                        <i class="fas fa-tasks feature-icon"></i>
                        <h3>Requerimientos</h3>
                        <p>Envía y gestiona tus requerimientos de manera eficiente.</p>
                        <a href="requirementForm.jsp" class="nav-button">
                            <i class="fas fa-plus-circle"></i>
                            Nuevo Requerimiento
                        </a>
                    </div>
                </div>
            </div>

            <footer>
                <p>&copy; 2025 Sistema Transaccional. Todos los derechos reservados.</p>
            </footer>
        </div>
    </div>

    <script src="node_modules/bootstrap/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>