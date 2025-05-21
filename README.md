# StockMaster

![Logo](https://img.icons8.com/color/96/000000/warehouse.png)

## ¿Qué es StockMaster?

StockMaster nació de la necesidad de tener un sistema sencillo pero potente para gestionar inventarios. Si alguna vez has trabajado con Excel para llevar el control de tus productos, sabes lo frustrante que puede ser. Este proyecto busca resolver ese problema.

Creamos una aplicación web con Spring Boot que te permite manejar productos, categorías, proveedores y ver reportes de forma intuitiva. Lo mejor es que implementamos un sistema de roles para que cada persona de tu equipo tenga acceso solo a lo que necesita.

## Cómo empezar

Necesitarás tener instalado:
- Java 8 o más reciente
- Maven para las dependencias
- MySQL para la base de datos
- Un navegador actualizado

Para la base de datos:
- Crea una BD llamada `datos_almacen`
- Usuario: `root`
- Contraseña: `root`

Para arrancar el proyecto es súper fácil:
1. Clona el repo
2. Ejecuta `stockmaster.bat`
3. Abre tu navegador en `http://localhost:8080`

El script se encarga de todo: verifica la conexión a la BD, libera el puerto si está ocupado, limpia cachés, compila y arranca la aplicación.

Para probar el sistema, puedes usar:
- Email: `admin@test.com`
- Contraseña: `admin123`

## Lo que encontrarás dentro

El proyecto está organizado de forma bastante estándar para una aplicación Spring Boot:

- **Controladores**: Manejan las peticiones web (HomeController, ProductController, etc.)
- **Modelos**: Definen las entidades como Product, Category, Usuario
- **Repositorios**: Para acceder a la base de datos
- **Servicios**: Donde está la lógica de negocio
- **Plantillas**: Vistas en Thymeleaf para la interfaz de usuario

Hemos usado tecnologías que nos facilitan el desarrollo y que son bastante comunes en el ecosistema Java:
- Spring Boot como framework principal
- Spring Security para la autenticación y control de acceso
- JPA/Hibernate para la persistencia
- Thymeleaf para las vistas
- Bootstrap y algo de JavaScript para que se vea bonito

## Roles de usuario

Uno de los puntos fuertes de StockMaster es su sistema de roles:

- **Administrador**: Puede hacer de todo. Es el superusuario.
- **Operador**: Maneja el inventario y puede ver reportes, pero no gestiona usuarios.
- **Auditor**: Solo puede ver reportes, ideal para supervisores o contadores.
- **Cliente**: Tiene acceso a su área personal (funcionalidad en desarrollo).

## Lo que puedes hacer con StockMaster

### Gestión de inventario
Añadir productos nuevos, editar los existentes, asignarles categorías y proveedores, y controlar el stock. El sistema te avisa cuando algún producto está por agotarse.

### Gestión de usuarios
Si eres administrador, puedes crear cuentas para tu equipo y asignarles el rol adecuado según sus responsabilidades.

### Reportes
Tenemos un dashboard con los datos más importantes a simple vista y reportes más detallados para cuando necesites profundizar.

### Interfaz amigable
Diseñamos la interfaz pensando en la facilidad de uso. Es responsive, así que funciona bien tanto en tu PC como en tablets.

## Scripts útiles

Hemos incluido algunos scripts para hacerte la vida más fácil:

- `stockmaster.bat`: El principal, que arranca todo el sistema
- `limpiar-innecesarios.bat`: Para cuando quieras hacer limpieza

## Actualizaciones recientes

### Mayo 2025 - Mejoras del sistema

#### Rendimiento y mantenimiento
- Optimización general del proyecto para mejorar velocidad y estabilidad
- Actualización de la estructura de datos para un acceso más eficiente

#### Interfaz de usuario
- Mejoras en el menú de perfil de usuario
- Rediseño de la página de inicio para una experiencia más intuitiva
- Simplificación de formularios y pantallas de edición
