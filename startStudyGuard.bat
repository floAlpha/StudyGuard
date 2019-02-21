%1 mshta vbscript:CreateObject("WScript.Shell").Run("%~s0 ::",0,FALSE)(window.close)&&exit
@echo off
choice /t 5 /d y /n >nul
cd C:\Users\chuan\OneDrive - m.scnu.edu.cn\CloudDisk\跨平台绿色软件\学习监控程序V1.0
java -jar 学习监控程序V1.0.jar

