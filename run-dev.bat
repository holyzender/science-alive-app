@echo off
REM Convenience launcher: sets JAVA_HOME (Temurin 21) and starts the app.
REM Honours a PORT env var (server.port=${PORT:8080}); defaults to 8080.
set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot"
cd /d "%~dp0"
call gradlew.bat bootRun --console=plain
