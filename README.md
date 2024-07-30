# Yoga-app

Yoga-app is a web application used to plan yoga sessions between coachs and customers.

## Prerequisites

You need a MySQL DataBase Server.
Java 17 or above.
Maven 3.5 or above (nvm)
Node Package Manager (npm)

## Installation

### 1. Create the database

Create a new Database on a MySQL Server.
Import the script in `ressources/sql/script.sql`

Create a database user with INSERT, UPDATE, DELETE privileges on this DataBase.

### 2. Configure the back-end

All the services app is located in `back` directory.
Set up your DataBase connection in  `src/main/resources/application.properties`.

### 3. Start the back-end

with command-line under `back` directory :

Install dependenciens and install the app in your maven local repo:
```
mvn clean install
```

Then run the app:
```
mvn spring-boot:run
```

### 4. Start the front-end

Install dependencies and build the app :
```
npm install
```

Run it :
```
npm run start
```

## Use the app :

You can register a new user and use it as a customer.

For admin tasks, a default admin account exists with theses credentials:
- login: yoga@studio.com
- password: test!1234

## Automated back-end tests 

### Run test in command-line under `back` directory :
```
mvn clean test
```

### Check coverage report
With your web browser, open `index.html` located in `back/target/site/jacoco`.


## Automated front-end tests

### Run unit test in command-line under `front` directory :
```
npm run test
```

### Renerate coverage report for unit tests:
```
npm run test:coverage
```

#### Check coverage report for unit tests 

With your web browser, open `index.html` located in `front/coverage/jest/lcov-report`.


### Run end-to-end tests in command-line under `front` directory :
```
npm run e2e
```

### generate end-to-end coverage report for unit tests:
```
npm run e2e:coverage
```

#### Check coverage report for end-to-end

With your web browser, open `index.html` located in `front/coverage/lcov-report`.
