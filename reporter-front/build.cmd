@echo off

xcopy target\*.js ..\reporter-back\src\main\resources\static\
xcopy target\*.map ..\reporter-back\src\main\resources\static\
xcopy target\*.ico ..\reporter-back\src\main\resources\static\
xcopy target\*.html ..\reporter-back\src\main\resources\static\

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
