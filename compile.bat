@ECHO OFF

cd ..\act-platform
CALL compile.bat
cd ..\cashflow

mkdir build\libs
docker build -t cashflow-build-img -f Dockerfile-compile .
docker run --rm -v %CD%/build/libs:/tmp/project/build/libs cashflow-build-img

del build\libs\*plain*.jar

ECHO ON
