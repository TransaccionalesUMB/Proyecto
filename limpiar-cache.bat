@echo off
echo =====================================================
echo Limpiando cache y archivos temporales
echo =====================================================

echo Deteniendo procesos Java en ejecución...
taskkill /F /IM java.exe 2>nul

echo Limpiando directorios temporales...
rmdir /S /Q "c:\Users\David Rojas\OneDrive\Escritorio\U\Transaccionales\Proyecto\V2\target\classes\templates" 2>nul

echo Copiando archivos actualizados...
xcopy /E /Y "c:\Users\David Rojas\OneDrive\Escritorio\U\Transaccionales\Proyecto\V2\src\main\resources\templates" "c:\Users\David Rojas\OneDrive\Escritorio\U\Transaccionales\Proyecto\V2\target\classes\templates\"

echo.
echo Archivos actualizados. Por favor reinicie la aplicación.
echo.

pause
