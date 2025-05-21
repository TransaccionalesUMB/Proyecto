@echo off
setlocal enabledelayedexpansion

REM Definir variables
set PROJECT_DIR=%~dp0
set MAVEN_HOME=%PROJECT_DIR%apache-maven-3.9.6
set WAR_FILE=%PROJECT_DIR%target\transactional-system-1.0-SNAPSHOT.war
set MYSQL_CMD=mysql -u root -proot -h localhost datos_almacen
set MYSQL_TEST_CMD=mysql -u root -proot -h localhost -e "SELECT 1"

echo =====================================================
echo    INICIANDO STOCKMASTER AUTOMATICAMENTE
echo =====================================================
echo.

echo [1/5] Verificando conexion a la base de datos...
%MYSQL_TEST_CMD% > nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo   [ERROR] No se pudo conectar a la base de datos MySQL.
    echo   Asegurese de que el servidor MySQL este en ejecucion.
    pause
    exit /b 1
) else (
    echo   [OK] Conexion a MySQL establecida.
)

echo.
echo [2/5] Verificando si el puerto 8080 esta en uso...
for /f "tokens=5" %%p in ('netstat -ano ^| findstr :8080 ^| findstr LISTENING') do (
    echo   Terminando proceso con PID: %%p
    taskkill /F /PID %%p > nul 2>&1
)
echo   [OK] Puerto 8080 liberado.

echo.
echo [3/5] Limpiando cache y archivos temporales...
echo   Eliminando directorio target...
if exist target (
    rd /s /q target > nul 2>&1
    if %ERRORLEVEL% NEQ 0 (
        echo   [ADVERTENCIA] No se pudo eliminar completamente el directorio target.
        echo   Algunos archivos pueden estar en uso.
    ) else (
        echo   [OK] Directorio target eliminado.
    )
) else (
    echo   [OK] No existe directorio target para eliminar.
)

echo.
echo [4/5] Compilando el proyecto...
if exist "%MAVEN_HOME%" (
    echo   Usando Maven local en %MAVEN_HOME%
    call "%MAVEN_HOME%\bin\mvn" clean package -DskipTests
) else (
    echo   Intentando usar Maven global...
    call mvn clean package -DskipTests
)

if %ERRORLEVEL% NEQ 0 (
    echo   [ERROR] Error al compilar el proyecto.
    pause
    exit /b 1
) else (
    echo   [OK] Proyecto compilado correctamente.
)

echo.
echo [5/5] Iniciando la aplicacion...
echo   Para acceder a la aplicacion, abra un navegador y visite:
echo   http://localhost:8080
echo.
echo   Para detener la aplicacion, presione Ctrl+C.
echo =====================================================

java -jar "%WAR_FILE%"
pause
exit /b 0
