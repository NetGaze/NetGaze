build:
	mvn clean install

start-services:
	docker compose -f ./docker/docker-compose.yaml down -v; \
    docker compose -f ./docker/docker-compose.yaml rm -fsv; \
    docker compose -f ./docker/docker-compose.yaml up --remove-orphans -d;

stop-services:
	docker compose -f ./docker/docker-compose.yaml down -v; \
    docker compose -f ./docker/docker-compose.yaml rm -fsv;