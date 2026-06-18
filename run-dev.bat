@echo off
REM ============================================================
REM  사이언스얼라이브 (Science Alive) - Windows 원클릭 실행
REM  요구사항: JDK 17 이상이 설치되어 PATH에 있어야 합니다.
REM           (확인: 터미널에서  java -version )
REM ============================================================
cd /d "%~dp0"

where java >nul 2>nul
if errorlevel 1 (
  echo.
  echo [오류] Java를 찾을 수 없습니다.
  echo        JDK 17 이상을 설치하세요: https://adoptium.net
  echo.
  pause
  exit /b 1
)

echo.
echo === 빌드 중... 최초 실행은 몇 분 걸릴 수 있어요 (인터넷 필요) ===
call gradlew.bat clean bootJar -Pvaadin.productionMode=true
if errorlevel 1 (
  echo.
  echo [오류] 빌드 실패. JDK 17+ 설치 여부를 확인하세요 ( java -version ).
  echo.
  pause
  exit /b 1
)

echo.
echo === 서버 시작. 잠시 후 브라우저가 http://localhost:8080 으로 열립니다 ===
echo === 종료하려면 이 창을 닫으세요 ===
echo.
start "" cmd /c "timeout /t 25 /nobreak >nul && start http://localhost:8080"
java -jar build\libs\science-alive-0.0.1-SNAPSHOT.jar
