
# Simplified PSP

A prototyped version of a virtual wallet system made with Java and Spring where you can transfer funds between users.


## Run Locally

You must have a MySQL database with the following settings
```bash
  port: 3306
  database name: sps_database
```
Clone the project

```bash
  git clone https://github.com/zam0k/simplified-psp
```

Go to the project directory

```bash
  cd simplified-psp
```
Start the server

```bash
  mvn spring-boot:run
```


## Running With Docker
Clone the project

```bash
  git clone https://github.com/zam0k/simplified-psp
```

Go to the project directory

```bash
  cd simplified-psp
```

Insert the following command

```bash
  docker-composer up -d --build
```

## Documentation

Once the application is running, you can find its documentation in
```bash
  http://localhost/swagger-ui/index.html
```


## Running Tests

First you must have docker running in your machine

To run tests, run the following command

```bash
  mvn test
```


## Tech Stack

- Java 11
- Spring MVC
- Spring Data JPA
- Hibernate
- MySQL
- Flyway
- Junit5
- Mockito
- Rest-Assured
- Testcontainers
- Docker

