@echo off
echo =====================================================
echo         Iniciando StockMaster - Sistema de Inventario
echo =====================================================
echo.

REM Definir variables
set JAVA_HOME=%JAVA_HOME%
set PROJECT_DIR=%~dp0
set WAR_FILE=%PROJECT_DIR%target\transactional-system-1.0-SNAPSHOT.war

echo Verificando procesos Java en ejecucion...
taskkill /F /IM java.exe 2>nul
if %ERRORLEVEL% EQU 0 (
    echo Se detuvieron procesos Java existentes.
) else (
    echo No se encontraron procesos Java en ejecucion.
)
echo.

REM Comprobar si existe el archivo WAR
if not exist "%WAR_FILE%" (
    echo El archivo WAR no existe. Compilando el proyecto...
    echo.
    cd /d "%PROJECT_DIR%"
    call .\apache-maven\apache-maven-3.9.6\bin\mvn clean package -DskipTests
    if %ERRORLEVEL% NEQ 0 (
        echo Error al compilar el proyecto.
        goto error
    )
) else (
    echo Archivo WAR encontrado.
)
echo.

echo Iniciando la aplicacion StockMaster...
echo.
echo Para acceder a la aplicacion, abra un navegador y visite:
echo   http://localhost:8080
echo.
echo Para detener la aplicacion, presione Ctrl+C.
echo.
echo Iniciando servidor...
echo =====================================================

java -jar "%WAR_FILE%"

if %ERRORLEVEL% NEQ 0 goto error
goto end

:error
echo.
echo Ha ocurrido un error al iniciar la aplicacion. Revise los mensajes anteriores.
pause
exit /b 1

:end
pause
exit /b 0
