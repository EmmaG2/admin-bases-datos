.PHONY: build run db clean

JAR := target/admin-bases-datos-1.0.0-SNAPSHOT.jar

-include .env
export DB_PASSWORD

run: export POSTGRES_URL = jdbc:postgresql://127.0.0.1:5432/admin_bases_datos
run: export MYSQL_URL = jdbc:mysql://127.0.0.1:3306/segunda_unidad?sslMode=REQUIRED
run: export MARIADB_URL = jdbc:mariadb://127.0.0.1:3307/admin_bases_datos

build:
	mvn package

run:
	mvn -Dmaven.test.skip=true compile javafx:run

db:
	docker compose up -d

clean:
	mvn clean
