COMPOSE_FILE := docker-compose.yml

up:
	docker compose -f ${COMPOSE_FILE} up -d

down:
	docker compose -f ${COMPOSE_FILE} down

ps:
	docker compose -f ${COMPOSE_FILE} ps

logs:
	docker compose -f ${COMPOSE_FILE} logs -f

rebuild:
	docker compose -f ${COMPOSE_FILE} up -d --build

stop:
	docker compose -f ${COMPOSE_FILE} stop

start:
	docker compose -f ${COMPOSE_FILE} start