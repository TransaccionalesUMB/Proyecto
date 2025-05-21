@echo off
echo =====================================================
echo    ELIMINANDO SCRIPTS OBSOLETOS
echo =====================================================
echo.

echo Eliminando scripts antiguos...

set SCRIPTS_OBSOLETOS=0

if exist "stockmaster.bat" (
    del "stockmaster.bat"
    echo - stockmaster.bat eliminado
    set /a SCRIPTS_OBSOLETOS+=1
)

if exist "iniciar-stockmaster.bat" (
    del "iniciar-stockmaster.bat"
    echo - iniciar-stockmaster.bat eliminado
    set /a SCRIPTS_OBSOLETOS+=1
)

if exist "reiniciar-stockmaster.bat" (
    del "reiniciar-stockmaster.bat"
    echo - reiniciar-stockmaster.bat eliminado
    set /a SCRIPTS_OBSOLETOS+=1
)

if exist "limpiar-cache.bat" (
    del "limpiar-cache.bat"
    echo - limpiar-cache.bat eliminado
    set /a SCRIPTS_OBSOLETOS+=1
)

if exist "limpiar-proyecto.bat" (
    del "limpiar-proyecto.bat"
    echo - limpiar-proyecto.bat eliminado
    set /a SCRIPTS_OBSOLETOS+=1
)

if exist "configurar-dominio.bat" (
    del "configurar-dominio.bat"
    echo - configurar-dominio.bat eliminado
    set /a SCRIPTS_OBSOLETOS+=1
)

if exist "eliminar-scripts-antiguos.bat" (
    del "eliminar-scripts-antiguos.bat"
    echo - eliminar-scripts-antiguos.bat eliminado
    set /a SCRIPTS_OBSOLETOS+=1
)

echo.
if %SCRIPTS_OBSOLETOS% GTR 0 (
    echo Se eliminaron %SCRIPTS_OBSOLETOS% scripts obsoletos.
) else (
    echo No se encontraron scripts obsoletos para eliminar.
)
echo.
echo Ahora solo necesitas usar iniciar-automatico.bat para ejecutar la aplicacion.
echo =====================================================

pause
