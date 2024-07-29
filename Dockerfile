FROM maven:3.9.6-eclipse-temurin-21

COPY ./ /app

WORKDIR /app

RUN cd /app && mvn clean install

RUN cp /app/server/src/main/resources/application.yaml /app/config.yaml

CMD ["java", "-jar", "/app/server/target/netgaze-server-netgaze-revision.jar", "--spring.config.location=/app/config.yaml"]