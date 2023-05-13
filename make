#!/bin/bash
./compile

docker build -t cashflow-img:1.0 .
docker build -t cashflow-img:latest .

./gradlew clean
rm *.log
rm -Rvf build

