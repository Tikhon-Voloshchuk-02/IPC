#!/bin/bash

echo "📦 Kompiliere Java-Dateien..."

mkdir -p build

javac -cp src/ipc/lib/json-20240303.jar -d build $(find src -name "*.java")

if [ $? -ne 0 ]; then
    echo "❌ Fehler bei der Kompilation"
    exit 1
fi

echo "<Programm compiled>"

# Запуск в зависимости от аргумента
MODE=$1

if [ "$MODE" = "test" ]; then
    echo " Starte TestMode..."
    java -cp build:src/ipc/lib/json-20240303.jar ipc.TestMode
elif [ "$MODE" = "run" ] || [ -z "$MODE" ]; then
    echo " Starte Main..."
    java -cp build:src/ipc/lib/json-20240303.jar ipc.Main
else
    echo "❓ Unbekannter Modus: $MODE"
    echo "Verwende: ./build.sh [run|test]"
    exit 2
fi

