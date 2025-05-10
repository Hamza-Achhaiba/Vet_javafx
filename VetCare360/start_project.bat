@echo off
echo Starting VetCare360 application...
echo.
echo This script will build and run the VetCare360 application.
echo Please make sure you have Java 17 or later installed.
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo Error: Java is not installed or not in the PATH.
    echo Please install Java 17 or later and try again.
    pause
    exit /b 1
)

REM Navigate to the project directory
cd %~dp0

REM Build and run the application using Maven wrapper
echo Building and starting the application...
call mvnw.cmd clean javafx:run

REM If the application exits with an error
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo An error occurred while running the application.
    echo Please check the error messages above.
    pause
)

exit /b