FROM ubuntu:latest
RUN apt update && apt -y install openjdk-17-jdk && echo ttf-mscorefonts-installer msttcorefonts/accepted-mscorefonts-eula select true | debconf-set-selections && apt-get install -y -q ttf-mscorefonts-installer ;apt autoclean;apt -y autoremove;apt clean;mkdir -p /tmp/reports
WORKDIR /tmp
EXPOSE 8080
ADD /build/libs/cashflow*.jar /tmp/app.jar

ENTRYPOINT ["java", "-Djava.awt.headless=true", "-XX:-UseGCOverheadLimit", "-Xms512M", "-Xmx6G", "-jar", "app.jar"]