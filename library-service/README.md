# Coffee Ordering Application (Pimjer 2)

## Description

This is a simple coffee ordering application that allows users to register new coffee types to the menu,
change their availability and order them. The project is an implementation of Primjer 2 from the lecture.

## Build

To build the project, run the following command:

```bash
mvn clean install
```

## Run

### Database

The project uses MariaDB as a database. To run the project, you need to have a MariaDB server running.
There is a docker-compose file in the `primjer_2` root directory that you can use to start a MariaDB server.
You'll see that the application is configured to connect to the database on `localhost:3306` with a password
read from the `MARIADB_COFFEE_ROOT_PASSWORD` environment variable. You can set this variable in the `.env` file
in the `primjer_2` root directory so that the docker-compose file can read it.

For integration tests, the application uses an in-memory H2 database.
You can check the application properties files to explore the configuration.


### Application

To run the application, run the following command:

```bash
mvn spring-boot:run
```
Don't forget to make the `MARIADB_COFFEE_ROOT_PASSWORD` environment variable available. 
