@echo off
title Servidor Cleo
chcp 65001 >nul

echo ============================================================
echo  Sistema Cleo — Build e inicializacao
echo ============================================================

REM ------------------------------------------------------------
REM  Encerra instancia anterior na porta 8080 (se houver)
REM ------------------------------------------------------------
echo Verificando porta 8080...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8080 ^| findstr LISTENING') do (
    echo Encerrando processo anterior (PID %%a)...
    taskkill /PID %%a /F 2>nul
)

REM ------------------------------------------------------------
REM  Garante que as pastas de output existem
REM ------------------------------------------------------------
if not exist out\controller mkdir out\controller
if not exist out\dao        mkdir out\dao
if not exist out\handler    mkdir out\handler
if not exist out\main       mkdir out\main
if not exist out\model      mkdir out\model
if not exist out\service    mkdir out\service
if not exist out\util       mkdir out\util
if not exist data           mkdir data

REM ------------------------------------------------------------
REM  Compilacao — pega todos os .java recursivamente via PowerShell
REM  O classpath inclui todos os .jar da pasta lib automaticamente
REM ------------------------------------------------------------
echo.
echo Compilando o projeto...
powershell -Command "javac -encoding UTF-8 -cp 'lib/*' -d out (Get-ChildItem -Recurse -Filter '*.java' | Select-Object -ExpandProperty FullName)"

if %errorlevel% neq 0 (
    echo.
    echo [ERRO] Falha na compilacao. Verifique os erros acima.
    pause >nul
    exit /b 1
)

echo [OK] Compilacao concluida com sucesso.

REM ------------------------------------------------------------
REM  Carrega .env se existir (exibe aviso se nao encontrar)
REM ------------------------------------------------------------
echo.
if exist .env (
    echo Arquivo .env encontrado — variaveis serao carregadas pelo Main.java.
) else (
    echo Aviso: .env nao encontrado — usando banco H2 local ^(modo desenvolvimento^).
)

REM ------------------------------------------------------------
REM  Inicia o servidor
REM ------------------------------------------------------------
echo.
echo Iniciando servidor em http://localhost:8080 ...
echo Para encerrar, feche esta janela ou pressione Ctrl+C.
echo ============================================================
echo.

java -cp "out;lib/*" main.Main

pause
