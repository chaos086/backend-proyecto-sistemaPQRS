#!/bin/bash

echo "========================================"
echo "  Compilando Proyecto PQRS - UniQuindio"
echo "========================================"
echo ""

echo "[1] Limpiando build anterior..."
./gradlew clean

echo ""
echo "[2] Compilando código fuente..."
./gradlew compileJava

if [ $? -ne 0 ]; then
    echo ""
    echo "[ERROR] ¡La compilación falló!"
    read -p "Presiona Enter para salir..."
    exit 1
fi

echo ""
echo "[3] Ejecutando pruebas..."
./gradlew test

if [ $? -ne 0 ]; then
    echo ""
    echo "[ADVERTENCIA] ¡Algunas pruebas fallaron!"
    read -p "Presiona Enter para salir..."
    exit 1
fi

echo ""
echo "========================================"
echo "  COMPILACIÓN Y PRUEBAS EXITOSAS!"
echo "========================================"
echo ""
echo "Reporte de pruebas disponible en:"
echo "build/reports/tests/test/index.html"
echo ""
read -p "Presiona Enter para salir..."
