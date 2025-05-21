@echo off
echo Ejecutando script SQL para corregir relaciones...
mysql -u root -proot datos_almacen < corregir_relaciones_bd.sql
if %ERRORLEVEL% == 0 (
    echo Script SQL ejecutado correctamente.
) else (
    echo Error al ejecutar el script SQL.
)
pause
