FROM maven:3.9.6-eclipse-temurin-21

COPY ./ /app

WORKDIR /app

RUN cd /app && mvn clean install

RUN mv /app/sample-agent-config.yaml /app/config.yaml

CMD ["java", "-jar", "/app/server/target/netwatch-server-netwatch-revision.jar", "--spring.config.location=/app/config.yaml"]