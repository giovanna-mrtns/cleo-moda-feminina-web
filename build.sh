#!/bin/bash
# ============================================================
#  build.sh — executado pelo Render na etapa de build
# ============================================================

echo "Criando pastas de output..."
mkdir -p out/controller out/dao out/handler out/main out/model out/service out/util

echo "Compilando projeto Java..."
find . -name "*.java" > fontes.txt
javac -encoding UTF-8 -cp "lib/*" -d out @fontes.txt

if [ $? -ne 0 ]; then
    echo "ERRO na compilação."
    exit 1
fi

echo "Build concluído com sucesso."
