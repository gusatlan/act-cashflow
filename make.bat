@ECHO OFF
CALL compile.bat

docker build -t cashflow-img:1.0 .
docker build -t cashflow-img:latest .

cd nginx
CALL make.bat
cd ..

