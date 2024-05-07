FROM maven:3.9.6-eclipse-temurin-21

COPY ./ /app

WORKDIR /app

RUN cd /app && mvn clean install

CMD ["java", "-jar", "/app/server/target/netwatch-server-netwatch-revision.jar"]