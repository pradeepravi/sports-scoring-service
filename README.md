# sports-scoring-service

### Spring Boot REST API Service
#### mvn spring-boot:run

### Before starting make sure the following is done: 
1) PostGreSql is the DB. Set that
2) Run queries in db_scripts.sql under resources 
3) Following application.properties are important to configure the API

spring.datasource.url=jdbc:postgresql://localhost:5432/sports-score
spring.datasource.username=postgres
spring.datasource.password=admin

### Swagger API
http://localhost:8080/swagger-ui.html

### URL of the Angular Application tat shouldn't be blocked by the Services
supported.settings.cors_origin=http://localhost:4200
