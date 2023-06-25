# Spring batch demo

Dummy code to test the functionality of Spring batch using
chunks reading from a db and printing the values.

## before starting the application
You need to setup the postgres db. You can start the
service with docker compose:
```shell
docker compose up -d
```

Then you should enter the db with:
```shell
docker exec -it postgres_batch bash
```
and login with the batch user:
```shell
psql -U batch
```

you can change the db with:
```shell
\c batch
```

At this point you should apply the changes from the file
[src/main/resources/db/version_1.sql](src/main/resources/db/version_1.sql)


## Compile the application

Use maven to compile with:
```shell
mvn clean install
```

## Run the application

```shell
java -jar target/batch*.jar
```