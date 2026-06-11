#!/bin/bash
# ============================================================
#  start.sh — executado pelo Render para iniciar o servidor
# ============================================================

echo "Iniciando servidor Cleo..."
java -cp "out:lib/*" main.Main
