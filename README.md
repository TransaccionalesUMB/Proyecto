# Sistema de Gestión de Requerimientos - Documentación

## 📋 Descripción General
Este proyecto es una aplicación web para la gestión de requerimientos de usuarios, desarrollada con Java Spring Boot en el backend y tecnologías web modernas en el frontend.

## 🚀 Inicio Rápido

### Requisitos Previos
- Java 8 o superior
- Maven
- MySQL 8.0
- IDE (recomendado: IntelliJ IDEA o Eclipse)

### Configuración de la Base de Datos
1. Crear una base de datos MySQL llamada `datos_almacen`
2. Usuario por defecto: `root`
3. Contraseña por defecto: `2101`

### Ejecución del Proyecto
1. Clonar el repositorio
2. Navegar al directorio del backend:
   ```bash
   cd backend
   ```
3. Ejecutar la aplicación:
   ```bash
   mvn clean spring-boot:run
   ```
4. Acceder a la aplicación:
   ```
   http://localhost:8090/login
   ```

### Credenciales de Prueba
- Email: `juan@example.com`
- Contraseña: `hash123`

## 📁 Estructura del Proyecto

```
proyecto/
├── backend/                           # Código del servidor
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/
│   │   │   │       └── example/
│   │   │   │           └── transactional/
│   │   │   │               ├── config/         # Configuraciones
│   │   │   │               ├── controller/     # Controladores REST
│   │   │   │               ├── model/          # Entidades
│   │   │   │               └── repository/     # Repositorios JPA
│   │   │   └── resources/              # Recursos y configuraciones
│   │   └── test/                       # Pruebas unitarias
│   └── pom.xml                        # Configuración Maven
└── frontend/                          # Código del cliente
    ├── css/                           # Estilos
    │   └── styles.css                 # Estilos principales
    ├── js/                            # JavaScript
    ├── index.jsp                      # Página principal
    ├── login.jsp                      # Página de inicio de sesión
    └── requirementForm.jsp            # Formulario de requerimientos
```

## 🛠️ Tecnologías Utilizadas

### Backend
- Java 8
- Spring Boot 2.5.4
- Spring Security
- Spring Data JPA
- MySQL 8.0
- Maven

### Frontend
- JSP (JavaServer Pages)
- Bootstrap 5.1.3
- Font Awesome 6.0.0
- CSS3
- JavaScript

## 🔒 Seguridad
- Autenticación basada en formularios con Spring Security
- Protección contra CSRF
- Sesiones seguras
- Almacenamiento seguro de contraseñas

## 📝 Características Principales
1. **Autenticación de Usuarios**
   - Login seguro
   - Gestión de sesiones
   - Recordar sesión

2. **Gestión de Requerimientos**
   - Crear nuevos requerimientos
   - Listar requerimientos existentes
   - Actualizar estado de requerimientos
   - Eliminar requerimientos

## 👥 Contribución
Para contribuir al proyecto:
1. Fork el repositorio
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📄 Licencia
Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## ✨ Agradecimientos
- Universidad Manuela Beltrán
- Equipo de desarrollo
- Contribuidores
