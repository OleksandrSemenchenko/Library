# Library Service

### Table of contents

1. [ Project description ](#project-description)

2. [ Initial requirements ](#initial-requirements)

3. [ Build, deploy and run ](#Build-deploy-and-run)

### Project description

This is a service responsible for holding and processing data related to books and users

### Initial requirements

To build and run the application, you need to be installed and configured:

1. Java 17 (Amazon)
2. Maven
3. Git

### Build, deploy and run

1. You can test and build the application with Maven command:
   
`mvn clean package`

3. You can run the application in 'dev' profile using Maven command, but before it is needed to configure the database according to settings in the 'application.yaml' file:
   
`mvn spring-boot:run -Dspring-boot.run.profiles=dev`
2. You can run the application having docker on your machine:

`docker compose up`

4. Use next endpoints to ensure that application has started successfully:

- [http://localhost:8081/v3/api-docs](http://localhost:8081/v3/api-docs)
- [http://localhost:8081/v3/api-docs.yaml](http://localhost:8081/v3/api-docs)
- [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)
- [http://localhost:8081/actuator](http://localhost:8081/actuator)
- [http://localhost:8081/actuator/health](http://localhost:8081/actuator/health)