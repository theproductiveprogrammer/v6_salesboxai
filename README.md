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
2. Java 8+/Java 14
3. Golang
4. NodeJS
5. Kafka
6. Uber Cadence with Cassandra

## How to Use

1. Start all the backend services needed

   ```sh
   $> cd <kafka folder>
   $> ./bin/zookeeper-server-start.sh ./config/zookeeper.properties
   $> ./bin/kafka-server-start.sh ./config/server.properties
   
   $> cd <cassandra folder>
   $> ./bin/cassandra -f
   
   $> cd backend/cadence-scripts
   $> ./start-cadence.sh
   ```

2. Create the cadence domain

   ```sh
   $> cd backend/cadence-scripts
   $> ./setup-domain.sh
   ```

3. Start the MySQL database

   1. Running on localhost with root and no password by default

   

4. Create all the required databases:

   ```sh
   $> cd backend/setup-db
   $> ./gradlew run --args=create-dbs
   (or to clear and restart)
   $> ./gradlew run --args=recreate-dbs
   ```

5. Start the Authenticator microservice:

   ```sh
   $> cd backend/authenticator
   $> ./gradlew run
   ```

6. Populate the tenant DB with sample data:

   ```sh
   $> cd backend/setup-db
   $> ./gradlew run --args=create-tenants
   ```

7. Start the ‘business object’ REST microservice:

   ```sh
   $> cd backend/biz-objects
   $> ./gradlew run
   ```

8. Populate the biz objects with sample workflow metadata:

   ```sh
   $> cd backend/setup-db
   $> ./gradlew run --args=create-workflow-meta
   ```

9. Start the other backend Microservices:

   ```sh
   $> cd backend/importer
   $> ./gradlew run
   $> cd backend/workflow-engine
   $> ./gradlew run
   ```

   

10. Start the front end webapp:

   ```sh
   $> cd webapp
   $> npm start
   ```

11. Start the API Gateway:

    ```sh
    $> cd backend/api-gateway
    $> go run gateway.go
    ```

12. Navigate to http://localhost/ and Sign Up to Get Started!

## Trying the Application

1. Go to the workflow area and create the workflows you want for your tenant.

2. Generate a test import file:

   ```sh
   $> cd leadgen
   $> ./gradlew run --args=<num of leads>
   ```

3. Go to Import area and drag the import file to start the import. This will trigger the workflow for new leads.

4. Go to Lead view and click the buttons that represent the lead performing certain actions. This will trigger the workflow for the appropriate events.

--------

