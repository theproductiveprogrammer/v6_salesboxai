# Salesbox (v6) Architecture Reference Implementation

This is the Salesbox v6 Reference Architecture

## Structure

It is helpful to get an overview of the layout of this repository. The layout itself has been divided into two major portions:

* The Backend and
* The Frontend Webapp

The frontend webapp is a SPA sample app we can use to play with the architecture.

The backend is decoupled from the front end and contains all the microservices with REST api’s to serve any front end.

```
.
├── backend
│   ├── api-gateway
│   ├── authenticator
│   ├── biz-objects
│   └── setup-db
└── webapp
```

## Prerequisities

1. MySQL/MariaDB
   - Running on localhost with root and no password by default
2. Java 8+/Java 14
3. Golang
4. NodeJS
5. Kafka
6. Uber Cadence with Cassandra

## How to Use

1. Create all the required databases:

   ```sh
   $> cd backend/setup-db
   $> ./gradlew run --args=create-dbs
   ```

2. Start the Authenticator microservice:

   ```sh
   $> cd backend/authenticator
   $> ./gradlew run
   ```

3. Populate the tenant DB with sample data:

   ```sh
   $> cd backend/setup-db
   $> ./gradlew run --args=create-tenants
   ```

4. Start the ‘business object’ REST microservice:

   ```sh
   $> cd backend/biz-objects
   $> ./gradlew run
   ```

5. Populate the biz objects with sample workflow metadata:

   ```sh
   $> cd backend/setup-db
   $> ./gradlew run --args=create-workflow-meta
   ```

6. Start the front end webapp:

   ```sh
   $> cd webapp
   $> npm start
   ```

7. Start the API Gateway:

   ```sh
   $> cd backend/api-gateway
   $> go run gateway.go
   ```

8. Navigate to http://localhost/ and Sign Up to Get Started!



--------

