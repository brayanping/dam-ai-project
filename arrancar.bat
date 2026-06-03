@echo off
chcp 65001 >nul
cls
echo.
echo ==========================================
echo    INICIADOR AUTOMATICO - EduIA
echo ==========================================
echo.

cd /d "%~dp0"

REM 1. BACKEND JAVA
echo [1/3] Iniciando Backend Spring Boot...
start "Backend EduIA" cmd /k "cd /d %~dp0 && mvnw.cmd spring-boot:run"
timeout /t 10 /nobreak >nul

REM 2. SERVICIO IA PYTHON
echo [2/3] Iniciando Servicio IA Python...
start "Servicio IA" cmd /k "cd /d %~dp0\edu-ia-service && venv\Scripts\activate && uvicorn main:app --reload --port 8000"
timeout /t 5 /nobreak >nul

REM 3. FRONTEND REACT
echo [3/3] Iniciando Frontend React...
if not exist edu-ia-frontend\node_modules (
    echo Instalando dependencias npm por primera vez...
    cd edu-ia-frontend
    call npm install
    cd ..
)
start "Frontend EduIA" cmd /k "cd /d %~dp0\edu-ia-frontend && npm start"

echo.
echo ==========================================
echo  Todo arrancando en ventanas separadas
echo  Espera 60 segundos y abre:
echo  http://localhost:3000
echo ==========================================
echo.
timeout /t 5 /nobreak >nul
REM start http://localhost:3000
