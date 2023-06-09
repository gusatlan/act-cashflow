#!/bin/bash
./compile

docker build -t cashflow-img:1.0 .
docker build -t cashflow-img:latest .

cd nginx
./make
cd ..

./gradlew clean
rm *.log
rm -Rvf build

