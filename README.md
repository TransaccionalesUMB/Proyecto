# Sistema de Gestión de Requerimientos - Documentación

## 📋 Descripción General
Este proyecto es una aplicación web para la gestión de requerimientos de usuarios, desarrollada con Java Spring Boot en el backend y tecnologías web modernas en el frontend.

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

## 📂 Descripción de Archivos

### Backend

#### Configuración (`backend/src/main/java/com/example/transactional/config/`)
- **DatabaseConfig.java**
  - Ubicación: `config/DatabaseConfig.java`
  - Función: Configura la conexión a la base de datos usando Spring Boot
  - Detalles: Define el DataSource y las propiedades de conexión

#### Controladores (`backend/src/main/java/com/example/transactional/controller/`)
- **TestController.java**
  - Ubicación: `controller/TestController.java`
  - Función: Proporciona endpoints REST para pruebas
  - Endpoints:
    - GET `/test`: Prueba la conexión a la base de datos

#### Modelos (`backend/src/main/java/com/example/transactional/model/`)
- **User.java**
  - Ubicación: `model/User.java`
  - Función: Define la entidad Usuario
  - Campos:
    - id: Long (Primary Key)
    - email: String (único)
    - password: String
    - fullName: String

#### Repositorios (`backend/src/main/java/com/example/transactional/repository/`)
- **UserRepository.java**
  - Ubicación: `repository/UserRepository.java`
  - Función: Maneja las operaciones de base de datos para usuarios
  - Métodos:
    - findByEmail(): Busca usuario por email

#### Recursos (`backend/src/main/resources/`)
- **application.properties**
  - Ubicación: `resources/application.properties`
  - Función: Configuración de la aplicación
  - Contiene:
    - Configuración de base de datos
    - Configuración de JPA
    - Configuración del servidor
    - Configuración de JWT
    - Configuración de logging

### Frontend

#### Estilos (`frontend/css/`)
- **styles.css**
  - Ubicación: `css/styles.css`
  - Función: Define los estilos de la aplicación
  - Características:
    - Diseño responsivo con gradientes modernos
    - Paleta de colores corporativa (azul índigo)
    - Estilos de formularios con efectos de profundidad
    - Animaciones y transiciones suaves
    - Tarjetas interactivas con hover effects
    - Iconografía con Font Awesome
    - Diseño centrado en la experiencia del usuario

#### Páginas JSP (`frontend/`)
- **index.jsp**
  - Ubicación: `frontend/index.jsp`
  - Función: Página principal de la aplicación
  - Características:
    - Diseño moderno con tarjetas de características
    - Iconografía descriptiva
    - Efectos de hover y transiciones suaves
    - Interfaz intuitiva y atractiva
    - Sección de características principales
    - Navegación clara y accesible

- **login.jsp**
  - Ubicación: `frontend/login.jsp`
  - Función: Página de inicio de sesión
  - Características:
    - Formulario de login con diseño moderno
    - Iconos intuitivos para cada campo
    - Validación de campos en tiempo real
    - Manejo de errores con feedback visual
    - Opción "Recordar sesión"
    - Diseño responsivo y accesible

- **requirementForm.jsp**
  - Ubicación: `frontend/requirementForm.jsp`
  - Función: Formulario para envío de requerimientos
  - Características:
    - Formulario intuitivo y bien estructurado
    - Campos con iconos descriptivos
    - Textarea expandible para descripción
    - Selector de estado con opciones predefinidas
    - Botones de acción con feedback visual
    - Validación de campos requeridos

## 🛠 Tecnologías Utilizadas

### Backend
- Java 8
- Spring Boot 2.5.4
- Spring Security
- Spring Data JPA
- Hibernate
- MySQL
- Maven

### Frontend
- HTML5
- CSS3
- JavaScript
- Bootstrap 5.1.3
- Font Awesome (iconos)

## ⚙️ Configuración del Proyecto

### Base de Datos
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/transaccionales
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

### JPA
```properties
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

## 🚀 Ejecución del Proyecto

### Backend
1. Navegar al directorio backend:
```bash
cd backend
```

2. Compilar el proyecto:
```bash
mvn clean install
```

3. Ejecutar el servidor:
```bash
mvn spring-boot:run
```

### Frontend
1. Asegurarse de que el backend esté corriendo
2. Acceder a través del navegador:
```
http://localhost:8080
```

## 🔒 Seguridad
- Autenticación basada en JWT
- Contraseñas hasheadas con BCrypt
- Validación de entrada
- Protección CSRF
- Manejo seguro de sesiones

## 📝 Convenciones de Código

### Java
- Nombres de clases: PascalCase
- Nombres de métodos y variables: camelCase
- Constantes: UPPER_SNAKE_CASE
- Paquetes: minúsculas

### Frontend
- Clases CSS: kebab-case
- IDs: camelCase
- Archivos: snake_case

## 👥 Equipo de Desarrollo
Desarrollado para la asignatura de Sistemas Transaccionales - Universidad Manuela Beltrán

---

## 📄 Licencia
Este proyecto está bajo la Licencia MIT - ver el archivo LICENSE.md para detalles

---

