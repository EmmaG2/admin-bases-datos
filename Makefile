.PHONY: build run db clean

JAR := target/admin-bases-datos-1.0.0-SNAPSHOT.jar

build:
	mvn package

run: build
	java -jar $(JAR)

db:
	docker compose run --rm app $(ARGS)

clean:
	mvn clean
