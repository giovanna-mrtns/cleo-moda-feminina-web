@echo off
title Servidor Cleo

echo Encerrando servidor anterior (se houver)...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8080 ^| findstr LISTENING') do taskkill /PID %%a /F 2>nul

echo Compilando o projeto...
powershell -Command "javac -cp 'lib/*' -d out (Get-ChildItem -Recurse -Filter '*.java' | Select-Object -ExpandProperty FullName)"

if %errorlevel% neq 0 (
    echo.
    echo ERRO na compilacao. Pressione qualquer tecla para fechar.
    pause >nul
    exit
)

echo.
echo Iniciando servidor em http://localhost:8080 ...
echo Para encerrar, feche esta janela.
echo.

java -cp "out;lib/*" main.Main
pause