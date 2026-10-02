@echo off
title KodeWala E-Commerce
echo Compiling...
javac -d out -sourcepath src src\com\kodewala\ecommerce\main\EcommerceApplication.java
if errorlevel 1 (
    echo BUILD FAILED
    pause
    exit /b 1
)
echo Running...
java -cp out com.kodewala.ecommerce.main.EcommerceApplication
pause
