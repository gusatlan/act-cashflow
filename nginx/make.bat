@ECHO OFF

docker build -t nginx-app-img:1.0 .
docker build -t nginx-app-img:latest .

ECHO ON
