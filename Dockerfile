FROM ubuntu:latest
WORKDIR /tmp
EXPOSE 8080

RUN apt update && apt -y install openjdk-17-jdk wget curl && echo ttf-mscorefonts-installer msttcorefonts/accepted-mscorefonts-eula select true | debconf-set-selections && apt-get install -y -q ttf-mscorefonts-installer ;apt autoclean;apt -y autoremove;apt clean;mkdir -p /tmp/reports
RUN keytool -genkeypair -alias app -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore app.p12 -validity 3650 -keypass changeme -storepass changeme -dname "CN=Act,OU=IT,O=Act,L=Jundiai,ST=Sao Paulo,C=Brazil"
RUN keytool -genkeypair -alias app -keyalg RSA -keysize 2048 -keystore app.jks -validity 3650 -keypass changeme -storepass changeme -dname "CN=Act,OU=IT,O=Act,L=Jundiai,ST=Sao Paulo,C=Brazil"
RUN keytool -importkeystore -srckeystore app.jks -destkeystore app.p12 -deststoretype pkcs12 -srcstorepass changeme -deststorepass changeme -noprompt
RUN wget https://dlcdn.apache.org/cassandra/4.1.1/apache-cassandra-4.1.1-bin.tar.gz -O - | tar -xzv;mv apache-cassandra* cassandra


ADD /build/libs/cashflow*.jar /tmp/app.jar
ADD /run_cashflow /tmp/

#ENTRYPOINT ["java", "-Djava.awt.headless=true", "-XX:-UseGCOverheadLimit", "-Xms512M", "-Xmx6G", "-jar", "app.jar"]
ENTRYPOINT ["/tmp/run_cashflow"]