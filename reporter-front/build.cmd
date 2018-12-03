@echo off

xcopy /s target\*.js ..\reporter-back\src\main\resources\static\
xcopy /s target\*.map ..\reporter-back\src\main\resources\static\
xcopy /s target\*.ico ..\reporter-back\src\main\resources\static\
xcopy /s target\*.html ..\reporter-back\src\main\resources\static\

IF errorlevel 1 GOTO MOVE_FAILURE

echo.
echo ----------------------------
echo        SUCCESS!
echo ----------------------------
echo.

GOTO :EOF

:MOVE_FAILURE
color 4F
echo.
echo ----------------------------
echo    Couldn't move files
echo ----------------------------
echo.
