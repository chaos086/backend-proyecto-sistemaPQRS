@echo off
cd /d "%~dp0proyecto"

echo ========================================
echo   Compilando Proyecto PQRS - UniQuindio
echo ========================================
echo.

echo [1] Limpiando build anterior...
call gradlew clean

echo.
echo [2] Compilando codigo fuente...
call gradlew compileJava

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] La compilacion fallo!
    pause
    exit /b 1
)

echo.
echo [3] Ejecutando pruebas...
call gradlew test

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ADVERTENCIA] Algunas pruebas fallaron!
    pause
    exit /b 1
)

echo.
echo ========================================
echo   COMPILACION Y PRUEBAS EXITOSAS!
echo ========================================
echo.
echo Reporte de pruebas disponible en:
echo proyecto\build\reports\tests\test\index.html
echo.
pause
