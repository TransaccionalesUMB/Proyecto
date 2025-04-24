<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Formulario de Requerimientos</title>
    <link href="node_modules/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="node_modules/@fortawesome/fontawesome-free/css/all.min.css" rel="stylesheet">
    <link href="css/styles.css" rel="stylesheet">
    <style>
        .requirement-form {
            background: rgba(255, 255, 255, 0.95);
            border-radius: 15px;
            padding: 2rem;
            box-shadow: 0 8px 32px rgba(0,0,0,0.1);
            max-width: 800px;
            width: 100%;
        }

        .form-title {
            color: #1a237e;
            font-size: 2rem;
            margin-bottom: 1.5rem;
            text-align: center;
            font-weight: 600;
        }

        .form-group {
            margin-bottom: 1.5rem;
        }

        .form-label {
            color: #333;
            font-weight: 500;
            margin-bottom: 0.5rem;
        }

        .form-control {
            border-radius: 8px;
            border: 1px solid #e0e0e0;
            padding: 0.8rem;
            transition: all 0.3s ease;
        }

        .form-control:focus {
            border-color: #1a237e;
            box-shadow: 0 0 0 0.2rem rgba(26,35,126,0.15);
        }

        textarea.form-control {
            min-height: 150px;
            resize: vertical;
        }

        .status-select {
            background-color: white;
        }

        .btn-group {
            display: flex;
            gap: 1rem;
            margin-top: 2rem;
        }

        .btn-submit {
            background-color: #1a237e;
            color: white;
            padding: 0.8rem 2rem;
            border: none;
            border-radius: 8px;
            font-weight: 500;
            transition: all 0.3s ease;
        }

        .btn-submit:hover {
            background-color: #303f9f;
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }

        .btn-cancel {
            background-color: #e0e0e0;
            color: #333;
            padding: 0.8rem 2rem;
            border: none;
            border-radius: 8px;
            font-weight: 500;
            transition: all 0.3s ease;
            text-decoration: none;
        }

        .btn-cancel:hover {
            background-color: #d5d5d5;
            color: #333;
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="requirement-form">
            <h1 class="form-title">
                <i class="fas fa-clipboard-list me-2"></i>
                Nuevo Requerimiento
            </h1>
            
            <form action="submitRequirement" method="post">
                <div class="form-group">
                    <label class="form-label" for="title">
                        <i class="fas fa-heading me-2"></i>
                        Título
                    </label>
                    <input type="text" class="form-control" id="title" name="title" required 
                           placeholder="Ingrese un título descriptivo">
                </div>

                <div class="form-group">
                    <label class="form-label" for="description">
                        <i class="fas fa-align-left me-2"></i>
                        Descripción
                    </label>
                    <textarea class="form-control" id="description" name="description" required
                              placeholder="Describa detalladamente su requerimiento"></textarea>
                </div>

                <div class="form-group">
                    <label class="form-label" for="status">
                        <i class="fas fa-tasks me-2"></i>
                        Estado
                    </label>
                    <select class="form-control status-select" id="status" name="status">
                        <option value="pending">Pendiente</option>
                        <option value="in_progress">En Progreso</option>
                        <option value="completed">Completado</option>
                        <option value="cancelled">Cancelado</option>
                    </select>
                </div>

                <div class="btn-group">
                    <a href="index.jsp" class="btn-cancel">
                        <i class="fas fa-times me-2"></i>
                        Cancelar
                    </a>
                    <button type="submit" class="btn-submit">
                        <i class="fas fa-paper-plane me-2"></i>
                        Enviar Requerimiento
                    </button>
                </div>
            </form>
        </div>
    </div>

    <script src="node_modules/bootstrap/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>